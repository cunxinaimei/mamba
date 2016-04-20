package com.yxc.mamba.http.okhttp;

import android.util.Log;
import com.yxc.mamba.tool.JsonParser;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import org.json.JSONException;

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
        String jsonParam = null;
        try {
            jsonParam = JsonParser.mapToJson(getParameters()).toString();
        } catch (JSONException e) {
            Log.e(TAG, "Parameters to JSON Error");
            e.printStackTrace();
        }
        return RequestBody.create(MediaType.parse("application/json; charset=utf-8"), jsonParam);
    }


}
