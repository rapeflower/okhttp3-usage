package com.lily.http.network.listener;

import com.lily.http.network.exception.OkHttpException;

/***********
 * @Author rape flower
 * @Date 2017-03-15 14:40
 * @Describe 请求回调处理（处理数据的回调监听）
 */
public interface ProcessorListener {

    /**
     * 请求成功回调处理
     * @param responseObj
     */
    void onSuccess(Object responseObj);

    /**
     * 请求失败回调处理
     * @param e
     */
    void onFailure(OkHttpException e);
}
