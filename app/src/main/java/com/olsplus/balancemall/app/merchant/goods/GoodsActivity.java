package com.olsplus.balancemall.app.merchant.goods;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;

import com.olsplus.balancemall.R;
import com.olsplus.balancemall.core.app.BaseCompatActivity;
import com.olsplus.balancemall.core.util.StrConst;

import java.util.Arrays;
import java.util.List;

public class GoodsActivity extends BaseCompatActivity implements View.OnClickListener {

    private List<String> list = Arrays.asList("出售中", "已售完", "已下架");

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_goods;
    }

    @Override
    protected void setupWindowAnimations() {
    }

    @Override
    protected void initUI() {

        // 标题
        setTitle("商品管理");

        TextView tvAddGoods = (TextView) findViewById(R.id.tvAddGoods);
        tvAddGoods.setOnClickListener(this);

        ViewPager vpGoods = (ViewPager) findViewById(R.id.vpGoods);
        vpGoods.setOffscreenPageLimit(2);
        TabLayout tabGoods = (TabLayout) findViewById(R.id.tabGoods);
        for (String str : list) {
            tabGoods.addTab(tabGoods.newTab().setText(str));
        }
        vpGoods.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {

            @Override
            public int getCount() {

                return list.size();
            }

            @Override
            public Fragment getItem(int position) {
                GoodsFragment fragment = new GoodsFragment();
                Bundle bundle = new Bundle();
                switch (position) {
                    case 0://出售中
                        bundle.putString(StrConst.extra.goods_type, StrConst.goods.ON_SALE);
                        break;
                    case 1://已售完
                        bundle.putString(StrConst.extra.goods_type, StrConst.goods.SOLD_OUT);
                        break;
                    case 2://已下架
                        bundle.putString(StrConst.extra.goods_type, StrConst.goods.OFF_SALE);
                        break;
                }
                fragment.setArguments(bundle);
                return fragment;
            }
        });
        vpGoods.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabGoods));
        tabGoods.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(vpGoods));

    }

    @Override
    protected void initData() {
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.tvAddGoods:// 添加商品
                intent = new Intent(this, AddGoodsActivity.class);
                intent.putExtra(StrConst.extra.goods_edit_type, StrConst.input.adding);
                startActivity(intent);
                break;
        }
    }
}
