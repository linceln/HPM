package com.olsplus.balancemall.app.login.request;


import com.olsplus.balancemall.app.login.bean.LoginResultEntity;
import com.olsplus.balancemall.app.login.bean.RegisterResultEntity;
import com.olsplus.balancemall.core.bean.BaseResultEntity;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

public interface LoginService {
    @FormUrlEncoded
    @POST("v1/login")
    Observable<LoginResultEntity> login(
            @Field("phone") String phone,
            @Field("pwd") String pwd,
            @Field("platform") String platform,
            @Field("channel") String channel,
            @Field("version") String version,
            @Field("timestamp") String timestamp,
            @Field("sign") String sign
    );

    @FormUrlEncoded
    @POST("v1/register")
    Observable<RegisterResultEntity> register(
            @Field("phone") String phone,
            @Field("name") String name,
            @Field("pwd") String pwd,
            @Field("platform") String platform,
            @Field("channel") String channel,
            @Field("version") String version,
            @Field("register_type") String register_type,
            @Field("timestamp") String timestamp,
            @Field("sign") String sign
    );

    @FormUrlEncoded
    @POST("v1/register/sms_send")
    Observable<BaseResultEntity> sendSms(
            @Field("phone") String phone,
            @Field("timestamp") String timestamp,
            @Field("sign") String sign
    );


    @FormUrlEncoded
    @POST("v1/changepwd/sms_send")
    Observable<BaseResultEntity> sendPwdSms(
            @Field("phone") String phone,
            @Field("timestamp") String timestamp,
            @Field("sign") String sign
    );


    @FormUrlEncoded
    @POST("v1/changepwd")
    Observable<BaseResultEntity> forgetPwd(
            @Field("newpwd") String newpwd,
            @Field("phone") String phone,
            @Field("timestamp") String timestamp,
            @Field("sign") String sign
    );

    @FormUrlEncoded
    @POST("v1/logout")
    Observable<BaseResultEntity> loginOut(
            @Field("uid") String uid,
            @Field("token") String token,
            @Field("platform") String platform,
            @Field("channel") String channel,
            @Field("version") String version,
            @Field("timestamp") String timestamp,
            @Field("sign") String sign
    );

    // 校验短信
    @FormUrlEncoded
    @POST("v1/register/sms_check")
    Observable<BaseResultEntity> checkSms(
            @Field("phone") String phone,
            @Field("sms") String sms,
            @Field("type") String type,
            @Field("timestamp") String timestamp,
            @Field("sign") String sign
    );
}
