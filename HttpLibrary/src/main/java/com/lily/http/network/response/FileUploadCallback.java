package com.lily.http.network.response;

import android.os.Handler;
import android.os.Looper;

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
 * @Date 2017-03-15 16:48
 * @Describe 文件上传
 */
public class FileUploadCallback implements Callback {

    protected final int NETWORK_ERROR = -1; //网络错误
    private Handler mHandler;
    private ProcessorListener mListener;

    public FileUploadCallback(DataProcessor dataProcessor) {
        this.mListener = dataProcessor.listener;
        mHandler = new Handler(Looper.getMainLooper());
    }

    @Override
    public void onFailure(Call call, final IOException e) {
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
    public void onResponse(Call call, Response response) throws IOException {
        final String result = handleResponse(response);

        mHandler.post(new Runnable() {
            @Override
            public void run() {
                if (mListener != null) {
                    mListener.onSuccess(result);
                }
            }
        });
    }

    /**
     * 处理返回结果
     *
     * @param response
     * @return
     */
    private String handleResponse(Response response) {
        if (response == null) {
            return "";
        }

        String str = "";
        try {
            final String result = response.body().string();
            com.lily.http.network.model.Response rsp = JsonHelper.parseJSON(result, String.class);
            if (rsp != null) {
                str = String.valueOf(rsp.data);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return str;
    }
}
