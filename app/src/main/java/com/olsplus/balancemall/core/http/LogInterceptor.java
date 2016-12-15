package com.olsplus.balancemall.core.http;


import android.support.annotation.NonNull;

import com.olsplus.balancemall.core.util.LogUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.charset.Charset;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okio.Buffer;

/**
 * 自定义OkHttp3的拦截器
 * 打印网络请求日志
 */
public class LogInterceptor implements Interceptor {

    private static final String TAG = "HTTP";

    @Override
    public okhttp3.Response intercept(Chain chain) throws IOException {

        Request request = chain.request();
        LogUtil.e(TAG, "---------------------------------Start-------------------------------------");
        LogUtil.e(TAG, request.method());
        if ("GET".equals(request.method())) {
            LogUtil.e(TAG, request.url().toString());
        } else if ("POST".equals(request.method())) {
            String paramsStr = getRequestParams(request);
            LogUtil.e(TAG, request.url().toString() + "\n" + paramsStr);
        }

        okhttp3.Response response = chain.proceed(chain.request());
        String content = response.body().string();
        try {
            LogUtil.e(TAG, new JSONObject(content).toString(4));
        } catch (JSONException e) {
            e.printStackTrace();
            LogUtil.e(TAG, content);
        }
        LogUtil.e(TAG, "-----------------------------------End------------------------------------");

        okhttp3.MediaType mediaType = response.body().contentType();
        return response.newBuilder()
                .body(okhttp3.ResponseBody.create(mediaType, content))
                .build();
    }

    @NonNull
    private String getRequestParams(Request request) {
        RequestBody requestBody = request.body();
        Buffer buffer = new Buffer();
        try {
            requestBody.writeTo(buffer);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Charset charset = Charset.forName("UTF-8");
        MediaType contentType = requestBody.contentType();
        if (contentType != null) {
            charset = contentType.charset(Charset.forName("UTF-8"));
        }
        String paramsStr = buffer.readString(charset);

        String[] split = paramsStr.split("&");
        String str = "";
        for (String s : split) {
            str += s + "\n";
        }
        return str;
    }
}
