package com.olsplus.balancemall.core.http;

import com.olsplus.balancemall.core.bean.BaseResultEntity;

public interface RequestCallback<T extends BaseResultEntity> {
    void onSuccess(T baseResultEntity);

    void onError(String msg);
}
