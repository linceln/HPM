package com.olsplus.balancemall.core.http.token;


import com.olsplus.balancemall.core.bean.BaseResultEntity;

public class TokenResult extends BaseResultEntity {

    private String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
