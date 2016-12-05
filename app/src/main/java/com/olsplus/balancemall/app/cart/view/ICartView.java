package com.olsplus.balancemall.app.cart.view;


import com.olsplus.balancemall.app.cart.bean.ShoppingItemGroup;

import java.util.List;

public interface ICartView {

    void showDeleteCartSuccessView();

    void showDeleteCartErrorView(String msg);

    void showGetCartDataView(List<ShoppingItemGroup> datas);

    void showCartDataErrorView(String msg);

    void load(List<ShoppingItemGroup> datas);


}
