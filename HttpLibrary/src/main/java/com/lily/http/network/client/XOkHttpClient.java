package com.lily.http.network.client;

import com.lily.http.network.interceptor.HeaderInterceptor;
import com.lily.http.network.interceptor.LoggerInterceptor;
import com.lily.http.network.response.JsonCallback;
import com.lily.http.network.response.XmlCallback;
import com.lily.http.network.security.HttpsUtils;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;

/***********
 * @Author rape flower
 * @Date 2017-03-15 14:04
 * @Describe 创建OkHttpClient对象，并且配置支持https，以及发送请求
 */
public class XOkHttpClient {

    private static final int TIME_OUT = 20;
    private static OkHttpClient mOkHttpClient = null;

    private XOkHttpClient() {
        throw new AssertionError();
    }

    /**
     * 初始化：为OkHttpClient配置参数
     */
    public static void initConfig(Map<String, String> headers) {
        /****** 配置基本参数 ******/
        OkHttpClient.Builder okHttpClientBuilder = new OkHttpClient.Builder();
        //设置连接超时时间
        okHttpClientBuilder.connectTimeout(TIME_OUT, TimeUnit.SECONDS);
        //设置写操作超时时间
        okHttpClientBuilder.writeTimeout(TIME_OUT, TimeUnit.SECONDS);
        //设置读操作超时时间
        okHttpClientBuilder.readTimeout(TIME_OUT, TimeUnit.SECONDS);
        //设置重定向
        okHttpClientBuilder.followRedirects(true);

        /****** 添加请求头拦截器 ******/
        okHttpClientBuilder.addInterceptor(new HeaderInterceptor(headers));
        /****** 添加日志显示拦截器 ******/
        if (OkConfig.isDebug()) {
            okHttpClientBuilder.addInterceptor(new LoggerInterceptor());
        }

        /****** 添加https支持 ******/
        okHttpClientBuilder.hostnameVerifier(new HostnameVerifier() {
            @Override
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        });
        //信任所有Https
        okHttpClientBuilder.sslSocketFactory(HttpsUtils.initSSLSocketFactory(), HttpsUtils.initTrustManager());
        mOkHttpClient = okHttpClientBuilder.build();
    }

    /**
     * 发送http/https请求
     *
     * @param request
     * @param jsonCallback
     * @return
     */
    public static Call sendRequest(Request request, JsonCallback jsonCallback) {
        Call call = mOkHttpClient.newCall(request);
        call.enqueue(jsonCallback);
        return call;
    }

    /**
     * 发送http/https请求
     *
     * @param request
     * @param xmlCallback
     * @return
     */
    public static Call sendRequest(Request request, XmlCallback xmlCallback) {
        Call call = mOkHttpClient.newCall(request);
        call.enqueue(xmlCallback);
        return call;
    }

    /**
     * 发送http/https请求
     *
     * @param request
     * @param callback
     * @return
     */
    public static Call sendRequest(Request request, Callback callback) {
        Call call = mOkHttpClient.newCall(request);
        call.enqueue(callback);
        return call;
    }

}
