package com.lily.http.network.response;

import com.lily.http.network.listener.DisposeDataHandle;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/***********
 * @Author rape flower
 * @Date 2017-03-15 16:47
 * @Describe 文件下载
 */
public class FileDownloadCallback implements Callback{

    public FileDownloadCallback(DisposeDataHandle handle) {

    }

    @Override
    public void onFailure(Call call, IOException e) {

    }

    @Override
    public void onResponse(Call call, Response response) throws IOException {

    }
}
