package com.olsplus.balancemall.app.cart.request;


import com.olsplus.balancemall.app.cart.bean.CartResultEntity;
import com.olsplus.balancemall.app.cart.bean.DeleteCartReusltEntity;
import com.olsplus.balancemall.app.cart.bean.ShoppingBag;

import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.Url;
import rx.Observable;

public interface CartService {

    @FormUrlEncoded
    @POST("v1/cart/add")
    Observable<CartResultEntity> addCart(
            @Field("uid") String uid,
            @Field("token") String token,
            @Field("timestamp") String timestamp,
            @Field("sign") String sign,
            @Field("local_service_id") String local_service_id,
            @Field("product_id") String product_id,
            @Field("count") String count,
            @Field("price") String price,
            @Field("sku_id") String sku_id,
            @Field("sku_value") String sku_value,
            @Field("schedule_time") String schedule_time
    );

//    @FormUrlEncoded
//    @POST("v1/cart/delete")
//    Observable<DeleteCartReusltEntity> deleteCart(
//            @Field("uid") String uid,
//            @Field("token") String token,
//            @Field("timestamp") String timestamp,
//            @Field("sign") String sign,
//            @Field("local_service_id") String local_service_id,
//            @Field("product_id") String product_id
//    );

    @Headers({"Content-Type: application/json","Accept: application/json"})
    @POST
    Observable<DeleteCartReusltEntity> deleteCart(
            @Url String url,
            @Body RequestBody order
    );

    @GET("v1/cart")
    Observable<ShoppingBag> getCartListData(
            @Query("uid") String uid,
            @Query("token") String token,
            @Query("timestamp") String timestamp,
            @Query("sign") String sign,
            @Query("page") String page,
            @Query("count") String count
    );

}
