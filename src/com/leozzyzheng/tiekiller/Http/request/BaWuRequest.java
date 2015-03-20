package com.leozzyzheng.tiekiller.http.request;

import android.content.Context;
import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.leozzyzheng.tiekiller.account.AccountManger;
import com.leozzyzheng.tiekiller.http.HttpEngineProvider;
import com.leozzyzheng.tiekiller.http.VolleyHttpEngine;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by leozzyzheng on 2015/3/20.
 */
public class BaWuRequest extends StringRequest {

    public BaWuRequest(Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(Method.POST, "http://tieba.baidu.com/bawu2/platform/index?word=%E6%B9%96%E5%8D%97%E5%A4%A7%E5%AD%A6&ie=utf-8", listener, errorListener);
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        Map<String, String> a = new HashMap<String, String>();
        a.put("Cookie", "BDUSS=" + AccountManger.getInstance().getBdusstoken());
        return a;
    }

    public void send(Context context) {
        VolleyHttpEngine.getInstance(context).send(this);
    }
}
