package com.olsplus.balancemall.app.merchant.order.request;


import com.olsplus.balancemall.app.merchant.earning.bean.EarningListEntity;
import com.olsplus.balancemall.app.merchant.order.bean.MerchantEntity;
import com.olsplus.balancemall.app.merchant.order.bean.MerchantOrderDetailEntity;
import com.olsplus.balancemall.app.merchant.order.bean.MerchantOrderEntity;
import com.olsplus.balancemall.core.bean.BaseResultEntity;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

public interface MerchantService {

    // 商家首页
    @GET("v1/merchant/info")
    Observable<MerchantEntity> getMerchant(
            @Query("uid") String uid,
            @Query("token") String token,
            @Query("local_service_id") String local_service_id,
            @Query("timestamp") String timestamp,
            @Query("sign") String sign
    );

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

    //订单列表
    @GET("v1/merchant/order/list")
    Observable<MerchantOrderEntity> getMerchantOrder(
            @Query("uid") String uid,
            @Query("token") String token,
            @Query("local_service_id") String local_service_id,
            @Query("orderType") String orderType,
            @Query("timestamp") String timestamp,
            @Query("page") int page,
            @Query("count") int count,
            @Query("sign") String sign
    );

    // 订单详情
    @GET("v1/merchant/order/detail")
    Observable<MerchantOrderDetailEntity> getMerchantOrderDetail(
            @Query("uid") String uid,
            @Query(("token")) String token,
            @Query(("timestamp")) String timestamp,
            @Query("sign") String sign,
            @Query("order_id") long order_id
    );

    // 同意退款
    @FormUrlEncoded
    @POST("v1/merchant/refund/accept")
    Observable<BaseResultEntity> agreeRefund(
            @Field("uid") String uid,
            @Field(("token")) String token,
            @Field(("timestamp")) String timestamp,
            @Field("sign") String sign,
            @Field("order_id") long order_id,
            @Field("product_id") long product_id
    );

    // 拒绝退款
    @FormUrlEncoded
    @POST("v1/merchant/refund/refuseRefund")
    Observable<BaseResultEntity> refuseRefund(
            @Field("uid") String uid,
            @Field(("token")) String token,
            @Field(("timestamp")) String timestamp,
            @Field("sign") String sign,
            @Field("order_id") long order_id,
            @Field("product_id") long product_id
    );

    // 完成订单
    @FormUrlEncoded
    @POST("v1/merchant/order/finish")
    Observable<BaseResultEntity> finishOrder(
            @Field("uid") String uid,
            @Field(("token")) String token,
            @Field(("timestamp")) String timestamp,
            @Field("sign") String sign,
            @Field("order_id") long order_id,
            @Field("product_id") long product_id
    );

    // 取消订单
    @FormUrlEncoded
    @POST("v1/merchant/order/cancel")
    Observable<BaseResultEntity> cancelMerchantOrder(
            @Field("uid") String uid,
            @Field(("token")) String token,
            @Field(("timestamp")) String timestamp,
            @Field("sign") String sign,
            @Field("order_id") long order_id
    );
}
