package com.olsplus.balancemall.app.mine.bean;

import com.olsplus.balancemall.core.bean.BaseResultEntity;

import java.util.List;

public class MyOrderResult extends BaseResultEntity {

    private List<MyOrderItem> orders;

    public List<MyOrderItem> getOrders() {
        return orders;
    }

    public void setOrders(List<MyOrderItem> orders) {
        this.orders = orders;
    }
}
