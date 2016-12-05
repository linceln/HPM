package com.olsplus.balancemall.app.pay.bean;

import java.io.Serializable;

public class ShoppingProductEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;
    private String sku_id;
    private String schedule_time;
    private int count;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSku_id() {
        return sku_id;
    }

    public void setSku_id(String sku_id) {
        this.sku_id = sku_id;
    }

    public String getSchedule_time() {
        return schedule_time;
    }

    public void setSchedule_time(String schedule_time) {
        this.schedule_time = schedule_time;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
