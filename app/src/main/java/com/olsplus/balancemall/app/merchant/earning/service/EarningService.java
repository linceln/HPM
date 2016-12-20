package com.olsplus.balancemall.app.merchant.earning.service;


import com.olsplus.balancemall.app.merchant.earning.bean.EarningListEntity;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

public interface EarningService {

    // 收入列表
    @GET("v1/merchant/revenue")
    Observable<EarningListEntity> getEarning(
            @Query("uid") String uid,
            @Query("token") String token,
            @Query("local_service_id") String local_service_id,
            @Query("type") String type,
            @Query("timestamp") String timestamp,
            @Query("page") int page,
            @Query("count") int count,
            @Query("sign") String sign
    );
}
