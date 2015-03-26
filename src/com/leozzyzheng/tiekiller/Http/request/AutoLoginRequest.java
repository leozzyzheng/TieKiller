package com.leozzyzheng.tiekiller.http.request;

import com.leozzyzheng.tiekiller.account.AccountManger;
import com.leozzyzheng.tiekiller.http.UrlAddr;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

/**
 * Created by leozzyzheng on 2015/3/19.
 * 自动登录请求,也用来获取用户的详细信息
 */
public class AutoLoginRequest extends JsonObjectBaseRequest {

    private OnLoginListenr listener;

    public static interface OnLoginListenr {
        public void onLoginSuccess();

        public void onLoginFailed(String message);
    }

    public AutoLoginRequest(OnLoginListenr listener) {
        super(UrlAddr.AUTO_LOGIN, LOGIN_TYPE_BODY, POST);
        this.listener = listener;
    }

    @Override
    public Map<String, String> getBodyParams() {
        return null;
    }

    @Override
    protected void onRequestSuccess(JSONObject data) {
        try {
            JSONObject user = data.getJSONObject("user");
            String id = user.getString("id");
            String name = user.getString("name");
            String bduss = user.getString("BDUSS");
            String portrait = user.getString("portrait");

            AccountManger.getInstance().setName(name);
            AccountManger.getInstance().setId(id);
            AccountManger.getInstance().setBdusstoken(bduss);
            AccountManger.getInstance().setPortraitMd5(portrait);

            if (listener != null) {
                listener.onLoginSuccess();
            }

        } catch (JSONException e) {
            if (listener != null) {
                listener.onLoginFailed("自动登录失败,请手动登陆");
            }
        }
    }

    @Override
    protected void onRequestFailed(Exception reason) {
        if (listener != null) {
            listener.onLoginFailed("自动登录失败,请手动登陆");
        }
    }
}
