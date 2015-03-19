package com.leozzyzheng.tiekiller.http.request;

import com.leozzyzheng.tiekiller.account.AccountManger;
import com.leozzyzheng.tiekiller.http.HttpEngineProvider;
import com.leozzyzheng.tiekiller.http.exception.CannotParseDataException;
import com.leozzyzheng.tiekiller.utils.MD5;

import java.util.*;

/**
 * Created by leozzyzheng on 2015/3/12.
 * 基础请求，所有请求都是继承自这个请求，这个请求可以自定义是否需要带登陆信息
 * T是模板类，用来规定请求返回的数据类型
 */
public abstract class BaseRequest<T> {
    private boolean mIsNeedLoginInfo = false;//默认不带登陆信息
    private Map<String, String> mCookieHeader = new HashMap<String, String>();
    private Map<String, String> mHeaderMap = new HashMap<String, String>();
    private int mMethod;
    private String mUrl;

    protected static final int POST = 1;
    protected static final int GET = 0;

    protected BaseRequest(String url) {
        this(url, true, POST);
    }

    protected BaseRequest(String url, boolean isNeed, int method) {
        setIsNeedLoginInfo(isNeed);
        mMethod = method;
        mUrl = url;
        mCookieHeader.put("ka", "open");
    }

    public void setIsNeedLoginInfo(boolean isNeed) {
        mIsNeedLoginInfo = isNeed;
    }

    protected void setCookieHeader(String key, String value) {
        if (key != null) {
            if (value == null) {
                mCookieHeader.remove(key);
            } else {
                mCookieHeader.put(key, value);
            }
        }
    }

    protected void setHttpHeader(String key, String value) {
        if (key != null) {
            if (value == null) {
                mCookieHeader.remove(key);
            } else {
                mCookieHeader.put(key, value);
            }
        }
    }

    public int getMethod() {
        return mMethod;
    }

    public String getUrl() {
        return mUrl;
    }

    /**
     * 获取最终的请求头
     *
     * @return 请求头
     */
    final public Map<String, String> parseHeader() {
        Map<String, String> header = new HashMap<String, String>();

        String cookie = "";
        for (Map.Entry<String, String> mapping : mCookieHeader.entrySet()) {
            cookie += mapping.getKey() + "=" + mapping.getValue() + "; ";
        }

        String a = mHeaderMap.remove("cookie");
        String b = mHeaderMap.remove("Cookie");

        if (a != null && a.length() != 0) {
            cookie += a + "; ";
        }

        if (b != null && b.length() != 0) {
            cookie += b + "; ";
        }

        for (Map.Entry<String, String> mapping : mHeaderMap.entrySet()) {
            header.put(mapping.getKey(), mapping.getValue());
        }

        header.put("Cookie", cookie);

        return header;
    }

    /**
     * 生成数据body,根据标示看看要不要带登陆信息
     *
     * @return 数据body
     */
    final public String parseBody() {
        Map<String, String> params = getBodyParams();

        if (params == null) {
            params = new LinkedHashMap<String, String>();
        }

        String imei = "8633568451235";//随便写的

        if (mIsNeedLoginInfo) {
            params.put("_client_type", "2");
            params.put("_client_version", "6.5.8");
            params.put("_phone_imei", imei);
            params.put("_phone_newimei", MD5.getMD5((imei + "0").getBytes()).toUpperCase());
            params.put("bdusstoken", AccountManger.getInstance().getBdusstoken());
            params.put("channel_id", "");
            params.put("channel_uid", "");
            params.put("cuid", "baidutiebaapp");
            params.put("from", "Ad_xiaomi");
            params.put("ka", "open");
            params.put("model", "2015011");
            params.put("stErrorNums", "0");
            params.put("timestamp", "" + System.currentTimeMillis());
        }

        //字典排序
        List<Map.Entry<String, String>> list = new ArrayList<Map.Entry<String, String>>(params.entrySet());
        Collections.sort(list, new Comparator<Map.Entry<String, String>>() {
                    public int compare(Map.Entry<String, String> o1,
                                       Map.Entry<String, String> o2) {
                        return o1.getKey().compareTo(o2.getKey());
                    }
                }
        );

        String param = "";
        String body = "";
        for (Map.Entry<String, String> mapping : params.entrySet()) {
            param += mapping.getKey() + "=" + mapping.getValue();
            body += mapping.getKey() + "=" + mapping.getValue() + "&";
        }

        param += "tiebaclient!!!";

        String md5 = MD5.getMD5(param.getBytes());

        body += "sign=" + md5;

        return body;
    }

    abstract public Map<String, String> getBodyParams();

    /**
     * 发送请求
     */
    public void send() {
        HttpEngineProvider.getInstance().getEngine().send(this);
    }

    /**
     * 请求回调，内部调用请勿外部使用
     *
     * @param response 返回内容
     */
    public void onNetworkRequestFinish(Response response) {
        if (response.isSucc()) {
            T data = parseData(response);

            if (data == null) {
                onRequestFailed(new CannotParseDataException());
            } else {
                onRequestSuccess(data);
            }

        } else {
            onRequestFailed(response.getError());
        }
    }

    /**
     * 用来实现对请求返回的内容转换为具体需要的数据结构的函数
     *
     * @param response 请求返回内容
     * @return 具体数据结构
     */
    abstract protected T parseData(Response response);

    /**
     * 请求成功
     *
     * @param data 数据
     */
    abstract protected void onRequestSuccess(T data);

    /**
     * 请求失败
     *
     * @param reason 失败原因
     */
    abstract protected void onRequestFailed(Exception reason);

    public static interface Listener<T> {
        public void onRequestSuccess(T data);

        public void onRequestFailed(Exception reason);
    }
}
