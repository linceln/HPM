package com.olsplus.balancemall.app.pay;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;

import com.olsplus.balancemall.R;
import com.olsplus.balancemall.app.pay.bean.ShopingOrderSubmitResultEntity;
import com.olsplus.balancemall.app.pay.bean.ShopingPayQueryEntity;
import com.olsplus.balancemall.app.pay.bean.ShoppingCartEntity;
import com.olsplus.balancemall.app.pay.bean.ShoppingPayResultEntity;
import com.olsplus.balancemall.app.pay.bean.ShoppingResultByZero;
import com.olsplus.balancemall.app.pay.bean.ShoppingVoucherEntity;
import com.olsplus.balancemall.app.pay.bean.ShoppingWxPayResult;
import com.olsplus.balancemall.app.pay.bean.ShoppingWxPayment;
import com.olsplus.balancemall.app.pay.request.CheckOutRequestImpl;
import com.olsplus.balancemall.app.pay.view.CheckoutOrderInfoView;
import com.olsplus.balancemall.app.pay.view.CheckoutProductView;
import com.olsplus.balancemall.app.pay.view.ICheckOutView;
import com.olsplus.balancemall.app.pay.view.IShowVoucherView;
import com.olsplus.balancemall.app.pay.voucher.MyCouponActivity;
import com.olsplus.balancemall.core.app.BaseFragment;
import com.olsplus.balancemall.core.app.MainActivity;
import com.olsplus.balancemall.core.event.WxPayEvent;
import com.olsplus.balancemall.core.pay.alipay.AlipayUtil;
import com.olsplus.balancemall.core.pay.alipay.IPayCallBack;
import com.olsplus.balancemall.core.pay.wxpay.WXPayUtil;
import com.olsplus.balancemall.core.util.ApiConst;
import com.olsplus.balancemall.core.util.SPUtil;
import com.olsplus.balancemall.core.util.ToastUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;


public class CheckoutMainActivity extends MainActivity implements ICheckOutView, CheckoutProductView.OnOrderViewChangeListener, IShowVoucherView {

    private static final int REQUEST_CODE_COUPON = 104;
    private LinearLayout productLinear;
    private LinearLayout paymentLinear;
    private LinearLayout orderInfoLinear;

    private CheckBox aliCheck;
    private CheckBox wxCheck;
    private CheckoutProductView checkoutProductView;
    private CheckoutOrderInfoView checkoutOrderInfoView;

    private String orderParmas;
    private String from;
    private ArrayList<ShoppingCartEntity> services;
    private CheckOutRequestImpl orderRequest;
    private CheckOutRequestImpl outRequest;
    private ShopingOrderSubmitResultEntity currentCheckOutResult;
    /**
     * 支付方式
     */
    private String payWay = ApiConst.PAY_ALI;
    /**
     * 抵用券id
     */
    private String voucher = "";

    private String trade_id;

    private String orderIds;

    private double total_fee;

    private int pointsUsed;

    private double pointsAmount;

    private double currentPayAmount;

    private double vouchePrice = 0;


    @Override
    protected BaseFragment getFirstFragment() {
        return null;
    }

    @Override
    protected int getFragmentContentId() {
        return 0;
    }

    @Override
    protected int getTitleViewId() {
        return R.layout.action_bar_view;
    }

    @Override
    protected int getContentViewId() {
        return R.layout.pay_checkout_main;
    }

