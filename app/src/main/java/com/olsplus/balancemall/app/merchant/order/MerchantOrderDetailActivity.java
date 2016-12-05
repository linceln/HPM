package com.olsplus.balancemall.app.merchant.order;

import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jude.easyrecyclerview.decoration.DividerDecoration;
import com.olsplus.balancemall.R;
import com.olsplus.balancemall.app.merchant.order.Business.MerchantBusiness;
import com.olsplus.balancemall.app.merchant.order.adapter.MerchantOrderDetailAdapter;
import com.olsplus.balancemall.app.merchant.order.bean.MerchantOrderDetailEntity;
import com.olsplus.balancemall.app.mystore.util.OrderHelper;
import com.olsplus.balancemall.core.app.BaseCompatActivity;
import com.olsplus.balancemall.core.bean.BaseResultEntity;
import com.olsplus.balancemall.core.exception.layout.DefaultExceptionListener;
import com.olsplus.balancemall.core.exception.layout.ExceptionManager;
import com.olsplus.balancemall.core.http.RequestCallback;
import com.olsplus.balancemall.core.util.LogUtil;
import com.olsplus.balancemall.core.util.SnackbarUtil;
import com.olsplus.balancemall.core.util.StrConst;
import com.olsplus.balancemall.core.util.ToastUtil;
import com.olsplus.balancemall.core.util.UIUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import static com.olsplus.balancemall.core.util.LoadingDialogManager.showLoading;
import static com.olsplus.balancemall.core.util.LoadingDialogManager.dismissLoading;

public class MerchantOrderDetailActivity extends BaseCompatActivity implements MerchantOrderDetailAdapter.OnOrderOperationListener, View.OnClickListener {

    private List<MerchantOrderDetailEntity.OrderDetailBean.SubordersBean> list = new ArrayList<>();
    private MerchantOrderDetailAdapter adapter;
    private TextView tvOrderDetailTotal;
    private TextView tvOrderDetailIntegral;
    private TextView tvOrderDetailAmount;
    private TextView tvOrderNumber;
    private TextView tvDealNumber;
    private TextView tvCreatedTime;
    private TextView tvPayTime;
    private LinearLayout linearOrderDetailAmount;
    private LinearLayout linearOrderDetailIntegral;

    private long order_id;
    private ExceptionManager manager;
    private View container;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_merchant_order_detail;
    }

    @Override
    protected void setupWindowAnimations() {
    }

    @Override
    protected void initUI() {

        setTitle("订单详情");

        container = findViewById(R.id.container);

        linearOrderDetailIntegral = (LinearLayout) findViewById(R.id.linearOrderDetailIntegral);
        linearOrderDetailAmount = (LinearLayout) findViewById(R.id.linearOrderDetailAmount);

        tvOrderDetailTotal = (TextView) findViewById(R.id.tvOrderDetailTotal);
        tvOrderDetailIntegral = (TextView) findViewById(R.id.tvOrderDetailIntegral);
        tvOrderDetailAmount = (TextView) findViewById(R.id.tvOrderDetailAmount);
        tvOrderNumber = (TextView) findViewById(R.id.tvOrderNumber);
        tvDealNumber = (TextView) findViewById(R.id.tvDealNumber);
        tvCreatedTime = (TextView) findViewById(R.id.tvCreatedTime);
        tvPayTime = (TextView) findViewById(R.id.tvPayTime);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerOrderDetail);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerDecoration(this.getResources().getColor(android.R.color.transparent), 1));
        adapter = new MerchantOrderDetailAdapter(this, list);
        adapter.setOnOrderOperationListener(this);
        recyclerView.setAdapter(adapter);

        manager = ExceptionManager.initialize(container, new DefaultExceptionListener(this));
    }

    @Override
    protected void getExtras() {

        order_id = getIntent().getLongExtra(StrConst.extra.merchant_order_id, 0);
    }

    @Override
    protected void initData() {

        MerchantBusiness.getMerchantOrderDetail(this, order_id, new RequestCallback<BaseResultEntity>() {

            @Override
            public void onSuccess(BaseResultEntity baseResultEntity) {
                manager.hide();
                MerchantOrderDetailEntity entity = (MerchantOrderDetailEntity) baseResultEntity;
                MerchantOrderDetailEntity.OrderDetailBean orderDetail = entity.getOrderDetail();
                adapter.setOrderDetail(orderDetail);
                if (orderDetail != null) {

                    // 订单未支付不显示
                    if (OrderHelper.WAIT_BUYER_PAY.equals(orderDetail.getStatus())) {
                        linearOrderDetailIntegral.setVisibility(View.GONE);
                        linearOrderDetailAmount.setVisibility(View.GONE);
                    } else {
                        linearOrderDetailIntegral.setVisibility(View.VISIBLE);
                        linearOrderDetailAmount.setVisibility(View.VISIBLE);
                    }

                    tvOrderDetailTotal.setText(UIUtil.formatLablePrice(orderDetail.getTotal()));
                    tvOrderDetailIntegral.setText(UIUtil.formatLablePrice(orderDetail.getPointPay()));
                    tvOrderDetailAmount.setText(UIUtil.formatLablePrice(orderDetail.getPay()));

                    tvOrderNumber.setText("订单编号：" + String.valueOf(orderDetail.getOrder_id()));
                    if (orderDetail.getTrade_id() != null) {
                        tvDealNumber.setText("交易编号：" + orderDetail.getTrade_id());
                    }
                    if (orderDetail.getCreated_time() != null) {
                        tvCreatedTime.setText("创建时间：" + orderDetail.getCreated_time());
                    }
                    if (orderDetail.getPay_time() != null) {
                        tvPayTime.setText("付款时间：" + orderDetail.getPay_time());
                    }
                    if (orderDetail.getSuborders() != null) {
                        list.clear();
                        list.addAll(orderDetail.getSuborders());
                        adapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onError(String msg) {
                manager.showRetry();
                ToastUtil.showShort(MerchantOrderDetailActivity.this, msg);
            }
        });
    }

    /**
     * 同意退款
     *
     * @param position
     */
    @Override
    public void agreeRefund(int position) {
        MerchantOrderDetailEntity.OrderDetailBean.SubordersBean bean = list.get(position);
        showLoading(this, getString(R.string.loading_default));
        MerchantBusiness.agreeRefund(this, order_id, bean.getProduct_id(), new ClickCallBack());
    }

    /**
     * 拒绝退款
     *
     * @param position
     */
    @Override
    public void refuseRefund(int position) {
        MerchantOrderDetailEntity.OrderDetailBean.SubordersBean bean = list.get(position);
        showLoading(this, getString(R.string.loading_default));
        MerchantBusiness.refuseRefund(this, order_id, bean.getProduct_id(), new ClickCallBack());
    }

    /**
     * 完成订单
     *
     * @param position
     */
    @Override
    public void finishOrder(int position) {
        MerchantOrderDetailEntity.OrderDetailBean.SubordersBean bean = list.get(position);
        MerchantBusiness.finishMerchantOrder(this, order_id, bean.getProduct_id(), new ClickCallBack());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.empty:
            case R.id.retry:
                initData();
                break;
        }
    }

    private class ClickCallBack implements RequestCallback {

        @Override
        public void onSuccess(BaseResultEntity baseResultEntity) {
            dismissLoading();
            initData();
            EventBus.getDefault().post("Button clicked");
            SnackbarUtil.showShort(container, baseResultEntity.getMsg());
        }

        @Override
        public void onError(String msg) {
            dismissLoading();
            LogUtil.e("完成", msg);
            SnackbarUtil.showShort(container, msg);
        }
    }
}
