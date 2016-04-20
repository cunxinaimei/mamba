package com.yxc.mamba.http.okhttp;

import android.util.Log;
import com.yxc.mamba.http.BaseRequest;
import com.yxc.mamba.http.Parameter;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

import java.io.File;
import java.util.Map;

/**
 * Parameters For Request
 * Created by robin on 16/3/30.
 *
 * @author yangxc
 */
public class OkHttpParameter extends Parameter {

    public OkHttpParameter(String url) {
        super(url);
    }

    @Override
    public BaseRequest boundRequest(String tag) {
        return new OkHttpRequest(tag);
    }


    public RequestBody generateRequestBody() {
        if (getFileParameters() != null && getFileParameters().size()>0) {
            return uploadBody();
        }else{
            return normalBody();
        }
    }

    private RequestBody normalBody(){
        String body1 = generateUrlParams();
        String body2 = generateUrlParams(false, null);
        Log.d(TAG, body1);
        Log.d(TAG, body2);
        return RequestBody.create(MediaType.parse("application/x-www-form-urlencoded"), generateUrlParams(true, "UTF-8"));
    }

    private RequestBody uploadBody(){
        MultipartBody.Builder multipartBuilder = new MultipartBody.Builder();
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
