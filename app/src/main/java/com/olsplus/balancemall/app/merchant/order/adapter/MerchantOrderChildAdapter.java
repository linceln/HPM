package com.olsplus.balancemall.app.merchant.order.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.olsplus.balancemall.R;
import com.olsplus.balancemall.app.merchant.order.bean.MerchantOrderEntity;
import com.olsplus.balancemall.component.recycler.BaseAdapter;
import com.olsplus.balancemall.component.recycler.CustomViewHolder;

import java.util.List;


public class MerchantOrderChildAdapter extends BaseAdapter {

    public MerchantOrderChildAdapter(Context context, List<MerchantOrderEntity.OrdersBean.SubordersBean> list) {
        super(context, list);
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recycler_item_merchant_order_child, parent, false);
        return new CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CustomViewHolder holder, int position) {
        MerchantOrderEntity.OrdersBean.SubordersBean bean = (MerchantOrderEntity.OrdersBean.SubordersBean) list.get(position);
        holder.setImage(context, R.id.ivItemOrderImg, bean.getThumbnail());
        holder.setText(R.id.tvItemOrderGoodsName, bean.getTitle());
        holder.setText(R.id.tvItemOrderTime, "预约时间：" + bean.getSchedule_time());
        holder.setText(R.id.tvItemOrderGoodsPrice, String.valueOf(bean.getProduct_price()));
        holder.setText(R.id.tvItemNum, "x" + bean.getProduct_count());
    }
}
