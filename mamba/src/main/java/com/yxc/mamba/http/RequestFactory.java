package com.yxc.mamba.http;

import com.yxc.mamba.http.okhttp.OkHttpRequest;
import com.yxc.mamba.http.urlconnection.URLRequest;

/**
 * RequestFactory
 * Created by robin on 15/12/4.
 *
 * @author yangxc
 */
public class RequestFactory {

    public static BaseRequest createOkHttpRequest(String tag){

        return new OkHttpRequest(tag);
    }

    public static BaseRequest createURLRequest(String tag){

        return new URLRequest(tag);
    }

}
