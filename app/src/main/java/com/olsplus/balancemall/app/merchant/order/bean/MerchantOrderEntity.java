package com.olsplus.balancemall.app.merchant.order.bean;

import com.olsplus.balancemall.core.bean.BaseResultEntity;

import java.util.List;


public class MerchantOrderEntity extends BaseResultEntity {

    private List<OrdersBean> orders;

    public List<OrdersBean> getOrders() {
        return orders;
    }

    public void setOrders(List<OrdersBean> orders) {
        this.orders = orders;
    }

    public static class OrdersBean {

        private long order_id;
        private double total;
        private String pay;
        private String status;
        private String refund_status;
        private List<SubordersBean> suborders;

        public long getOrder_id() {
            return order_id;
        }

        public void setOrder_id(long order_id) {
            this.order_id = order_id;
        }

        public double getTotal() {
            return total;
        }

        public void setTotal(double total) {
            this.total = total;
        }

        public String getPay() {
            return pay;
        }

        public void setPay(String pay) {
            this.pay = pay;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getRefund_status() {
            return refund_status;
        }

        public void setRefund_status(String refund_status) {
            this.refund_status = refund_status;
        }

        public List<SubordersBean> getSuborders() {
            return suborders;
        }

        public void setSuborders(List<SubordersBean> suborders) {
            this.suborders = suborders;
        }

        public static class SubordersBean {

            private String title;
            private long product_id;
            private int product_count;
            private double product_price;
            private String sku_value;
            private String schedule_time;
            private String thumbnail;

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public long getProduct_id() {
                return product_id;
            }

            public void setProduct_id(long product_id) {
                this.product_id = product_id;
            }

            public int getProduct_count() {
                return product_count;
            }

            public void setProduct_count(int product_count) {
                this.product_count = product_count;
            }

            public double getProduct_price() {
                return product_price;
            }

            public void setProduct_price(double product_price) {
                this.product_price = product_price;
            }

            public String getSku_value() {
                return sku_value;
            }

            public void setSku_value(String sku_value) {
                this.sku_value = sku_value;
            }

            public String getSchedule_time() {
                return schedule_time;
            }

            public void setSchedule_time(String schedule_time) {
                this.schedule_time = schedule_time;
            }

            public String getThumbnail() {
                return thumbnail;
            }

            public void setThumbnail(String thumbnail) {
                this.thumbnail = thumbnail;
            }
        }
    }
}
