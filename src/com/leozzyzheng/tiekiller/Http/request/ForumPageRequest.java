package com.leozzyzheng.tiekiller.http.request;

import com.leozzyzheng.tiekiller.http.UrlAddr;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by leozzyzheng on 2015/4/20.
 * 直接获取指定贴吧前多少个帖子
 */
public class ForumPageRequest extends JsonObjectBaseRequest {

    private String forumName;
    private int num;

    public ForumPageRequest(String forumName, int num) {
        super(UrlAddr.FORUMPAGE);
        this.forumName = forumName;
        this.num = num;
    }

    public ForumPageRequest(String forumName) {
        this(forumName, 50);
    }

    @Override
    public Map<String, String> getBodyParams() {
        Map<String, String> body = new HashMap<String, String>();
        body.put("kw", forumName);
        body.put("rn", String.valueOf(num));
        return body;
    }

    @Override
    protected void onRequestSuccess(JSONObject data) {
        int a = 0;
    }

    @Override
    protected void onRequestFailed(Exception reason) {
        int a = 0;
    }
}
