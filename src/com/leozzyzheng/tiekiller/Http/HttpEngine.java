package com.leozzyzheng.tiekiller.http;

import com.leozzyzheng.tiekiller.http.request.BaseRequest;

/**
 * Created by leozzyzheng on 2015/3/12.
 * Http请求引擎，如果不在Android上运行需要更换别的实现方法
 */
public interface HttpEngine {
    public void send(BaseRequest<?> request);
}
