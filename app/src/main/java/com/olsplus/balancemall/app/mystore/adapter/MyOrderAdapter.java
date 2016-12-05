package com.olsplus.balancemall.app.mystore.adapter;


import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.olsplus.balancemall.R;
import com.olsplus.balancemall.app.mystore.bean.MyOrderItem;
import com.olsplus.balancemall.app.mystore.view.MyOrderViewHolder;

public class MyOrderAdapter extends RecyclerArrayAdapter<MyOrderItem> {

    private OnOrderItemClickListener mOnItemClickListener;

    public interface OnOrderItemClickListener {
        void onItemClick(int position, BaseViewHolder holder);
    }

    public void setOnOrderItemClickListener(OnOrderItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    private OnOrderOpeartionListener mOnOrderOpeartionListener;

    public interface OnOrderOpeartionListener{
        public void pay(int  position);
        public void cancle(int  position);
        public void delete(int  position);
        public void comment(int  position);
    }

    public void setOnOrderOpeartionListener( OnOrderOpeartionListener mOnOrderOpeartionListener){
        this.mOnOrderOpeartionListener = mOnOrderOpeartionListener;
    }


    public MyOrderAdapter(Context context) {
        super(context);
    }

    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyOrderViewHolder(parent);
    }

    @Override
    public void OnBindViewHolder(final BaseViewHolder holder, final int position) {
        super.OnBindViewHolder(holder, position);
        final View divider = (View) holder.itemView.findViewById(R.id.divider_view);
        if(position == 0){
            divider.setVisibility(View.GONE);
        }else{
            divider.setVisibility(View.VISIBLE);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick(position, holder);
                }
            }
        });
        holder.itemView.findViewById(R.id.pay_imm_pop_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mOnOrderOpeartionListener!=null){
                    mOnOrderOpeartionListener.pay(position);
                }
            }
        });
        holder.itemView.findViewById(R.id.order_to_delete_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mOnOrderOpeartionListener!=null){
                    mOnOrderOpeartionListener.delete(position);
                }
            }
        });
        holder.itemView.findViewById(R.id.order_to_cancel_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mOnOrderOpeartionListener!=null){
                    mOnOrderOpeartionListener.cancle(position);
                }
            }
        });
        holder.itemView.findViewById(R.id.order_to_comment_tv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mOnOrderOpeartionListener!=null){
                    mOnOrderOpeartionListener.comment(position);
                }
            }
        });



    }
}
