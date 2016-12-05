package com.olsplus.balancemall.app.merchant.goods.bean;

import com.olsplus.balancemall.core.bean.BaseResultEntity;

import java.util.List;


public class EditGoodsEntity extends BaseResultEntity {

    /**
     * ret : 0
     * product : {"id":15,"title":"222商品水电费","thumbnail":"xy.png","mainImage":"xxxx.png","price":12,"inventory":100,"start_time":11391089,"sku_info":[{"property_value":"黄色","price":100,"inventory":2},{"property_value":"红色","price":200,"inventory":9}],"desc":"eyJkZXNjIjoiPHA+Jmx0O2RpdiBjbGFzcz1cInNlY3Rpb25zXCImZ3Q7ICAgICAgICAmbHQ7c2VjdGlvbiBjbGFzcz1cImxpbmstc2VjdGlvblwiJmd0OyAgICAgICAgICAgICZsdDtoMyZndDsgICAgICAgICAgICAgICAgJmx0O2ltZyBzcmM9XCJcL1wvc3RhLjM2a3JjbmQuY29tXC9jb21tb24tbW9kdWxlXC9jb21tb24taGVhZGVyXC9pbWFnZXNcL2xvZ28ucG5nXCIgaGVpZ2h0PVwiMTZweFwiIHN0eWxlPVwicG9zaXRpb246IHJlbGF0aXZlO3RvcDogMXB4O21hcmdpbi1yaWdodDogMTBweDtcIiBhbHQ9XCIzNlx1NmMyYVwiJmd0OyBcdThiYTlcdTUyMWJcdTRlMWFcdTY2ZjRcdTdiODBcdTUzNTUgICAgICAgICAgICAmbHQ7XC9oMyZndDsgICAgICAgICZsdDtkaXYgY2xhc3M9XCJsaW5rc1wiJmd0OyAgICAgICAgICAgICZsdDthIGhyZWY9XCJodHRwOlwvXC8zNmtyLmNvbVwvcGFnZXNcL2Fib3V0XCIgcmV"}
     */

    private ProductBean product;

    public ProductBean getProduct() {
        return product;
    }

    public void setProduct(ProductBean product) {
        this.product = product;
    }

    public static class ProductBean {
        /**
         * id : 15
         * title : 222商品水电费
         * thumbnail : xy.png
         * mainImage : xxxx.png
         * price : 12
         * inventory : 100
         * start_time : 11391089
         * sku_info : [{"property_value":"黄色","price":100,"inventory":2},{"property_value":"红色","price":200,"inventory":9}]
         * desc : eyJkZXNjIjoiPHA+Jmx0O2RpdiBjbGFzcz1cInNlY3Rpb25zXCImZ3Q7ICAgICAgICAmbHQ7c2VjdGlvbiBjbGFzcz1cImxpbmstc2VjdGlvblwiJmd0OyAgICAgICAgICAgICZsdDtoMyZndDsgICAgICAgICAgICAgICAgJmx0O2ltZyBzcmM9XCJcL1wvc3RhLjM2a3JjbmQuY29tXC9jb21tb24tbW9kdWxlXC9jb21tb24taGVhZGVyXC9pbWFnZXNcL2xvZ28ucG5nXCIgaGVpZ2h0PVwiMTZweFwiIHN0eWxlPVwicG9zaXRpb246IHJlbGF0aXZlO3RvcDogMXB4O21hcmdpbi1yaWdodDogMTBweDtcIiBhbHQ9XCIzNlx1NmMyYVwiJmd0OyBcdThiYTlcdTUyMWJcdTRlMWFcdTY2ZjRcdTdiODBcdTUzNTUgICAgICAgICAgICAmbHQ7XC9oMyZndDsgICAgICAgICZsdDtkaXYgY2xhc3M9XCJsaW5rc1wiJmd0OyAgICAgICAgICAgICZsdDthIGhyZWY9XCJodHRwOlwvXC8zNmtyLmNvbVwvcGFnZXNcL2Fib3V0XCIgcmV
         */

        private long id;
        private String title;
        private String thumbnail;
        private String mainImage;
        private double price;
        private int inventory;
        private long start_time;
        private String desc;
        private List<SkuInfoBean> sku_info;

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

        public String getMainImage() {
            return mainImage;
        }

        public void setMainImage(String mainImage) {
            this.mainImage = mainImage;
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

        public long getStart_time() {
            return start_time;
        }

        public void setStart_time(long start_time) {
            this.start_time = start_time;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public List<SkuInfoBean> getSku_info() {
            return sku_info;
        }

        public void setSku_info(List<SkuInfoBean> sku_info) {
            this.sku_info = sku_info;
        }

        public static class SkuInfoBean {
            /**
             * property_value : 黄色
             * price : 100
             * inventory : 2
             */

            private String property_value;
            private double price;
            private int inventory;

            public String getProperty_value() {
                return property_value;
            }

            public void setProperty_value(String property_value) {
                this.property_value = property_value;
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
}
