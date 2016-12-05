package com.olsplus.balancemall.app.pay.view;


import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.olsplus.balancemall.R;
import com.olsplus.balancemall.core.util.UIUtil;


public class CheckoutOrderInfoView extends LinearLayout {
    private TextView amountTv;
    private Button submitBtn;
    private double needPayPrice;

    public CheckoutOrderInfoView(Context context) {
        super(context);
        init();
    }

    public CheckoutOrderInfoView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.pay_checkout_order_info, this, true);
        amountTv = ((TextView) findViewById(R.id.pay_checkout_order_amount));
        submitBtn = ((Button) findViewById(R.id.pay_checkout_submit_button));
    }

    /**
     * 设置总价
     *
     * @param amount
     */
    public void setAmount(double amount) {
        amountTv.setVisibility(VISIBLE);
        refreshAmount(amount);
    }

    /**
     * 刷新总价
     */
    public void refreshAmount(double amount){
        if(amount < 0){
            needPayPrice  = 0;
        }else {
            needPayPrice = amount;
        }
        amountTv.setText(UIUtil.formatLablePrice(needPayPrice));
    }

    public double getNeedPayAmount(){
        return needPayPrice;
    }

    /**
     * 设置立即支付按键状态
     */
    public void refreshSubmitBtnStatus(boolean enabled) {
        this.submitBtn.setEnabled(enabled);
        if (enabled) {
            submitBtn.setBackgroundResource(R.color.red_e13228);
        } else {
            submitBtn.setBackgroundResource(R.color.product_detail_gray_bdbdbd);
        }
    }


}
