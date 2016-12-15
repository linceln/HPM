package com.olsplus.balancemall.app.merchant.order.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.olsplus.balancemall.R;
import com.olsplus.balancemall.app.merchant.order.bean.MerchantOrderDetailEntity;
import com.olsplus.balancemall.app.mine.util.OrderHelper;
import com.olsplus.balancemall.component.recycler.BaseAdapter;
import com.olsplus.balancemall.component.recycler.CustomViewHolder;

import java.util.List;


public class MerchantOrderDetailAdapter extends BaseAdapter {

    private OnOrderOperationListener listener;
    private MerchantOrderDetailEntity.OrderDetailBean orderDetail;

    public MerchantOrderDetailAdapter(Context context, List<MerchantOrderDetailEntity.OrderDetailBean.SubordersBean> list) {
        super(context, list);
    }

    public void setOrderDetail(MerchantOrderDetailEntity.OrderDetailBean orderDetail) {
        this.orderDetail = orderDetail;
    }

    public interface OnOrderOperationListener {
        void agreeRefund(int position);

        void refuseRefund(int position);

        void finishOrder(int position);
    }

    public void setOnOrderOperationListener(OnOrderOperationListener listener) {

        this.listener = listener;
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recycler_item_merchant_order_child, parent, false);
        return new CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CustomViewHolder holder, final int position) {
        MerchantOrderDetailEntity.OrderDetailBean.SubordersBean bean = (MerchantOrderDetailEntity.OrderDetailBean.SubordersBean) list.get(position);

        holder.setImage(context, R.id.ivItemOrderImg, bean.getThumbnail());
        holder.setText(R.id.tvItemOrderGoodsName, bean.getTitle());
        holder.setText(R.id.tvItemOrderTime, bean.getSchedule_time());
        holder.setText(R.id.tvItemOrderGoodsPrice, String.valueOf(bean.getProduct_price()));
        holder.setText(R.id.tvItemNum, "x" + bean.getProduct_count());

        holder.setVisibility(R.id.linearItemStatus, View.GONE);

        // 等待商家确认退款，显示“同意”和“拒绝”按钮
        if (OrderHelper.WAIT_SELLER_CONFIRM.equals(bean.getRefund_status())) {
            holder.setVisibility(R.id.linearItemStatus, View.VISIBLE);
            holder.setText(R.id.tvItemDetailStatus, OrderHelper.getReturnStatus(bean.getRefund_status()));
            holder.setText(R.id.tvItemDetailButtonFirst, "同 意");
            holder.setOnClickListener(R.id.tvItemDetailButtonFirst, new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        listener.agreeRefund(position);
                    }
                }
            });
            holder.setText(R.id.tvItemDetailButtonSecond, "拒 绝");
            holder.setOnClickListener(R.id.tvItemDetailButtonSecond, new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        listener.refuseRefund(position);
                    }
                }
            });
        }

        // 退款状态已经确认，只显示状态，不显示按钮
        if (OrderHelper.REFUNDING.equals(bean.getRefund_status())
                || OrderHelper.REFUNDED.equals(bean.getRefund_status())
                || OrderHelper.REFUND_FAILED.equals(bean.getRefund_status())) {
            holder.setVisibility(R.id.linearItemStatus, View.VISIBLE);
            holder.setText(R.id.tvItemDetailStatus, OrderHelper.getReturnStatus(bean.getRefund_status()));
            holder.setVisibility(R.id.tvItemDetailButtonFirst, View.GONE);
            holder.setVisibility(R.id.tvItemDetailButtonSecond, View.GONE);
        }

        // 已支付但是未完成服务，显示完成按钮
        if (OrderHelper.WAIT_FOR_SERVE.equals(orderDetail.getStatus())// 等待服务
                && bean.getTotal() != 0 // 已支付
                && bean.getFinish_time() == null) { // 服务未完成
            holder.setVisibility(R.id.linearItemStatus, View.VISIBLE);
            holder.setText(R.id.tvItemDetailStatus, "等待服务完成");
            holder.setVisibility(R.id.tvItemDetailButtonFirst, View.VISIBLE);
            holder.setText(R.id.tvItemDetailButtonFirst, "完 成");
            holder.setOnClickListener(R.id.tvItemDetailButtonFirst, new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        listener.finishOrder(position);
                    }
                }
            });
            holder.setVisibility(R.id.tvItemDetailButtonSecond, View.GONE);
        }
    }
}
