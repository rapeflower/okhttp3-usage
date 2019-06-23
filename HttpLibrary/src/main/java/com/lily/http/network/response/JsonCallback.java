package com.lily.http.network.response;

import android.os.Handler;
import android.os.Looper;

import com.lily.http.network.client.OkConfig;
import com.lily.http.network.exception.OkHttpException;
import com.lily.http.network.listener.DataProcessor;
import com.lily.http.network.listener.ProcessorListener;
import com.lily.http.network.utils.JsonHelper;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/***********
 * @Author rape flower
 * @Date 2017-03-15 14:38
 * @Describe 处理请求响应的Json数据
 */
public class JsonCallback implements Callback {

    protected final String RESULT_CODE = "error_code"; // 有返回则对于http请求来说是成功的，但还有可能是业务逻辑上的错误
    protected final int RESULT_CODE_VALUE = 0;
    protected final String ERROR_MSG = "error_msg";
    protected final String EMPTY_MSG = "";

    protected final int NETWORK_ERROR = -1; //网络错误
    protected final int JSON_ERROR = -2; //JSON数据相关的错误
    protected final int OTHER_ERROR = -3; //未知的错误

    private Handler mHandler;
    private ProcessorListener mListener;
    private Class<?> mClass;

    public JsonCallback(DataProcessor dataProcessor) {
        this.mListener = dataProcessor.listener;
        this.mClass = dataProcessor.cls;
        this.mHandler = new Handler(Looper.getMainLooper());
    }

    @Override
    public void onFailure(final Call call, final IOException e) {
        //此时还是在非UI线程，需要通过Handler转发
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                if (mListener != null) {
                    mListener.onFailure(new OkHttpException(NETWORK_ERROR, e));
                }
            }
        });
    }

    @Override
    public void onResponse(final Call call, final Response response) throws IOException {
        final Object resultObj = handleResponse(response);

        mHandler.post(new Runnable() {
            @Override
            public void run() {
                if (resultObj == null) {
                    if (mListener != null) {
                        mListener.onFailure(new OkHttpException(NETWORK_ERROR, EMPTY_MSG));
                    }
                } else {
                    if (mListener != null) {
                        mListener.onSuccess(resultObj);
                    }
                }
            }
        });
    }

    /**
     * 处理返回结果，解析JSON数据
     * <p>此时是在异步线程（子线程）中</p>
     *
     * @param response
     */
    private Object handleResponse(Response response) {
        if (response == null || !response.isSuccessful()) {
            return null;
        }

        Object obj = null;
        try {
            final String result = response.body().string();
            if (OkConfig.isDebug()) {
                android.util.Log.w("Debug mode" , "result = " + result);
            }
            if (mClass == null) {
                obj = result;
            } else {
                //将json转换成实体类对象或对象集合
                obj = JsonHelper.parseJSON(result, mClass);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return obj;
    }
}
