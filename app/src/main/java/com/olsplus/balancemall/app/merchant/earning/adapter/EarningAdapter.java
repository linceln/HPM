package com.olsplus.balancemall.app.merchant.earning.adapter;

import android.content.Context;
import android.view.ViewGroup;

import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.olsplus.balancemall.R;
import com.olsplus.balancemall.app.merchant.earning.bean.EarningListEntity;
import com.olsplus.balancemall.app.merchant.earning.viewholder.EarningViewHolder;


public class EarningAdapter extends RecyclerArrayAdapter<EarningListEntity.DataBean> {
    public EarningAdapter(Context context) {
        super(context);
    }

    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
        return new EarningViewHolder(parent, R.layout.recycler_item_earning);
    }

    @Override
    public void OnBindViewHolder(BaseViewHolder holder, int position) {
        super.OnBindViewHolder(holder, position);
    }
}
