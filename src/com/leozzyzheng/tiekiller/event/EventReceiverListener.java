package com.leozzyzheng.tiekiller.event;

/**
 * Created by leozzyzheng on 2015/5/6.
 * 监听事件回调的接口,实现这个接口才能注册事件监听
 */
public interface EventReceiverListener {
    public void onReceivedMsg(Msg msg);
}
