package com.olsplus.balancemall.app.mystore;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jude.easyrecyclerview.EasyRecyclerView;
import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.olsplus.balancemall.R;
import com.olsplus.balancemall.app.mystore.adapter.MyOrderAdapter;
import com.olsplus.balancemall.app.mystore.bean.MyOrderItem;
import com.olsplus.balancemall.app.mystore.request.MyOrderImpl;
import com.olsplus.balancemall.app.mystore.view.ICancelOrderView;
import com.olsplus.balancemall.app.mystore.view.IDeleteOrderView;
import com.olsplus.balancemall.app.mystore.view.IShowMyOrderListView;
import com.olsplus.balancemall.app.pay.CheckoutMainActivity;
import com.olsplus.balancemall.core.app.BaseFragment;
import com.olsplus.balancemall.core.util.LogUtil;
import com.olsplus.balancemall.core.util.ToastUtil;

import java.util.List;


public class MyOrderFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener, IShowMyOrderListView, RecyclerArrayAdapter.OnLoadMoreListener, MyOrderAdapter.OnOrderItemClickListener, MyOrderAdapter.OnOrderOpeartionListener, ICancelOrderView, IDeleteOrderView {

    private LinearLayout emptyLinear;
    private TextView emptyTv;
    private EasyRecyclerView mOrderListView;
    private MyOrderAdapter myOrderAdapter;
    private MyOrderImpl myOrderImpl;

    private String orderType;
    private int pageNo = 1;// 第几页
    private int totalPage = -1;// 总页数
    private int pageSize = 4;// 每页多少条数据

    public static MyOrderFragment getInstance(String orderStatus) {
        MyOrderFragment myOrderFragment = new MyOrderFragment();
        Bundle bundle = new Bundle();
        bundle.putString("orderStatus", orderStatus);
        myOrderFragment.setArguments(bundle);
        return myOrderFragment;
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        emptyLinear = (LinearLayout) view.findViewById(R.id.myorder_null_linear);
        emptyTv = (TextView) view.findViewById(R.id.myorder_null_tv);
        mOrderListView = (EasyRecyclerView) view.findViewById(R.id.myorder_list_listview);
        mOrderListView.setLayoutManager(new LinearLayoutManager(mActivity));
//        DividerDecoration itemDecoration = new DividerDecoration(getResources().getColor(R.color.divider_line_color), DensityUtil.dp2px(mActivity, 10f),0, 0);
//        mOrderListView.addItemDecoration(itemDecoration);

        myOrderAdapter = new MyOrderAdapter(mActivity);
        myOrderAdapter.setMore(R.layout.load_more_layout, this);
//        myOrderAdapter.setNoMore(R.layout.no_more_layout);
        myOrderAdapter.setError(R.layout.error_layout);
        myOrderAdapter.setOnOrderItemClickListener(this);
        myOrderAdapter.setOnOrderOpeartionListener(this);
        mOrderListView.setAdapterWithProgress(myOrderAdapter);
        mOrderListView.setRefreshListener(this);
        Bundle bundle = getArguments();
        if (bundle != null) {
            orderType = bundle.getString("orderStatus");
            myOrderImpl = new MyOrderImpl(mActivity);
            myOrderImpl.setiShowMyOrderListView(this);
            myOrderImpl.setiDeleteOrderView(this);
            myOrderImpl.setiCancelOrderView(this);
            myOrderImpl.getOrderList(orderType, pageNo, pageSize, true);
        }

    }


    @Override
    protected int getLayoutId() {
        return R.layout.fragement_order_list;
    }

    @Override
    public void onRefresh() {
        pageNo = 1;
        myOrderImpl.getOrderList(orderType, pageNo, pageSize, true);
    }

    @Override
    public void showOrderListFailedView(String msg) {
        mOrderListView.showError();
        ToastUtil.showShort(mActivity, msg);
    }

    @Override
    public void showOrderList(List<MyOrderItem> data) {
        myOrderAdapter.clear();
        myOrderAdapter.addAll(data);
    }

    @Override
    public void load(List<MyOrderItem> data) {
        myOrderAdapter.addAll(data);
    }

    @Override
    public void onLoadMore() {
        LogUtil.d("yongyuan.w", "onloadmore");
        pageNo++;
        myOrderImpl.getOrderList(orderType, pageNo, pageSize, false);
    }

