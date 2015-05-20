package com.leozzyzheng.tiekiller.event;

import android.os.Handler;
import android.os.Looper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by leozzyzheng on 2015/4/1.
 * 非常简单的事件派发,在注册的消息太多的情况下可能性能有点捉急，所以这就要求使用这个类不能有太多的Listener存在于台面上
 * 不过也许写那么多阻塞的Listener本身的业务逻辑就有问题
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

    private Map<EventReceiverListener, List<Msg.MsgCode>> mListenerToMsgMap = new HashMap<EventReceiverListener, List<Msg.MsgCode>>();

    /**
     * 监听消息的发送
     *
     * @param listener 监听者
     * @param msg      消息
     */
    public void listen(EventReceiverListener listener, Msg.MsgCode msg) {
        synchronized (this) {
            List<Msg.MsgCode> msgList = mListenerToMsgMap.get(listener);

            if (msgList == null) {
                msgList = new ArrayList<Msg.MsgCode>();
            }

            msgList.add(msg);
            mListenerToMsgMap.put(listener, msgList);
        }

    }

    /**
     * 移除listener相关的所有监听
     *
     * @param listener listener
     */
    public void remove(EventReceiverListener listener) {
        remove(listener, null);
    }

    /**
     * 移除listener指定的msg监听
     *
     * @param listener listener
     * @param msg      消息
     */
    public void remove(EventReceiverListener listener, Msg.MsgCode msg) {
        synchronized (this) {
            if (msg == null) {
                mListenerToMsgMap.remove(listener);
            } else {
                List<Msg.MsgCode> msgList = mListenerToMsgMap.get(listener);

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
    public void trigger(Msg.MsgCode msg, Object data) {
        synchronized (this) {
            for (Map.Entry<EventReceiverListener, List<Msg.MsgCode>> mapping : mListenerToMsgMap.entrySet()) {
                List<Msg.MsgCode> msgList = mapping.getValue();

                if (msgList != null && msgList.contains(msg)) {
                    mapping.getKey().onReceivedMsg(new Msg(msg, data));
                }
            }
        }
    }

    /**
     * 重载，见{@link #trigger(Msg.MsgCode msg, Object data)}
     *
     * @param msg 不需要附带数据的消息
     */
    public void trigger(Msg.MsgCode msg) {
        trigger(msg, null);
    }

    /**
     * 延后触发，异步操作,会放在主线程里面加载
     *
     * @param msg  消息
     * @param data 数据
     */
    public void post(final Msg.MsgCode msg, final Object data) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                trigger(msg, data);
            }
        });
    }
}
