package com.leozzyzheng.tiekiller.event;

import android.os.Handler;
import android.os.Looper;
import com.leozzyzheng.tiekiller.ui.base.BaseActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by leozzyzheng on 2015/4/1.
 * 非常简单的事件派发,在注册的消息太多的情况下可能性能有点捉急，所以这就要求使用这个类不能有太多的Activity存在于台面上
 */
public class EventCenter {

    private static EventCenter instance;

    private EventCenter() {

    }

    public static EventCenter getInstance() {
        if (instance == null) {
            synchronized (EventCenter.class) {
                if (instance == null) {
                    instance = new EventCenter();
                }
            }
        }

        return instance;
    }

    private Map<BaseActivity, List<MSG.MSG_CODE>> mActivityToMsgMap = new HashMap<BaseActivity, List<MSG.MSG_CODE>>();

    /**
     * 监听消息的发送
     *
     * @param activity 监听者
     * @param msg      消息
     */
    public void listen(BaseActivity activity, MSG.MSG_CODE msg) {
        synchronized (this) {
            List<MSG.MSG_CODE> msgList = mActivityToMsgMap.get(activity);

            if (msgList == null) {
                msgList = new ArrayList<MSG.MSG_CODE>();
            }

            msgList.add(msg);
            mActivityToMsgMap.put(activity, msgList);
        }

    }

    /**
     * 移除Activity相关的所有监听
     *
     * @param activity Activity
     */
    public void remove(BaseActivity activity) {
        remove(activity, null);
    }

    /**
     * 移除Activity指定的msg监听
     *
     * @param activity Activity
     * @param msg      消息
     */
    public void remove(BaseActivity activity, MSG.MSG_CODE msg) {
        synchronized (this) {
            if (msg == null) {
                mActivityToMsgMap.remove(activity);
            } else {
                List<MSG.MSG_CODE> msgList = mActivityToMsgMap.get(activity);

                if (msgList != null) {
                    msgList.remove(msg);
                }
            }
        }
    }

    /**
     * 触发消息发送，同步操作
     *
     * @param msg  消息
     * @param data 数据
     */
    public void trigger(MSG.MSG_CODE msg, Object data) {
        synchronized (this) {
            for (Map.Entry<BaseActivity, List<MSG.MSG_CODE>> mapping : mActivityToMsgMap.entrySet()) {
                List<MSG.MSG_CODE> msgList = mapping.getValue();

                if (msgList != null && msgList.contains(msg)) {
                    mapping.getKey().onReceivedMsg(new MSG(msg, data));
                }
            }
        }
    }

    /**
     * 延后触发，异步操作,会放在主线程里面加载
     *
     * @param msg  消息
     * @param data 数据
     */
    public void post(final MSG.MSG_CODE msg, final Object data) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                trigger(msg, data);
            }
        });
    }
}
