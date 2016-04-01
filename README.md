# Mamba Library #
> 之前在项目中遇到过的坑，基于某种原因，需要更换网络请求框架，so，代码一堆大改。所以我在想能不能再遇到这种情况的时候只需要修改极少的地方就可以完成底层框架的更换呢？于是就尝试着搞了一个Lib。至于有没有用，好不好用，欢迎大家拍砖。

## 主要类的简介

- RequestManager

Library的入口
	
提供了Parameter类的设置, 参数为BaseRequestParameter的子类，另外需要保证这个类有一个单String参数的构造方法
	
在获取Parameter的时候，需要使用createParameter方法
	
```
public static void injectParameter(Class cls){
    paramCls = cls;
}
```
	
```
public static BaseRequestParameter createParameter(String url)
```
	
提供了网络请求的入口，目前只提供了get、post请求
	
```
public static void get(String tag,
                           final BaseRequestParameter parameter,
                           final RequestCallBack callBack) {
        doRequest(tag, Method.GET, parameter, callBack);
}
```
    
```
public static void post(String tag,
                           final BaseRequestParameter parameter,
                           final RequestCallBack callBack) {
        doRequest(tag, Method.POST, parameter, callBack);
}
```
	
	提供了公共参数的设置，设置之后，所有的请求都会自动带上这些参数
	
```
public static void addCommonParameter(String key, String value)
```
	
```
public static void addCommonHeader(String key, String value)
```
    
## 接入其他框架
一般来说只需要实现两个类即可

Request 继承 BaseRequest

Parameter 继承 BaseRequestParameter

具体可以参考mambaokhttp的简单实现

## 使用

Gradle

```
dependencies {
    compile 'com.yxc.mamba:mamba:0.2.2'
}
```


如果自己有接入其他框架，那么需要指定一下Parameter类型

```
RequestManager.injectParameter(YourParameter.class);
```
	
发起请求

```
BaseRequestParameter param = RequestManager.createParameter("YourAPIUrl");
param.addHeader("HeaderKey", "HeaderValue");
param.addParameter("key1", "value1");
param.addParameter("key2", "value2");
param.addParameter("key3", "value3");
RequestManager.get(tag, param, YourCallBack);
```