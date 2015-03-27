package com.leozzyzheng.tiekiller.ui;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.widget.ListView;
import com.leozzyzheng.tiekiller.R;
import com.leozzyzheng.tiekiller.http.data.LikeForumListData;
import com.leozzyzheng.tiekiller.http.request.RecommendedForumRequest;
import com.leozzyzheng.tiekiller.ui.adapter.LikeForumListAdapter;
import com.leozzyzheng.tiekiller.ui.base.BaseActivity;

/**
 * Created by leozzyzheng on 2015/3/26.
 * 喜欢的吧，有权限的吧的界面
 */
public class ForumActivity extends BaseActivity {
    private SwipeRefreshLayout swipeView;
    private ListView likeForumListView;
    private LikeForumListAdapter mLikeForumAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forum);
        swipeView = (SwipeRefreshLayout) findViewById(R.id.swipe);
        swipeView.setColorSchemeResources(R.color.holo_blue_dark, R.color.holo_blue_light, R.color.holo_green_light, R.color.holo_green_light);
        swipeView.setOnRefreshListener(mRefreshListener);
        likeForumListView = (ListView) findViewById(R.id.like_forum_list);
    }

    /**
     * 用来回到查询喜欢贴吧数据请求的接口
     */
    private RecommendedForumRequest.OnReceivedLikeForumListener mLikeForumListener = new RecommendedForumRequest.OnReceivedLikeForumListener() {
        @Override
        public void onReceived(LikeForumListData likeForumListData) {
            mLikeForumAdapter = new LikeForumListAdapter(ForumActivity.this, likeForumListData.getLikeForumDatas());
            likeForumListView.setAdapter(mLikeForumAdapter);
            swipeView.setRefreshing(false);
        }

        @Override
        public void onFailed() {
            swipeView.setRefreshing(false);
        }
    };

    private SwipeRefreshLayout.OnRefreshListener mRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            RecommendedForumRequest request = new RecommendedForumRequest(mLikeForumListener);
            request.send();
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();

        mLikeForumListener = null;
        mRefreshListener = null;
    }
}
