package com.olsplus.balancemall.app.province;


import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.olsplus.balancemall.R;
import com.olsplus.balancemall.app.province.bean.AddressEntity;
import com.olsplus.balancemall.app.province.view.CityPopWindow;
import com.olsplus.balancemall.core.app.BaseFragment;
import com.olsplus.balancemall.core.app.MainActivity;
import com.olsplus.balancemall.core.util.ToastUtil;

import java.util.List;

public class BuildCityActivity extends MainActivity implements CityPopWindow.OnCityClickListener {

    private String cityId;
    private String cityName;
    private String from;

    private CityPopWindow cityPopWindow;
    private BuildCityFragment buildCityFragment;

    @Override
    protected BaseFragment getFirstFragment() {
        buildCityFragment = BuildCityFragment.getInstance(cityId, from);
        return buildCityFragment;
    }

    @Override
    protected int getFragmentContentId() {
        return R.id.fragment_container;
    }

    @Override
    protected int getTitleViewId() {
        return R.layout.action_bar_view;
    }

    @Override
    protected int getContentViewId() {
        return R.layout.fragment_main_container;
    }

    @Override
    protected void handleIntent(Intent intent) {
        super.handleIntent(intent);
        cityId = intent.getStringExtra("city_id");
        cityName = intent.getStringExtra("city_name");
        from = intent.getStringExtra("from");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setActionBar();
        mTitleName.setText("选择写字楼");
        if (!TextUtils.isEmpty(cityName)) {
            mRightLayout.setVisibility(View.VISIBLE);
            mRightOperationDes.setText(cityName);
        }
        mRightOperationDes.setOnClickListener(this);
        mLeftOperationImageView.setBackgroundResource(R.drawable.back_normal);
    }

    protected String getCurrentCity() {
        return cityName;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.right_operation_tv:
                if (cityPopWindow == null) {
                    cityPopWindow = new CityPopWindow(this);
                    cityPopWindow.setOnCityClickListener(this);
                }
                List<AddressEntity> addressEntityList = buildCityFragment.getAllCitys();
                if (addressEntityList.isEmpty()) {
                    ToastUtil.showShort(this, "查询不到城市");
                    return;
                }
                cityPopWindow.setView(addressEntityList);
                cityPopWindow.show(mRightOperationDes);
                break;
        }
    }

    @Override
    public void onCityClick(String id) {
        if (!TextUtils.isEmpty(id)) {
            buildCityFragment.onRefresh();
        }
    }
}