    @Override
    protected void handleIntent(Intent intent) {
        super.handleIntent(intent);
        orderParmas = intent.getStringExtra("sumit_order_parmas");
        from = intent.getStringExtra("from");
        services = (ArrayList<ShoppingCartEntity>) intent.getSerializableExtra("service");
        if (from.equals("2")) {
            total_fee = intent.getDoubleExtra("total_fee", 0);
            orderIds = intent.getStringExtra("order_id");

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setActionBar();
        mTitleName.setText("付款");
        mLeftOperationImageView.setBackgroundResource(R.drawable.back_normal);
        productLinear = ((LinearLayout) findViewById(R.id.pay_checkout_layout_products));
        paymentLinear = ((LinearLayout) findViewById(R.id.pay_checkout_layout_payment));
        orderInfoLinear = ((LinearLayout) findViewById(R.id.pay_checkout_layout_order_info));
        initData();
    }

    private void initData() {
        EventBus.getDefault().register(this);
        orderRequest = new CheckOutRequestImpl(this);
        orderRequest.setShowCheckOutView(this);
        outRequest = new CheckOutRequestImpl(this);
        outRequest.setShowVoucherView(this);
        if (from.equals("0")) {
            if (!TextUtils.isEmpty(orderParmas)) {
                orderRequest.sumitOrder(orderParmas);
            }
        } else if (from.equals("1")) {
            if (services != null) {
                orderRequest.sumitOrder(services);
            }
        } else if (from.equals("2")) {
            outRequest.getVouchers();
            currentCheckOutResult = new ShopingOrderSubmitResultEntity();
            currentCheckOutResult.setTotal_fee(total_fee);
            String pointsRule = (String) SPUtil.get(this, SPUtil.POINTS_RULE, "");
            if (TextUtils.isEmpty(pointsRule)) {
                currentCheckOutResult.setPoint_rule(0);
            } else {
                double pointsRuleDouble = Double.parseDouble(pointsRule);
                currentCheckOutResult.setPoint_rule(pointsRuleDouble);
            }
            currentCheckOutResult.setPoint_rule(ApiConst.POINT_RULE);
            List<String> orders = new ArrayList<String>();
            orders.add(orderIds);
            currentCheckOutResult.setOrder_ids(orders);
            initOrderView();
            initPaymentLayout();
            initOrderInfo();
        }


    }


    /**
     * 初始化支付方式
     */
    private void initPaymentLayout() {
        paymentLinear.removeAllViews();
        LinearLayout paymentView = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.pay_checkout_payment, null);
        aliCheck = (CheckBox) paymentView.findViewById(R.id.bank_choose_ali_check);
        wxCheck = (CheckBox) paymentView.findViewById(R.id.bank_choose_weixin_check);
        LinearLayout aliLayout = (LinearLayout) paymentView.findViewById(R.id.pay_ali_layout);
        LinearLayout wxLayout = (LinearLayout) paymentView.findViewById(R.id.pay_wx_layout);
        aliLayout.setOnClickListener(this);
        wxLayout.setOnClickListener(this);
        aliCheck.setOnClickListener(this);
        wxCheck.setOnClickListener(this);
        paymentLinear.addView(paymentView);
    }

    /**
     * 初始化订单信息
     */
    private void initOrderView() {
        productLinear.removeAllViews();
        checkoutProductView = new CheckoutProductView(this);
        checkoutProductView.setOnOrderViewChangeListener(this);
        checkoutProductView.fillView(currentCheckOutResult);
        productLinear.addView(checkoutProductView);
    }

