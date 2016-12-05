package com.olsplus.balancemall.app.pay.voucher;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jude.easyrecyclerview.EasyRecyclerView;
import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.olsplus.balancemall.R;
import com.olsplus.balancemall.app.pay.CheckoutMainActivity;
import com.olsplus.balancemall.app.pay.bean.ShoppingVoucherEntity;
import com.olsplus.balancemall.app.pay.request.CheckOutRequestImpl;
import com.olsplus.balancemall.app.pay.view.IShowVoucherView;
import com.olsplus.balancemall.app.pay.voucher.adapter.PayCouponAdapter;
import com.olsplus.balancemall.core.app.BaseFragment;
import com.olsplus.balancemall.core.app.MainActivity;

import java.util.List;

public class MyCouponActivity extends MainActivity implements IShowVoucherView,PayCouponAdapter.OnVoucherItemClickListener{

    private TextView emptyTv;
    private LinearLayout emptyLinear;
    private EasyRecyclerView mListView;
    private PayCouponAdapter payCouponAdapter;
    private CheckOutRequestImpl outRequest;

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
        return R.layout.pay_checkout_my_voucher;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setActionBar();
        mTitleName.setText("我的票劵");
        mLeftOperationImageView.setBackgroundResource(R.drawable.back_normal);
        emptyTv = ((TextView) findViewById(R.id.pay_coupon_null_tv));
        emptyLinear = ((LinearLayout) findViewById(R.id.pay_coupon_null_linear));
        mListView = ((EasyRecyclerView) findViewById(R.id.pay_coupon_list));
        mListView.setLayoutManager(new LinearLayoutManager(this));
        payCouponAdapter = new PayCouponAdapter(this);
        payCouponAdapter.setOnVoucherItemClickListener(this);
        mListView.setAdapterWithProgress(payCouponAdapter);
        initData();
    }

    private void initData(){
        outRequest = new CheckOutRequestImpl(this);
        outRequest.setShowVoucherView(this);
        outRequest.getVouchers();
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void showVoucherError(String msg) {
        emptyLinear.setVisibility(View.VISIBLE);
        emptyTv.setText(msg);
    }

    @Override
    public void showVoucherView(List<ShoppingVoucherEntity> datas) {
        payCouponAdapter.addAll(datas);
    }

    @Override
    public void onItemClick(int position, BaseViewHolder holder) {
        List<ShoppingVoucherEntity> datas = payCouponAdapter.getAllData();
        if(datas!=null && !datas.isEmpty()){
            ShoppingVoucherEntity data = datas.get(position);
            if(data!=null){
                Intent intent = new Intent(MyCouponActivity.this,CheckoutMainActivity.class);
                intent.putExtra("voucher_select",data);
                setResult(RESULT_OK,intent);
                finish();
            }
        }
    }
}
