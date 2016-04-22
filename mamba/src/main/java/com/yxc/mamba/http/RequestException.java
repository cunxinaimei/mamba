package com.yxc.mamba.http;

/**
 * RequestException
 * Created by robin on 15/12/4.
 *
 * @author yangxc
 */
public class RequestException extends Exception {

    public static final int CODE_UNKNOWN = 1000;
    public static final int CODE_SOCKET_TIMED_OUT = 1001;

    private int errorCode;

    private String errorMessage;

    public RequestException() {
        super();
    }

    public RequestException(ExceptionEnum exceptionEnum){
        super();
        this.errorCode = exceptionEnum.errCode;
        this.errorMessage = exceptionEnum.message;
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

    @Override
    public String toString() {
        return "Error Code : " + getErrorCode() + " ; " + "Error Message : " + getMessage();
    }
}
