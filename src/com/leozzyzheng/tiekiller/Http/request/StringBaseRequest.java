package com.leozzyzheng.tiekiller.http.request;

/**
 * Created by leozzyzheng on 2015/3/26.
 * 字符串返回类型的请求基础
 */
public abstract class StringBaseRequest extends BaseRequest<String> {

    protected StringBaseRequest(String url) {
        super(url);
    }

    protected StringBaseRequest(String url, int loginTye, int method) {
        super(url, loginTye, method);
    }

    @Override
    protected String parseData(Response response) {
        byte[] raw = response.getRaw();
        return new String(raw);
    }
}
