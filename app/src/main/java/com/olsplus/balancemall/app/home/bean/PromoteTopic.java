package com.olsplus.balancemall.app.home.bean;


import java.io.Serializable;

public class PromoteTopic implements Serializable
{
    private static final long serialVersionUID = 1L;

    private String local_service_id;

    private String category_order;

    private String category_name;

    private String provider_name;

    private String provider_order;

    private String provider_desc;

    private String res_path;

    private String per_page;

    private String icon;

    public String getLocal_service_id() {
        return local_service_id;
    }

    public void setLocal_service_id(String local_service_id) {
        this.local_service_id = local_service_id;
    }

    public String getCategory_order() {
        return category_order;
    }

    public void setCategory_order(String category_order) {
        this.category_order = category_order;
    }

    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }

    public String getProvider_name() {
        return provider_name;
    }

    public void setProvider_name(String provider_name) {
        this.provider_name = provider_name;
    }

    public String getProvider_order() {
        return provider_order;
    }

    public void setProvider_order(String provider_order) {
        this.provider_order = provider_order;
    }

    public String getProvider_desc() {
        return provider_desc;
    }

    public void setProvider_desc(String provider_desc) {
        this.provider_desc = provider_desc;
    }

    public String getRes_path() {
        return res_path;
    }

    public void setRes_path(String res_path) {
        this.res_path = res_path;
    }

    public String getPer_page() {
        return per_page;
    }

    public void setPer_page(String per_page) {
        this.per_page = per_page;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }
}
