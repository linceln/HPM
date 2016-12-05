package com.olsplus.balancemall.app.province.view;


import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.olsplus.balancemall.R;
import com.olsplus.balancemall.app.province.bean.AddressEntity;

public class CitysViewHolder extends BaseViewHolder<AddressEntity> {

    private TextView cityTv;
    private ImageView selectedIv;

    public CitysViewHolder(ViewGroup parent) {
        super(parent, R.layout.home_province_list_item);
        cityTv = $(R.id.text);
        selectedIv= $(R.id.home_province_item_mark_iv);
    }

    @Override
    public void setData(AddressEntity data) {
        super.setData(data);
        if(data!=null){
            cityTv.setText(data.getName());
        }
    }
}
