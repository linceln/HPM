package com.olsplus.balancemall.app.mystore.bean;

import com.olsplus.balancemall.core.bean.BaseResultEntity;


public class MySeesionEntity extends BaseResultEntity {

    private UserInfo userinfo;
    private MyStoreOrderResult order_count;
    private int msgs;
    private String point_rule;

    public UserInfo getUserinfo() {
        return userinfo;
    }

    public void setUserinfo(UserInfo userinfo) {
        this.userinfo = userinfo;
    }

    public MyStoreOrderResult getOrder_count() {
        return order_count;
    }

    public void setOrder_count(MyStoreOrderResult order_count) {
        this.order_count = order_count;
    }

    public int getMsgs() {
        return msgs;
    }

    public void setMsgs(int msgs) {
        this.msgs = msgs;
    }

    public String getPoint_rule() {
        return point_rule;
    }

    public void setPoint_rule(String point_rule) {
        this.point_rule = point_rule;
    }
}
