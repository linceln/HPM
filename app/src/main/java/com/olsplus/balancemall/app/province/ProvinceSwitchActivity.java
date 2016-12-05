package com.olsplus.balancemall.app.province;

import android.os.Bundle;
import android.view.View;

import com.olsplus.balancemall.R;
import com.olsplus.balancemall.core.app.BaseFragment;
import com.olsplus.balancemall.core.app.MainActivity;


public class ProvinceSwitchActivity extends MainActivity {

    @Override
    protected BaseFragment getFirstFragment() {
        return ProvinceSwitchFragment.getInstance();
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setActionBar();
        mTitleName.setText("选择写字楼");
        mLeftOperationImageView.setBackgroundResource(R.drawable.back_normal);
    }

    @Override
    public void onClick(View v) {

    }
}
