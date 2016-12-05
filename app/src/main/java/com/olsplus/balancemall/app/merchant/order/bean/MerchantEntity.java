package com.olsplus.balancemall.app.merchant.order.bean;

import com.olsplus.balancemall.core.bean.BaseResultEntity;


public class MerchantEntity extends BaseResultEntity {

    private long local_service_id;
    private String provider_name;
    private String res_path;
    private String order_count;
    private String day_revenue;
    private String month_revenue;

    public long getLocal_service_id() {
        return local_service_id;
    }

    public void setLocal_service_id(long local_service_id) {
        this.local_service_id = local_service_id;
    }

    public String getProvider_name() {
        return provider_name;
    }

    public void setProvider_name(String provider_name) {
        this.provider_name = provider_name;
    }

    public String getRes_path() {
        return res_path;
    }

    public void setRes_path(String res_path) {
        this.res_path = res_path;
    }

    public String getOrder_count() {
        return order_count;
    }

    public void setOrder_count(String order_count) {
        this.order_count = order_count;
    }

    public String getDay_revenue() {
        return day_revenue;
    }

    public void setDay_revenue(String day_revenue) {
        this.day_revenue = day_revenue;
    }

    public String getMonth_revenue() {
        return month_revenue;
    }

    public void setMonth_revenue(String month_revenue) {
        this.month_revenue = month_revenue;
    }
}
