package com.leozzyzheng.tiekiller.http.request;

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
    public RecommendedForumRequest(OnReceivedLikeForumListener listener) {
        super(UrlAddr.FORUMRECOMMEND);
        this.mListener = listener;
    }

    private OnReceivedLikeForumListener mListener;

    public static interface OnReceivedLikeForumListener {
        public void onReceived(LikeForumListData likeForumListData);

        public void onFailed();
    }

    @Override
    public Map<String, String> getBodyParams() {
        return null;
    }

    @Override
    protected void onRequestSuccess(JSONObject data) {
        try {
            LikeForumListData likeForumListData = LikeForumListData.parse(data);
            if (mListener != null) {
                mListener.onReceived(likeForumListData);
            }

        } catch (JSONException e) {
            if (mListener != null) {
                mListener.onFailed();
            }
        }

    }

    @Override
    protected void onRequestFailed(Exception reason) {
        if (mListener != null) {
            mListener.onFailed();
        }
    }
}
