package com.yxc.mamba.http;

import android.util.Log;
import com.yxc.mamba.http.urlconnection.DefaultParameter;

import java.lang.reflect.Constructor;
import java.util.Map;

/**
 * RequestManager
 * Created by robin on 15/12/4.
 *
 * @author yangxc
 */
public class RequestManager {

    static final String TAG = "RequestManager";

    static Class paramCls;

    public static <T extends Parameter> void injectParameter(Class<T> cls){
        paramCls = cls;
    }

    public static Parameter createParameter(String url) {
        return createParameter(url, paramCls);
    }

    public static <T extends Parameter> Parameter createParameter(String url, Class<T> cls){
        if (cls==null){
            Log.i(TAG, "You can use your own Parameter Class by  \" injectParameter \" function. The default Parameter Class is <DefaultParameter>(based on HttpUrlConnection)");
            Log.i(TAG, "你可以通过injectParameter方法使用你自定义的Parameter类, 默认使用DefaultParameter(基于HttpUrlConnection)");
            cls = (Class<T>) DefaultParameter.class;
        }
        Parameter parameter = null;
        Class[] paramTypes = {String.class};
        Object[] paramValues = {url};
        try {
            Constructor constructor = cls.getConstructor(paramTypes);
            parameter = (Parameter) constructor.newInstance(paramValues);
        } catch (Exception e) {
            throw new RuntimeException("please confirm your Parameter Class has a constructor with url(String)");
        }
        return parameter;
    }

    public static void get(String tag,
                           final Parameter parameter,
                           final RequestCallBack callBack) {
        doRequest(tag, Method.GET, parameter, callBack);
    }
    public static void post(String tag,
                           final Parameter parameter,
                           final RequestCallBack callBack) {
        doRequest(tag, Method.POST, parameter, callBack);
    }

    private static void doRequest(String tag,
                                  final Method method,
                                  final Parameter parameter,
                                  final RequestCallBack callBack){
        BaseRequest request = parameter.boundRequest(tag);
        if (request==null){
            return;
        }
        Map<String, Object> commonParams = getCommonParameter().getCommonParameters();
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

    public static void addCommonParameter(String key, Object value){
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
