package com.leozzyzheng.tiekiller.ui.activity;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.res.Configuration;
import android.os.Bundle;
import com.leozzyzheng.tiekiller.R;
import com.leozzyzheng.tiekiller.event.Msg;
import com.leozzyzheng.tiekiller.ui.base.BaseActivity;
import com.leozzyzheng.tiekiller.ui.fragment.ForumListFragment;

/**
 * Created by leozzyzheng on 2015/3/26.
 * 喜欢的吧，有权限的吧的界面
 */
public class ForumActivity extends BaseActivity {
    private final static String forumListFragmentTag = "forum_list";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forum);

        addListen(Msg.LIKE_FORUM_REQUEST);
        addListen(Msg.MANAGE_FORUM_REQUEST);

        FragmentManager fm = getFragmentManager();

        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.forum_list_container, new ForumListFragment(), forumListFragmentTag);
        ft.commit();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }
}
