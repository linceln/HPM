package com.olsplus.balancemall.app.pay.view;


import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.olsplus.balancemall.R;
import com.olsplus.balancemall.app.pay.bean.ShopingOrderSubmitResultEntity;
import com.olsplus.balancemall.core.util.SPUtil;
import com.olsplus.balancemall.core.util.UIUtil;

public class CheckoutProductView extends LinearLayout implements View.OnClickListener {


    private LinearLayout voucherLinear;
    private TextView voucherTv;

    private LinearLayout pointLinear;
    private TextView pointTv;
    private CheckBox pointCb;

    private LinearLayout orderPublicLinear;
    private TextView orderAmountTv;

    private double orderAmount;

    private int points;

    private int pointsUsed;

    private double totalVoucher;

    /**
     * 可抵扣现金数
     */
    private double integral;

    private OnOrderViewChangeListener onOrderViewChangeListener;

    private ShopingOrderSubmitResultEntity current;

    public interface OnOrderViewChangeListener {
        public void OnOrderPriceChange(boolean isCheck, double price);

        public void onVoucherClickListener();
    }


    public CheckoutProductView(Context context) {
        super(context);
        init();
    }

    public CheckoutProductView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.pay_checkout_shop_products, this, true);
        //总计
        orderPublicLinear = ((LinearLayout) findViewById(R.id.pay_checkout_amount_layout));
        orderAmountTv = ((TextView) findViewById(R.id.pay_checkout_shop_order_amount));

        //积分
        pointLinear = ((LinearLayout) findViewById(R.id.pay_checkout_point_layout));
        pointTv = (TextView) findViewById(R.id.pay_checkout_shop_order_point_tv);
        pointCb = ((CheckBox) findViewById(R.id.pay_checkout_shop_order_point_need));
        pointCb.setOnClickListener(this);

        //抵用券
        voucherLinear = ((LinearLayout) findViewById(R.id.pay_checkout_voucher_layout));
//        voucherTv = ((TextView) findViewById(R.id.pay_checkout_selected_voucher_text_view));
        voucherTv = ((TextView) findViewById(R.id.pay_checkout_shop_voucher_info));

        voucherLinear.setOnClickListener(this);

    }

    public void setOnOrderViewChangeListener(OnOrderViewChangeListener onOrderViewChangeListener) {
        this.onOrderViewChangeListener = onOrderViewChangeListener;
    }

    public void fillView(ShopingOrderSubmitResultEntity shopingOrderSubmitResultEntity) {
        current = shopingOrderSubmitResultEntity;
        orderAmount = shopingOrderSubmitResultEntity.getTotal_fee();
        orderAmountTv.setText(UIUtil.formatLablePrice(orderAmount));
        points = (int) SPUtil.get(getContext(), SPUtil.POINTS, 0);
        double rule = shopingOrderSubmitResultEntity.getPoint_rule();
        integral = UIUtil.formatPrice(points * rule);

        pointTv.setText("可用" + points + "积分,抵用现金" + integral + "元");
        if (integral > orderAmount) {
            integral = orderAmount;
            try {
                int availablePoints = (int) (integral / rule);
                pointsUsed = availablePoints;
                pointTv.setText("可用" + availablePoints + "积分,抵用现金" + integral + "元");
            } catch (Throwable e) {
                e.printStackTrace();
            }
        } else {
            pointsUsed = points;
        }

//        if (points == 0) {
//            pointCb.setEnabled(false);
//        } else {
//            pointCb.setEnabled(true);
//        }
    }


    public void refreshPointUsed(double pointsAmount) {
        integral = pointsAmount;
        double rule = current.getPoint_rule();
        pointsUsed = (int) (pointsAmount / rule);
        pointTv.setText("可用" + pointsUsed + "积分,抵用现金" + integral + "元");
    }

    public void initCouponContent(double couponValue) {
        if (couponValue == 0) {
            voucherTv.setText("没有抵用券");
        }
//        else{
//            voucherTv.setTextOnCounting("可抵扣"+couponValue+"元");
//        }
        totalVoucher = couponValue;
    }

    public void refreshCouponContent(double couponValue) {
        if (couponValue == 0) {
//            voucherTv.setTextOnCounting("可抵扣"+totalVoucher+"元");
        } else {
            voucherTv.setText("抵扣" + couponValue + "元");
        }
    }

    /**
     * 获取当前被消费的积分
     *
     * @return
     */
    public int getPointsUsed() {
        if (pointCb.isChecked()) {
            return pointsUsed;
        }
        return 0;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.pay_checkout_shop_order_point_need:
                double availableAmount = 0;
                if (pointCb.isChecked()) {
                    availableAmount = integral;
                } else {
//                    integral  = UIUtil.formatPrice(points *current.getPoint_rule());
//                    pointTv.setTextOnCounting("可用" + points + "积分,抵用现金" + integral + "元");
                    availableAmount = 0;
                }
                if (onOrderViewChangeListener != null) {
                    onOrderViewChangeListener.OnOrderPriceChange(pointCb.isChecked(), availableAmount);
                }
                break;
            case R.id.pay_checkout_voucher_layout:
                if (onOrderViewChangeListener != null) {
                    onOrderViewChangeListener.onVoucherClickListener();
                }

                break;
        }
    }
}
