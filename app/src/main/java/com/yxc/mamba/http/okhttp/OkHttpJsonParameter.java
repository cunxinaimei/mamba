package com.yxc.mamba.http.okhttp;

import com.alibaba.fastjson.JSON;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.RequestBody;

/**
 * 类注释
 * Created by robin on 16/3/30.
 *
 * @author yangxc
 */
public class OkHttpJsonParameter extends OkHttpParameter {
    public OkHttpJsonParameter(String url) {
        super(url);
    }

    public RequestBody generateRequestBody() {
        String jsonParam = JSON.toJSONString(getParameters());
        return RequestBody.create(MediaType.parse("application/json; charset=utf-8"), jsonParam);
    }


}
