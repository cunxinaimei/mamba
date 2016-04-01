package com.yxc.mamba.http;

import android.os.Looper;

import java.util.HashMap;

/**
 * BaseRequest
 * Created by robin on 15/12/7.
 *
 * @author yangxc
 */
public abstract class BaseRequest {
    protected CallBackHandler callBackHandler;

    protected String tag;
    private BaseRequestParameter parameter;
    private Method method;

    public BaseRequest(String tag) {
        this.tag = tag;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public Method getMethod() {
        return method;
    }

    public void doRequest(Method method, BaseRequestParameter parameter, RequestCallBack callBack) {
        initCallBackHandler(callBack);
        this.method = method;
        this.parameter = parameter;
        switch (method) {
            case GET:
                doGet(parameter);
                break;
            case POST:
                doPost(parameter);
                break;
            default:
                break;
        }
    }

    public abstract boolean isAsynchronous();

    protected abstract <T extends BaseRequestParameter> void doGet(T parameter);

    protected abstract <T extends BaseRequestParameter> void doPost(T parameter);

    protected abstract BaseResponse transferToResponse(Object originResponse);


    protected void requestStarted(){
        HashMap<String, Object> messageObj = new HashMap<>();
        messageObj.put(CallBackHandler.KEY_REQUEST, this);
        messageObj.put(CallBackHandler.KEY_PARAMETER, parameter);
        callBackHandler.sendMessage(callBackHandler.obtainMessage(CallBackHandler.EVENT_TYPE_START, messageObj));
    }

    protected void requestSuccess(Object originResponse){
        HashMap<String, Object> messageObj = new HashMap<>();
        messageObj.put(CallBackHandler.KEY_REQUEST, this);
        messageObj.put(CallBackHandler.KEY_RESPONSE, transferToResponse(originResponse));
        callBackHandler.sendMessage(callBackHandler.obtainMessage(CallBackHandler.EVENT_TYPE_SUCCESS, messageObj));
    }

    protected void requestFailure(RequestException e){
        HashMap<String, Object> messageObj = new HashMap<>();
        messageObj.put(CallBackHandler.KEY_REQUEST, this);
        //TODO--Exception 处理
        messageObj.put(CallBackHandler.KEY_ERROR, e);
        callBackHandler.sendMessage(callBackHandler.obtainMessage(CallBackHandler.EVENT_TYPE_FAILURE, messageObj));
    }

    private void initCallBackHandler(RequestCallBack callBack){
        if (callBackHandler==null){
            callBackHandler = new CallBackHandler(Looper.getMainLooper(), callBack);
        }
    }
}
