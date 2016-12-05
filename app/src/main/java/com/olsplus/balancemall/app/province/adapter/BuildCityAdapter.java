package com.olsplus.balancemall.app.province.adapter;


import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.olsplus.balancemall.app.province.bean.BuildingAddressEntity;
import com.olsplus.balancemall.app.province.view.BuildCityViewHolder;

public class BuildCityAdapter extends RecyclerArrayAdapter<BuildingAddressEntity> {

    private OnBuildingItemClickListener mOnItemClickListener;

    public interface OnBuildingItemClickListener {
        void onItemClick(int position, BaseViewHolder holder);
    }

    public void setOnBuildingItemClickListener(OnBuildingItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    public BuildCityAdapter(Context context) {
        super(context);
    }

    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
        return new BuildCityViewHolder(parent);
    }

    @Override
    public void OnBindViewHolder(final BaseViewHolder holder, final int position) {
        super.OnBindViewHolder(holder, position);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick(position, holder);
                }
            }
        });
    }
}
