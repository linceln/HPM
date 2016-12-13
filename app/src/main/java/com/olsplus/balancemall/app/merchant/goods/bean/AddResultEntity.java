package com.olsplus.balancemall.app.merchant.goods.bean;

import com.olsplus.balancemall.core.bean.BaseResultEntity;

import java.io.Serializable;


public class AddResultEntity extends BaseResultEntity {

    /**
     * ret : 0
     * product : {"id":2,"title":"商品名称","price":123.13,"sold":0,"inventory":100,"thnumbnail":"xxxxxx/xxx.png"}
     */

    private ProductBean product;

    public ProductBean getProduct() {
        return product;
    }

    public void setProduct(ProductBean product) {
        this.product = product;
    }

    public static class ProductBean implements Serializable{
        /**
         * id : 2
         * title : 商品名称
         * price : 123.13
         * sold : 0
         * inventory : 100
         * thnumbnail : xxxxxx/xxx.png
         */

        private int id;
        private String title;
        private double price;
        private int sold;
        private int inventory;
        private String thnumbnail;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public double getPrice() {
            return price;
        }

        public void setPrice(double price) {
            this.price = price;
        }

        public int getSold() {
            return sold;
        }

        public void setSold(int sold) {
            this.sold = sold;
        }

        public int getInventory() {
            return inventory;
        }

        public void setInventory(int inventory) {
            this.inventory = inventory;
        }

        public String getThnumbnail() {
            return thnumbnail;
        }

        public void setThnumbnail(String thnumbnail) {
            this.thnumbnail = thnumbnail;
        }
    }
}
