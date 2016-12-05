package com.olsplus.balancemall.app.mystore.message.request;


import com.olsplus.balancemall.app.mystore.bean.MessageCenterInfoList;
import com.olsplus.balancemall.core.bean.BaseResultEntity;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

public interface MessageService {

    @GET("v1/message/list")
    Observable<MessageCenterInfoList> getMessageList(
            @Query("uid") String uid,
            @Query("token") String token,
            @Query("timestamp") String timestamp,
            @Query("sign") String sign,
            @Query("page") String page,
            @Query("count") String count
    );


    @FormUrlEncoded
    @POST("v1/message/read")
    Observable<BaseResultEntity> readMessage(
            @Field("uid") String uid,
            @Field("token") String token,
            @Field("timestamp") String timestamp,
            @Query("sign") String sign,
            @Field("msg_id") String msg_id
    );

}
