package com.yxc.mamba;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import com.yxc.mamba.http.*;
import com.yxc.mamba.http.okhttp.OkHttpParameter;
import com.yxc.mamba.tool.JsonParser;
import com.yxc.mambalibrary.R;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                testHttp();
//                testJsonParser();
            }
        });
    }

    private void testHttp(){
//        RequestManager.injectParameter(OkHttpJsonParameter.class);
//        RequestManager.addCommonHeader("apikey", "ac9c1c27e1997fc2e8e4e6a27a70aae8");
//
//        BaseRequestParameter parameter = RequestManager.createParameter("http://apis.baidu.com/baidu_openkg/xiaoshuo_kg/xiaoshuo_kg");
//        parameter.addParameter("query", "大主宰");
//        parameter.addParameter("resource", "spo_novel");


        RequestManager.injectParameter(OkHttpParameter.class);

//        BaseRequestParameter parameter = RequestManager.createParameter("http://29.onpos.cn/sakura/api/file/uploadImg.xhtml");
//        parameter.addParameter("fileName", "test.png");
////        parameter.addParameter("fileMaxSize", 10);
//        String fPath = Environment.getExternalStorageDirectory() + "/test/bg.png";
//        parameter.addFileParameter("myFile", new File(fPath));

        Parameter parameter = RequestManager.createParameter("http://172.18.204.188:8080/service/post111");
        parameter.addParameter("name", "世界你好");
        parameter.addParameter("age", "19");

//        Parameter parameter = RequestManager.createParameter("https://www.baidu.com/s");
//        parameter.addParameter("name", "世界你好");
//        parameter.addParameter("age", "19");

        RequestManager.post("HB", parameter, new RequestCallBack() {
            @Override
            public void onStart(BaseRequest request) {
                Toast.makeText(MainActivity.this, "Request **" + request.getTag() + "** Started", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(BaseRequest request, RequestException exception) {
                Toast.makeText(MainActivity.this, exception.getMessage(), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onSuccess(BaseRequest request, BaseResponse response) {
                Toast.makeText(MainActivity.this, response.getResult().toString(), Toast.LENGTH_LONG).show();
            }
        });
    }

    public void testJsonParser(){
        Client client = new Client();
        Log.d("OOOOOO", JsonParser.objToJson(client).toString());
    }

    static class Client implements Serializable{
        public String device_mk = "10010";
        //设备平台类型 "android"
        public String device_pt = "android";
        //设备屏幕大小
        public String device_screen = "1280x720";
        //操作系统
        public String os = "android";
        //网络类型
        public String networkType = "wifi";
        //客户端wifi_mac MAC地址
        public String wifi_mac = "00:00:00:00:00:00";
        //网络运营商信息
        public String networkServer = "";
        //经度
        public String longitude = "";
        //纬度
        public String latitude = "";
        //客户端app版本号
        public String app_versionCode = "125";

        public Man child = new Man();

        public List<Man> list;

        public Map<String, Man> map;

        public Client() {
            list = new ArrayList<>();
            list.add(new Man());
            list.add(new Man());
            list.add(new Man());
            list.add(new Man());
            list.add(new Man());

            map = new HashMap<>();
            map.put("A", new Man());
            map.put("B", new Man());
            map.put("C", new Man());
            map.put("D", new Man());
        }
    }

    static class Man{
        public String name = "Niu BE";
        public int age = 23;
    }
}
