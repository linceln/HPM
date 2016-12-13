package com.olsplus.balancemall.app.merchant.earning.bean;

import com.olsplus.balancemall.core.bean.BaseResultEntity;

import java.io.Serializable;
import java.util.List;

public class EarningListEntity extends BaseResultEntity {


    private List<DataBean> data;

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean implements Serializable{
        /**
         * type : REFUND
         * order_id : 1609212133333652251
         * time : 1476879999
         * refund_amount : 20
         * total : 244
         * pay : 244
         */

        private String type;
        private long order_id;
        private long time;
        private int refund_amount;
        private double total;
        private double pay;

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public long getOrder_id() {
            return order_id;
        }

        public void setOrder_id(long order_id) {
            this.order_id = order_id;
        }

        public long getTime() {
            return time;
        }

        public void setTime(long time) {
            this.time = time;
        }

        public int getRefund_amount() {
            return refund_amount;
        }

        public void setRefund_amount(int refund_amount) {
            this.refund_amount = refund_amount;
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
    }
}
