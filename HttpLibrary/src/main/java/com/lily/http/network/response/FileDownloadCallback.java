package com.lily.http.network.response;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.lily.http.network.exception.OkHttpException;
import com.lily.http.network.listener.DataProcessor;
import com.lily.http.network.listener.DownloadListener;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/***********
 * @Author rape flower
 * @Date 2017-03-15 16:47
 * @Describe 文件下载
 */
public class FileDownloadCallback implements Callback {

    private final int NETWORK_ERROR = -1; //网络错误
    private final String EMPTY_MSG = "";
    private final int MESSAGE_DOWNLOAD_PROGRESS = 1;
    private String mFilePath;
    private int mProgress;
    private Handler mHandler;
    private DownloadListener mListener;

    public FileDownloadCallback(DataProcessor dataProcessor) {
        if (dataProcessor.listener instanceof DownloadListener) {
            this.mListener = (DownloadListener) dataProcessor.listener;
        }
        this.mFilePath = dataProcessor.source;
        this.mHandler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case MESSAGE_DOWNLOAD_PROGRESS:
                        if (mListener != null) {
                            mListener.onProgress((int) msg.obj);
                        }
                        break;
                }
            }
        };
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
        final File file = handleResponse(response);

        mHandler.post(new Runnable() {
            @Override
            public void run() {
                if (mListener != null) {
                    if (file != null) {
                        mListener.onSuccess(file);
                    } else {
                        mListener.onFailure(new OkHttpException(NETWORK_ERROR, EMPTY_MSG));
                    }
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
    private File handleResponse(Response response) {
        if (response == null) {
            return null;
        }

        int length;
        int currentLength = 0;
        long contentLength = 0;
        byte[] buffer = new byte[1024];
        InputStream inputStream = null;
        File file = null;
        FileOutputStream fos = null;

        try {
            checkLocalFile(mFilePath);
            file = new File(mFilePath);
            fos = new FileOutputStream(file);
            inputStream = response.body().byteStream();
            contentLength = response.body().contentLength();

            while ((length = inputStream.read(buffer)) != -1) {
                fos.write(buffer, 0, length);
                currentLength += length;
                mProgress = (int) (currentLength / contentLength * 100);
                mHandler.obtainMessage(MESSAGE_DOWNLOAD_PROGRESS, mProgress).sendToTarget();
            }
            fos.flush();
        } catch (Exception e) {
            e.printStackTrace();
            file = null;
        } finally {
            try {
                if (fos != null) {
                    fos.close();
                }
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return file;
    }

    /**
     * 检查是否有对应路径的文件
     *
     * @param filePath
     */
    private void checkLocalFile(String filePath) {
        File dir = new File(filePath.substring(0, filePath.lastIndexOf("/" + 1)));
        File file = new File(filePath);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
