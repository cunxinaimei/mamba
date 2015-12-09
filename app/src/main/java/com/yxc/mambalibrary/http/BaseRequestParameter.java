package com.yxc.mambalibrary.http;

import android.text.TextUtils;

import java.util.*;

/**
 * BaseRequestParameter
 * Created by robin on 15/12/7.
 *
 * @author yangxc
 */
public class BaseRequestParameter {

    private String url;
    private Map<String, String> normalParamMap;
    private List<RequestHeader> headerList;

    public BaseRequestParameter(String url) {
        this.url = url;
        normalParamMap = new HashMap<>();
        headerList = new ArrayList<>();
    }

    public String generateUrlParams() {
        StringBuilder stringBuilder = new StringBuilder();
        for (String key : normalParamMap.keySet()) {
            String value = normalParamMap.get(key);
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

    public void addParameter(String key, String value) {
        normalParamMap.put(key, value);
    }

    public void addHeader(String key, String value) {
        headerList.add(new RequestHeader(key, value));
    }

    public Map<String, String> getParameters() {
        return normalParamMap;
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
