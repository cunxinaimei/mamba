package com.yxc.mamba.http;

/**
 * RequestCallBack
 * Created by robin on 15/12/7.
 *
 * @author yangxc
 */
public interface RequestCallBack {

    void onStart(BaseRequest request);

    void onFailure(BaseRequest request, RequestException exception);

    void onSuccess(BaseRequest request, BaseResponse response);

}
