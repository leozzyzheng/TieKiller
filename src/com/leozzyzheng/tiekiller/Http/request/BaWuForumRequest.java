package com.leozzyzheng.tiekiller.http.request;

import com.leozzyzheng.tiekiller.controller.ForumManager;
import com.leozzyzheng.tiekiller.http.data.BaWuForumListData;
import com.leozzyzheng.tiekiller.http.exception.CannotParseDataException;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

/**
 * Created by leozzyzheng on 2015/3/20.
 * 获取吧务信息的请求
 */
public class BaWuForumRequest extends StringBaseRequest {

    public BaWuForumRequest(String name) {
        super("");

        String gbkName;
        try {
            gbkName = URLEncoder.encode(name, "GBK");
        } catch (UnsupportedEncodingException e) {
            gbkName = name;
        }

        setUrl("http://tieba.baidu.com/bawu2/platform/index?word=" + gbkName);
    }

    @Override
    public Map<String, String> getBodyParams() {
        return null;
    }

    @Override
    protected void onRequestSuccess(String data) {
        try {
            BaWuForumListData forumListData = BaWuForumListData.parse(data);
            ForumManager.getInstance().__onReceivedBaWuForumRequest(forumListData);
        } catch (CannotParseDataException e) {
            ForumManager.getInstance().__onReceivedBaWuForumRequest(null);
        }
    }

    @Override
    protected void onRequestFailed(Exception reason) {
        ForumManager.getInstance().__onReceivedBaWuForumRequest(null);
    }
}
