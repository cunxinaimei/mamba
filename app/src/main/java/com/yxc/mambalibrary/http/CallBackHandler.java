package com.yxc.mambalibrary.http;


import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import java.util.HashMap;
import java.util.Map;

/**
 * CallBackHandler
 * Created by robin on 15/12/4.
 *
 * @author yangxc
 */
public class CallBackHandler extends Handler {

    public final String TAG = "CallBackHandler";

    public final static int EVENT_TYPE_START = 0;
    public final static int EVENT_TYPE_SUCCESS = 1;
    public final static int EVENT_TYPE_FAILURE = 2;

    public final static String KEY_REQUEST = "request";
    public final static String KEY_RESPONSE = "response";
    public final static String KEY_ERROR = "error";
    public final static String KEY_PARAMETER = "parameter";

    private RequestCallBack callBack;

    public CallBackHandler() {
        super(Looper.getMainLooper());
    }

    public CallBackHandler(Looper looper, RequestCallBack callBack) {
        super(looper);
        this.callBack = callBack;
    }

    public void setCallBack(RequestCallBack callBack) {
        this.callBack = callBack;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        if (callBack == null) {
            Log.w("CallBackHandler WARNING", "CallBack is null");
            return;
        }
        HashMap<String, Object> messageObj = null;
        try {
            messageObj = (HashMap<String, Object>) msg.obj;
        } catch (Exception e) {
            Log.e("CallBackHandler ERROR", "CallBackHandler 内部错误 : " + e.toString());
            return;
        }
        BaseRequest request = (BaseRequest) messageObj.get(KEY_REQUEST);
        BaseResponse response = (BaseResponse) messageObj.get(KEY_RESPONSE);
        RequestException error = (RequestException) messageObj.get(KEY_ERROR);
        BaseRequestParameter parameter = (BaseRequestParameter) messageObj.get(KEY_PARAMETER);
        switch (msg.what) {
            case EVENT_TYPE_START:
                requestStart(request, parameter);
                break;
            case EVENT_TYPE_SUCCESS:
                if (request==null){
                    throw new RuntimeException("Request is null, please check");
                }
                if (response.hasError()){
                    requestFailure(request, response.throwExceptionWhenHasError());
                } else {
                    requestSuccess(request, response);
                }
                break;
            case EVENT_TYPE_FAILURE:
                requestFailure(request, error);
                break;
            default:
                break;
        }
    }

    private void requestStart(BaseRequest request, BaseRequestParameter parameter){
        Log.i(TAG, "******************* Request " + request.getTag() + " start ********************");
        Log.i(TAG, "URL : " + parameter.getUrl());
        if (request.getMethod()== Method.GET) {
            Log.i(TAG, "URL FOR GET : " + parameter.generateGetURL());
        }
        Log.i(TAG, "PARAMETER : " + getParamString(parameter));
        Log.i(TAG, "HEADER : " + getHeaderString(parameter));
        Log.i(TAG, "*******************************************************************************");

        callBack.onStart(request);
    }

    private void requestSuccess(BaseRequest request, BaseResponse response){
        Log.i(TAG, "******************* Request " + request.getTag() + " success *****************");
        Log.i(TAG, "Response : " + response.getResult().toString());
        callBack.onSuccess(request, response);
    }

    private void requestFailure(BaseRequest request, RequestException error){
        Log.i(TAG, "******************* Request " + request.getTag() + " failure *****************");
        Log.i(TAG, "Exception : " + error.getMessage());
        callBack.onFailure(request, error);
    }

    private String getParamString(BaseRequestParameter parameter){
        StringBuilder paramBuilder = new StringBuilder();
        Map<String, String> params = parameter.getParameters();
        for (String key: params.keySet()){
            paramBuilder.append(key)
                    .append("=")
                    .append(params.get(key))
                    .append(";");
        }
        if (paramBuilder.length()>0){
            paramBuilder.deleteCharAt(paramBuilder.length()-1);
        }
        return paramBuilder.toString();
    }

    private String getHeaderString(BaseRequestParameter parameter){
        StringBuilder headerBuilder = new StringBuilder();
        for (RequestHeader header: parameter.getHeaders()){
            headerBuilder.append(header.getKey())
                    .append("=")
                    .append(header.getValue())
                    .append(";");
        }
        if (headerBuilder.length()>0){
            headerBuilder.deleteCharAt(headerBuilder.length()-1);
        }
        return headerBuilder.toString();
    }
}
