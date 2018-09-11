package com.lily.http.network.interceptor;

import android.util.Log;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/***********
 * @Author rape flower
 * @Date 2017-10-20 14:06
 * @Describe 日志显示拦截器
 */
public class LoggerInterceptor implements Interceptor{

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        String method = request.method();
        String parameter = "null";
        if ("POST".equals(method)) {
            RequestBody requestBody = request.body();
            StringBuilder sb = new StringBuilder();
            if (requestBody instanceof FormBody) {
                FormBody body = (FormBody) requestBody;
                int size = body.size();
                for (int i = 0; i < size; i++) {
                    //sb.append(body.encodedName(i) + "=" + body.encodedValue(i) + ",");//进行了编码处理
                    sb.append(body.name(i) + "=" + body.value(i) + ",");
                }
                sb.delete(sb.length() - 1, sb.length());
                parameter = "RequestParams:{" + sb.toString() + "}";
            }
        }
        String log = String.format("Sending request %s on %s%n%s%s", request.url(), chain.connection(),
                request.headers(), parameter);
        Log.w("LoggerInterceptor", "log = " + log);
        return chain.proceed(request);
    }
}
