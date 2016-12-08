package com.olsplus.balancemall.app.mystore.request;


import com.olsplus.balancemall.app.merchant.goods.bean.ImageUploadEntity;
import com.olsplus.balancemall.app.mystore.bean.MyOrderDetailResult;
import com.olsplus.balancemall.app.mystore.bean.MyOrderResult;
import com.olsplus.balancemall.app.mystore.bean.ReturnImgResult;
import com.olsplus.balancemall.core.bean.BaseResultEntity;

import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;
import retrofit2.http.Url;
import rx.Observable;

public interface MyOrderService {


    @GET("v1/order/list")
    Observable<MyOrderResult> getMyOrderListData(
            @Query("uid") String uid,
            @Query("token") String token,
            @Query("timestamp") String timestamp,
            @Query("sign") String sign,
            @Query("orderType") String orderType,
            @Query("page") String page,
            @Query("count") String count
    );

    @GET("v1/order/detail")
    Observable<MyOrderDetailResult> getMyOrderDetailData(
            @Query("uid") String uid,
            @Query("token") String token,
            @Query("timestamp") String timestamp,
            @Query("sign") String sign,
            @Query("order_id") String order_id
    );

    // 上传退款凭证
    @FormUrlEncoded
    @POST("v1/upload/refund_img/qiniu")
    Observable<ImageUploadEntity> getProofToken(
            @Field("uid") String uid,
            @Field("token") String token,
            @Field("sign") String sign,
            @Field("timestamp") String timestamp,
            @Field("img") String img
    );

    @FormUrlEncoded
    @POST("v1/order/cancel")
    Observable<BaseResultEntity> cancelOrder(
            @Field("uid") String uid,
            @Field("token") String token,
            @Field("timestamp") String timestamp,
            @Field("sign") String sign,
            @Field("order_id") String order_id
    );

    @FormUrlEncoded
    @POST("v1/order/delete")
    Observable<BaseResultEntity> deleteOrder(
            @Field("uid") String uid,
            @Field("token") String token,
            @Field("timestamp") String timestamp,
            @Field("sign") String sign,
            @Field("order_id") String order_id
    );

    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST
    Observable<BaseResultEntity> requestReturnOrder(
            @Url String url,
            @Body RequestBody body
    );

    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST
    Observable<BaseResultEntity> sumitComment(
            @Url String url,
            @Body RequestBody body
    );

    @Multipart
    @POST("v1/upload/refund_img")
    Observable<ReturnImgResult> uploadReturnImg(
            @Query("uid") String uid,
            @Query("token") String token,
            @Query("timestamp") String timestamp,
            @Query("sign") String sign,
            @Part("img\";filename=\"temp.jpg") RequestBody temp);

}
