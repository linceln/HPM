package com.olsplus.balancemall.app.merchant;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.olsplus.balancemall.R;
import com.olsplus.balancemall.app.merchant.earning.EarningActivity;
import com.olsplus.balancemall.app.merchant.goods.GoodsActivity;
import com.olsplus.balancemall.app.merchant.order.Business.MerchantBusiness;
import com.olsplus.balancemall.app.merchant.order.MerchantOrderActivity;
import com.olsplus.balancemall.app.merchant.order.bean.MerchantEntity;
import com.olsplus.balancemall.core.app.BaseCompatActivity;
import com.olsplus.balancemall.core.http.HttpResultObserver;
import com.olsplus.balancemall.core.util.SPUtil;
import com.olsplus.balancemall.core.util.SnackbarUtil;
import com.olsplus.balancemall.core.util.StrConst;
import com.trello.rxlifecycle.android.ActivityEvent;

public class MerchantActivity extends BaseCompatActivity implements View.OnClickListener {

    private TextView tvOrderNum;
    private TextView tvEarningToday;
    private TextView tvEarningMonth;
    private View container;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_merchant;
    }

    @Override
    protected void setupWindowAnimations() {
    }

    @Override
    protected void initUI() {
        // 标题栏
        setTitle("商家管理");

        container = findViewById(R.id.activity_business);
        tvOrderNum = (TextView) findViewById(R.id.tvOrderNum);
        tvEarningToday = (TextView) findViewById(R.id.tvEarningToday);
        tvEarningMonth = (TextView) findViewById(R.id.tvEarningMonth);

        findViewById(R.id.linearEarnMonth).setOnClickListener(this);
        findViewById(R.id.linearEarnToday).setOnClickListener(this);
        findViewById(R.id.linearOrderToday).setOnClickListener(this);

        TextView tvOrderManage = (TextView) findViewById(R.id.tvOrderManage);
        tvOrderManage.setOnClickListener(this);
        TextView tvGoodsManage = (TextView) findViewById(R.id.tvGoodsManage);
        tvGoodsManage.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        MerchantBusiness.getMerchant()
                .compose(this.<MerchantEntity>bindUntilEvent(ActivityEvent.STOP))// 离开页面之后不再发送
                .subscribe(new HttpResultObserver<MerchantEntity>() {

                    @Override
                    public void onSuccess(MerchantEntity entity) {
                        SPUtil.put(MerchantActivity.this, SPUtil.RES_PATH, entity.getRes_path());
                        SPUtil.put(MerchantActivity.this, SPUtil.PROVIDER_NAME, entity.getProvider_name());
                        tvOrderNum.setText(entity.getOrder_count());
                        tvEarningToday.setText(entity.getDay_revenue());
                        tvEarningMonth.setText(entity.getMonth_revenue());
                    }

                    @Override
                    public void onFail(String msg) {
                        SnackbarUtil.showShort(container, msg);
                    }
                });
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.linearOrderToday://今日订单
                intent = new Intent(this, EarningActivity.class);
                intent.putExtra(StrConst.extra.earning_type, StrConst.earn.day);
                intent.putExtra(StrConst.extra.order_today, StrConst.earn.day);
                startActivity(intent);
                break;
            case R.id.linearEarnToday://今日收入
                intent = new Intent(this, EarningActivity.class);
                intent.putExtra(StrConst.extra.earning_type, StrConst.earn.day);
                startActivity(intent);
                break;
            case R.id.linearEarnMonth://本月收入
                intent = new Intent(this, EarningActivity.class);
                intent.putExtra(StrConst.extra.earning_type, StrConst.earn.month);
                startActivity(intent);
                break;
            case R.id.tvOrderManage://订单管理
                intent = new Intent(this, MerchantOrderActivity.class);
                startActivity(intent);
                break;
            case R.id.tvGoodsManage://商品管理
                intent = new Intent(this, GoodsActivity.class);
                startActivity(intent);
                break;
        }
    }
}
