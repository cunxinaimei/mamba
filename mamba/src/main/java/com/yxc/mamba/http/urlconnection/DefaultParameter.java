package com.yxc.mamba.http.urlconnection;

import com.yxc.mamba.http.BaseRequest;
import com.yxc.mamba.http.BaseRequestParameter;

/**
 * DefaultParameter for UrlConnection
 * Created by robin on 16/3/31.
 *
 * @author yangxc
 */
public class DefaultParameter extends BaseRequestParameter {
    public DefaultParameter(String url) {
        super(url);
    }

    @Override
    public BaseRequest boundRequest(String tag) {
        return new DefaultRequest(tag);
    }
}
