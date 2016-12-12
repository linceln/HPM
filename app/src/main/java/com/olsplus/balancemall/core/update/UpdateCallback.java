package com.olsplus.balancemall.core.update;


public interface UpdateCallback {
    void onSuccess(UpdateResult data);

    void onError(String msg);
}
