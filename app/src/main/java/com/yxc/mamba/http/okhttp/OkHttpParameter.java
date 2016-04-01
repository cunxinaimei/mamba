package com.yxc.mamba.http.okhttp;

import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.MultipartBuilder;
import com.squareup.okhttp.RequestBody;
import com.yxc.mamba.http.BaseRequest;
import com.yxc.mamba.http.BaseRequestParameter;

import java.io.File;
import java.util.Map;

/**
 * Parameters For Request
 * Created by robin on 16/3/30.
 *
 * @author yangxc
 */
public class OkHttpParameter extends BaseRequestParameter {

    public OkHttpParameter(String url) {
        super(url);
    }

    @Override
    public BaseRequest boundRequest(String tag) {
        return new OkHttpRequest(tag);
    }


    public RequestBody generateRequestBody() {
        MultipartBuilder multipartBuilder = new MultipartBuilder();
        if (getFileParameters() != null) {
            for (String key : getFileParameters().keySet()) {
                File file = getFileParameters().get(key);
                multipartBuilder.addFormDataPart(key, key, RequestBody.create(MediaType.parse("application/octet-stream"), file));
            }
        }
        Map<String, Object> params = getParameters();
        for (String key : params.keySet()) {
            Object valueObj = params.get(key);
            if (valueObj instanceof String) {
                multipartBuilder.addFormDataPart(key, (String) valueObj);
            }
        }
        return multipartBuilder.build();
    }
}
