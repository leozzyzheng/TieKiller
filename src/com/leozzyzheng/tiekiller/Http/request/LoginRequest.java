package com.leozzyzheng.tiekiller.http.request;

import com.leozzyzheng.tiekiller.account.AccountManger;
import com.leozzyzheng.tiekiller.http.UrlAddr;
import com.leozzyzheng.tiekiller.utils.JsRSAUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by leozzyzheng on 2015/3/19.
 * 登陆请求协议
 */
public class LoginRequest extends JsonObjectBaseRequest {

    private String username;
    private String password;
    private OnLoginListenr loginListenr;

    public static interface OnLoginListenr {
        public void onLoginSuccess();

        public void onLoginFailed(String message);
    }

    public LoginRequest(String username, String password, OnLoginListenr listener) {
        super(UrlAddr.LOGIN, LOGIN_TYPE_NOTHING, POST);
        this.username = username;
        this.password = password;
        this.loginListenr = listener;
    }

    @Override
    public Map<String, String> getBodyParams() {
        String t = "";

        String mod = "B3C61EBBA4659C4CE3639287EE871F1F48F7930EA977991C7AFE3CC442FEA49643212E7D570C853F368065CC57A2014666DA8AE7D493FD47D171C0D894EEE3ED7F99F6798B7FFD7B5873227038AD23E3197631A8CB642213B9F27D4901AB0D92BFA27542AE890855396ED92775255C977F5C302F1E7ED4B1E369C12CB6B1822F";
        String exp = "10001";
        JsRSAUtils.KeyPair keyPair = JsRSAUtils.getInstance().generateKeyPair(exp, "", mod);
        String encryptedPassword = JsRSAUtils.getInstance().encryptedString(keyPair, password + t);

        Map<String, String> body = new HashMap<String, String>();
        body.put("username", username);
        body.put("password", encryptedPassword);
        body.put("verifycode", "");
        body.put("clientfrom", "");
        body.put("tpl", "tb");
        body.put("login_share_strategy", "silent");
        body.put("client", "android");
        body.put("t", t);
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
        body.put("servertime", t);
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

                if (loginListenr != null) {
                    loginListenr.onLoginSuccess();
                    return;
                }
            }

        } catch (JSONException e) {
        }

        if (loginListenr != null) {
            loginListenr.onLoginFailed("登陆失败，账号或密码错误或者是登陆太频繁了");
        }
    }

    @Override
    protected void onRequestFailed(Exception reason) {
        if (loginListenr != null) {
            loginListenr.onLoginFailed("网络不好");
        }
    }

    @SuppressWarnings("unused")
    private boolean parserXml(String xmlStr) {
        StringReader sr = null;

        try {
            sr = new StringReader(xmlStr);
            InputSource is = new InputSource(sr);
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document document = db.parse(is);

            Element data = (Element) document.getElementsByTagName("data").item(0);
            Element errno = (Element) data.getElementsByTagName("errno").item(0);
            if (errno.getNodeValue().equalsIgnoreCase("0")) {
                Element res = (Element) data.getElementsByTagName("res").item(0);
                NodeList infos = res.getChildNodes();

                String uname = null;
                String uid = null;
                String portrait = null;
                String bduss = null;

                for (int i = 0; i < infos.getLength(); ++i) {
                    Node info = infos.item(i);
                    String nodeName = info.getNodeName();
                    if (nodeName.equalsIgnoreCase("displayname")) {
                        uname = info.getNodeValue();
                    } else if (nodeName.equalsIgnoreCase("uid")) {
                        uid = info.getNodeValue();
                    } else if (nodeName.equalsIgnoreCase("portrait")) {
                        portrait = info.getNodeValue();
                    } else if (nodeName.equalsIgnoreCase("bduss")) {
                        bduss = info.getNodeValue();
                    }
                }

                if (uname != null && uid != null && portrait != null && bduss != null) {
                    AccountManger.getInstance().setName(uname);
                    AccountManger.getInstance().setId(uid);
                    AccountManger.getInstance().setPortrait(portrait);
                    AccountManger.getInstance().setBdusstoken(bduss);
                    return true;
                }
            }
            return false;
        } catch (Exception e) {
            return false;
        } finally {
            if (sr != null) {
                sr.close();
            }
        }
    }
}
