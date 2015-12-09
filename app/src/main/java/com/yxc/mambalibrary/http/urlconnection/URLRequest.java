package com.yxc.mambalibrary.http.urlconnection;

import android.util.Log;
import com.yxc.mambalibrary.http.*;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

/**
 * URLRequest with HttpUrlConnection
 * Created by robin on 15/12/4.
 *
 * @author yangxc
 */
public class URLRequest extends BaseRequest {

    private final String TAG = "URLRequest";

    public URLRequest(String tag) {
        super(tag);
    }

    @Override
    protected void doGet(BaseRequestParameter parameter) {
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
    protected void doPost(BaseRequestParameter parameter) {
        HttpURLConnection connection = null;
        try {
            URL url = new URL(parameter.generateGetURL());
            connection = (HttpURLConnection) url.openConnection();
            for (RequestHeader header: parameter.getHeaders()) {
                connection.setRequestProperty(header.getKey(), header.getValue());
            }
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            String paramStr = parameter.generateUrlParams();
            connection.getOutputStream().write(paramStr.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
        doCall(connection);

    }

    private void doCall(HttpURLConnection connection){
        try {
            requestStarted();
            InputStream in = new BufferedInputStream(connection.getInputStream());
            String result = readInStream(in);
            requestSuccess(result);
            Log.d(TAG, result);
        } catch (IOException e) {
            requestFailure(new RequestException(-1, e.toString()));
        } finally {
            if (connection!=null) {
                connection.disconnect();
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
