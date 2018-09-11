package com.lily.http.network.request;

import org.json.JSONObject;

import java.io.File;
import java.util.Map;

import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;

/***********
 * @Author rape flower
 * @Date 2017-03-14 17:15
 * @Describe 根据请求参数生成Request对象（okhttp request）
 */
public class BuildRequest {

    /**
     * Default
     */
    private static final MediaType DEFAULT_TYPE = MediaType.parse("application/x-www-form-urlencoded");
    /**
     * JSON
     */
    private static final MediaType JSON_TYPE = MediaType.parse("application/json; charset=utf-8");
    /**
     * File
     */
    private static final MediaType FILE_TYPE = MediaType.parse("application/octet-stream");

    /**
     * 创建Get请求
     *
     * @param url 请求地址
     * @param params 请求参数
     * @return 返回一个创建好的get请求的Request对象
     */
    public static Request createGetRequest(String url, RequestParams params) {
        StringBuilder urlBuilder = new StringBuilder(url).append("?");
        if (params != null) {
            for (Map.Entry<String, String> entry : params.urlParams.entrySet()) {
                //将请求参数添加到请求体中
                urlBuilder.append(entry.getKey())
                        .append("=")
                        .append(entry.getValue())
                        .append("&");
            }
        }
        return new Request.Builder()
                .url(urlBuilder.substring(0, urlBuilder.length() - 1))
                .get()
                .build();
    }

    /**
     * 创建Post请求
     *
     * @param url
     * @param params
     * @return
     */
    public static Request createPostRequest(String url, RequestParams params) {
        Request request = null;
        if (params != null) {
            if (params.isJsonParam()) {
                if (params.jsonParam instanceof JSONObject) {
                    JSONObject jsonObject = (JSONObject) params.jsonParam;
                    RequestBody body = RequestBody.create(JSON_TYPE, jsonObject.toString());
                    request = new Request.Builder().url(url).post(body).build();
                } else {
                    throw new IllegalArgumentException("RequestParams.jsonParam must a JsonObject");
                }
            } else {
                FormBody.Builder bodyBuilder = new FormBody.Builder();
                for (Map.Entry<String, String> entry : params.urlParams.entrySet()) {
                    //将请求参数添加到请求体中
                    bodyBuilder.add(entry.getKey(), entry.getValue());
                }
                FormBody body = bodyBuilder.build();
                request = new Request.Builder().url(url).post(body).build();
            }
        } else {
            //如果请求参数为空时，当成get请求处理
            request = new Request.Builder().url(url).get().build();
        }

        return request;
    }

    /**
     * 文件上传请求
     *
     * @param url
     * @param params
     * @return
     */
    public static Request createMultiPostRequest(String url, RequestParams params) {
        MultipartBody.Builder requestBody = new MultipartBody.Builder();
        requestBody.setType(MultipartBody.FORM);
        if (params != null) {
            for (Map.Entry<String, Object> entry : params.fileParams.entrySet()) {
                if (entry.getValue() instanceof File) {
                    String key = entry.getKey();
                    File file = (File) entry.getValue();
                    requestBody.addFormDataPart(key, file.getName(), RequestBody.create(FILE_TYPE, file));
                } else if (entry.getValue() instanceof String) {
                    requestBody.addFormDataPart(entry.getKey(), entry.getKey() , RequestBody.create(DEFAULT_TYPE, (String) entry.getValue()));
                }
            }
        }
        MultipartBody body = requestBody.build();
        return new Request.Builder()
                .url(url)
                .post(body)
                .build();
    }
}
