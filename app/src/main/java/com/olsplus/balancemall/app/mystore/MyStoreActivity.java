package com.olsplus.balancemall.app.mystore;

import android.os.Bundle;
import android.view.View;

import com.olsplus.balancemall.R;
import com.olsplus.balancemall.app.bottom.BottomNavigateFragment;
import com.olsplus.balancemall.core.app.BaseFragment;
import com.olsplus.balancemall.core.app.MainActivity;


public class MyStoreActivity extends MainActivity {


    @Override
    protected BaseFragment getFirstFragment() {
        return MyStoreFragment.getInstance();
    }

    @Override
    protected int getContentViewId() {
        return R.layout.mystore_main;
    }

    @Override
    protected int getFragmentContentId() {
        return R.id.fragment_container_mystore;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setBottomFragment(new BottomNavigateFragment());
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onClick(View v) {

    }
}
