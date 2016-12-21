package com.olsplus.balancemall.app.pay.view;


import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.olsplus.balancemall.R;
import com.olsplus.balancemall.app.pay.bean.ShoppingVoucherEntity;
import com.olsplus.balancemall.core.util.DateUtil;
import com.olsplus.balancemall.core.util.SPUtil;

public class PayVoucherViewHolder extends BaseViewHolder<ShoppingVoucherEntity> {
    private TextView nameTv;
    private TextView ruleTv;
    private TextView timeTv;
    private TextView endTimeTv;
    private ImageView ivSelect;

    public PayVoucherViewHolder(ViewGroup parent) {
        super(parent, R.layout.pay_coupon_view);
        nameTv = $(R.id.home_coupon_name_tv);
        ruleTv = $(R.id.home_coupon_rule_tv);
        timeTv = $(R.id.home_coupon_time_tv);
        ivSelect = $(R.id.ivCouponSelect);
    }

    @Override
    public void setData(ShoppingVoucherEntity data) {
        super.setData(data);
        if (data != null) {

            String couponId = (String) SPUtil.get(getContext(), "couponId", "");
            if (data.getId().equals(couponId)) {
                ivSelect.setVisibility(View.VISIBLE);
            } else {
                ivSelect.setVisibility(View.GONE);
            }
            nameTv.setText(data.getApply_service_name());
            ruleTv.setText("满" + data.getMin_spend() + "减" + data.getVoucher_value());
            timeTv.setText("有效期" + DateUtil.getDay(data.getStart_time()) + "至" + DateUtil.getDay(data.getEnd_time()));
        }
    }
}
