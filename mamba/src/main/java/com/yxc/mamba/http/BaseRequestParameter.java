package com.yxc.mamba.http;

import android.text.TextUtils;

import java.io.File;
import java.util.*;

/**
 * BaseRequestParameter
 * Created by robin on 15/12/7.
 *
 * @author yangxc
 */
public abstract class BaseRequestParameter {

    public static String TAG = BaseRequestParameter.class.getSimpleName();

    protected String url;
    protected Map<String, Object> normalParamMap;
    protected Map<String, File> fileMap;
    protected List<RequestHeader> headerList;

    public BaseRequestParameter(String url) {
        this.url = url;
        normalParamMap = new HashMap<>();
        headerList = new ArrayList<>();
    }

    public abstract BaseRequest boundRequest(String tag);

    public String generateUrlParams() {
        StringBuilder stringBuilder = new StringBuilder();
        for (String key : normalParamMap.keySet()) {
            Object valueObj = normalParamMap.get(key);
            if (!(valueObj instanceof String)){
                continue;
            }
            String value = (String) valueObj;
            if (TextUtils.isEmpty(value)) {
                value = "";
            }
            stringBuilder.append("&");
            stringBuilder.append(key);
            stringBuilder.append("=");
//            try {
//                value = URLEncoder.encode(value, "utf-8");
//            } catch (UnsupportedEncodingException e) {
//                Log.e("DefaultParameter ERROR", "URLEncoder error with charset utf-8");
//                value = "";
//            }
            stringBuilder.append(value);
        }
        if (stringBuilder.length() > 0) {
            stringBuilder.deleteCharAt(0);

        }
        return stringBuilder.toString();
    }

    public String generatePostBodyString(){
        return generateUrlParams();
    }

    public void addParameter(String key, Object value) {
        normalParamMap.put(key, value);
    }

    public void addFileParameter(String key, File file){
        if (fileMap==null){
            fileMap = new HashMap<>();
        }
        fileMap.put(key, file);
    }

    public void addHeader(String key, String value) {
        headerList.add(new RequestHeader(key, value));
    }

    public Map<String, Object> getParameters() {
        return normalParamMap;
    }

    public Map<String, File> getFileParameters(){
        return fileMap;
    }

    public Collection<RequestHeader> getHeaders() {
        return headerList;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public String generateGetURL() {
        String params = generateUrlParams();
        if (params.length() == 0) {
            return url;
        }
        StringBuilder wholeUrl = new StringBuilder(url);
        if (url.contains("?")) {
            if (url.endsWith("?")) {
                wholeUrl.append(params);
            } else {
                wholeUrl.append("&").append(params);
            }
        } else {
            wholeUrl.append("?").append(params);
        }
        return wholeUrl.toString();
    }
}
