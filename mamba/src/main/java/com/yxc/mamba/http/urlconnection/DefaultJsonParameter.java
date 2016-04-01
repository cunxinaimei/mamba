package com.yxc.mamba.http.urlconnection;

import android.util.Log;
import com.yxc.mamba.tool.JsonParser;
import org.json.JSONException;

/**
 * JsonParameter for HttpUrlConnection
 * Created by robin on 16/4/1.
 *
 * @author yangxc
 */
public class DefaultJsonParameter extends DefaultParameter {
    public DefaultJsonParameter(String url) {
        super(url);
    }

    @Override
    public String generatePostBodyString() {
        try {
            return JsonParser.mapToJson(getParameters()).toString();
        } catch (JSONException e) {
            Log.e(TAG, "Parameters to JSON Error");
            e.printStackTrace();
        }
        return null;
    }
}
