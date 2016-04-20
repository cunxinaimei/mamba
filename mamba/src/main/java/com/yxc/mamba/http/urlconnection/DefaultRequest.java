package com.yxc.mamba.http.urlconnection;

import android.util.Log;
import com.yxc.mamba.http.*;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import java.util.Scanner;
import java.util.UUID;

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
    protected <T extends Parameter> void doGet(T parameter) {
        HttpURLConnection connection = null;
        try {
            URL url = new URL(parameter.generateGetURL());
            connection = (HttpURLConnection)url.openConnection();
            if (connection instanceof HttpsURLConnection) {
//                ((HttpsURLConnection)connection).setHostnameVerifier(DO_NOT_VERIFY);
                ((HttpsURLConnection)connection).setSSLSocketFactory(SSLSocketFactoryKeeper.getTrustAllSSLSocketFactory());
            }
            for (RequestHeader header: parameter.getHeaders()) {
                connection.setRequestProperty(header.getKey(), header.getValue());
            }
            doCall(connection);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    final static HostnameVerifier DO_NOT_VERIFY = new HostnameVerifier() {

        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    };

    @Override
    protected <T extends Parameter> void doPost(T parameter) {
        if (parameter.getFileParameters()==null || parameter.getFileParameters().size()==0){
            doNormalPost(parameter);
        } else {
            doUpload(parameter);
        }
    }

    private <T extends Parameter> void doNormalPost(T parameter){
        HttpURLConnection connection = null;
        OutputStreamWriter wr = null;
        try {
            URL url = new URL(parameter.getUrl());
            connection = (HttpURLConnection) url.openConnection();
            for (RequestHeader header : parameter.getHeaders()) {
                connection.setRequestProperty(header.getKey(), header.getValue());
            }
            connection.setRequestMethod("POST");
            connection.setRequestProperty("connection", "keep-alive");
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setUseCaches(false);

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

    private <T extends Parameter> void doUpload(T parameter){
        String BOUNDARY = "----"+UUID.randomUUID().toString(); // 边界标识 随机生成
        String PREFIX = "--", LINE_END = "\r\n";
        String CONTENT_TYPE = "multipart/form-data"; // 内容类型

        HttpURLConnection connection = null;
        OutputStreamWriter wr = null;
        DataOutputStream dos = null;
        try {
            URL url = new URL(parameter.getUrl());
            connection = (HttpURLConnection) url.openConnection();
            for (RequestHeader header : parameter.getHeaders()) {
                connection.setRequestProperty(header.getKey(), header.getValue());
            }
            connection.setRequestMethod("POST");
            connection.setRequestProperty("connection", "keep-alive");
            connection.setRequestProperty("Content-Type", CONTENT_TYPE + ";boundary=" + BOUNDARY);
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setUseCaches(false);

            dos = new DataOutputStream(
                    connection.getOutputStream());

            if (parameter.getFileParameters()!=null && parameter.getFileParameters().size()>0) {
                String key = parameter.getFileParameters().keySet().iterator().next();
                File file = parameter.getFileParameters().get(key);
                // 当文件不为空时执行上传

                StringBuffer sb = new StringBuffer();
                sb.append(PREFIX);
                sb.append(BOUNDARY);
                sb.append(LINE_END);

                sb.append("Content-Disposition: form-data; name=\""+key+"\"; filename=\""
                        + file.getName() + "\"" + LINE_END);
                sb.append("Content-Type: application/octet-stream; charset="
                        + "UTF-8" + LINE_END);
                sb.append(LINE_END);
                dos.write(sb.toString().getBytes());
                Log.i("EEE", "SB=" + sb.toString());
                InputStream is = new FileInputStream(file);
                byte[] bytes = new byte[1024];
                int len = 0;
                while ((len = is.read(bytes)) != -1) {
                    dos.write(bytes, 0, len);
                }
                is.close();
            }

            StringBuilder textEntity = new StringBuilder();
            for (Map.Entry<String, Object> entry : parameter.getParameters().entrySet()) {// 构造文本类型参数的实体数据
                textEntity.append(PREFIX);
                textEntity.append(BOUNDARY);
                textEntity.append(LINE_END);
                textEntity.append("Content-Disposition: form-data; name=\""
                        + entry.getKey() + "\"\r\n\r\n");
                textEntity.append(entry.getValue());
                textEntity.append("\r\n");
            }

            dos.write(textEntity.toString().getBytes());
            dos.write(LINE_END.getBytes());
            byte[] end_data = (PREFIX + BOUNDARY + PREFIX + LINE_END)
                    .getBytes();
            dos.write(end_data);
            dos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (wr!=null) {
                    wr.close();
                }
                if (dos!=null){
                    dos.close();
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
            int code = connection.getResponseCode();
            String message = connection.getResponseMessage();
            if (code>=400){
                requestFailure(new RequestException(code, message));
                return;
            }
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
