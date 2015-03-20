package com.leozzyzheng.tiekiller.http;

import android.content.Context;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.leozzyzheng.tiekiller.http.request.BaseRequest;
import com.leozzyzheng.tiekiller.http.request.HttpEngine;

import java.util.Map;

/**
 * Created by leozzyzheng on 2015/3/18.
 * 由google的Volley网络框架实现的Http请求方法
 */
public class VolleyHttpEngine implements HttpEngine {
    private static VolleyHttpEngine mInstance;

    public static VolleyHttpEngine getInstance(Context context) {
        synchronized (VolleyHttpEngine.class) {
            if (mInstance == null) {
                mInstance = new VolleyHttpEngine(context);
            }

            return mInstance;
        }
    }

    private RequestQueue mQueue;

    private VolleyHttpEngine(Context context) {
        mQueue = Volley.newRequestQueue(context);
        mQueue.start();
    }

    @Override
    public void send(final BaseRequest<?> request) {
        BaseRequestWrapper volleyRequest = new BaseRequestWrapper(request, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                request.onNetworkRequestFinish(new com.leozzyzheng.tiekiller.http.request.Response().succ(s.getBytes()));
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                request.onNetworkRequestFinish(new com.leozzyzheng.tiekiller.http.request.Response().fail(volleyError));
            }
        });
        mQueue.add(volleyRequest);
    }

    public void send(Request r) {
        mQueue.add(r);
    }

    /**
     * 用来包装基础请求用于适配Volley的类
     */
    private static class BaseRequestWrapper extends StringRequest {

        private BaseRequest mBaseRequest;

        public BaseRequestWrapper(BaseRequest request, Response.Listener<String> listener, Response.ErrorListener errorListener) {
            super(request.getMethod(), request.getUrl(), listener, errorListener);
            mBaseRequest = request;
        }

        @Override
        public Map<String, String> getHeaders() {
            return mBaseRequest.parseHeader();
        }

        @Override
        public byte[] getBody() {
            return mBaseRequest.parseBody().getBytes();
        }
    }
}
