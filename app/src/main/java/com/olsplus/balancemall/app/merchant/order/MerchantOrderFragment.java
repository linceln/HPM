package com.olsplus.balancemall.app.merchant.order;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jude.easyrecyclerview.EasyRecyclerView;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.jude.easyrecyclerview.decoration.DividerDecoration;
import com.olsplus.balancemall.R;
import com.olsplus.balancemall.app.merchant.order.Business.MerchantBusiness;
import com.olsplus.balancemall.app.merchant.order.adapter.MerchantOrderAdapter;
import com.olsplus.balancemall.app.merchant.order.bean.MerchantOrderEntity;
import com.olsplus.balancemall.core.app.BaseCompatFragment;
import com.olsplus.balancemall.core.bean.BaseResultEntity;
import com.olsplus.balancemall.core.exception.layout.DefaultExceptionListener;
import com.olsplus.balancemall.core.exception.layout.ExceptionManager;
import com.olsplus.balancemall.core.http.RequestCallback;
import com.olsplus.balancemall.core.util.DensityUtil;
import com.olsplus.balancemall.core.util.SPUtil;
import com.olsplus.balancemall.core.util.StrConst;
import com.olsplus.balancemall.core.util.ToastUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

public class MerchantOrderFragment extends BaseCompatFragment implements SwipeRefreshLayout.OnRefreshListener,
        RecyclerArrayAdapter.OnLoadMoreListener, MerchantOrderAdapter.OnOrderOperationListener, View.OnClickListener {

    private Context context;
    private View view;
    private EasyRecyclerView recyclerMerchantOrder;
    private MerchantOrderAdapter merchantOrderAdapter;
    private ExceptionManager manager;

    private String orderType;

    private int page = 1;
    private int count = 4;


    public MerchantOrderFragment() {
    }

    @Override
    public void onStart() {
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        super.onStart();
    }

    @Override
    public void onDestroy() {
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
        super.onDestroy();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (view == null) {
            view = inflater.inflate(R.layout.fragment_merchant_order, container, false);
            context = getContext();
            getExtras();
            initUI();
            initData(orderType);
        }
        return view;
    }

    @Subscribe
    public void onDetailClick(String msg) {// 详情页有按钮点击事件时刷新列表
        onRefresh();
    }

    private void getExtras() {
        orderType = getArguments().getString(StrConst.extra.merchant_order_type);
    }

    private void initUI() {

        recyclerMerchantOrder = (EasyRecyclerView) view.findViewById(R.id.recyclerBusinessOrder);
        recyclerMerchantOrder.setRefreshListener(this);
        RecyclerView recyclerView = recyclerMerchantOrder.getRecyclerView();
        recyclerView.setPadding(0, DensityUtil.dp2px(context, 20), 0, 0);
        recyclerView.setClipToPadding(false);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerMerchantOrder.setLayoutManager(linearLayoutManager);
        merchantOrderAdapter = new MerchantOrderAdapter(getContext());

        merchantOrderAdapter.setMore(R.layout.load_more_layout, this);
        merchantOrderAdapter.setOnOrderOperationListener(this);
        recyclerMerchantOrder.setAdapter(merchantOrderAdapter);
        recyclerMerchantOrder.addItemDecoration(new DividerDecoration(context.getResources().getColor(android.R.color.transparent), DensityUtil.dp2px(context, 20)));
//        recyclerMerchantOrder.setItemAnimator(new DefaultItemAnimator());

        // 空页面
        manager = ExceptionManager.initialize(recyclerMerchantOrder, new DefaultExceptionListener(this));
    }

    /**
     * 请求订单列表
     *
     * @param orderType
     */
    private void initData(String orderType) {

        String local_service_id = (String) SPUtil.get(context, SPUtil.LOCAL_SERVICE_ID, "");
        MerchantBusiness.getOrderList(context, page, count, local_service_id, orderType, new RequestCallback<BaseResultEntity>() {
            @Override
            public void onSuccess(BaseResultEntity baseResultEntity) {
                MerchantOrderEntity entity = (MerchantOrderEntity) baseResultEntity;
                if (entity.getOrders() != null) {

                    merchantOrderAdapter.addAll(entity.getOrders());

                    if (merchantOrderAdapter.getCount() == 0) {
                        // 空页面
                        manager.showEmpty();
                    } else {
                        manager.hide();
                    }
                }
            }

            @Override
            public void onError(String msg) {
//                ToastUtil.showShort(context, msg);
                manager.showRetry();
            }
        });
    }

    /**
     * 取消订单
     *
     * @param position
     */
    @Override
    public void cancelOrder(int position) {
        MerchantBusiness.cancelMerchantOrder(context, merchantOrderAdapter.getItem(position).getOrder_id(), new RequestCallback<BaseResultEntity>() {
            @Override
            public void onSuccess(BaseResultEntity baseResultEntity) {
                ToastUtil.showShort(context, baseResultEntity.getMsg());
                // 取消成功刷新列表
                onRefresh();
            }

            @Override
            public void onError(String msg) {
                ToastUtil.showShort(context, msg);
            }
        });
    }

    @Override
    public void onItemClick(int position) {

        Intent intent = new Intent(context, MerchantOrderDetailActivity.class);
        intent.putExtra(StrConst.extra.merchant_order_id, merchantOrderAdapter.getItem(position).getOrder_id());
        startActivity(intent);
    }

    @Override
    public void onRefresh() {
        page = 1;
        merchantOrderAdapter.clear();
        initData(orderType);
    }

    @Override
    public void onLoadMore() {
        page++;
        initData(orderType);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.retry:
            case R.id.empty:
                onRefresh();
                break;
        }
    }
}
