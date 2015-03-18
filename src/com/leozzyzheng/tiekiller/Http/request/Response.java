package com.leozzyzheng.tiekiller.http.request;

/**
 * Created by leozzyzheng on 2015/3/18.
 */
public class Response {

    private byte[] mRaw;
    private boolean mIsSucc;
    private Exception mError;

    public boolean isSucc() {
        return mIsSucc;
    }

    public Exception getError() {
        return mError;
    }

    public byte[] getRaw() {
        return mRaw;
    }

    public Response succ(byte[] raw) {
        mIsSucc = true;
        mRaw = raw;
        return this;
    }

    public Response fail(Exception e) {
        mIsSucc = false;
        mError = e;
        return this;
    }
}

