package com.olsplus.balancemall.app.mine;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import com.olsplus.balancemall.R;
import com.olsplus.balancemall.app.mine.util.OrderHelper;
import com.olsplus.balancemall.component.view.PagerSlidingTabStrip;
import com.olsplus.balancemall.core.app.BaseFragment;
import com.olsplus.balancemall.core.app.MainActivity;


public class MyOrderListActivity extends MainActivity {

    private ViewPager pager;
    private PagerSlidingTabStrip tab;
    private FragmentPagerAdapter adapter;

    private int currentItem;


    @Override
    protected BaseFragment getFirstFragment() {
        return null;
    }

    @Override
    protected int getFragmentContentId() {
        return 0;
    }

    @Override
    protected int getTitleViewId() {
        return R.layout.action_bar_view;
    }

    @Override
    protected int getContentViewId() {
        return R.layout.mystore_order_list;
    }

    @Override
    protected void handleIntent(Intent intent) {
        super.handleIntent(intent);
        currentItem = intent.getIntExtra("current_item",0);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setActionBar();
        mTitleName.setText("我的订单");
        mLeftOperationImageView.setBackgroundResource(R.drawable.back_normal);

        pager = (ViewPager) findViewById(R.id.pager);
        tab = (PagerSlidingTabStrip) findViewById(R.id.indicator);
        adapter = new TabPageIndicatorAdapter(getSupportFragmentManager());
        pager.setAdapter(adapter);
        tab.setViewPager(pager);
        pager.setCurrentItem(currentItem);
    }


    @Override
    public void onClick(View v) {

    }

    class TabPageIndicatorAdapter extends FragmentPagerAdapter {
        public TabPageIndicatorAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            MyOrderFragment myOrderFragment = (MyOrderFragment) super.instantiateItem(container, position);
            return myOrderFragment;
        }

        @Override
        public int getItemPosition(Object object) {
            return PagerAdapter.POSITION_NONE;

        }

        @Override
        public Fragment getItem(int position) {
            String orderStatus= OrderHelper.ALL;
            switch (position){
                case 0:
                    orderStatus = OrderHelper.ALL;
                    break;
                case 1:
                    orderStatus =OrderHelper.TYPE_WAIT_BUYER_PAY;
                    break;
                case 2:
                    orderStatus =OrderHelper.TYPE_WAIT_FOR_EVALUATE;
                    break;
                case 3:
                    orderStatus  =OrderHelper.TYPE_REFUND;
                    break;

            }
            MyOrderFragment myOrderFragment = MyOrderFragment.getInstance(orderStatus);
            return myOrderFragment;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            String title = "";
            switch (position) {
                case 0:
                    title = "全部";
                    break;
                case 1:
                    title = "待付款";
                    break;
                case 2:
                    title = "待评价";
                    break;
                case 3:
                    title = "退款单";
                    break;

            }
            return title;
        }

        @Override
        public int getCount() {
            return 4;
        }
    }
}
