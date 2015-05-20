package com.leozzyzheng.tiekiller.http.request;

import com.leozzyzheng.tiekiller.controller.ForumManager;
import com.leozzyzheng.tiekiller.http.UrlAddr;
import com.leozzyzheng.tiekiller.http.data.LikeForumListData;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

/**
 * Created by leozzyzheng on 2015/3/20.
 * 请求所有关注的吧
 */
public class RecommendedForumRequest extends JsonObjectBaseRequest {
    public RecommendedForumRequest() {
        super(UrlAddr.FORUMRECOMMEND);
    }

    @Override
    public Map<String, String> getBodyParams() {
        return null;
    }

    @Override
    protected void onRequestSuccess(JSONObject data) {
        try {
            LikeForumListData likeForumListData = LikeForumListData.parse(data);
            ForumManager.getInstance().__onReceivedLikeFormRequest(likeForumListData);
        } catch (JSONException e) {
            ForumManager.getInstance().__onReceivedLikeFormRequest(null);
        }
    }

    @Override
    protected void onRequestFailed(Exception reason) {
        ForumManager.getInstance().__onReceivedLikeFormRequest(null);
    }
}
