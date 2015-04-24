package com.leozzyzheng.tiekiller.ui.base;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.view.MenuItem;
import com.leozzyzheng.tiekiller.R;
import com.leozzyzheng.tiekiller.event.EventCenter;
import com.leozzyzheng.tiekiller.event.MSG;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by leozzyzheng on 2015/3/26.
 * Activity的基类，用来做一些统一的操作
 */
public class BaseActivity extends Activity {

    private List<BaseFragment> mBaseFragments = new ArrayList<BaseFragment>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar bar = getActionBar();

        if (bar != null) {
            bar.setDisplayHomeAsUpEnabled(true);
        }

        addListen(MSG.NONE);
        addListen(MSG.MSG_PROCEEDING_STATE);
    }

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        return super.onMenuItemSelected(featureId, item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mBaseFragments.clear();
        EventCenter.getInstance().remove(this);
    }

    final protected void addListen(MSG.MSG_CODE msg) {
        EventCenter.getInstance().listen(this, msg);
    }

    /**
     * 将Activity的BaseFragment加入到消息监听中,这个方法是不需要自己去调用的
     *
     * @param fragment BaseFragment
     */
    final public void addBaseFragmentToListen(BaseFragment fragment) {
        mBaseFragments.add(fragment);
    }

    /**
     * 将Activity的BaseFragment消息监听列表中去掉,这个方法是不需要自己去调用的
     *
     * @param fragment BaseFragment
     */
    final public void removeBaseFragmentFromListen(BaseFragment fragment) {
        mBaseFragments.remove(fragment);
    }

    /**
     * 接收监听请求，重写时请务必调用父类方法
     *
     * @param msg 消息
     */
    public void onReceivedMsg(MSG msg) {
        if (isFinishing()) {
            return;
        }

        //先发送给自己所有的Fragment
        for (BaseFragment fragment : mBaseFragments) {
            fragment.onReceivedMsg(msg);
        }

        if (getActionBar() != null) {
            //处理一下状态，如果有文字，则app的标题栏显示该文字，否则则显示应用名称
            if (msg.getCode() == MSG.MSG_PROCEEDING_STATE) {
                if (msg.getData() == null) {
                    getActionBar().setTitle(R.string.app_name);
                } else {
                    getActionBar().setTitle((String) msg.getData());
                }
            } else if (msg.getCode() == MSG.NONE) {
                getActionBar().setTitle(R.string.app_name);
            }
        }
    }
}
