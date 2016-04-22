package com.yxc.mamba.http.okhttp;

import com.yxc.mamba.http.*;
import okhttp3.*;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.Collection;

/**
 * OkHttpRequest
 * Created by robin on 15/12/9.
 *
 * @author yangxc
 */
public class OkHttpRequest extends BaseRequest {

    public OkHttpRequest(String tag) {
        super(tag);
    }

    @Override
    protected <T extends Parameter> void doGet(T parameter) {
        Request.Builder builder = new Request.Builder();
        builder = builder.url(parameter.generateGetURL());
        for (RequestHeader header: parameter.getHeaders()){
            builder.addHeader(header.getKey(), header.getValue());
        }
        doCall(builder.build());
    }

    @Override
    protected <T extends Parameter> void doPost(T parameter) {
        OkHttpParameter okParameter = (OkHttpParameter) parameter;
        Request.Builder builder = new Request.Builder();
        builder.url(okParameter.getUrl());
        builder.post(okParameter.generateRequestBody());
        Collection<RequestHeader> headers = okParameter.getHeaders();
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
        return new BaseResponse(RequestException.CODE_UNKNOWN, errMsg);
    }

    private void doCall(Request request){
        OkHttpClient client = OkHttpClientHolder.getInstance().getClient();
        Call call = client.newCall(request);
        requestStarted();
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                RequestException exception;
                if (e instanceof SocketTimeoutException){
                    exception = new RequestException(ExceptionEnum.EXCEPTION_SOCKET_TIMED_OUT);
                }else{
                    exception = new RequestException(RequestException.CODE_UNKNOWN, e.toString());
                }
                requestFailure(exception);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                int code = response.code();
                String message = response.message();
                if (code>=400){
                    requestFailure(new RequestException(code, message));
                    return;
                }
                requestSuccess(response);
            }
        });
    }
}
