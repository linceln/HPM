package com.olsplus.balancemall.app.province.view;


import android.text.TextUtils;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.olsplus.balancemall.R;

public class CityFootViewHolder extends BaseViewHolder<String> {

    private TextView footTv;

    public CityFootViewHolder(ViewGroup parent) {
        super(parent, R.layout.home_province_list_foot);
        footTv = $(R.id.province_list_foot_tv);
    }

    @Override
    public void setData(String data) {
        super.setData(data);
        if(!TextUtils.isEmpty(data)){
            footTv.setText(data);
        }
    }
}