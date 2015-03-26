package com.leozzyzheng.tiekiller.http.request;

import com.leozzyzheng.tiekiller.account.AccountManger;
import com.leozzyzheng.tiekiller.http.data.BaWuForumListData;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

/**
 * Created by leozzyzheng on 2015/3/20.
 * 获取吧务信息的请求
 */
public class BaWuForumRequest extends StringBaseRequest {

    public static interface OnBaWuListReceivedListener {
        public void onReceived(BaWuForumListData forumListData);

        public void onFailed();
    }

    private OnBaWuListReceivedListener mListener;

    public BaWuForumRequest(String name, OnBaWuListReceivedListener listener) {
        super("");

        String gbkName;
        try {
            gbkName = URLEncoder.encode(name, "GBK");
        } catch (UnsupportedEncodingException e) {
            gbkName = name;
        }

        setUrl("http://tieba.baidu.com/bawu2/platform/index?word=" + gbkName);
        mListener = listener;
    }

    @Override
    public Map<String, String> getBodyParams() {
        return null;
    }

    @Override
    protected void onRequestSuccess(String data) {
        BaWuForumListData forumListData = BaWuForumListData.parse(data);

        if (forumListData != null) {
            mListener.onReceived(forumListData);
        } else {
            mListener.onFailed();
        }
    }

    @Override
    protected void onRequestFailed(Exception reason) {
        if (mListener != null) {
            mListener.onFailed();
        }
    }
}