    @Override
    public void onItemClick(int position, BaseViewHolder holder) {
        MyOrderItem myOrderItem = myOrderAdapter.getItem(position);
        if (myOrderItem != null) {
            Intent intent = new Intent(mActivity, OrderDetailActivity.class);
            intent.putExtra("order_id", myOrderItem.getOrder_id());
            startActivityForResult(intent,100);
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == mActivity.RESULT_OK){
            onRefresh();
        }
    }

    @Override
    public void pay(int position) {
        MyOrderItem myOrderItem = myOrderAdapter.getItem(position);
        if (myOrderItem != null) {
            Intent intent = new Intent(mActivity, CheckoutMainActivity.class);
            intent.putExtra("order_id", myOrderItem.getOrder_id());
            intent.putExtra("total_fee", myOrderItem.getTotal());
            intent.putExtra("from", "2");
            mActivity.startActivity(intent);
        }

//        MyOrderItem myOrderItem = myOrderAdapter.getItem(position);
//        if(myOrderItem!=null){
//            ArrayList<ShoppingCartEntity> services = new ArrayList<ShoppingCartEntity>();
//            List<MyOrderInfo> suborders = myOrderItem.getSuborders();
//            int count = suborders.size();
//            ShoppingCartEntity shoppingCartEntity = new ShoppingCartEntity();
//            shoppingCartEntity.setLocal_service_id(myOrderItem.getOrder_id());
//            List<ShoppingProductEntity> products = new ArrayList<ShoppingProductEntity>();
//            for(int i = 0;i<count;i++){
//                MyOrderInfo myOrderInfo = suborders.get(i);
//                if(myOrderInfo!=null){
//                    ShoppingProductEntity shoppingProductEntity = new ShoppingProductEntity();
//                    shoppingProductEntity.setId(myOrderInfo.getProduct_id());
//                    shoppingProductEntity.setCount(myOrderInfo.getProduct_count());
//                    shoppingProductEntity.setSchedule_time(myOrderInfo.getSchedule_time());
//                    products.add(shoppingProductEntity);
//                }
//            }
//            shoppingCartEntity.setProducts(products);
//            services.add(shoppingCartEntity);
//            Intent intent = new Intent(mActivity, CheckoutMainActivity.class);
//            intent.putExtra("service",services);
//            intent.putExtra("from","1");
//            startActivity(intent);
//        }
    }

    @Override
    public void cancle(int position) {
        MyOrderItem myOrderItem = myOrderAdapter.getItem(position);
        if (myOrderItem != null) {
            cancle(myOrderItem.getOrder_id());
        }
    }

    @Override
    public void delete(int position) {
        MyOrderItem myOrderItem = myOrderAdapter.getItem(position);
        if (myOrderItem != null) {
            delete(myOrderItem.getOrder_id());
        }
    }

    @Override
    public void comment(int position) {
        MyOrderItem myOrderItem = myOrderAdapter.getItem(position);
        if (myOrderItem != null) {
            Intent intent = new Intent(getContext(), CommentUploadActivity.class);
            intent.putExtra("order_id", myOrderItem.getOrder_id());
            intent.putExtra("comment_product_list", myOrderItem.getSuborders());
            startActivityForResult(intent, 100);
        }
    }

    /**
     * 取消订单
     */
    public void cancle(final String orderId) {
        DialogInterface.OnClickListener negativeListener = new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        };
        DialogInterface.OnClickListener positiveListener = new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                myOrderImpl.cancelOrder(orderId);
            }
        };
        mDialogHelper.showCustomDialog("取消订单", "您是否要取消当前订单？", "是", positiveListener, "否", negativeListener);
    }

    /**
     * 删除订单
     */
    public void delete(final String orderId) {

        DialogInterface.OnClickListener negativeListener = new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        };

        DialogInterface.OnClickListener positiveListener = new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                myOrderImpl.deleteOrder(orderId);
            }

        };
        mDialogHelper.showCustomDialog("删除订单", "您是否要删除当前订单？", "是", positiveListener, "否", negativeListener);
    }

    @Override
    public void showCancelOrderFailedView(String msg) {
        ToastUtil.showShort(mActivity, "取消订单失败:"+msg);
    }

    @Override
    public void showCancelOrderView() {
        myOrderImpl.getOrderList(orderType, pageNo, pageSize, true);
    }

    @Override
    public void showDeleteOrderFailedView(String msg) {
        ToastUtil.showShort(mActivity, "删除订单失败");
    }

    @Override
    public void showDeleteOrderView() {
        myOrderImpl.getOrderList(orderType, pageNo, pageSize, true);
    }
}
