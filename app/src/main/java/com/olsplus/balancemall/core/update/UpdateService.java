package com.olsplus.balancemall.core.update;


import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.Streaming;
import retrofit2.http.Url;
import rx.Observable;

public interface UpdateService {


    @GET("v1/version")
    Observable<UpdateResult> updateApp(
            @Query("platform") String platform,
            @Query("channel") String channel,
            @Query("timestamp") String timestamp,
            @Query("sign") String sign
    );

    // 下载文件
    @Streaming
    @GET
    Observable<ResponseBody> download(@Url String url);
}
