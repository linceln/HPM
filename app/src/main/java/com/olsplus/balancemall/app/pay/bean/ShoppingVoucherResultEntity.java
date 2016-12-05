package com.olsplus.balancemall.app.pay.bean;


import com.olsplus.balancemall.core.bean.BaseResultEntity;

import java.util.List;

public class ShoppingVoucherResultEntity extends BaseResultEntity{

    private List<ShoppingVoucherEntity> vouchers;

    public List<ShoppingVoucherEntity> getVouchers() {
        return vouchers;
    }

    public void setVouchers(List<ShoppingVoucherEntity> vouchers) {
        this.vouchers = vouchers;
    }
}
