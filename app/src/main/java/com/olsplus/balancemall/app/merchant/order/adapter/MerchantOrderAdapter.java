package com.olsplus.balancemall.app.merchant.order.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.olsplus.balancemall.R;
import com.olsplus.balancemall.app.merchant.order.bean.MerchantOrderEntity;
import com.olsplus.balancemall.app.merchant.order.viewholder.MerchantOrderViewHolder;

public class MerchantOrderAdapter extends RecyclerArrayAdapter<MerchantOrderEntity.OrdersBean> {

    private OnOrderOperationListener listener;

    public MerchantOrderAdapter(Context context) {
        super(context);
    }

    public interface OnOrderOperationListener {
        void cancelOrder(int position);

        void onItemClick(int position);
    }

    public void setOnOrderOperationListener(OnOrderOperationListener listener) {
        this.listener = listener;
    }

    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
        return new MerchantOrderViewHolder(parent, R.layout.recycler_item_merchant_order);
    }

    @Override
    public void OnBindViewHolder(final BaseViewHolder holder, final int position) {
        super.OnBindViewHolder(holder, position);

        // 取消订单
        holder.itemView.findViewById(R.id.tvItemDeleteOrder).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    listener.cancelOrder(position);
                }
            }
        });

        // 行点击事件
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    listener.onItemClick(position);
                }
            }
        });

//        holder.itemView.findViewById(R.id.recyclerBusinessOrderChild).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (listener != null) {
//                    listener.onItemClick(position);
//                }
//            }
//        });
    }
}
