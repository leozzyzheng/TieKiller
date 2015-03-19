package com.leozzyzheng.tiekiller.http;

import android.content.Context;
import com.leozzyzheng.tiekiller.http.request.HttpEngine;

/**
 * Created by leozzyzheng on 2015/3/18.
 * Http请求引擎提供类
 */
public class HttpEngineProvider {
    private static HttpEngineProvider mInstance;

    public static HttpEngineProvider getInstance() {
        synchronized (HttpEngineProvider.class) {
            if (mInstance == null) {
                mInstance = new HttpEngineProvider();
            }

            return mInstance;
        }
    }

    private HttpEngineProvider() {
    }

    private VolleyHttpEngine mVolleyEngine;

    public void createVollyEngine(Context context) {
        mVolleyEngine = VolleyHttpEngine.getInstance(context.getApplicationContext());
    }

    public HttpEngine getEngine() {
        return mVolleyEngine;
    }
}
