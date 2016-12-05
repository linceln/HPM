package com.olsplus.balancemall.app.merchant.earning;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.jude.easyrecyclerview.EasyRecyclerView;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.jude.easyrecyclerview.decoration.DividerDecoration;
import com.olsplus.balancemall.R;
import com.olsplus.balancemall.app.merchant.earning.adapter.EarningAdapter;
import com.olsplus.balancemall.app.merchant.earning.bean.EarningListEntity;
import com.olsplus.balancemall.app.merchant.order.Business.MerchantBusiness;
import com.olsplus.balancemall.core.app.BaseCompatActivity;
import com.olsplus.balancemall.core.bean.BaseResultEntity;
import com.olsplus.balancemall.core.exception.layout.DefaultExceptionListener;
import com.olsplus.balancemall.core.exception.layout.ExceptionManager;
import com.olsplus.balancemall.core.http.RequestCallback;
import com.olsplus.balancemall.core.util.DensityUtil;
import com.olsplus.balancemall.core.util.StrConst;
import com.olsplus.balancemall.core.util.ToastUtil;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class EarningActivity extends BaseCompatActivity implements SwipeRefreshLayout.OnRefreshListener, RecyclerArrayAdapter.OnLoadMoreListener, View.OnClickListener {


    private EarningAdapter earningAdapter;
    private String earningType;

    private int page = 1;
    private int count = 10;
    private ExceptionManager manager;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_earning;
    }

    @Override
    protected void setupWindowAnimations() {
    }

    @Override
    protected void initUI() {
        String order_today = getIntent().getStringExtra(StrConst.extra.order_today);
        earningType = getIntent().getStringExtra(StrConst.extra.earning_type);
        if (earningType.equals(StrConst.earn.day)) {
            if (order_today == null) {
                setTitle("今日收入");
            } else {
                setTitle("今日订单");
            }
        } else if (earningType.equals(StrConst.earn.month)) {
            setTitle("本月收入");
        }

        EasyRecyclerView recyclerEarning = (EasyRecyclerView) findViewById(R.id.recyclerEarning);
        recyclerEarning.setRefreshListener(this);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerEarning.setLayoutManager(linearLayoutManager);
        earningAdapter = new EarningAdapter(this);
        earningAdapter.setMore(R.layout.load_more_layout, this);
        recyclerEarning.setAdapter(earningAdapter);
        recyclerEarning.addItemDecoration(new DividerDecoration(getResources().getColor(android.R.color.transparent), DensityUtil.dp2px(this, 1)));
        recyclerEarning.setItemAnimator(new DefaultItemAnimator());

        manager = ExceptionManager.initialize(recyclerEarning, new DefaultExceptionListener(this));

    }

    @Override
    protected void initData() {
        MerchantBusiness.getEarning(this, page, count, earningType, new RequestCallback<BaseResultEntity>() {
            @Override
            public void onSuccess(BaseResultEntity baseResultEntity) {
                EarningListEntity entity = (EarningListEntity) baseResultEntity;
                if (entity.getData() != null && !entity.getData().isEmpty()) {

                    // 去重逻辑
                    int position = -1;
                    List<EarningListEntity.DataBean> allData = earningAdapter.getAllData();
                    List<EarningListEntity.DataBean> list = entity.getData();
                    EarningListEntity.DataBean firstData = list.get(0);
                    for (int i = 0; i < allData.size(); i++) {
                        if (allData.get(i).getOrder_id() == firstData.getOrder_id()
                                && allData.get(i).getTime() == firstData.getTime()) {
                            // 新数据第一条订单号和时间戳和上个数组重复
                            position = count - i;
                            break;
                        }
                    }
                    if (position != -1) {
                        // 有重复，去重
                        Iterator<EarningListEntity.DataBean> iterator = list.iterator();
                        while (iterator.hasNext()) {
                            if (position >= 0) {
                                iterator.remove();
                                position--;
                            }
                        }
                        earningAdapter.addAll(list);
                        if (list.size() < 5) {
                            // 重复元素超过五个，自动请求下一页数据
                            onLoadMore();
                        }
                    } else {
                        // 无重复
                        earningAdapter.addAll(list);
                    }
                } else {
                    earningAdapter.addAll(new ArrayList<EarningListEntity.DataBean>());
                }

                if (earningAdapter.getCount() == 0) {
                    // 空页面
                    manager.showEmpty();
                } else {
                    manager.hide();
                }

            }

            @Override
            public void onError(String msg) {
                manager.showRetry();
                ToastUtil.showShort(EarningActivity.this, msg);
            }
        });
    }

    @Override
    public void onRefresh() {
        page = 1;
        earningAdapter.clear();
        initData();
    }

    @Override
    public void onLoadMore() {
        page++;
        initData();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.empty:
            case R.id.retry:
                onRefresh();
                break;
        }
    }
}
