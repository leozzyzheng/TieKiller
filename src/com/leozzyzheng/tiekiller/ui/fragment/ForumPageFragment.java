package com.leozzyzheng.tiekiller.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.leozzyzheng.tiekiller.ui.base.BaseFragment;

/**
 * Created by leozzyzheng on 2015/4/24.
 * 显示贴吧帖子列表
 */
public class ForumPageFragment extends BaseFragment {

    @Override
    public boolean listenMsg() {
        return true;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }
}
