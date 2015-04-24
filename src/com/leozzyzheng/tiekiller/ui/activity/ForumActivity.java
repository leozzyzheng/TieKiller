package com.leozzyzheng.tiekiller.ui.activity;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.SparseArray;
import android.view.View;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;
import com.leozzyzheng.tiekiller.R;
import com.leozzyzheng.tiekiller.event.MSG;
import com.leozzyzheng.tiekiller.http.data.BaWuForumListData;
import com.leozzyzheng.tiekiller.http.data.LikeForumListData;
import com.leozzyzheng.tiekiller.http.request.BaWuForumRequest;
import com.leozzyzheng.tiekiller.http.request.RecommendedForumRequest;
import com.leozzyzheng.tiekiller.ui.adapter.BaWuForumAdapter;
import com.leozzyzheng.tiekiller.ui.adapter.LikeForumListAdapter;
import com.leozzyzheng.tiekiller.ui.adapter.SimplePageAdapter;
import com.leozzyzheng.tiekiller.ui.base.BaseActivity;
import com.leozzyzheng.tiekiller.ui.fragment.ForumListFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by leozzyzheng on 2015/3/26.
 * 喜欢的吧，有权限的吧的界面
 */
public class ForumActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forum);

        addListen(MSG.LIKE_FORUM_REQUEST_SUCCESS);
        addListen(MSG.MANAGE_FORUM_REQUEST_SUCCESS);

        FragmentManager fm = getFragmentManager();

        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.forum_list_container, new ForumListFragment());
        ft.commit();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
