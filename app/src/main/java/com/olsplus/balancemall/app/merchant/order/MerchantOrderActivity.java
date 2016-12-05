package com.olsplus.balancemall.app.merchant.order;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.olsplus.balancemall.R;
import com.olsplus.balancemall.core.app.BaseCompatActivity;
import com.olsplus.balancemall.core.util.StrConst;

import java.util.Arrays;
import java.util.List;

public class MerchantOrderActivity extends BaseCompatActivity {

    private List<String> list = Arrays.asList("待付款", "待完成", "退款", "已完成", "全部");

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_merchant_order;
    }

    @Override
    protected void setupWindowAnimations() {
    }

    @Override
    protected void initUI() {

        // 标题
        setTitle("订单管理");

        ViewPager vpOrder = (ViewPager) findViewById(R.id.vpOrder);
        TabLayout tabOrder = (TabLayout) findViewById(R.id.tabOrder);
        for (String str : list) {
            tabOrder.addTab(tabOrder.newTab().setText(str));
        }
        vpOrder.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {

            @Override
            public int getCount() {

                return list.size();
            }

            @Override
            public Fragment getItem(int position) {
                MerchantOrderFragment fragment = new MerchantOrderFragment();
                Bundle bundle = new Bundle();
                switch (position) {
                    case 0://待付款
                        bundle.putString(StrConst.extra.merchant_order_type, "WAIT_BUYER_PAY");
                        break;
                    case 1://待完成
                        bundle.putString(StrConst.extra.merchant_order_type, "WAIT_FOR_SERVE");
                        break;
                    case 2://退款
                        bundle.putString(StrConst.extra.merchant_order_type, "REFUND");
                        break;
                    case 3://已完成
                        bundle.putString(StrConst.extra.merchant_order_type, "FINISHED");
                        break;
                    case 4://全部
                        bundle.putString(StrConst.extra.merchant_order_type, "ALL");
                        break;

                }
                fragment.setArguments(bundle);
                return fragment;
            }
        });
        vpOrder.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabOrder));
        tabOrder.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(vpOrder));
    }

    @Override
    protected void initData() {
    }
}
