package com.olsplus.balancemall.app.merchant.goods.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.olsplus.balancemall.R;
import com.olsplus.balancemall.app.merchant.goods.bean.GoodsListEntity;
import com.olsplus.balancemall.app.merchant.goods.viewholder.GoodsViewHolder;


public class GoodsAdapter extends RecyclerArrayAdapter<GoodsListEntity.ProductListBean> {

    private OnGoodsOperationListener listener;

    public GoodsAdapter(Context context) {
        super(context);
    }

    public interface OnGoodsOperationListener {
        void onItemClick(int position);
    }

    public void setOnGoodsOperationListener(OnGoodsOperationListener listener) {

        this.listener = listener;
    }

    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
        return new GoodsViewHolder(parent, R.layout.recycler_item_goods);
    }

    @Override
    public void OnBindViewHolder(BaseViewHolder holder, final int position) {
        super.OnBindViewHolder(holder, position);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    listener.onItemClick(position);
                }
            }
        });

    }
}
