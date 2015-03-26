package com.leozzyzheng.tiekiller.http.request;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by leozzyzheng on 2015/3/19.
 * Json对象返回类型的请求基础
 */
public abstract class JsonObjectBaseRequest extends BaseRequest<JSONObject> {

    protected JsonObjectBaseRequest(String url) {
        super(url);
    }

    protected JsonObjectBaseRequest(String url, int loginTye, int method) {
        super(url, loginTye, method);
    }

    @Override
    protected JSONObject parseData(Response response) {
        byte[] raw = response.getRaw();
        try {
            return new JSONObject(new String(raw));
        } catch (JSONException e) {
            return null;
        }

    }
}
