package com.olsplus.balancemall.app.cart.bean;


import java.io.Serializable;

public class AddCartRequest implements Serializable {
    private static final long serialVersionUID = 1L;

    private String local_service_id;

    private String product_id;

    private String sku_id;

    private String sku_value;

    private double price;

    private int count;

    private String schedule_time;

    public String getLocal_service_id() {
        return local_service_id;
    }

    public void setLocal_service_id(String local_service_id) {
        this.local_service_id = local_service_id;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getSku_id() {
        return sku_id;
    }

    public void setSku_id(String sku_id) {
        this.sku_id = sku_id;
    }

    public String getSku_value() {
        return sku_value;
    }

    public void setSku_value(String sku_value) {
        this.sku_value = sku_value;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getSchedule_time() {
        return schedule_time;
    }

    public void setSchedule_time(String schedule_time) {
        this.schedule_time = schedule_time;
    }
}
