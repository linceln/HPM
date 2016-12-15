package com.olsplus.balancemall.app.merchant.order.viewholder;

import android.support.annotation.LayoutRes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.olsplus.balancemall.R;
import com.olsplus.balancemall.app.merchant.order.bean.MerchantOrderEntity;
import com.olsplus.balancemall.app.mine.util.OrderHelper;
import com.olsplus.balancemall.core.image.ImageHelper;
import com.olsplus.balancemall.core.util.UIUtil;


public class MerchantOrderViewHolder extends BaseViewHolder<MerchantOrderEntity.OrdersBean> {

    private final TextView tvItemOrderNo;
    private final TextView tvItemOrderStatus;
    private final TextView tvItemGoodsNum;
    private final TextView tvItemOrderAmount;
    private final LinearLayout linearItemBusinessOrderChild;
    private final TextView tvItemDeleteOrder;

    public MerchantOrderViewHolder(ViewGroup parent, @LayoutRes int layoutRes) {
        super(parent, layoutRes);
        tvItemOrderNo = $(R.id.tvItemOrderNo);
        tvItemOrderStatus = $(R.id.tvItemOrderStatus);
        tvItemGoodsNum = $(R.id.tvItemGoodsNum);
        tvItemOrderAmount = $(R.id.tvItemOrderAmount);
        tvItemDeleteOrder = $(R.id.tvItemDeleteOrder);
        linearItemBusinessOrderChild = $(R.id.linearItemBusinessOrderChild);
//        recyclerBusinessOrderChild = $(R.id.recyclerBusinessOrderChild);
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

        // 动态添加子订单列表
        if (data.getSuborders() != null && data.getSuborders().isEmpty()) {
            linearItemBusinessOrderChild.setVisibility(View.GONE);
        } else {
            linearItemBusinessOrderChild.removeAllViews();
            linearItemBusinessOrderChild.setVisibility(View.VISIBLE);
            for (MerchantOrderEntity.OrdersBean.SubordersBean bean : data.getSuborders()) {
                View view = LayoutInflater.from(getContext()).inflate(R.layout.recycler_item_merchant_order_child, linearItemBusinessOrderChild, false);

                ImageView ivItemOrderImg = (ImageView) view.findViewById(R.id.ivItemOrderImg);
                TextView tvItemOrderGoodsName = (TextView) view.findViewById(R.id.tvItemOrderGoodsName);
                TextView tvItemOrderTime = (TextView) view.findViewById(R.id.tvItemOrderTime);
                TextView tvItemOrderGoodsPrice = (TextView) view.findViewById(R.id.tvItemOrderGoodsPrice);
                TextView tvItemNum = (TextView) view.findViewById(R.id.tvItemNum);

                ImageHelper.display(getContext(), ivItemOrderImg, bean.getThumbnail());
                tvItemOrderGoodsName.setText(bean.getTitle());
                tvItemOrderTime.setText("预约时间：" + bean.getSchedule_time());
                tvItemOrderGoodsPrice.setText(String.valueOf(bean.getProduct_price()));
                tvItemNum.setText("x" + bean.getProduct_count());
                linearItemBusinessOrderChild.addView(view);
            }
        }

//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
//        recyclerBusinessOrderChild.setLayoutManager(linearLayoutManager);
//        MerchantOrderChildAdapter childAdapter = new MerchantOrderChildAdapter(getContext(), data.getSuborders());
//        recyclerBusinessOrderChild.setAdapter(childAdapter);
//        recyclerBusinessOrderChild.addItemDecoration(new DividerDecoration(getContext().getResources().getColor(android.R.color.transparent), 1));
    }
}
