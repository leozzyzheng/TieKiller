package com.leozzyzheng.tiekiller.http.exception;

/**
 * Created by leozzyzheng on 2015/3/18.
 */
public class CannotParseDataException extends Exception {
    public CannotParseDataException() {
        super("Cannot parse http response to specific data!");
    }
}
