package com.yxc.mambalibrary.http;

import com.yxc.mambalibrary.http.RequestException;

/**
 * BaseResponse
 * Created by robin on 15/12/7.
 *
 * @author yangxc
 */
public class BaseResponse {

    private int errCode;

    private String errMsg;

    private Object result;

    public BaseResponse() {
    }

    public BaseResponse(Object result) {
        this.result = result;
    }

    public BaseResponse(int errCode, String errMsg) {
        this.errCode = errCode;
        this.errMsg = errMsg;
    }

    public int getErrCode() {
        return errCode;
    }

    public void setErrCode(int errCode) {
        this.errCode = errCode;
    }

    public String getErrMsg() {
        return errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }

    public void setResult(Object result) {
        this.result = result;
    }

    public Object getResult(){
        return result;
    }

    public boolean hasError() {
        return errCode>0;
    }

    public RequestException throwExceptionWhenHasError() {
        return new RequestException(errCode, errMsg);
    }

}
