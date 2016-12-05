package com.olsplus.balancemall.core.update;


import com.olsplus.balancemall.core.bean.BaseResultEntity;

public class CheckUpdateResultEntity extends BaseResultEntity {

    private UpdateInfo body;

    public UpdateInfo getBody() {
        return body;
    }

    public void setBody(UpdateInfo body) {
        this.body = body;
    }
}
