package com.olsplus.balancemall.app.pay.voucher.adapter;


import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.olsplus.balancemall.app.pay.bean.ShoppingVoucherEntity;
import com.olsplus.balancemall.app.pay.view.PayVoucherViewHolder;
import com.olsplus.balancemall.core.util.SPUtil;

public class PayCouponAdapter extends RecyclerArrayAdapter<ShoppingVoucherEntity> {

    private OnVoucherItemClickListener mOnVoucherItemClickListener;

    public interface OnVoucherItemClickListener {
        void onItemClick(int position, BaseViewHolder holder);
    }

    public void setOnVoucherItemClickListener(OnVoucherItemClickListener listener) {
        this.mOnVoucherItemClickListener = listener;
    }

    public PayCouponAdapter(Context context) {
        super(context);
    }

    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
        return new PayVoucherViewHolder(parent);
    }

    @Override
    public void OnBindViewHolder(final BaseViewHolder holder, final int position) {
        super.OnBindViewHolder(holder, position);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                for (ShoppingVoucherEntity entity : mObjects) {
                    entity.setSelected(false);
                }
                mObjects.get(position).setSelected(true);
                SPUtil.put(getContext(),"couponId", mObjects.get(position).getId());
                notifyDataSetChanged();

                if (mOnVoucherItemClickListener != null) {
                    mOnVoucherItemClickListener.onItemClick(position, holder);
                }
            }
        });
    }
}
