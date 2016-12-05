package com.olsplus.balancemall.app.province.request;


import com.olsplus.balancemall.app.province.bean.BuildingResultEntity;
import com.olsplus.balancemall.app.province.bean.CityResultEntity;
import com.olsplus.balancemall.app.province.bean.UpdateBuildResultEntity;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

public interface CityBuildingService {

    @GET("v1/city")
    Observable<CityResultEntity> getCitys(
            @Query("uid") String uid,
            @Query("token") String token,
            @Query("timestamp") String page,
            @Query("sign") String sign
    );

    @GET("v1/building")
    Observable<BuildingResultEntity> buildCity(
            @Query("city_id") String city_id,
            @Query("uid") String uid,
            @Query("token") String token,
            @Query("timestamp") String page,
            @Query("sign") String sign
    );

    @FormUrlEncoded
    @POST("v1/building")
    Observable<UpdateBuildResultEntity> building(
            @Field("building_id") String building_id,
            @Field("uid") String uid,
            @Field("token") String token,
            @Field("timestamp") String timestamp,
            @Field("sign") String sign
    );



}
