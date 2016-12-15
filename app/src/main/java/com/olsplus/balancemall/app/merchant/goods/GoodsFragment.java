package com.olsplus.balancemall.app.merchant.goods;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.jude.easyrecyclerview.EasyRecyclerView;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.jude.easyrecyclerview.decoration.DividerDecoration;
import com.olsplus.balancemall.R;
import com.olsplus.balancemall.app.merchant.goods.adapter.GoodsAdapter;
import com.olsplus.balancemall.app.merchant.goods.bean.GoodsListEntity;
import com.olsplus.balancemall.app.merchant.goods.business.GoodsBusiness;
import com.olsplus.balancemall.core.app.BaseCompatFragment;
import com.olsplus.balancemall.core.bean.BaseResultEntity;
import com.olsplus.balancemall.core.exception.layout.DefaultExceptionListener;
import com.olsplus.balancemall.core.exception.layout.ExceptionManager;
import com.olsplus.balancemall.core.http.RequestCallback;
import com.olsplus.balancemall.core.util.DensityUtil;
import com.olsplus.balancemall.core.util.StrConst;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;


public class GoodsFragment extends BaseCompatFragment implements SwipeRefreshLayout.OnRefreshListener,
        RecyclerArrayAdapter.OnLoadMoreListener, GoodsAdapter.OnGoodsOperationListener, View.OnClickListener {


    private View view;
    private Context context;
    private GoodsAdapter goodsAdapter;

    private int page = 1;
    private int count = 6;
    private String type;
    private ExceptionManager manager;

    public GoodsFragment() {
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
            view = inflater.inflate(R.layout.fragment_goods, container, false);
            context = getContext();
            getExtras();
            initUI();
            initData();
        }
        return view;
    }


    private void getExtras() {
        type = getArguments().getString(StrConst.extra.goods_type);
    }

    private void initUI() {

        EasyRecyclerView recyclerGoods = (EasyRecyclerView) view.findViewById(R.id.recyclerGoods);
        recyclerGoods.setRefreshListener(this);
        RecyclerView recyclerView = recyclerGoods.getRecyclerView();
        recyclerView.setPadding(0, DensityUtil.dp2px(context, 20), 0, 0);
        recyclerView.setClipToPadding(false);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerGoods.setLayoutManager(linearLayoutManager);
        goodsAdapter = new GoodsAdapter(context);
        goodsAdapter.setOnGoodsOperationListener(this);
        goodsAdapter.setMore(R.layout.load_more_layout, this);
        recyclerGoods.setAdapter(goodsAdapter);
        recyclerGoods.addItemDecoration(new DividerDecoration(context.getResources().getColor(android.R.color.transparent), DensityUtil.dp2px(context, 20)));
//        recyclerGoods.setItemAnimator(new DefaultItemAnimator());
        // 空页面
        manager = ExceptionManager.initialize(recyclerGoods, new DefaultExceptionListener(this));
    }

    private void initData() {
        GoodsBusiness.getGoodsList(context, page, count, type, new RequestCallback<BaseResultEntity>() {
            @Override
            public void onSuccess(BaseResultEntity baseResultEntity) {
                GoodsListEntity entity = (GoodsListEntity) baseResultEntity;
                if (entity.getProduct_list() != null) {
                    goodsAdapter.addAll(entity.getProduct_list());
                }

                // 空页面
                if (goodsAdapter.getCount() == 0) {
                    manager.showEmpty();
                } else {
                    manager.hide();
                }
            }

            @Override
            public void onError(String msg) {
                Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
                manager.showRetry();
            }
        });
    }

    /**
     * 行点击事件
     *
     * @param position
     */
    @Override
    public void onItemClick(int position) {
        GoodsListEntity.ProductListBean bean = goodsAdapter.getItem(position);
        Intent intent = new Intent(context, GoodsDetailActivity.class);
        intent.putExtra(StrConst.extra.goods_type, type);
        intent.putExtra(StrConst.extra.goods_id, bean.getId());
        startActivity(intent);
    }

    /**
     * 新增商品成功回调 || 下架或删除
     *
     * @param str
     */
    @Subscribe
    public void onCallback(String str) {
        onRefresh();
    }

    @Override
    public void onRefresh() {
        page = 1;
        goodsAdapter.clear();
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
            case R.id.retry:
            case R.id.empty:
                onRefresh();
                break;
        }
    }
}
