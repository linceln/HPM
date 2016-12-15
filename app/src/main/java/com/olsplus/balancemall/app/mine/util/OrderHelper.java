package com.olsplus.balancemall.app.mine.util;


import android.text.TextUtils;

public class OrderHelper {
    public final static String ALL = "ALL";
    public final static String TYPE_WAIT_BUYER_PAY = "WAIT_BUYER_PAY";
    public final static String TYPE_WAIT_FOR_EVALUATE = "WAIT_FOR_EVALUATE";
    public final static String TYPE_REFUND = "REFUND";


    public final static String WAIT_BUYER_PAY = "WAIT_BUYER_PAY";//等待付款
    public final static String WAIT_FOR_SERVE = "WAIT_FOR_SERVE";//等待服务
    public final static String WAIT_FOR_EVALUATE = "WAIT_FOR_EVALUATE";//交易成功
    public final static String CANCELED_BY_USER = "CANCELED_BY_USER";//交易关闭
    public final static String CANCELED_BY_SYSTEM = "CANCELED_BY_SYSTEM";//交易关闭
    public final static String CANCELED_BY_SELLER = "CANCELED_BY_SELLER";//交易关闭
    public final static String CLOSED = "CLOSED";//交易关闭
    public final static String FINISHED = "FINISHED";//交易成功

    public final static String NO_REFUND = "NO_REFUND";
    public final static String PARTIAL_REFUNDING = "PARTIAL_REFUNDING";//部分退款中
    public final static String PARTIAL_REFUNDED = "PARTIAL_REFUNDED";// 部分退款完成
    public final static String PARTIAL_REFUND_FAILED = "PARTIAL_REFUND_FAILED"; // 部分退款失败
    public final static String FULL_REFUNDING = "FULL_REFUNDING";//退款中
    public final static String FULL_REFUNDED = "FULL_REFUNDED"; //退款完成
    public final static String FULL_REFUND_FAILED = "FULL_REFUND_FAILED";// 退款失败

    public final static String WAIT_SELLER_CONFIRM = "WAIT_SELLER_CONFIRM";// 退款等待商家确认
    public final static String REFUNDING = "REFUNDING";// 退款处理中
    public final static String REFUNDED = "REFUNDED";// 退款成功
    public final static String REFUND_FAILED  = "REFUND_FAILED ";// 退款失败



    /**
     * 转换订单状态
     *
     * @param status
     * @return
     */
    public static String getOrderStatus(String refundStatus, String status) {
        if (TextUtils.isEmpty(refundStatus) || TextUtils.isEmpty(status)) {
            return "";
        }
        if (refundStatus.equals(NO_REFUND)) {
            if (status.equals(WAIT_BUYER_PAY)) {
                return "等待付款";
            }
            if (status.equals(WAIT_FOR_SERVE)) {
                return "等待服务";
            }
            if (status.equals(WAIT_FOR_EVALUATE)) {
                return "交易成功";
            }
            if (status.equals(CANCELED_BY_USER)) {
                return "交易关闭";
            }
            if (status.equals(CANCELED_BY_SYSTEM)) {
                return "交易关闭";
            }
            if (status.equals(CANCELED_BY_SELLER)) {
                return "交易关闭";
            }
            if (status.equals(CLOSED)) {
                return "交易关闭";
            }
            if (status.equals(FINISHED)) {
                return "交易成功";
            }
        } else {
            if (refundStatus.equals(PARTIAL_REFUNDING)) {
                return "部分退款中";
            }
            if (refundStatus.equals(PARTIAL_REFUNDED)) {
                return "部分退款完成";
            }
            if (refundStatus.equals(PARTIAL_REFUND_FAILED)) {
                return "部分退款失败";
            }
            if (refundStatus.equals(FULL_REFUNDING)) {
                return "退款中";
            }
            if (refundStatus.equals(FULL_REFUNDED)) {
                return "退款完成";
            }
            if (refundStatus.equals(FULL_REFUND_FAILED)) {
                return "退款失败";
            }
        }

        return "";
    }

    /**
     * 转换订单状态
     * @param status
     * @return
     */
    public static String getReturnStatus(String status){
        if (status.equals(WAIT_SELLER_CONFIRM)) {
            return "退款等待商家确认";
        }
        else if (status.equals(REFUNDING)) {
            return "退款处理中";
        }
        else if (status.equals(REFUNDED)) {
            return "退款成功";
        }
        else if (status.equals(REFUND_FAILED)) {
            return "退款失败";
        }
        return null;
    }
}
