package com.olsplus.balancemall.app.cart;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.olsplus.balancemall.R;
import com.olsplus.balancemall.app.bottom.BottomNavigateFragment;
import com.olsplus.balancemall.core.app.BaseFragment;
import com.olsplus.balancemall.core.app.MainActivity;


public class CartActivity extends MainActivity {

//    private String addCartJson;
    private String from;

    @Override
    protected BaseFragment getFirstFragment() {
        return CartFragment.getInstance();
    }

    @Override
    protected int getFragmentContentId() {
        return R.id.fragment_container;
    }


    @Override
    protected int getContentViewId() {
        return R.layout.fragment_main_container;
    }

//    @Override
//    protected void handleIntent(Intent intent) {
//        super.handleIntent(intent);
////        addCartJson = intent.getStringExtra("add_cart_parmas");
//
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (null != getIntent()) {
            from =  getIntent().getStringExtra("from");
        }

        if(TextUtils.isEmpty(from)){
            setBottomFragment(new BottomNavigateFragment());
        }
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onClick(View v) {

    }
}
