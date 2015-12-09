package com.yxc.mambalibrary.http.okhttp;

import com.squareup.okhttp.OkHttpClient;

/**
 * OkHttpClientHolder
 * Created by robin on 15/12/4.
 *
 * @author yangxc
 */
public class OkHttpClientHolder {

    private static OkHttpClientHolder instance;

    public static OkHttpClientHolder getInstance(){
        if (instance == null) {
            synchronized (OkHttpClientHolder.class) {
                if (instance == null) {
                    instance = new OkHttpClientHolder();
                }
            }
        }
        return instance;
    }

    private OkHttpClient client;

    private OkHttpClientHolder(){
        client = new OkHttpClient();
    }

    public OkHttpClient getClient() {
        return client;
    }
}
