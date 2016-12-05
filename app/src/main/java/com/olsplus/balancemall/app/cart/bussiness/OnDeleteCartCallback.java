package com.olsplus.balancemall.app.cart.bussiness;


public interface OnDeleteCartCallback {

    void deleteCartSuccess();

    void deleteCartFailed(String msg);
}
