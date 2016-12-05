package com.olsplus.balancemall.app.pay.view;

import com.olsplus.balancemall.app.pay.bean.ShoppingVoucherEntity;
import com.olsplus.balancemall.core.util.BaseView;

import java.util.List;


public interface IShowVoucherView extends BaseView {
    void showVoucherError(String msg);

    void showVoucherView(List<ShoppingVoucherEntity> datas);
}
