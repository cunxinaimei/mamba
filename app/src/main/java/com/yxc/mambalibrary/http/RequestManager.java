package com.yxc.mambalibrary.http;

import java.util.Map;

/**
 * RequestManager
 * Created by robin on 15/12/4.
 *
 * @author yangxc
 */
public class RequestManager {

    private static HttpType defaultType = HttpType.OK_HTTP;

    public static void get(String tag,
                           final BaseRequestParameter parameter,
                           final RequestCallBack callBack) {
        doRequest(tag, defaultType, Method.GET, parameter, callBack);
    }
    public static void post(String tag,
                           final BaseRequestParameter parameter,
                           final RequestCallBack callBack) {
        doRequest(tag, defaultType, Method.POST, parameter, callBack);
    }

    public static void get(String tag, HttpType httpType,
                           final BaseRequestParameter parameter,
                           final RequestCallBack callBack) {
        doRequest(tag, httpType, Method.GET, parameter, callBack);
    }
    public static void post(String tag, HttpType httpType,
                           final BaseRequestParameter parameter,
                           final RequestCallBack callBack) {
        doRequest(tag, httpType, Method.POST, parameter, callBack);
    }

    private static void doRequest(String tag, HttpType httpType,
                                  final Method method,
                                  final BaseRequestParameter parameter,
                                  final RequestCallBack callBack){
        BaseRequest request = null;
        switch (httpType) {
            case OK_HTTP:
                request = RequestFactory.createOkHttpRequest(tag);
                break;
            case HTTP_URL_CONNECTION:
                request = RequestFactory.createURLRequest(tag);
                break;
            default:
                break;
        }
        Map<String, String> commonParams = getCommonParameter().getCommonParameters();
        for (String key: commonParams.keySet()){
            parameter.addParameter(key, commonParams.get(key));
        }
        for (RequestHeader header: getCommonParameter().getCommonHeaders()){
            parameter.addHeader(header.getKey(), header.getValue());
        }
        final BaseRequest finalRequest = request;
        if (finalRequest.isAsynchronous()){
            finalRequest.doRequest(method, parameter, callBack);
        }else {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    finalRequest.doRequest(method, parameter, callBack);
                }
            }).start();
        }
    }

    public static void addCommonParameter(String key, String value){
        getCommonParameter().addCommonParameter(key, value);
    }

    public static void removeCommonParameter(String key){
        getCommonParameter().removeCommonParameter(key);
    }

    public static void addCommonHeader(String key, String value){
        getCommonParameter().addCommonHeader(key, value);
    }

    public static void removeCommonHeader(String key){
        getCommonParameter().removeCommonHeader(key);
    }

    public static void clearCommonParameter(){
        getCommonParameter().clearCommonParameter();
    }

    public static void clearCommonHeader(){
        getCommonParameter().clearCommonHeader();
    }

    public static void clearAllCommonInfo(){
        getCommonParameter().clearAll();
    }

    private static ICommonParameter getCommonParameter() {
        return CommonParameter.getInstance();
    }

}
