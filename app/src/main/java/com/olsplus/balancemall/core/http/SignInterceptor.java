package com.olsplus.balancemall.core.http;


import android.support.annotation.NonNull;
import android.text.TextUtils;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.HashMap;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.Buffer;

/**
 * 拦截原始请求生成带签名的请求
 */
public class SignInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        return chain.proceed(generateSignRequest(chain.request()));
    }

    /**
     * 生成带sign参数的Request-
     *
     * @param request
     * @return
     */
    private Request generateSignRequest(Request request) {

        Request newRequest;
        switch (request.method()) {
            case "GET":
                newRequest = request.newBuilder().url(getNewUrl(request)).build();
                break;
            case "POST":
                if (request.headers().get("Accept").equals("application/json")) {
                    // POST JSON
                    newRequest = request.newBuilder().url(getNewUrl(request)).build();
                } else {
                    // POST
                    String sign = HttpUtil.sign(request.method(), request.url().toString(), splitParams(getOriginalBody(request)));
                    String newBody = getOriginalBody(request) + "&sign=" + sign;
                    newRequest = request.newBuilder().method("POST", RequestBody.create(request.body().contentType(), newBody)).build();
                }
                break;
            default:
                newRequest = request;
                break;
        }
        return newRequest;
    }

    private String getNewUrl(Request request) {

        // "?"分隔出路径和参数
        String[] split = request.url().toString().split("\\?");
        if (split.length > 1 && !TextUtils.isEmpty(split[1])) {
            // 生成签名
            String sign;
            if (request.body() == null) {
                sign = HttpUtil.sign(request.method(), split[0], splitParams(split[1]));
            } else {
                sign = HttpUtil.signWithJson(request.method(), split[0], splitParams(split[1]), getOriginalBody(request));
            }
            // 在参数中加上签名
            return request.url().toString() + "&sign=" + sign;
        }
        return request.url().toString();
    }


    @NonNull
    private String getOriginalBody(Request request) {
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
        return paramsStr;
    }

    private HashMap<String, String> splitParams(String paramsString) {
        HashMap<String, String> hashMap = new HashMap<>();
        // 用"&"分隔出键值对"key=value&key=value&key=value"
        String[] paramPairs = paramsString.split("&");
        for (String paramPair : paramPairs) {
            // 用"="分隔每个"key=value"
            String[] keyValue = paramPair.split("=");
            if (keyValue.length > 1) {
                hashMap.put(keyValue[0], keyValue[1]);
            }
        }
        return hashMap;
    }
}
