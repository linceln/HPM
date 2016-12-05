package com.olsplus.balancemall.app.merchant.goods.request;


import com.olsplus.balancemall.app.merchant.goods.bean.AddResultEntity;
import com.olsplus.balancemall.app.merchant.goods.bean.EditGoodsEntity;
import com.olsplus.balancemall.app.merchant.goods.bean.GoodsDetail;
import com.olsplus.balancemall.app.merchant.goods.bean.GoodsListEntity;
import com.olsplus.balancemall.app.merchant.goods.bean.ImageUploadEntity;
import com.olsplus.balancemall.core.bean.BaseResultEntity;

import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

public interface GoodsService {

    // 商品列表
    @GET("v1/merchant/product/list")
    Observable<GoodsListEntity> getGoodsList(
            @Query("uid") String uid,
            @Query("token") String token,
            @Query("sign") String sign,
            @Query("timestamp") String timestamp,
            @Query("local_service_id") String local_service_id,
            @Query("type") String type,
            @Query("page") int page,
            @Query("count") int count
    );

    // 商品上架
    @FormUrlEncoded
    @POST("v1/merchant/product/onsale")
    Observable<BaseResultEntity> onSale(
            @Field("uid") String uid,
            @Field("token") String token,
            @Field("sign") String sign,
            @Field("timestamp") String timestamp,
            @Field("local_service_id") String local_service_id,
            @Field("product_id") long product_id
    );

    // 商品下架
    @FormUrlEncoded
    @POST("v1/merchant/product/offsale")
    Observable<BaseResultEntity> offSale(
            @Field("uid") String uid,
            @Field("token") String token,
            @Field("sign") String sign,
            @Field("timestamp") String timestamp,
            @Field("local_service_id") String local_service_id,
            @Field("product_id") long product_id
    );

    // 商品删除
    @FormUrlEncoded
    @POST("v1/merchant/product/delete")
    Observable<BaseResultEntity> delete(
            @Field("uid") String uid,
            @Field("token") String token,
            @Field("sign") String sign,
            @Field("timestamp") String timestamp,
            @Field("local_service_id") String local_service_id,
            @Field("product_id") long product_id
    );

    // 得到上传图片的token
    @FormUrlEncoded
    @POST("v1/{local_service_id}/product/image/qiniu")
    Observable<ImageUploadEntity> getGoodsToken(
            @Path("local_service_id") String local_service_id,
            @Field("uid") String uid,
            @Field("token") String token,
            @Field("sign") String sign,
            @Field("timestamp") String timestamp,
            @Field("img") String img// 上传的图片名称
    );

    // 添加商品
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("v1/{local_service_id}/product")
    Observable<AddResultEntity> addGoods(
            @Path("local_service_id") String local_service_id,
            @Query("uid") String uid,
            @Query("token") String token,
            @Query("sign") String sign,
            @Query("timestamp") String timestamp,
            @Body GoodsDetail body
    );

    // 编辑商品
    @GET("v1/merchant/product/editinfo")
    Observable<EditGoodsEntity> editGoods(
            @Query("uid") String uid,
            @Query("token") String token,
            @Query("sign") String sign,
            @Query("timestamp") String timestamp,
            @Query("local_service_id") String local_service_id,
            @Query("product_id") long product_id
    );

    // 更新商品
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("v1/merchant/product/update")
    Observable<AddResultEntity> updateGoods(
            @Query("uid") String uid,
            @Query("token") String token,
            @Query("sign") String sign,
            @Query("timestamp") String timestamp,
            @Body GoodsDetail body
    );
}
