package com.yxc.mamba.http;

import android.util.Log;

/**
 * 默认的CallBack, 做了统一的onStart的处理
 * Created by robin on 16/4/20.
 *
 * @author yangxc
 */
public abstract class DefaultCallBack implements RequestCallBack {

    public static final String TAG = DefaultCallBack.class.getSimpleName();

    @Override
    public void onStart(BaseRequest request) {
        Log.d(TAG, "Request **"+request.getTag()+"** has started");
    }
}
