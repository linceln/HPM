package com.olsplus.balancemall.core.update;



import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

public interface UpdateService {


    @GET("v1/version")
    Observable<UpdateResult> updateApp(
            @Query("platform") String platform,
            @Query("channel") String channel,
            @Query("timestamp") String timestamp,
            @Query("sign") String sign
    );

}
