package com.olsplus.balancemall.app.merchant.order.bean;

import com.olsplus.balancemall.core.bean.BaseResultEntity;

import java.util.List;


public class MerchantOrderDetailEntity extends BaseResultEntity {

    private OrderDetailBean order_detail;

    public OrderDetailBean getOrderDetail() {
        return order_detail;
    }

    public void setOrderDetail(OrderDetailBean order_detail) {
        this.order_detail = order_detail;
    }

    public static class OrderDetailBean {
        /**
         * order_id : 1611072133332103305
         * total : 0.01
         * pay : 0.01
         * pointPay : 0
         * status : WAIT_FOR_EVALUATE
         * refund_status : PARTIAL_REFUNDING
         * created_time : 2016-11-07 15:21:45
         * trade_id : 16110713933334903308
         * pay_time : 2016-11-07 15:21:57
         * suborders : [{"title":"测试商品","total":0.01,"pay":0.01,"product_id":17,"product_count":1,"product_price":0.01,"sku_value":null,"schedule_time":"2016-11-07 08:30:00","finish_time":"2016-11-08 17:37:22","thumbnail":"uploads/product/db68c46cb958ce402862e59cd7014a864.jpg","refund_status":"REFUNDING"}]
         */

        private long order_id;
        private double total;
        private double pay;
        private double pointPay;
        private String status;
        private String refund_status;
        private String created_time;
        private String trade_id;
        private String pay_time;
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

        public double getPay() {
            return pay;
        }

        public void setPay(double pay) {
            this.pay = pay;
        }

        public double getPointPay() {
            return pointPay;
        }

        public void setPointPay(double pointPay) {
            this.pointPay = pointPay;
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

        public String getCreated_time() {
            return created_time;
        }

        public void setCreated_time(String created_time) {
            this.created_time = created_time;
        }

        public String getTrade_id() {
            return trade_id;
        }

        public void setTrade_id(String trade_id) {
            this.trade_id = trade_id;
        }

        public String getPay_time() {
            return pay_time;
        }

        public void setPay_time(String pay_time) {
            this.pay_time = pay_time;
        }

        public List<SubordersBean> getSuborders() {
            return suborders;
        }

        public void setSuborders(List<SubordersBean> suborders) {
            this.suborders = suborders;
        }

        public static class SubordersBean {
            /**
             * title : 测试商品
             * total : 0.01
             * pay : 0.01
             * product_id : 17
             * product_count : 1
             * product_price : 0.01
             * sku_value : null
             * schedule_time : 2016-11-07 08:30:00
             * finish_time : 2016-11-08 17:37:22
             * thumbnail : uploads/product/db68c46cb958ce402862e59cd7014a864.jpg
             * refund_status : REFUNDING
             */

            private String title;
            private double total;
            private double pay;
            private long product_id;
            private int product_count;
            private double product_price;
            private String sku_value;
            private String schedule_time;
            private String finish_time;
            private String thumbnail;
            private String refund_status;

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public double getTotal() {
                return total;
            }

            public void setTotal(double total) {
                this.total = total;
            }

            public double getPay() {
                return pay;
            }

            public void setPay(double pay) {
                this.pay = pay;
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

            public String getFinish_time() {
                return finish_time;
            }

            public void setFinish_time(String finish_time) {
                this.finish_time = finish_time;
            }

            public String getThumbnail() {
                return thumbnail;
            }

            public void setThumbnail(String thumbnail) {
                this.thumbnail = thumbnail;
            }

            public String getRefund_status() {
                return refund_status;
            }

            public void setRefund_status(String refund_status) {
                this.refund_status = refund_status;
            }
        }
    }
}
