package com.olsplus.balancemall.app.merchant.earning.viewholder;

import android.graphics.Paint;
import android.support.annotation.LayoutRes;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.olsplus.balancemall.R;
import com.olsplus.balancemall.app.merchant.earning.bean.EarningListEntity;
import com.olsplus.balancemall.core.util.DateUtil;
import com.olsplus.balancemall.core.util.UIUtil;


public class EarningViewHolder extends BaseViewHolder<EarningListEntity.DataBean> {

    private final TextView tvItemOrderNo;
    private final TextView tvItemOrderPay;
    private final TextView tvItemDate;
    private final TextView tvItemOrderPrice;

    public EarningViewHolder(ViewGroup parent, @LayoutRes int res) {
        super(parent, res);
        tvItemOrderNo = $(R.id.tvItemOrderNo);
        tvItemOrderPay = $(R.id.tvItemOrderPay);
        tvItemDate = $(R.id.tvItemDate);
        tvItemOrderPrice = $(R.id.tvItemOrderPrice);
    }

    @Override
    public void setData(EarningListEntity.DataBean data) {
        super.setData(data);
        tvItemOrderNo.setText("订单编号：" + data.getOrder_id());
        tvItemOrderPay.setText(UIUtil.formatLablePrice(data.getPay()));
        tvItemDate.setText(DateUtil.getDay(data.getTime()));
        tvItemOrderPrice.setText(UIUtil.formatLablePrice(data.getTotal()));

        tvItemOrderPrice.getPaint().setAntiAlias(true);//抗锯齿
        tvItemOrderPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG); //中划线

    }
}
