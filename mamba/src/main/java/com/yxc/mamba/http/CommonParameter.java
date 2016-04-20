package com.yxc.mamba.http;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * CommonParameter——用于管理公共参数及Header等信息
 * Created by robin on 15/12/4.
 *
 * @author yangxc
 */
class CommonParameter implements ICommonParameter {

    private static CommonParameter ourInstance;

    public static CommonParameter getInstance() {
        if (ourInstance==null){
            synchronized (CommonParameter.class){
                if (ourInstance==null){
                    ourInstance = new CommonParameter();
                }
            }
        }
        return ourInstance;
    }

    private CommonParameter() {
        normalParamMap = new HashMap<>();
        headerMap = new HashMap<>();
    }


    private Map<String, Object> normalParamMap;

    private Map<String, RequestHeader> headerMap;

    @Override
    public void addCommonParameter(String key, Object value){
        normalParamMap.put(key, value);
    }

    @Override
    public void removeCommonParameter(String key){
        normalParamMap.remove(key);
    }

    @Override
    public void clearCommonParameter(){
        normalParamMap.clear();
    }

    @Override
    public void addCommonHeader(String key, String value){
        headerMap.put(key, new RequestHeader(key, value));
    }

    @Override
    public void removeCommonHeader(String key){
        headerMap.remove(key);
    }

    @Override
    public void clearCommonHeader(){
        headerMap.clear();
    }

    @Override
    public void clearAll(){
        normalParamMap.clear();
        headerMap.clear();
    }

    @Override
    public Map<String, Object> getCommonParameters(){
        return normalParamMap;
    }

    @Override
    public Collection<RequestHeader> getCommonHeaders(){
        return headerMap.values();
    }
}
