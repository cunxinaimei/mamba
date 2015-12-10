package com.yxc.mamba.http;

/**
 * RequestHeader
 * Created by robin on 15/12/4.
 *
 * @author yangxc
 */
public class RequestHeader {

    private String key;

    private String value;

    public RequestHeader(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
