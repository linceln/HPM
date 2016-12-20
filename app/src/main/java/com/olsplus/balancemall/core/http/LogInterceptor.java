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
 * 打印http请求日志
 */
public class LogInterceptor implements Interceptor {

    private static String TAG = "HTTP:";

    @Override
    public okhttp3.Response intercept(Chain chain) throws IOException {

        Request request = chain.request();
        TAG += request.hashCode();
        LogUtil.e(TAG, "---------------------------------Start-------------------------------------");
        LogUtil.e(TAG, request.method());
        if ("GET".equals(request.method())) {
            LogUtil.e(TAG, request.url().toString());
        } else if ("POST".equals(request.method())) {
            String paramsStr = getRequestParams(request);
            LogUtil.e(TAG, request.url().toString() + "\n" + paramsStr);
        }

        okhttp3.Response response = chain.proceed(request);
        try {
            LogUtil.e(TAG, new JSONObject(response.body().string()).toString(4));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        LogUtil.e(TAG, "-----------------------------------End-------------------------------------");
        // 不能return response，一个response只能用一次
        return chain.proceed(request);
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
