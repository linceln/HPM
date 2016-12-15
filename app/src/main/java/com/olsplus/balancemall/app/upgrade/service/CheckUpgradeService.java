package com.olsplus.balancemall.app.upgrade.service;


import com.olsplus.balancemall.app.upgrade.bean.UpdateResult;

import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.Streaming;
import retrofit2.http.Url;
import rx.Observable;

public interface CheckUpgradeService {


    @GET("v1/version")
    Observable<UpdateResult> checkUpgrade(
            @Query("platform") String platform,
            @Query("channel") String channel,
            @Query("timestamp") String timestamp,
            @Query("sign") String sign
    );

    // 下载文件
    @Streaming
    @GET
    Observable<ResponseBody> downloadApk(@Url String url);
}
