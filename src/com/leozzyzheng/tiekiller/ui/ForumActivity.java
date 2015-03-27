package com.leozzyzheng.tiekiller.ui;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;
import com.leozzyzheng.tiekiller.R;
import com.leozzyzheng.tiekiller.http.data.BaWuForumListData;
import com.leozzyzheng.tiekiller.http.data.LikeForumListData;
import com.leozzyzheng.tiekiller.http.request.BaWuForumRequest;
import com.leozzyzheng.tiekiller.http.request.RecommendedForumRequest;
import com.leozzyzheng.tiekiller.ui.adapter.BaWuForumAdapter;
import com.leozzyzheng.tiekiller.ui.adapter.LikeForumListAdapter;
import com.leozzyzheng.tiekiller.ui.adapter.SimplePageAdapter;
import com.leozzyzheng.tiekiller.ui.base.BaseActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by leozzyzheng on 2015/3/26.
 * 喜欢的吧，有权限的吧的界面
 */
public class ForumActivity extends BaseActivity {
    private Map<Integer, SwipeRefreshLayout> swipeViewMap = new HashMap<Integer, SwipeRefreshLayout>();
    private ListView likeForumListView;
    private ViewPager pagers;
    private LikeForumListAdapter likeForumAdapter;
    private ListView baWuForumListView;
    private BaWuForumAdapter baWuForumAdapter;
    private SearchView baWuSearchView;

    private String cachedSearchWord;

    public static final int LIKE_FORUM_INDEX = 0;
    public static final int BAWU_FORUM_INDEX = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forum);
        pagers = (ViewPager) findViewById(R.id.pagers);

        View likeForumView = getLayoutInflater().inflate(R.layout.list_like_forum, pagers, false);
        View baWuForumView = getLayoutInflater().inflate(R.layout.list_bawu_forum, pagers, false);

        initLikeForum(likeForumView);
        initBaWuForum(baWuForumView);

        List<View> list = new ArrayList<View>();
        list.add(LIKE_FORUM_INDEX, likeForumView);
        list.add(BAWU_FORUM_INDEX, baWuForumView);
        pagers.setAdapter(new SimplePageAdapter(list));
        pagers.setOnPageChangeListener(mPagerChangeListener);
    }

    private void initLikeForum(View likeForumView) {
        likeForumListView = (ListView) likeForumView.findViewById(R.id.like_forum_list_view);
        SwipeRefreshLayout likeForumSwipeView = (SwipeRefreshLayout) likeForumView.findViewById(R.id.swipe);
        likeForumSwipeView.setColorSchemeResources(R.color.holo_blue_dark, R.color.holo_blue_light, R.color.holo_green_light, R.color.holo_green_light);
        likeForumSwipeView.setOnRefreshListener(mRefreshListener);
        swipeViewMap.put(LIKE_FORUM_INDEX, likeForumSwipeView);
    }

    private void initBaWuForum(View baWuForumView) {
        baWuForumListView = (ListView) baWuForumView.findViewById(R.id.bawu_list_view);
        SwipeRefreshLayout baWuForumSwipeView = (SwipeRefreshLayout) baWuForumView.findViewById(R.id.swipe);
        baWuForumSwipeView.setColorSchemeResources(R.color.holo_blue_dark, R.color.holo_blue_light, R.color.holo_green_light, R.color.holo_green_light);
        baWuForumSwipeView.setOnRefreshListener(mRefreshListener);
        swipeViewMap.put(BAWU_FORUM_INDEX, baWuForumSwipeView);
        baWuSearchView = (SearchView) baWuForumView.findViewById(R.id.search_view);
        baWuSearchView.setOnQueryTextListener(mBaWuSearchViewListener);
    }

    /**
     * 用来回到查询喜欢贴吧数据请求的接口
     */
    private RecommendedForumRequest.OnReceivedLikeForumListener mLikeForumListener = new RecommendedForumRequest.OnReceivedLikeForumListener() {
        @Override
        public void onReceived(LikeForumListData likeForumListData) {
            if (likeForumAdapter == null) {
                likeForumAdapter = new LikeForumListAdapter(ForumActivity.this, likeForumListData.getLikeForumDatas());
                likeForumListView.setAdapter(likeForumAdapter);
            } else {
                likeForumAdapter.setData(likeForumListData.getLikeForumDatas());
                likeForumAdapter.notifyDataSetInvalidated();
            }

            swipeViewMap.get(LIKE_FORUM_INDEX).setRefreshing(false);
        }

        @Override
        public void onFailed() {
            swipeViewMap.get(LIKE_FORUM_INDEX).setRefreshing(false);
        }
    };

    private SwipeRefreshLayout.OnRefreshListener mRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            if (pagers.getCurrentItem() == LIKE_FORUM_INDEX) {
                RecommendedForumRequest request = new RecommendedForumRequest(mLikeForumListener);
                request.send();
            } else {
                if (cachedSearchWord != null) {
                    BaWuForumRequest request = new BaWuForumRequest(cachedSearchWord, mBaWuSearchListener);
                    request.send();
                } else {
                    swipeViewMap.get(pagers.getCurrentItem()).setRefreshing(false);
                }
            }
        }
    };

    private SearchView.OnQueryTextListener mBaWuSearchViewListener = new SearchView.OnQueryTextListener() {
        @Override
        public boolean onQueryTextSubmit(String query) {
            cachedSearchWord = query;
            swipeViewMap.get(BAWU_FORUM_INDEX).setRefreshing(true);
            BaWuForumRequest request = new BaWuForumRequest(query, mBaWuSearchListener);
            request.send();
            return true;
        }

        @Override
        public boolean onQueryTextChange(String newText) {
            return false;
        }
    };

    private BaWuForumRequest.OnBaWuListReceivedListener mBaWuSearchListener = new BaWuForumRequest.OnBaWuListReceivedListener() {
        @Override
        public void onReceived(BaWuForumListData forumListData) {
            baWuSearchView.setVisibility(View.GONE);
            if (baWuForumAdapter == null) {
                baWuForumAdapter = new BaWuForumAdapter(ForumActivity.this, forumListData.getForumList());
                baWuForumListView.setAdapter(baWuForumAdapter);
            } else {
                baWuForumAdapter.setData(forumListData.getForumList());
                baWuForumAdapter.notifyDataSetInvalidated();
            }

            swipeViewMap.get(BAWU_FORUM_INDEX).setRefreshing(false);
        }

        @Override
        public void onFailed() {
            Toast.makeText(ForumActivity.this, "没有根据输入查到有权限的贴吧", Toast.LENGTH_SHORT).show();
            swipeViewMap.get(BAWU_FORUM_INDEX).setRefreshing(false);
        }
    };

    private ViewPager.OnPageChangeListener mPagerChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }

        @Override
        public void onPageSelected(int position) {
        }

        @Override
        public void onPageScrollStateChanged(int state) {
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();

        mLikeForumListener = null;
        mRefreshListener = null;
        mBaWuSearchViewListener = null;
        mBaWuSearchListener = null;
        mPagerChangeListener = null;
    }
}
