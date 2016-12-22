package com.olsplus.balancemall.app.pay.bean;


import com.olsplus.balancemall.core.bean.BaseResultEntity;

import java.util.List;

public class ShopingOrderSubmitResultEntity extends BaseResultEntity {

    private double total_fee;

    private List<String> order_ids;

    private float point_rule;

    private int points;

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public double getTotal_fee() {
        return total_fee;
    }

    public void setTotal_fee(double total_fee) {
        this.total_fee = total_fee;
    }

    public List<String> getOrder_ids() {
        return order_ids;
    }

    public void setOrder_ids(List<String> order_ids) {
        this.order_ids = order_ids;
    }

    public float getPoint_rule() {
        return point_rule;
    }

    public void setPoint_rule(float point_rule) {
        this.point_rule = point_rule;
    }
}
