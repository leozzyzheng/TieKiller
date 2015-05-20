package com.leozzyzheng.tiekiller.ui.base;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.view.MenuItem;
import com.leozzyzheng.tiekiller.R;
import com.leozzyzheng.tiekiller.event.EventCenter;
import com.leozzyzheng.tiekiller.event.EventReceiverListener;
import com.leozzyzheng.tiekiller.event.Msg;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by leozzyzheng on 2015/3/26.
 * Activity的基类，用来做一些统一的操作
 */
public class BaseActivity extends Activity implements EventReceiverListener {

    private List<BaseFragment> mBaseFragments = new ArrayList<BaseFragment>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar bar = getActionBar();

        if (bar != null) {
            bar.setDisplayHomeAsUpEnabled(true);
        }
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

    final protected void addListen(Msg.MsgCode msg) {
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
    public void onReceivedMsg(final Msg msg) {
        if (isFinishing()) {
            return;
        }

        //先发送给自己所有的Fragment
        for (BaseFragment fragment : mBaseFragments) {
            fragment.onReceivedMsg(msg);
        }

        if (!onReceivedDirectThread(msg)) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    onReceivedOnUIThread(msg);
                }
            });
        }
    }

    /**
     * 直接发送事件,如果事件回调本来就在UI线程，该函数还是会先调用
     *
     * @param msg 派发的事件
     * @return true 表示事件已经处理，后续不再通知一次UI线程的派发函数<br/>
     * false 表示事件没有处理，需要后续再派发一次UI线程的
     */
    public boolean onReceivedDirectThread(Msg msg) {
        return false;
    }

    /**
     * 在UI线程派发消息，如果消息已经在非UI线程处理了，则这个函数不会被调用
     *
     * @param msg 消息
     */
    public void onReceivedOnUIThread(Msg msg) {

    }
}
