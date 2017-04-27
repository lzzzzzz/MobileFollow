package com.lz.mobilefollow.lzcore.framework.okhttp3;

import android.os.Build;
import android.os.Handler;
import android.os.Looper;

import com.google.gson.Gson;
import com.lz.mobilefollow.lzcore.constant.Config;
import com.lz.mobilefollow.lzcore.framework.log.LogUtils;

import java.io.File;
import java.io.IOException;
import java.net.FileNameMap;
import java.net.URLConnection;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by LZ on 2016/11/9.
 */

public class HttpClicentFactory {

    /**设置读取超时时间*/
    private static final long OKHTTP_READ_TIMEOUT=30*1000;
    /**设置连接超时时间*/
    private static final long OKHTTP_CONNECT_TIMEOUT=10*1000;
    /**设置连接超时时间*/
    private static final long OKHTTP_WRITE_TIMEOUT=60*1000;
    /**是否使用https true:使用https请求 false:不使用https请求*/
    public final static boolean HTTPS_USEFUL_FLAG = false;


    private static HttpClicentFactory instance;;
    private int HTTP_TYPE_GET=0x01;//get请求方法
    private int HTTP_TYPE_PUT=0x02;//put请求方法
    private int HTTP_TYPE_POST=0x03;//get请求方法
    private int HTTP_TYPE_DELETE=0x04;//get请求方法

    private static OkHttpClient httpClient;
    private Handler handler = new Handler(Looper.getMainLooper());

    public static HttpClicentFactory getIstance(){
        if(null==instance){
            instance=new HttpClicentFactory();
        }
        return instance;
    }
    private HttpClicentFactory(){
        httpClient=getOKHttpCilent();
    }

