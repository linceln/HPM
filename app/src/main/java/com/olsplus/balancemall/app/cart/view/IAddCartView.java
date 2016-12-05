package com.olsplus.balancemall.app.cart.view;

import com.olsplus.balancemall.core.util.BaseView;


public interface IAddCartView extends BaseView {

    void showAddCartSuccessView();

    void showAddCartErrorView(String msg);


}
