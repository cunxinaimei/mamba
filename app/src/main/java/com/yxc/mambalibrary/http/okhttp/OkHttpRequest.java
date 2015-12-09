package com.yxc.mambalibrary.http.okhttp;

import com.squareup.okhttp.*;
import com.yxc.mambalibrary.http.*;

import java.io.IOException;
import java.util.Collection;
import java.util.Map;

/**
 * 类注释
 * Created by robin on 15/12/9.
 *
 * @author yangxc
 */
public class OkHttpRequest extends BaseRequest {

    public OkHttpRequest(String tag) {
        super(tag);
    }

    @Override
    protected void doGet(BaseRequestParameter parameter) {
        Request.Builder builder = new Request.Builder();
        builder = builder.url(parameter.generateGetURL());
        for (RequestHeader header: parameter.getHeaders()){
            builder.addHeader(header.getKey(), header.getValue());
        }
        doCall(builder.build());
    }

    @Override
    protected void doPost(BaseRequestParameter parameter) {
        Request.Builder builder = new Request.Builder();
        FormEncodingBuilder form = new FormEncodingBuilder();
        Map<String, String> params = parameter.getParameters();
        if (params!=null) {
            for (String key :params.keySet()){
                form.add(key, params.get(key));
            }
        }
        builder.url(parameter.getUrl());
        builder.post(form.build());
        Collection<RequestHeader> headers = parameter.getHeaders();
        if (headers!=null) {
            for (RequestHeader header : headers) {
                builder.addHeader(header.getKey(), header.getValue());
            }
        }
        doCall(builder.build());
    }

    @Override
    public boolean isAsynchronous() {
        return true;
    }

    @Override
    public BaseResponse transferToResponse(Object originResponse) {
        Response response = (Response) originResponse;
        String errMsg = "";
        try {
            return new BaseResponse(response.body().string());
        } catch (IOException e) {
            errMsg = e.toString();
        }
        return new BaseResponse(1, errMsg);
    }

    private void doCall(Request request){
        OkHttpClient client = OkHttpClientHolder.getInstance().getClient();
        Call call = client.newCall(request);
        requestStarted();
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                requestFailure(new RequestException(1, e.toString()));
            }

            @Override
            public void onResponse(Response response) throws IOException {
                requestSuccess(response);
            }
        });
    }
}
