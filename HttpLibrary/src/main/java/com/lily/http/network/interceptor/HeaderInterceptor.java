package com.lily.http.network.interceptor;

import android.content.Context;
import android.os.Build;
import android.webkit.WebSettings;
import android.webkit.WebView;

import java.io.IOException;
import java.util.Map;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/***********
 * @Author rape flower
 * @Date 2017-10-20 14:00
 * @Describe 添加统一固定请求头的拦截器
 */
public class HeaderInterceptor implements Interceptor{

    private Map<String, String> headerMap;

    public HeaderInterceptor() {
        this(null);
    }

    public HeaderInterceptor(Map<String, String> headers) {
        this.headerMap = headers;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request.Builder builder = chain.request().newBuilder();
        // 设置"User-Agent"
        builder.addHeader("User-Agent", getUserAgent());

        /**
         * addHeader("client", "android")
         * addHeader("version", "1.0.0")
         */
        if (headerMap != null && !headerMap.isEmpty()) {
            for (Map.Entry<String, String> entry : headerMap.entrySet()) {
                builder.addHeader(entry.getKey(), entry.getValue());
            }
        }
        Request request = builder.build();
        return chain.proceed(request);
    }

    /**
     * 获取"User-Agent"
     * <p>
     *     问题：当应用 WebSettings.getDefaultUserAgent(context) 时会报错：
     *     1、E/WebViewFactory: can't load with relro file; address space not reserved
     *     2、E/SysUtils: ApplicationContext is null in ApplicationStatus
     *     3、E/ActivityThread: Failed to find provider info for com.google.settings
     *     <pre>
     *        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
     *            try {
     *               userAgent = WebSettings.getDefaultUserAgent(context);
     *            } catch (Exception e) {
     *               userAgent = System.getProperty("http.agent");
     *               e.printStackTrace();
     *            }
     *        } else {
     *           userAgent = System.getProperty("http.agent");
     *        }
     *     </pre>
     * </p>
     *
     * @return
     */
    private static String getUserAgent() {
        String userAgent = "unknown";
        try {
            userAgent = System.getProperty("http.agent");
        } catch (Exception e) {
            e.printStackTrace();
        }
        StringBuffer sb = new StringBuffer();
        for (int i = 0, length = userAgent.length(); i < length; i++) {
            char c = userAgent.charAt(i);
            if (c <= '\u001f' || c >= '\u007f') {
                sb.append(String.format("\\u%04x", (int) c));
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }
}
