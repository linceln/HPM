package com.olsplus.balancemall.core.http;


import com.olsplus.balancemall.core.bean.TokenResult;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

public interface TokenService {

    @FormUrlEncoded
    @POST("v1/token")
    Observable<TokenResult> getToken(
            @Field("uid") String uid,
            @Field("token") String token,
            @Field("timestamp") String timestamp,
            @Field("sign") String sign
    );
}
