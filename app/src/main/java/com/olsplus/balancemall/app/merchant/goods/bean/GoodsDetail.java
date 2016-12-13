package com.olsplus.balancemall.app.merchant.goods.bean;


import java.io.Serializable;
import java.util.List;

public class GoodsDetail {

    private String local_service_id;
    private long product_id;
    private String title;
    private String img;
    private double price;
    private int inventory;
    private String detail;
    private long start_time;
    private List<SkuInfo> sku_info;

    public String getLocal_service_id() {
        return local_service_id;
    }

    public void setLocal_service_id(String local_service_id) {
        this.local_service_id = local_service_id;
    }

    public long getProduct_id() {
        return product_id;
    }

    public void setProduct_id(long product_id) {
        this.product_id = product_id;
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

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getInventory() {
        return inventory;
    }

    public void setInventory(int inventory) {
        this.inventory = inventory;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public long getStart_time() {
        return start_time;
    }

    public void setStart_time(long start_time) {
        this.start_time = start_time;
    }

    public List<SkuInfo> getSku_info() {
        return sku_info;
    }

    public void setSku_info(List<SkuInfo> sku_info) {
        this.sku_info = sku_info;
    }

    public static class SkuInfo implements Serializable{
        private String property;
        private double price;
        private int inventory;

        public String getProperty() {
            return property;
        }

        public void setProperty(String property) {
            this.property = property;
        }

        public double getPrice() {
            return price;
        }

        public void setPrice(double price) {
            this.price = price;
        }

        public int getInventory() {
            return inventory;
        }

        public void setInventory(int inventory) {
            this.inventory = inventory;
        }
    }
}
