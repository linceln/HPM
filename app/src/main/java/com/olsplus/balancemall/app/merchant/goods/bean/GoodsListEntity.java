package com.olsplus.balancemall.app.merchant.goods.bean;


import com.olsplus.balancemall.core.bean.BaseResultEntity;

import java.util.List;

public class GoodsListEntity extends BaseResultEntity{

    private List<ProductListBean> product_list;

    public List<ProductListBean> getProduct_list() {
        return product_list;
    }

    public void setProduct_list(List<ProductListBean> product_list) {
        this.product_list = product_list;
    }

    public static class ProductListBean {
        /**
         * id : 36
         * title : 气质风尚妆
         * thumbnail : uploads/product/1/8d9b8a835fded38eba5e6c8089fbb1165.png?imageView2/2/w/200/interlace/1
         * price : 132
         * inventory : 2
         * sold : 0
         */

        private long id;
        private String title;
        private String thumbnail;
        private double price;
        private int inventory;
        private int sold;

        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getThumbnail() {
            return thumbnail;
        }

        public void setThumbnail(String thumbnail) {
            this.thumbnail = thumbnail;
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

        public int getSold() {
            return sold;
        }

        public void setSold(int sold) {
            this.sold = sold;
        }
    }
}