    /**
     * 初始化底部支付信息
     */
    private void initOrderInfo() {
        orderInfoLinear.removeAllViews();
        checkoutOrderInfoView = new CheckoutOrderInfoView(this);
        checkoutOrderInfoView.setAmount(currentCheckOutResult.getTotal_fee());
        boolean submitStatus = (currentCheckOutResult.getOrder_ids() != null && !currentCheckOutResult.getOrder_ids().isEmpty());
        checkoutOrderInfoView.refreshSubmitBtnStatus(submitStatus);
        checkoutOrderInfoView.findViewById(R.id.pay_checkout_submit_button).setOnClickListener(this);
        orderInfoLinear.addView(checkoutOrderInfoView);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.pay_ali_layout:
                aliCheck.setChecked(true);
                if (aliCheck.isChecked()) {
                    payWay = ApiConst.PAY_ALI;
                } else {
                    payWay = null;
                }
                wxCheck.setChecked(false);
                break;

            case R.id.pay_wx_layout:
                wxCheck.setChecked(true);
                if (wxCheck.isChecked()) {
                    payWay = ApiConst.PAY_WX;
                } else {
                    payWay = null;
                }
                aliCheck.setChecked(false);
                break;
            case R.id.bank_choose_ali_check:
                if (aliCheck.isChecked()) {
                    payWay = ApiConst.PAY_ALI;
                } else {
                    payWay = null;
                }
                wxCheck.setChecked(false);
                break;
            case R.id.bank_choose_weixin_check:
                if (wxCheck.isChecked()) {
                    payWay = ApiConst.PAY_WX;
                } else {
                    payWay = null;
                }
                aliCheck.setChecked(false);
                break;
            case R.id.pay_checkout_submit_button:
                if (TextUtils.isEmpty(payWay)) {
                    ToastUtil.showShort(this, "请先确认支付方式");
                    break;
                }
                if (orderRequest != null) {
//                    double totalFee = currentCheckOutResult.getTotal_fee();
                    double needPayPrice = checkoutOrderInfoView.getNeedPayAmount();
                    int pointUsed = checkoutProductView.getPointsUsed();
//                    List<String> orders  = new ArrayList<String>();
//                    if(from.equals("2")){
//                        orders.add(orderIds);
//                    }else {
//                        orders = currentCheckOutResult.getOrder_ids();
//                    }
                    List<String> orders = currentCheckOutResult.getOrder_ids();
                    /**
                     * 有用积分或抵用券抵掉全额的情况，这种情况下，调用支付的接口会直接返回成功，然后你们按原来的流程，再发送一次查询请求，就可以了，不用跳转到第三方APP
                     */
                    if (needPayPrice == 0) {
                        if (pointUsed > 0 || !TextUtils.isEmpty(voucher)) {
                            payWay = "NONE";
                        }
                    }
                    orderRequest.startPay(total_fee, needPayPrice, pointUsed, voucher, payWay, orders);
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_COUPON) {
            if (resultCode == RESULT_OK) {
                if (data != null) {
                    ShoppingVoucherEntity shoppingVoucherVo = (ShoppingVoucherEntity) data.getSerializableExtra("voucher_select");
                    double needPrice = shoppingVoucherVo.getMin_spend();
                    if (currentCheckOutResult.getTotal_fee() >= needPrice) {

                        String voucherSelected = shoppingVoucherVo.getId();
                        if (!TextUtils.isEmpty(voucher)) {
                            if (voucher.equals(voucherSelected)) {
                                voucher = null;
                            } else {
                                voucher = voucherSelected;
                            }
                        } else {
                            voucher = voucherSelected;
                        }

                        if (TextUtils.isEmpty(voucher)) {
                            checkoutProductView.refreshCouponContent(0);
                            vouchePrice = 0;
                            checkoutOrderInfoView.refreshAmount(currentCheckOutResult.getTotal_fee() - pointsAmount);
                            return;
                        }
                        vouchePrice = shoppingVoucherVo.getVoucher_value();
                        double totalFee = currentCheckOutResult.getTotal_fee();
                        if (vouchePrice > totalFee) {
                            pointsAmount = 0;
                            checkoutProductView.refreshPointUsed(0);
                        } else {
                            double temp = vouchePrice + pointsAmount;
                            if (temp > totalFee) {
                                double availableAmount = totalFee - vouchePrice;
                                checkoutProductView.refreshPointUsed(availableAmount);
                            }
                        }
                        checkoutProductView.refreshCouponContent(vouchePrice);
//                        voucher = shoppingVoucherVo.getId();

                        //重新刷新支付价格
                        currentPayAmount = currentCheckOutResult.getTotal_fee() - vouchePrice - pointsAmount;
                        checkoutOrderInfoView.refreshAmount(currentPayAmount);
                    } else {
                        ToastUtil.showShort(this, "没达到满减要求");
                    }
                }
            }
        }
    }


    @Override
    public void showSumitOrderFailed(String msg) {
        ToastUtil.showShort(this, msg);
    }

    @Override
    public void onSumitOrderSuccess(ShopingOrderSubmitResultEntity shopingOrderSubmitResultEntity) {
        currentCheckOutResult = shopingOrderSubmitResultEntity;
        total_fee = currentCheckOutResult.getTotal_fee();
        outRequest.getVouchers();
        initOrderView();
        initPaymentLayout();
        initOrderInfo();
    }

    @Override
    public void showPayFailed(String msg) {
        ToastUtil.showShort(this, msg);
    }

