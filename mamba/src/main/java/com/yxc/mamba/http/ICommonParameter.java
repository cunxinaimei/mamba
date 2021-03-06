package com.yxc.mamba.http;

import java.util.Collection;
import java.util.Map;

/**
 * 类注释
 * Created by robin on 15/12/4.
 *
 * @author yangxc
 */
public interface ICommonParameter {

    void addCommonParameter(String key, Object value);

    void removeCommonParameter(String key);

    void clearCommonParameter();

    void addCommonHeader(String key, String value);

    void removeCommonHeader(String key);

    void clearCommonHeader();

    void clearAll();

    Map<String, Object> getCommonParameters();

    Collection<RequestHeader> getCommonHeaders();
}