    /**get方法*/
    public void get(String url, TreeMap paramters, final ResultCallback callback ){
        Request request=buildRequest(HTTP_TYPE_GET,url,paramters);
        onStart(callback);
        httpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                onFailed(callback,e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                LogUtils.d("response ==>"+new Gson().toJson(response));
                if(response.code()!=200){
                    onError(callback,new RuntimeException(String.valueOf(response.code())));
                    return;
                }
                onSuccess(callback,response);
        }});
    }
    /**put方法*/
    public void put(String url, TreeMap paramters, final ResultCallback callback ){
        Request request=buildRequest(HTTP_TYPE_PUT,url,paramters);
        onStart(callback);
        httpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                onFailed(callback,e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                LogUtils.d("response ==>"+new Gson().toJson(response));
                if(response.code()!=200){
                    onError(callback,new RuntimeException(String.valueOf(response.code())));
                    return;
                }
                onSuccess(callback,response);
            }});
    }
    /**post方法*/
    public void post(String url, TreeMap paramters, final ResultCallback callback ){
        Request request=buildRequest(HTTP_TYPE_POST,url,paramters);
        LogUtils.d("push request->"+request.toString());
        LogUtils.d("push request header->"+new Gson().toJson(request.headers()));
        LogUtils.d("push request body->"+new Gson().toJson(request.body()));
        onStart(callback);
//        httpClient=new OkHttpClient();
        httpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                LogUtils.d("----http failure");
                onFailed(callback,e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                LogUtils.d("response ==>"+response.toString());
                if(response.code()!=200){
                    onError(callback,new RuntimeException(String.valueOf(response.code())));
                    return;
                }
                onSuccess(callback,response);
            }});
    }
    /**post方法*/
    public void delete(String url, TreeMap paramters, final ResultCallback callback ){
        Request request=buildRequest(HTTP_TYPE_DELETE,url,paramters);
        onStart(callback);
        httpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                onFailed(callback,e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                LogUtils.d("response ==>"+new Gson().toJson(response));
                if(response.code()!=200){
                    onError(callback,new RuntimeException(String.valueOf(response.code())));
                    return;
                }
                onSuccess(callback,response);
            }});
    }
    /**post上传文件方法*/
    public void postFile(String url, TreeMap<String, String> fileParams,TreeMap paramters, final ResultCallback callback ){
        Request request=buildRequest(url,fileParams,paramters);
        onStart(callback);
        httpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                onFailed(callback,e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                LogUtils.d("response ==>"+new Gson().toJson(response));
                if(response.code()!=200){
                    onError(callback,new RuntimeException(String.valueOf(response.code())));
                    return;
                }
                onSuccess(callback,response);
            }});
    }

    /**开始请求*/
    private void onStart(ResultCallback callback) {
        if (null != callback) {
            callback.onStart();
        }
    }
    /**请求成功入口*/
    private void onSuccess(final ResultCallback callback, final Object data) {
        if (null != callback) {
            handler.post(new Runnable() {
                public void run() {
                    // 需要在主线程的操作。
                    callback.onSuccess(data);
                }
            });
        }
    }
    /**请求失败入口*/
    private void onFailed(final ResultCallback callback, final String message) {
        if (null != callback) {
            handler.post(new Runnable() {
                public void run() {
                    // 需要在主线程的操作。
                    callback.onFailure(message);
                }
            });
        }
    }
    /**请求出错入口*/
    private void onError(final ResultCallback callback,final Exception e) {
        if (null != callback) {
            handler.post(new Runnable() {
                public void run() {
                    // 需要在主线程的操作。
                    callback.onError(e);
                }
            });
        }
    }
    /**创建请求*/
    private OkHttpClient getOKHttpCilent(){
        OkHttpClient.Builder clientBuilder=new OkHttpClient.Builder();
        clientBuilder.readTimeout(OKHTTP_READ_TIMEOUT, TimeUnit.MILLISECONDS);//读取超时
        clientBuilder.connectTimeout(OKHTTP_CONNECT_TIMEOUT, TimeUnit.MILLISECONDS);//连接超时
        clientBuilder.writeTimeout(OKHTTP_WRITE_TIMEOUT, TimeUnit.MILLISECONDS);//写入超时
        if(HTTPS_USEFUL_FLAG){//https添加证书
            HttpsSrca.addSrca(clientBuilder);
        }
        return clientBuilder.build();
    }
    /**生成请求数据*/
    private Request buildRequest(int type,String url,TreeMap paramters){
        Request.Builder builder = new Request.Builder();
        builder.headers(getHeaders());//设置请求头
            builder.url(getUrl(type,url,paramters));
        ///添加请求参数
        if(null!=paramters){
            if(type==HTTP_TYPE_PUT){
                builder.put(buildParametersBody(paramters));
            }else if(type==HTTP_TYPE_POST){
                builder.post(buildParametersBody(paramters));
            }else if(type==HTTP_TYPE_DELETE){
                builder.delete(buildParametersBody(paramters));
            }
        }
        return builder.build();
    }
    /**文件上传生成请求数据*/
    private Request buildRequest(String url,TreeMap<String, String> fileParams,TreeMap paramters){
        Request.Builder builder = new Request.Builder();
        builder.headers(getHeaders());//设置请求头
            builder.url(url);
        ///添加请求参数
                builder.post(SetFileRequestBody(paramters,fileParams));
        LogUtils.d("push datas==>"+new Gson().toJson(paramters));
        return builder.build();
    }

    /**生成g请求url*/
    private String getUrl(int type,String url, TreeMap paramters) {
        if(HTTPS_USEFUL_FLAG){
            url="https://"+url;
        }else{
            url="http://"+url;
        }
        if(type==HTTP_TYPE_GET){
            LogUtils.d("push url==>"+url);
            return url+buildGetUrlParams(paramters);
        }else{
            LogUtils.d("push url==>"+url);
            return url;
        }
    }

    /**
     * 设置请求头
     * @return
     */
    private Headers getHeaders(){
        TreeMap<String,String> headerParams=new TreeMap<String,String>();
        headerParams.put("User-Agent", "OkHttp Headers.java");
        headerParams.put("Accept", "application/json; q=0.5");
        headerParams.put("Accept", "application/vnd.github.v3+json");
        headerParams.put("X-Platform", Config.PLATFORM);
        headerParams.put("X-Device", Build.DEVICE);
        headerParams.put("X-BuildVersion", Build.VERSION.RELEASE);
        Headers.Builder headersbuilder=new Headers.Builder();
        if(headerParams != null)
        {
            Iterator<String> iterator = headerParams.keySet().iterator();
            String key = "";
            while (iterator.hasNext()) {
                key = iterator.next().toString();
                headersbuilder.add(key, headerParams.get(key));
                LogUtils.d("get http", "get_headers==="+key+"===="+headerParams.get(key));
            }
        }

        return headersbuilder.build();
    }
    /**设置请求参数()*/
    private FormBody buildParametersBody(TreeMap parameters){
        FormBody.Builder builder = new FormBody.Builder();
        Iterator<String> iter = parameters.keySet().iterator();
        String key;
        while (iter.hasNext()) {
            key = iter.next();
            builder.add(key, String.valueOf(parameters.get(key)));
        }
        LogUtils.d("push datas==>"+new Gson().toJson(parameters));
        return builder.build();
    }
    /**
     * get方法连接拼加参数
     * @param mParam
     * @return
     */
    private String buildGetUrlParams( TreeMap<String, String> mParam){
        String strParams = "";
        if(mParam != null){
            Iterator<String> iterator = mParam.keySet().iterator();
            String key = "";
            while (iterator.hasNext()) {
                key = iterator.next().toString();
                strParams += "&"+ key + "=" + mParam.get(key);
            }
        }
        return strParams;
    }

    /**
     * 构建Post上传图片的参数
     * @param BodyParams
     * @param fileParams
     * @return
     */
    private RequestBody SetFileRequestBody(Map<String, String> BodyParams, TreeMap<String, String> fileParams){
        //带文件的Post参数
        RequestBody body=null;
        MultipartBody.Builder MultipartBodyBuilder=new MultipartBody.Builder();
        MultipartBodyBuilder.setType(MultipartBody.FORM);
        RequestBody fileBody = null;

        if(BodyParams != null){
            Iterator<String> iterator = BodyParams.keySet().iterator();
            String key = "";
            while (iterator.hasNext()) {
                key = iterator.next().toString();
                MultipartBodyBuilder.addFormDataPart(key, BodyParams.get(key));
                LogUtils.d("post http", "post_Params==="+key+"===="+BodyParams.get(key));
            }
        }

        if(fileParams != null){
            Iterator<String> iterator = fileParams.keySet().iterator();
            String key = "";
            int i=0;
            while (iterator.hasNext()) {
                key = iterator.next().toString();
                i++;
                MultipartBodyBuilder.addFormDataPart(key, fileParams.get(key));
                LogUtils.d("post http", "post_Params==="+key+"===="+fileParams.get(key));
                fileBody = RequestBody.create(MediaType.parse(guessMimeType(fileParams.get(key))), new File(fileParams.get(key)));
                MultipartBodyBuilder.addFormDataPart(key, i + ".png", fileBody);
            }
        }
        body=MultipartBodyBuilder.build();
        LogUtils.d("push files==>"+new Gson().toJson(fileParams));
        return body;

    }
    /**获取文件上传类型*/
    private String guessMimeType(String path) {
        FileNameMap fileNameMap = URLConnection.getFileNameMap();
        String contentTypeFor = fileNameMap.getContentTypeFor(path);
        if (contentTypeFor == null) {
            contentTypeFor = "application/octet-stream";
        }
        return contentTypeFor;
    }
}
