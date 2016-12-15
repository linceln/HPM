package com.olsplus.balancemall.app.mine.bean;

import com.olsplus.balancemall.core.bean.BaseResultEntity;


public class MyOrderDetailResult extends BaseResultEntity {

    private MyOrderDetail order_detail;

    public MyOrderDetail getOrder_detail() {
        return order_detail;
    }

    public void setOrder_detail(MyOrderDetail order_detail) {
        this.order_detail = order_detail;
    }
}
