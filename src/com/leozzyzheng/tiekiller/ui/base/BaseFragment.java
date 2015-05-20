package com.leozzyzheng.tiekiller.ui.base;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import com.leozzyzheng.tiekiller.event.Msg;
import com.leozzyzheng.tiekiller.utils.Tools;

/**
 * Created by leozzyzheng on 2015/4/23.
 * 所有Fragment通用的基类
 */
public abstract class BaseFragment extends Fragment {

    /**
     * 返回这个Fragment在最开始初始化的时候是否需要设置成为监听消息
     *
     * @return 是否监听
     */
    abstract public boolean listenMsg();

    /**
     * 手动取消Fragment监听Msg
     */
    protected void removeFromListenMsg() {
        Activity activity = getActivity();

        if (activity instanceof BaseActivity) {
            BaseActivity baseActivity = (BaseActivity) activity;
            baseActivity.removeBaseFragmentFromListen(this);
        }
    }

    /**
     * 手动添加Fragment监听Msg
     *
     * @param listen 是否监听，是则添加，否则取消
     */
    protected void setListenMsg(boolean listen) {
        if (listen) {
            Activity activity = getActivity();
            if (activity instanceof BaseActivity) {
                BaseActivity baseActivity = (BaseActivity) activity;
                baseActivity.addBaseFragmentToListen(this);
            }
        } else {
            removeFromListenMsg();
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //之所以重写这个函数，是因为这个函数会在UI初始化之后调用，所以也能够防止消息的发送触发空指针异常
        setListenMsg(listenMsg());
    }

    /**
     * 接收从Activity中转过来的Msg，这个会比父的处理更早，但是是否调用取决于是否监听
     *
     * @param msg 消息
     */
    protected void onReceivedMsg(final Msg msg) {
        if (isRemoving()) {
            return;
        }


        if (!onReceivedDirectThread(msg)) {
            Tools.runInUiThread(new Runnable() {
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

    /**
     * 返回appContext
     *
     * @return appContext
     */
    public Context getAppContext() {
        return getActivity().getApplicationContext();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        removeFromListenMsg();
    }
}
