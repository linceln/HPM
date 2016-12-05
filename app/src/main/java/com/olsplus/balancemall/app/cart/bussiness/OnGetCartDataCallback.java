package com.olsplus.balancemall.app.cart.bussiness;


import com.olsplus.balancemall.app.cart.bean.ShoppingItemGroup;

import java.util.List;

public interface OnGetCartDataCallback {

    void getCartSuccess(List<ShoppingItemGroup> datas);

    void getCartFailed(String msg);

}
