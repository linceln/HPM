package com.olsplus.balancemall.app.mystore.request;


import com.olsplus.balancemall.app.merchant.goods.bean.ImageUploadEntity;
import com.olsplus.balancemall.app.mystore.bean.AvatarResultEntity;
import com.olsplus.balancemall.app.mystore.bean.FeedBackResult;
import com.olsplus.balancemall.app.mystore.bean.MySeesionEntity;
import com.olsplus.balancemall.app.mystore.bean.UpdateGenderResultEntity;
import com.olsplus.balancemall.app.mystore.bean.UpdatePwdResultEntity;
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
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Url;
import rx.Observable;

public interface UserService {

//    @Multipart
//    @POST("v1/upload/avatar")
//    Observable<AvatarResultEntity> uploadAvatar(
//            @Query("uid") String uid,
//            @Query("token") String token,
//            @Query("timestamp") String timestamp,
//            @Query("sign") String sign,
//            @Part("uploadfile\";filename=\"temp.jpg") RequestBody temp);

    // 得到上传头像的token
    @FormUrlEncoded
    @POST("v1/upload/avatar/qiniu")
    Observable<ImageUploadEntity> getAvatarToken(
            @Field("uid") String uid,
            @Field("token") String token,
            @Field("timestamp") String timestamp,
            @Field("sign") String sign,
            @Field("uploadfile") String uploadfile// 上传头像名称
    );

    @FormUrlEncoded
    @POST("v1/gender")
    Observable<UpdateGenderResultEntity> updateGender(
            @Field("uid") String uid,
            @Field("token") String token,
            @Field("gender") String gender,
            @Field("timestamp") String timestamp,
            @Field("sign") String sign
    );

    @FormUrlEncoded
    @POST("v1/passwd")
    Observable<UpdatePwdResultEntity> updatePassword(
            @Field("uid") String uid,
            @Field("token") String token,
            @Field("oldpwd") String oldpwd,
            @Field("newpwd") String newpwd,
            @Field("timestamp") String timestamp,
            @Field("sign") String sign
    );

    @GET("v1/user/info")
    Observable<MySeesionEntity> getMySeesionData(
            @Query("uid") String uid,
            @Query("token") String token,
            @Query("timestamp") String timestamp,
            @Query("sign") String sign
    );

    @GET("v1/passwd")
    Observable<BaseResultEntity> checkPasswd(
            @Query("uid") String uid,
            @Query("token") String token,
            @Query("timestamp") String timestamp,
            @Query("sign") String sign,
            @Query("pwd") String pwd
    );


    @FormUrlEncoded
    @POST("v1/change_phone")
    Observable<BaseResultEntity> bindPhone(
            @Field("uid") String uid,
            @Field("token") String token,
            @Field("phone") String phone,
            @Field("sms") String sms,
            @Field("timestamp") String timestamp,
            @Field("sign") String sign
    );

    @FormUrlEncoded
    @POST("v1/phone/change_sms")
    Observable<BaseResultEntity> sendSms(
            @Field("uid") String uid,
            @Field("token") String token,
            @Field("phone") String phone,
            @Field("timestamp") String timestamp,
            @Field("sign") String sign
    );

    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST
    Observable<FeedBackResult> sumitFeedback(
            @Url String url,
            @Body RequestBody feedback
    );


}
