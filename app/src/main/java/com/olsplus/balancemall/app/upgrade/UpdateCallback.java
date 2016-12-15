package com.olsplus.balancemall.app.upgrade;


import com.olsplus.balancemall.app.upgrade.bean.UpdateResult;

public interface UpdateCallback {
    void onSuccess(UpdateResult data);

    void onError(String msg);
}
