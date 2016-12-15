package com.olsplus.balancemall.app.upgrade.bean;

import com.olsplus.balancemall.core.bean.BaseResultEntity;


public class UpdateResult extends BaseResultEntity {

    private UpdateInfo version;

    public UpdateInfo getVersion() {
        return version;
    }

    public void setVersion(UpdateInfo version) {
        this.version = version;
    }
}
