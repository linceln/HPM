package com.olsplus.balancemall.app.province.view;


import android.text.TextUtils;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.olsplus.balancemall.R;

public class CityHeadViewHolder extends BaseViewHolder<String> {

    private TextView headTv;

    public CityHeadViewHolder(ViewGroup parent) {
        super(parent, R.layout.home_province_list_head);
        headTv = $(R.id.province_list_head_tv);
    }

    @Override
    public void setData(String data) {
        super.setData(data);
        if(!TextUtils.isEmpty(data)){
            headTv.setText(data);
        }
    }
}

