package com.lily.http.network.listener;

/***********
 * @Author rape flower
 * @Date 2017-03-15 18:16
 * @Describe 文件下载监听
 */
public interface DownloadListener extends ProcessorListener {
    void onProgress(int progress);
}