    @Override
    public void showPaySuccess(ShoppingPayResultEntity shoppingPayResultEntity) {
        trade_id = shoppingPayResultEntity.getTrade_id();
        String orderStr = shoppingPayResultEntity.getAlipay().getOrderStr();
        doAlipay(orderStr);
    }

    @Override
    public void showWxPaySuccess(ShoppingWxPayResult shoppingWxPayResult) {

        ShoppingWxPayment shoppingWxPayment = shoppingWxPayResult.getWechat();

        String appid = shoppingWxPayment.getAppid();

        String noncestr = shoppingWxPayment.getNoncestr();

        String partnerid = shoppingWxPayment.getPartnerid();

        String prepayid = shoppingWxPayment.getPrepayid();

        String timestamp = shoppingWxPayment.getTimestamp();

        String sign = shoppingWxPayment.getSign();

        trade_id = shoppingWxPayment.getTrade_id();
        WXPayUtil wxPayUtil = WXPayUtil.getInstance(CheckoutMainActivity.this);
        wxPayUtil.startPayReq(appid, noncestr, partnerid, prepayid, timestamp, sign);
    }

    /**
     * 当支付金额为0的情况下
     *
     * @param shoppingResultByZero
     */
    @Override
    public void showSpecialPaySuccess(ShoppingResultByZero shoppingResultByZero) {
        if (shoppingResultByZero != null) {
            trade_id = shoppingResultByZero.getTrade_id();
            goToSuccessActivity();
        }
    }

    @Override
    public void OnOrderPriceChange(boolean isCheck, double price) {
        if (isCheck) {
            double availableAmount = currentCheckOutResult.getTotal_fee() - price;
            if (availableAmount < vouchePrice) {
                price = currentCheckOutResult.getTotal_fee() - vouchePrice;
                if (price <= 0) {
                    price = 0;
                }
                checkoutProductView.refreshPointUsed(price);
            }
        }
        pointsAmount = price;
        double current = currentCheckOutResult.getTotal_fee() - vouchePrice - price;
        ;
        checkoutOrderInfoView.refreshAmount(current);
    }

    @Override
    public void onVoucherClickListener() {
        Intent intent = new Intent(this, MyCouponActivity.class);
        startActivityForResult(intent, REQUEST_CODE_COUPON);
    }


    /**
     * 发起支付宝支付
     *
     * @param orderInfo
     */
    private void doAlipay(String orderInfo) {
        AlipayUtil alipayUtil = AlipayUtil.getInstance(this);
        alipayUtil.setCallBack(alipayListener);
        alipayUtil.startPay(orderInfo);
    }

    /**
     * 支付宝支付结果回调
     */
    IPayCallBack alipayListener = new IPayCallBack() {

        @Override
        public void onPaySuccess() {
            goToSuccessActivity();
        }

        @Override
        public void onPayFailed() {
            ToastUtil.showShort(CheckoutMainActivity.this, "支付失败!");
        }
    };


    /**
     * 微信支付回调
     *
     * @param wxPayEvent
     * @return
     */
    @Subscribe
    public void onWxPayEvent(WxPayEvent wxPayEvent) {
        int code = wxPayEvent.getCode();
        if (code == 1) {
            goToSuccessActivity();
        }

    }

    private void goToSuccessActivity() {
        if (TextUtils.isEmpty(trade_id)) {
            return;
        }
        ShopingPayQueryEntity shopingPayQueryEntity = new ShopingPayQueryEntity();
        shopingPayQueryEntity.setPay_type(payWay);
        shopingPayQueryEntity.setTrade_id(trade_id);
        Intent intent = new Intent(this, PaySuccessActivity.class);
        intent.putExtra("query_parmas", shopingPayQueryEntity);
        intent.putExtra("point_rule", currentCheckOutResult.getPoint_rule());
        startActivity(intent);
        finish();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void showVoucherError(String msg) {
        checkoutProductView.initCouponContent(0);
    }

    @Override
    public void showVoucherView(List<ShoppingVoucherEntity> datas) {
        double total = 0;
        for (ShoppingVoucherEntity shoppingVoucherEntity : datas) {
            total = total + shoppingVoucherEntity.getVoucher_value();
        }
        checkoutProductView.initCouponContent(total);
    }
}
