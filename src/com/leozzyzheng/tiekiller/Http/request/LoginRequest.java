package com.leozzyzheng.tiekiller.http.request;

import com.leozzyzheng.tiekiller.account.AccountManger;
import com.leozzyzheng.tiekiller.http.UrlAddr;
import com.leozzyzheng.tiekiller.utils.JsRSAUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by leozzyzheng on 2015/3/19.
 * 登陆请求协议
 */
public class LoginRequest extends JsonObjectBaseRequest {

    private String username;
    private String password;
    private String encryptedPassword;
    private OnLoginListenr loginListenr;

    public static interface OnLoginListenr {
        public void onLoginSuccess();

        public void onLoginFailed(String message);
    }

    public LoginRequest(String username, String password, OnLoginListenr listener) {
        super(UrlAddr.LOGIN, false, POST);
        this.username = username;
        this.password = password;
        this.loginListenr = listener;
    }

    @Override
    public Map<String, String> getBodyParams() {
        if (encryptedPassword == null) {
            String mod = "B3C61EBBA4659C4CE3639287EE871F1F48F7930EA977991C7AFE3CC442FEA49643212E7D570C853F368065CC57A2014666DA8AE7D493FD47D171C0D894EEE3ED7F99F6798B7FFD7B5873227038AD23E3197631A8CB642213B9F27D4901AB0D92BFA27542AE890855396ED92775255C977F5C302F1E7ED4B1E369C12CB6B1822F";
            String exp = "10001";
            JsRSAUtils.KeyPair keyPair = JsRSAUtils.getInstance().generateKeyPair(exp, "", mod);
            encryptedPassword = JsRSAUtils.getInstance().encryptedString(keyPair, password);
        }

        Map<String, String> body = new HashMap<String, String>();
        body.put("username", username);
        body.put("password", encryptedPassword);
        body.put("verifycode", "");
        body.put("clientfrom", "");
        body.put("tpl", "tb");
        body.put("login_share_strategy", "silent");
        body.put("client", "android");
        body.put("t", "" + System.currentTimeMillis());
        body.put("act", "implicit");
        body.put("loginInitType", "0");
        body.put("loginLink", "0");
        body.put("smsLoginLink", "0");
        body.put("lPFastRegLink", "0");
        body.put("fastRegLink", "1");
        body.put("lPlayout", "0");
        body.put("action", "login");
        body.put("loginmerge", "1");
        body.put("isphone", "0");
        body.put("servertime", "0");//这里可能要改
        body.put("logLoginType", "sdk_login");
        return body;
    }

    @Override
    protected void onRequestSuccess(JSONObject data) {
        try {
            JSONObject errInfo = data.getJSONObject("errInfo");
            int errNo = errInfo.optInt("no", -1);

            if (errNo == 0) {
                //登陆成功
                JSONObject d = data.getJSONObject("data");
                String bduss = d.getString("bduss");
                AccountManger.getInstance().setBdusstoken(bduss);
                AccountManger.getInstance().setName(username);
                loginListenr.onLoginSuccess();
            }

        } catch (JSONException e) {
            loginListenr.onLoginFailed("登陆失败，账号或密码错误或者是登陆太频繁了");
        }
    }

    @Override
    protected void onRequestFailed(Exception reason) {
        loginListenr.onLoginFailed("网络不好");
    }
}
