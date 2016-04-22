package com.yxc.mamba.http;

/**
 * 类注释
 * Created by robin on 16/4/22.
 *
 * @author robin
 */
public enum ExceptionEnum {
    EXCEPTION_SOCKET_TIMED_OUT(10001, "连接超时");
//    EXCEPTION_SOCKET_TIMED_OUT(10001, "连接超时");

    int errCode;
    String message;

    ExceptionEnum(int errCode, String message) {
        this.errCode = errCode;
        this.message = message;
    }
}
