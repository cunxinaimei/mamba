package com.yxc.mambalibrary.http;

/**
 * RequestException
 * Created by robin on 15/12/4.
 *
 * @author yangxc
 */
public class RequestException extends Exception {

    private int errorCode;

    private String errorMessage;

    public RequestException() {
        super();
    }

    public RequestException(int errorCode, String errorMessage) {
        super();
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    @Override
    public String getMessage() {
        return errorMessage == null ? "The exception message is ** null **" : errorMessage;
    }

    public int getErrorCode() {
        return errorCode;
    }
}
