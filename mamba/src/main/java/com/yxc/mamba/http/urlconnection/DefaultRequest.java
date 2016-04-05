package com.yxc.mamba.http.urlconnection;

import android.util.Log;
import com.yxc.mamba.http.*;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * URLRequest with HttpUrlConnection
 * Created by robin on 15/12/4.
 *
 * @author yangxc
 */
public class DefaultRequest extends BaseRequest {

    private final String TAG = "URLRequest";

    public DefaultRequest(String tag) {
        super(tag);
    }

    @Override
    protected <T extends BaseRequestParameter> void doGet(T parameter) {
        HttpURLConnection connection = null;
        try {
            URL url = new URL(parameter.generateGetURL());
            connection = (HttpURLConnection) url.openConnection();
            for (RequestHeader header: parameter.getHeaders()) {
                connection.setRequestProperty(header.getKey(), header.getValue());
            }
            doCall(connection);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected <T extends BaseRequestParameter> void doPost(T parameter) {
        HttpURLConnection connection = null;
        OutputStreamWriter wr = null;
        try {
            URL url = new URL(parameter.getUrl());
            connection = (HttpURLConnection) url.openConnection();
            for (RequestHeader header : parameter.getHeaders()) {
                connection.setRequestProperty(header.getKey(), header.getValue());
            }
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            connection.setDoOutput(true);
            String paramStr = parameter.generatePostBodyString();
            Log.d(TAG, paramStr);
            wr = new OutputStreamWriter(connection.getOutputStream(), "UTF-8");
            wr.write(paramStr);
            wr.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (wr!=null) {
                    wr.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        doCall(connection);

    }

    private void doCall(HttpURLConnection connection) {
        InputStream in = null;
        try {
            requestStarted();
            in = new BufferedInputStream(connection.getInputStream());
            String result = readInStream(in);
            requestSuccess(result);
            Log.d(TAG, result);
        } catch (IOException e) {
            requestFailure(new RequestException(-1, e.toString()));
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public BaseResponse transferToResponse(Object originResponse) {
        return new BaseResponse(originResponse);
    }

    @Override
    public boolean isAsynchronous() {
        return false;
    }

    private String readInStream(InputStream in) {
        Scanner scanner = new Scanner(in).useDelimiter("\\A");
        return scanner.hasNext() ? scanner.next() : "";
    }
}
