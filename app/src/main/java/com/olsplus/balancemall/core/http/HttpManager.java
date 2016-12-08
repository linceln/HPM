package com.olsplus.balancemall.core.http;


import android.support.annotation.NonNull;

import com.olsplus.balancemall.core.util.ApiConst;
import com.olsplus.balancemall.core.util.LogUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.security.KeyStore;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;

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
                    OkHttpClient client = new OkHttpClient.Builder()
                            .connectTimeout(20, TimeUnit.SECONDS)
                            .writeTimeout(6, TimeUnit.SECONDS)
                            .readTimeout(6, TimeUnit.SECONDS)
//                            .sslSocketFactory(getSslSocketFactory(context.getResources().openRawResource(R.raw.olsplus)))
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

    private static SSLSocketFactory getSslSocketFactory(InputStream certificates) {

        if (certificates == null) return null;

        SSLContext sslContext = null;
        try {
            CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");

            Certificate ca;
            try {
                ca = certificateFactory.generateCertificate(certificates);

            } finally {
                certificates.close();
            }

            // Create a KeyStore containing our trusted CAs
            String keyStoreType = KeyStore.getDefaultType();
            KeyStore keyStore = KeyStore.getInstance(keyStoreType);
            keyStore.load(null, null);
            keyStore.setCertificateEntry("ca", ca);

            // Create a TrustManager that trusts the CAs in our KeyStore
            String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
            TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
            tmf.init(keyStore);

            // Create an SSLContext that uses our TrustManager
            sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, tmf.getTrustManagers(), null);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return sslContext != null ? sslContext.getSocketFactory() : null;
    }

    private static class LogInterceptor implements Interceptor {
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
}
