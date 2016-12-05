package com.olsplus.balancemall.app.home.request;


import com.olsplus.balancemall.app.home.bean.Container;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

public interface HomeIndexService {

    @GET("v1/index")
    Observable<Container> getHomeIndex(
            @Query("building_id") String building_id,
            @Query("uid") String uid,
            @Query("token") String token,
            @Query("timestamp") String timestamp,
            @Query("sign") String sign
    );


}
