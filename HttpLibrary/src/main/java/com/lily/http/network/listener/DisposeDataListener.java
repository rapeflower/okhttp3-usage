package com.lily.http.network.listener;

import com.lily.http.network.exception.OkHttpException;

/***********
 * @Author rape flower
 * @Date 2017-03-15 14:40
 * @Describe 请求回调处理
 */
public interface DisposeDataListener {

    /**
     * 请求成功回调处理
     * @param responseObj
     */
    public void onSuccess(Object responseObj);

    /**
     * 请求失败回调处理
     * @param e
     */
    public void onFailure(OkHttpException e);
}
