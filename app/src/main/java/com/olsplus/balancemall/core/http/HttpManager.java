package com.olsplus.balancemall.core.http;


import com.olsplus.balancemall.BuildConfig;
import com.olsplus.balancemall.core.util.ApiConst;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * Retrofit
 */
public class HttpManager {
    private static retrofit2.Retrofit retrofit;
    private static retrofit2.Retrofit downLoadRetrofit;

    public static retrofit2.Retrofit getRetrofit() {
        if (retrofit == null) {
            synchronized (HttpManager.class) {
                if (retrofit == null) {

                    OkHttpClient.Builder builder = new OkHttpClient.Builder()
                            .connectTimeout(20, TimeUnit.SECONDS)
                            .writeTimeout(6, TimeUnit.SECONDS)
                            .readTimeout(6, TimeUnit.SECONDS);
//                            .addInterceptor(new SignInterceptor());// 自动签名

                    if (BuildConfig.DEBUG) {
                        builder.addInterceptor(new LogInterceptor());
                    }

                    retrofit = new retrofit2.Retrofit.Builder()
                            .baseUrl(ApiConst.BASE_URL)
                            .addConverterFactory(GsonConverterFactory.create())
                            .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                            .client(builder.build())
                            .build();
                }
            }
        }
        return retrofit;
    }

    /**
     * 下载用，有实时进度
     *
     * @return
     */
    public static retrofit2.Retrofit getDownloadRetrofit() {
        if (downLoadRetrofit == null) {
            synchronized (HttpManager.class) {
                if (downLoadRetrofit == null) {

                    OkHttpClient.Builder builder = new OkHttpClient.Builder()
                            .connectTimeout(20, TimeUnit.SECONDS)
                            .writeTimeout(6, TimeUnit.SECONDS)
                            .readTimeout(6, TimeUnit.SECONDS)
                            .addInterceptor(new ProgressInterceptor());

                    downLoadRetrofit = new retrofit2.Retrofit.Builder()
                            .baseUrl(ApiConst.BASE_URL)
                            .addConverterFactory(GsonConverterFactory.create())
                            .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                            .client(builder.build())
                            .build();
                }
            }
        }
        return downLoadRetrofit;
    }

    private static class ProgressInterceptor implements Interceptor {

        @Override
        public Response intercept(Chain chain) throws IOException {
            Response originalResponse = chain.proceed(chain.request());
            return originalResponse.newBuilder()
                    .body(new FileResponseBody(originalResponse.body()))
                    .build();
        }
    }
}
