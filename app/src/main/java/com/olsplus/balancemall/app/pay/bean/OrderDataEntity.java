package com.olsplus.balancemall.app.pay.bean;


import java.io.Serializable;
import java.util.List;

public class OrderDataEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    private String local_service_id;

    private String time;

    private List<OrderProductEntity> product;

    public String getLocal_service_id() {
        return local_service_id;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setLocal_service_id(String local_service_id) {
        this.local_service_id = local_service_id;
    }

    public List<OrderProductEntity> getProduct() {
        return product;
    }

    public void setProduct(List<OrderProductEntity> product) {
        this.product = product;
    }
}
