package com.leozzyzheng.tiekiller.http.request;

import com.leozzyzheng.tiekiller.http.UrlAddr;
import org.json.JSONObject;

import java.util.Map;

/**
 * Created by leozzyzheng on 2015/3/20.
 * 请求所有关注的吧
 */
public class RecommentForumRequest extends JsonObjectBaseRequest {
    public RecommentForumRequest() {
        super(UrlAddr.FORUMRECOMMEND);
    }

    @Override
    public Map<String, String> getBodyParams() {
        return null;
    }

    @Override
    protected void onRequestSuccess(JSONObject data) {
        int a = 0;
    }

    @Override
    protected void onRequestFailed(Exception reason) {
        int b = 0;
    }
}
