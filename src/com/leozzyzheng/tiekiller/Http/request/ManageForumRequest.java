package com.leozzyzheng.tiekiller.http.request;

import com.leozzyzheng.tiekiller.controller.ForumManager;
import com.leozzyzheng.tiekiller.http.data.ManageForumListData;
import com.leozzyzheng.tiekiller.http.exception.CannotParseDataException;
import com.leozzyzheng.tiekiller.utils.SerializedPhpParser;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by leozzyzheng on 2015/4/22.
 * 获取有管理权限贴吧的请求,这里只能获取贴吧名称，额外的数据没有,所以需要进一步的获取数据
 */
public class ManageForumRequest extends ForumPageRequest {

    public ManageForumRequest() {
        super("百度", 0);
    }

    @Override
    protected void onRequestSuccess(JSONObject data) {
        try {
            String card = data.getJSONObject("user").getJSONObject("new_user_info").getString("card");
            SerializedPhpParser phpParser = new SerializedPhpParser(card, "GBK");
            HashMap phpObject = (HashMap) phpParser.parse();
            ManageForumListData manageForumListData = ManageForumListData.parse(phpObject);
            ForumManager.getInstance().__onReceivedManagerForumRequest(manageForumListData);
        } catch (JSONException e) {
            ForumManager.getInstance().__onReceivedManagerForumRequest(null);
        } catch (CannotParseDataException e) {
            ForumManager.getInstance().__onReceivedManagerForumRequest(null);
        }
    }

    @Override
    protected void onRequestFailed(Exception reason) {
        ForumManager.getInstance().__onReceivedManagerForumRequest(null);
    }
}
