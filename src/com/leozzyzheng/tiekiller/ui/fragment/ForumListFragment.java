package com.leozzyzheng.tiekiller.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;
import com.leozzyzheng.tiekiller.R;
import com.leozzyzheng.tiekiller.controller.ForumManager;
import com.leozzyzheng.tiekiller.event.Msg;
import com.leozzyzheng.tiekiller.http.data.BaWuForumListData;
import com.leozzyzheng.tiekiller.http.data.LikeForumListData;
import com.leozzyzheng.tiekiller.ui.adapter.BaWuForumAdapter;
import com.leozzyzheng.tiekiller.ui.adapter.LikeForumListAdapter;
import com.leozzyzheng.tiekiller.ui.adapter.SimplePageAdapter;
import com.leozzyzheng.tiekiller.ui.base.BaseFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by leozzyzheng on 2015/4/23.
 * 显示贴吧列表的Fragment，目前有两个页面，一个是喜欢的贴吧页面，一个是有吧务权限的页面
 */
public class ForumListFragment extends BaseFragment {
    private View mWholeView;

    public static class ViewHolder {
        public ViewHolder(View whole) {
            this.whole = whole;
        }

        View whole;
        ListView listView;
        SwipeRefreshLayout swipeRefreshLayout;
    }

    final private static int LIKE_FORUM_INDEX = 0;
    final private static int MANAGE_FORUM_INDEX = 1;
    final private static int PAGE_INDEX_LENGTH = 2;

    private ViewPager mPagers;
    private List<ViewHolder> mViewArray = new ArrayList<ViewHolder>(PAGE_INDEX_LENGTH);

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mWholeView = inflater.inflate(R.layout.fragment_forum, container, false);
        initView(inflater);
        initViewData();
        return mWholeView;
    }

    /**
     * 初始化界面
     */
    private void initView(LayoutInflater inflater) {
        mPagers = (ViewPager) mWholeView.findViewById(R.id.pagers);

        View likeForumView = inflater.inflate(R.layout.list_like_forum, mPagers, false);
        View baWuForumView = inflater.inflate(R.layout.list_bawu_forum, mPagers, false);
        mViewArray.add(new ViewHolder(likeForumView));
        mViewArray.add(new ViewHolder(baWuForumView));

        //处理公共的UI部分
        for (ViewHolder viewHolder : mViewArray) {
            viewHolder.listView = (ListView) viewHolder.whole.findViewById(R.id.forum_list_view);
            viewHolder.swipeRefreshLayout = (SwipeRefreshLayout) viewHolder.whole.findViewById(R.id.swipe);
            viewHolder.swipeRefreshLayout.setColorSchemeResources(R.color.holo_blue_dark, R.color.holo_blue_light, R.color.holo_green_light, R.color.holo_green_light);
            viewHolder.swipeRefreshLayout.setOnRefreshListener(mRefreshListener);
        }

        //处理特殊的UI部分
        List<View> list = new ArrayList<View>();
        list.add(LIKE_FORUM_INDEX, likeForumView);
        list.add(MANAGE_FORUM_INDEX, baWuForumView);
        mPagers.setAdapter(new SimplePageAdapter(list));
    }

    /**
     * 初始化View相关的数据
     */
    private void initViewData() {

    }

    private SwipeRefreshLayout.OnRefreshListener mRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            switch (mPagers.getCurrentItem()) {
                case LIKE_FORUM_INDEX:
                    ForumManager.getInstance().requestLikeForum();
                    break;
                case MANAGE_FORUM_INDEX:
                    ForumManager.getInstance().requestManagerForum();
                    break;
            }
        }
    };

    @Override
    public boolean listenMsg() {
        return true;//表示一开始就要监听消息的接收
    }

    @Override
    protected void onReceivedMsg(Msg msg) {
        super.onReceivedMsg(msg);

        //喜欢贴吧的列表请求返回
        if (msg.getCode() == Msg.LIKE_FORUM_REQUEST) {
            ViewHolder viewHolder = mViewArray.get(LIKE_FORUM_INDEX);

            if (msg.getData() != null) {
                LikeForumListData data = (LikeForumListData) msg.getData();

                if (viewHolder.listView.getAdapter() == null) {
                    LikeForumListAdapter likeForumAdapter = new LikeForumListAdapter(getAppContext(), data.getLikeForumDatas());
                    viewHolder.listView.setAdapter(likeForumAdapter);
                } else {
                    LikeForumListAdapter likeForumAdapter = (LikeForumListAdapter) viewHolder.listView.getAdapter();
                    likeForumAdapter.setData(data.getLikeForumDatas());
                    likeForumAdapter.notifyDataSetInvalidated();
                }
            } else {
                Toast.makeText(getAppContext(), getString(R.string.no_network_toast), Toast.LENGTH_SHORT).show();
            }

            viewHolder.swipeRefreshLayout.setRefreshing(false);
        } else if (msg.getCode() == Msg.MANAGE_FORUM_REQUEST) {
            ViewHolder viewHolder = mViewArray.get(MANAGE_FORUM_INDEX);

            if (msg.getData() != null) {
                BaWuForumListData data = (BaWuForumListData) msg.getData();
                if (viewHolder.listView.getAdapter() == null) {
                    BaWuForumAdapter baWuForumAdapter = new BaWuForumAdapter(getAppContext(), data.getForumList());
                    viewHolder.listView.setAdapter(baWuForumAdapter);
                } else {
                    BaWuForumAdapter baWuForumAdapter = (BaWuForumAdapter) viewHolder.listView.getAdapter();
                    baWuForumAdapter.setData(data.getForumList());
                    baWuForumAdapter.notifyDataSetInvalidated();
                }

            } else {
                Toast.makeText(getAppContext(), getString(R.string.no_network_toast), Toast.LENGTH_SHORT).show();
            }

            viewHolder.swipeRefreshLayout.setRefreshing(false);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        mRefreshListener = null;
    }
}
