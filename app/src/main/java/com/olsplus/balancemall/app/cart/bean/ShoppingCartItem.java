package com.olsplus.balancemall.app.cart.bean;


import java.io.Serializable;

public class ShoppingCartItem implements Serializable {
    private static final long serialVersionUID = 3715003172355386380L;

    private String local_service_id;
    private String id;
    private double price;
    private int count;
    private String sku_id;
    private String sku_value;
    private String title;
    private String img;
    private boolean checked = true;
    private boolean isEditChecked = false;

    public String getLocal_service_id() {
        return local_service_id;
    }

    public void setLocal_service_id(String local_service_id) {
        this.local_service_id = local_service_id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
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
