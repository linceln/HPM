package com.olsplus.balancemall.app.cart.bean;


import java.io.Serializable;

public class ShoppingCartItemVo implements Serializable {
    private static final long serialVersionUID = 3715003172355386380L;

    private String goods_id;
    private String goods_name;
    private String store_id;
    private String store_name;
    private double goods_price;
    private String cart_id;
    private String buyer_id;
    private int goods_num;
    private int g_type;
    private int g_active_min;
    private int g_active_max;
    private String goods_image_url;
    private boolean checked = true;
    private boolean isEditChecked = false;

    public String getGoods_id() {
        return goods_id;
    }

    public void setGoods_id(String goods_id) {
        this.goods_id = goods_id;
    }

    public String getGoods_name() {
        return goods_name;
    }

    public void setGoods_name(String goods_name) {
        this.goods_name = goods_name;
    }

    public String getStore_id() {
        return store_id;
    }

    public void setStore_id(String store_id) {
        this.store_id = store_id;
    }

    public String getStore_name() {
        return store_name;
    }

    public void setStore_name(String store_name) {
        this.store_name = store_name;
    }

    public double getGoods_price() {
        return goods_price;
    }

    public void setGoods_price(double goods_price) {
        this.goods_price = goods_price;
    }

    public String getCart_id() {
        return cart_id;
    }

    public void setCart_id(String cart_id) {
        this.cart_id = cart_id;
    }

    public String getBuyer_id() {
        return buyer_id;
    }

    public void setBuyer_id(String buyer_id) {
        this.buyer_id = buyer_id;
    }

    public int getGoods_num() {
        return goods_num;
    }

    public void setGoods_num(int goods_num) {
        this.goods_num = goods_num;
    }

    public String getGoods_image_url() {
        return goods_image_url;
    }

    public int getG_type() {
        return g_type;
    }

    public void setG_type(int g_type) {
        this.g_type = g_type;
    }

    public int getG_active_min() {
        return g_active_min;
    }

    public void setG_active_min(int g_active_min) {
        this.g_active_min = g_active_min;
    }

    public int getG_active_max() {
        return g_active_max;
    }

    public void setG_active_max(int g_active_max) {
        this.g_active_max = g_active_max;
    }

    public void setGoods_image_url(String goods_image_url) {
        this.goods_image_url = goods_image_url;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public boolean isEditChecked() {
        return isEditChecked;
    }

    public void setEditChecked(boolean isEditChecked) {
        this.isEditChecked = isEditChecked;
    }
}
