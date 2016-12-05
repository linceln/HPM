package com.olsplus.balancemall.app.province.view;


import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.olsplus.balancemall.R;
import com.olsplus.balancemall.app.province.bean.BuildingAddressEntity;
import com.olsplus.balancemall.core.util.SPUtil;

public class BuildCityViewHolder extends BaseViewHolder<BuildingAddressEntity> {

    private TextView nameTv;
    private TextView addressTv;
    private ImageView selectedIv;

    public BuildCityViewHolder(ViewGroup parent) {
        super(parent, R.layout.home_build_city_list_item);
        nameTv = $(R.id.build_list_item_name);
        addressTv= $(R.id.build_list_item_address);
        selectedIv= $(R.id.home_build_item_mark_iv);
    }

    @Override
    public void setData(BuildingAddressEntity data) {
        super.setData(data);
        if(data!=null){
            nameTv.setText(data.getName());
            addressTv.setText(data.getAddr());
            String buildingName = (String) SPUtil.get(getContext(), SPUtil.BUILDING_NAME, "");
            if(buildingName.equals(data.getName())){
                selectedIv.setVisibility(View.VISIBLE);
            }else{
                selectedIv.setVisibility(View.GONE);
            }
        }
    }
}
