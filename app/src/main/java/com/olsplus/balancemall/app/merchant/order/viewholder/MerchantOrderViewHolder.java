package com.olsplus.balancemall.app.merchant.order.viewholder;

import android.support.annotation.LayoutRes;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.decoration.DividerDecoration;
import com.olsplus.balancemall.R;
import com.olsplus.balancemall.app.merchant.order.adapter.MerchantOrderChildAdapter;
import com.olsplus.balancemall.app.merchant.order.bean.MerchantOrderEntity;
import com.olsplus.balancemall.app.mystore.util.OrderHelper;
import com.olsplus.balancemall.core.util.UIUtil;


public class MerchantOrderViewHolder extends BaseViewHolder<MerchantOrderEntity.OrdersBean> {

    private final TextView tvItemOrderNo;
    private final TextView tvItemOrderStatus;
    private final TextView tvItemGoodsNum;
    private final TextView tvItemOrderAmount;
    private final RecyclerView recyclerBusinessOrderChild;
    private final TextView tvItemDeleteOrder;

    public MerchantOrderViewHolder(ViewGroup parent, @LayoutRes int layoutRes) {
        super(parent, layoutRes);
        tvItemOrderNo = $(R.id.tvItemOrderNo);
        tvItemOrderStatus = $(R.id.tvItemOrderStatus);
        tvItemGoodsNum = $(R.id.tvItemGoodsNum);
        tvItemOrderAmount = $(R.id.tvItemOrderAmount);
        tvItemDeleteOrder = $(R.id.tvItemDeleteOrder);
        recyclerBusinessOrderChild = $(R.id.recyclerBusinessOrderChild);
    }

    @Override
    public void setData(MerchantOrderEntity.OrdersBean data) {
        super.setData(data);
        tvItemOrderNo.setText("订单号：" + data.getOrder_id());
        tvItemGoodsNum.setText(getContext().getString(R.string.merchant_goods_count, String.valueOf(data.getSuborders().size())));
        String total = "合计：" + UIUtil.formatLablePrice(data.getTotal());
        tvItemOrderAmount.setText(UIUtil.getColorSpan(getContext(), total, 3, total.length(), R.color.red));
        tvItemOrderStatus.setText(OrderHelper.getOrderStatus(data.getRefund_status(), data.getStatus()));

        if (OrderHelper.TYPE_WAIT_BUYER_PAY.equals(data.getStatus())) {
            tvItemDeleteOrder.setVisibility(View.VISIBLE);
        } else {
            tvItemDeleteOrder.setVisibility(View.GONE);
        }

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerBusinessOrderChild.setLayoutManager(linearLayoutManager);
        MerchantOrderChildAdapter childAdapter = new MerchantOrderChildAdapter(getContext(), data.getSuborders());
        recyclerBusinessOrderChild.setAdapter(childAdapter);
        recyclerBusinessOrderChild.addItemDecoration(new DividerDecoration(getContext().getResources().getColor(android.R.color.transparent), 1));
    }
}
