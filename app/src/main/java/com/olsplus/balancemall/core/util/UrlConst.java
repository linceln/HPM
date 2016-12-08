package com.olsplus.balancemall.core.util;


public class UrlConst {

    public static final class general {
        // 注册
        public static final String login_check_sms = ApiConst.BASE_URL + "v1/register/sms_check";
        // 上传头像
        public static final String avatar_upload = ApiConst.BASE_URL + "v1/upload/avatar/qiniu";
    }

    public static final class customer {
        // 买家
        public static final String customer_order_proof_upload = ApiConst.BASE_URL + "v1/upload/refund_img/qiniu";
    }

    public static final class merchant {


        //商家首页
        public static final String merchant_home = ApiConst.BASE_URL + "v1/merchant/info";
        //商家收入
        public static final String merchant_earning = ApiConst.BASE_URL + "v1/merchant/revenue";
        //商家订单
        public static final String merchant_order_list = ApiConst.BASE_URL + "v1/merchant/order/list";
        public static final String merchant_order_detail = ApiConst.BASE_URL + "v1/merchant/order/detail";
        public static final String merchant_agree_refund = ApiConst.BASE_URL + "v1/merchant/refund/accept";
        public static final String merchant_refuse_refund = ApiConst.BASE_URL + "v1/merchant/refund/refuse";
        public static final String merchant_finish_order = ApiConst.BASE_URL + "v1/merchant/order/finish";
        public static final String merchant_cancel_order = ApiConst.BASE_URL + "v1/merchant/order/cancel";
        // 商家商品
        public static final String goods_list = ApiConst.BASE_URL + "v1/merchant/product/list";
        public static final String goods_off_sale = ApiConst.BASE_URL + "v1/merchant/product/offsale";
        public static final String goods_on_sale = ApiConst.BASE_URL + "v1/merchant/product/onsale";
        public static final String goods_delete = ApiConst.BASE_URL + "v1/merchant/product/delete";
        public static final String goods_edit = ApiConst.BASE_URL + "v1/merchant/product/editinfo";
        public static final String goods_update = ApiConst.BASE_URL + "v1/merchant/product/update";

    }
}
