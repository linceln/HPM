package com.olsplus.balancemall.app.pay.request;

import com.olsplus.balancemall.app.pay.bean.ShopingOrderSubmitResultEntity;
import com.olsplus.balancemall.app.pay.bean.ShoppingResultByZero;
import com.olsplus.balancemall.app.pay.bean.ShoppingSuccessResult;
import com.olsplus.balancemall.app.pay.bean.ShoppingPayResultEntity;
import com.olsplus.balancemall.app.pay.bean.ShoppingVoucherResultEntity;
import com.olsplus.balancemall.app.pay.bean.ShoppingWxPayResult;

import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.Url;
import rx.Observable;

public interface OrderService {

    @Headers({"Content-Type: application/json","Accept: application/json"})
    @POST
    Observable<ShopingOrderSubmitResultEntity> sumitOrder(
            @Url String url,
            @Body RequestBody order
    );

    @Headers({"Content-Type: application/json","Accept: application/json"})
    @POST
    Observable<ShoppingPayResultEntity> prepareAliPay(
            @Url String url,
            @Body RequestBody order
    );

    @Headers({"Content-Type: application/json","Accept: application/json"})
    @POST
    Observable<ShoppingWxPayResult> prepareWxPay(
            @Url String url,
            @Body RequestBody order
    );

    @Headers({"Content-Type: application/json","Accept: application/json"})
    @POST
    Observable<ShoppingResultByZero> preparePay(
            @Url String url,
            @Body RequestBody order
    );


    @GET("v1/voucher")
    Observable<ShoppingVoucherResultEntity> getVouchers(
            @Query("uid") String uid,
            @Query("token") String token,
            @Query("timestamp") String page,
            @Query("sign") String sign
    );

    @Headers({"Content-Type: application/json","Accept: application/json"})
    @POST
    Observable<ShoppingSuccessResult> payResultQuery(
            @Url String url,
            @Body RequestBody order
    );

}
