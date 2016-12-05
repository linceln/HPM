package com.olsplus.balancemall.app.province.adapter;


import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.olsplus.balancemall.R;
import com.olsplus.balancemall.app.province.bean.AddressEntity;
import com.olsplus.balancemall.app.province.view.CityFootViewHolder;
import com.olsplus.balancemall.app.province.view.CityHeadViewHolder;
import com.olsplus.balancemall.app.province.view.CitysViewHolder;

public class CityAdapter extends RecyclerArrayAdapter<Object> {
    public static final int TYPE_INVALID = 0;

    public static final int TYPE_HEAD = 1;

    public static final int TYPE_CONTENT = 2;

    public static final int TYPE_FOOT = 3;

    private OnCityItemClickListener mOnItemClickListener;

    public interface OnCityItemClickListener {
        void onItemClick(int position, BaseViewHolder holder);
    }

    public void setOnCityItemClickListener(OnCityItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    public CityAdapter(Context context) {
        super(context);
    }


    @Override
    public int getViewType(int position) {
        if (getItem(position) instanceof String) {
            if (position == 0) {
                return TYPE_HEAD;
            } else if (position == getCount() - 1) {
                return TYPE_FOOT;
            }
        } else if (getItem(position) instanceof AddressEntity) {
            return TYPE_CONTENT;
        }
        return TYPE_INVALID;
    }

    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case TYPE_HEAD:
                return new CityHeadViewHolder(parent);
            case TYPE_FOOT:
                return new CityFootViewHolder(parent);
            case TYPE_CONTENT:
                return new CitysViewHolder(parent);
        }
        return null;
    }

    @Override
    public void OnBindViewHolder(final BaseViewHolder holder, final int position) {
        super.OnBindViewHolder(holder, position);
        if (position != 0 && position != getCount() - 1) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnItemClickListener != null) {
                        mOnItemClickListener.onItemClick(position, holder);
                    }
                }
            });
        }

        if(getViewType(position) == TYPE_CONTENT){
            View divider = holder.itemView.findViewById(R.id.divider);
            if(position == getCount() - 1){
                divider.setVisibility(View.GONE);
            }else{
                divider.setVisibility(View.VISIBLE);
            }
        }
    }


}
