package com.olsplus.balancemall.core.http;


import android.support.annotation.NonNull;
import android.util.Log;

import com.github.simonpercic.oklog3.OkLogInterceptor;
import com.olsplus.balancemall.core.util.ApiConst;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okio.Buffer;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;


public class HttpManager {
    private static final String TAG = "HTTP";
    private static retrofit2.Retrofit retrofit;

    public static retrofit2.Retrofit getRetrofit() {
        if (retrofit == null) {
            synchronized (HttpManager.class) {
                if (retrofit == null) {
                    OkLogInterceptor okLogInterceptor = OkLogInterceptor.builder().build();
                    OkHttpClient client = new OkHttpClient.Builder()
                            .connectTimeout(20, TimeUnit.SECONDS)
                            .writeTimeout(6, TimeUnit.SECONDS)
                            .readTimeout(6, TimeUnit.SECONDS)
                            .addInterceptor(okLogInterceptor)
                            .addInterceptor(new LogInterceptor())
                            .build();
                    retrofit = new retrofit2.Retrofit.Builder()
                            .baseUrl(ApiConst.BASE_URL)
                            .addConverterFactory(GsonConverterFactory.create())
                            .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                            .client(client)
                            .build();
                }
            }
        }
        return retrofit;
    }

    private static class LogInterceptor implements Interceptor {
        @Override
        public okhttp3.Response intercept(Chain chain) throws IOException {
            Request request = chain.request();


            Log.e(TAG, request.method());
            if ("GET".equals(request.method())) {
                Log.e(TAG, request.url().toString());
            } else if ("POST".equals(request.method())) {
                String paramsStr = getRequestParams(request);
                Log.e(TAG, request.url().toString() + "?" + paramsStr);
            }

            okhttp3.Response response = chain.proceed(chain.request());
            String content = response.body().string();
            Log.e(TAG, content);

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
            return buffer.readString(charset);
        }
    }
}
