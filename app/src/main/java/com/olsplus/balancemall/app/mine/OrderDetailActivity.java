package com.olsplus.balancemall.app.mine;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.olsplus.balancemall.R;
import com.olsplus.balancemall.app.mine.bean.MyOrderDetail;
import com.olsplus.balancemall.app.mine.bean.MyOrderInfo;
import com.olsplus.balancemall.app.mine.request.MyOrderImpl;
import com.olsplus.balancemall.app.mine.util.OrderHelper;
import com.olsplus.balancemall.app.mine.view.ICancelOrderView;
import com.olsplus.balancemall.app.mine.view.IDeleteOrderView;
import com.olsplus.balancemall.app.mine.view.IShowMyOrderDetailView;
import com.olsplus.balancemall.app.pay.CheckoutMainActivity;
import com.olsplus.balancemall.component.view.ScrollSwipeRefreshLayout;
import com.olsplus.balancemall.core.app.BaseFragment;
import com.olsplus.balancemall.core.app.MainActivity;
import com.olsplus.balancemall.core.util.ApiConst;
import com.olsplus.balancemall.core.util.ToastUtil;
import com.olsplus.balancemall.core.util.UIUtil;

import java.util.ArrayList;


public class OrderDetailActivity extends MainActivity implements SwipeRefreshLayout.OnRefreshListener, IShowMyOrderDetailView, IDeleteOrderView, ICancelOrderView {

    private ScrollSwipeRefreshLayout scrollRefreshLayout;
    private TextView orderCodeTv;
    private TextView creatTimeTv;
    private TextView preOrderTimeTv;
    private TextView orderName;
    private TextView statusTv;
    private LinearLayout productListLinear;
    private TextView pointTv;
    private TextView orderPriceTv;
    private TextView orderTotalPriceTv;
    private Button returnBtn;
    private TextView deleteBtn;
    private Button cancelBtn;
    private TextView commentBtn;
    private Button popBtn;

    private String orderId;
    private MyOrderImpl myOrderImpl;
    private MyOrderDetail currentOrderDetail;

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
        return R.layout.mystore_orderdetail;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setActionBar();
        mTitleName.setText("订单详情");
        scrollRefreshLayout = (ScrollSwipeRefreshLayout) findViewById(R.id.refresh_layout);
        scrollRefreshLayout.setColorScheme(R.color.black_111111, R.color.black_111111, R.color.black_111111, R.color.black_111111);
        scrollRefreshLayout.setOnRefreshListener(this);
        orderCodeTv = ((TextView) findViewById(R.id.order_code));
        creatTimeTv = ((TextView) findViewById(R.id.order_time));
        orderName = ((TextView) findViewById(R.id.order_provider));
        statusTv = (TextView) findViewById(R.id.order_status_textview);
        pointTv = (TextView) findViewById(R.id.need_point_tv);

        preOrderTimeTv = ((TextView) findViewById(R.id.prepare_order_time));
        productListLinear = ((LinearLayout) findViewById(R.id.order_detail_product_list));
        orderTotalPriceTv = ((TextView) findViewById(R.id.order_price_tv));
        orderPriceTv = ((TextView) findViewById(R.id.need_price_tv));
        returnBtn = (Button) findViewById(R.id.order_to_return_btn);
        deleteBtn = (TextView) findViewById(R.id.order_to_delete_tv);
        cancelBtn = (Button) findViewById(R.id.order_to_cancel_btn);
        commentBtn = (TextView) findViewById(R.id.order_to_comment_tv);
        popBtn = (Button) findViewById(R.id.pay_imm_btn);
        returnBtn.setOnClickListener(this);
        deleteBtn.setOnClickListener(this);
        cancelBtn.setOnClickListener(this);
        commentBtn.setOnClickListener(this);
        popBtn.setOnClickListener(this);

        orderId = getIntent().getStringExtra("order_id");
        myOrderImpl = new MyOrderImpl(this);
        myOrderImpl.setiShowMyOrderDetailView(this);
        myOrderImpl.setiCancelOrderView(this);
        myOrderImpl.setiDeleteOrderView(this);

    }


    @Override
    protected void onResume() {
        super.onResume();
        if(myOrderImpl!=null){
            myOrderImpl.getOrderDetail(orderId);
        }
    }

    /**
     * 添加订单商品
     */
    private void addProductToLinear() {
        productListLinear.removeAllViews();
        ArrayList<MyOrderInfo> suborders = currentOrderDetail.getSuborders();
        if (suborders != null && !suborders.isEmpty()) {
            for (int i = 0; i < suborders.size(); i++) {
                final MyOrderInfo myOrderItem = suborders.get(i);
                if (myOrderItem != null) {
                    LayoutInflater localLayoutInflater = LayoutInflater.from(this);
                    LinearLayout itemLinear = (LinearLayout) localLayoutInflater.inflate(R.layout.mystore_order_product_detail_item, productListLinear, false);
                    LinearLayout productStatusLinear = (LinearLayout) itemLinear.findViewById(R.id.order_product_status_layout);
                    TextView returnBtn = (TextView) itemLinear.findViewById(R.id.order_product_return_btn);
                    TextView returnTv = (TextView) itemLinear.findViewById(R.id.order_product_return_tv);
                    ImageView productIv = (ImageView) itemLinear.findViewById(R.id.order_product_imageview);
                    TextView productTv = (TextView) itemLinear.findViewById(R.id.order_product_name_textview);
                    TextView numTv = (TextView) itemLinear.findViewById(R.id.order_product_num_textview);
                    TextView priceTv = (TextView) itemLinear.findViewById(R.id.order_product_price_textview);
                    TextView subTv = (TextView) itemLinear.findViewById(R.id.order_product_sub_textview);
                    TextView timeTv = (TextView) itemLinear.findViewById(R.id.order_time_textview);
                    Glide.with(this)
                            .load(ApiConst.BASE_IMAGE_URL + myOrderItem.getThumbnail())
                            .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                            .into(productIv);
                    productTv.setText(myOrderItem.getTitle());
                    numTv.setText("x" + myOrderItem.getProduct_count());
                    priceTv.setText(UIUtil.formatLablePrice(myOrderItem.getProduct_price()));
                    if (TextUtils.isEmpty(myOrderItem.getSku_value())) {
                        subTv.setVisibility(View.INVISIBLE);
                    } else {
                        subTv.setVisibility(View.VISIBLE);
                        subTv.setText(myOrderItem.getSku_value());
                    }
                    if(!TextUtils.isEmpty(myOrderItem.getSchedule_time())){
                        timeTv.setText("预约时间:"+myOrderItem.getSchedule_time());
                    }
                    String refund = myOrderItem.getRefund_status();
                    String pay = myOrderItem.getPay();
                    String tradid = currentOrderDetail.getTrade_id();
                    if (!TextUtils.isEmpty(pay) && !TextUtils.isEmpty(tradid)) {
                        productStatusLinear.setVisibility(View.VISIBLE);
                        if (refund.equals(OrderHelper.NO_REFUND)) {
                            returnBtn.setVisibility(View.VISIBLE);
                            returnTv.setVisibility(View.GONE);
                        } else if (refund.equals(OrderHelper.REFUND_FAILED)) {
                            returnBtn.setVisibility(View.VISIBLE);
                            returnTv.setText(OrderHelper.getReturnStatus(refund));
                        } else if (refund.equals(OrderHelper.REFUNDING) || refund.equals(OrderHelper.REFUNDED) || refund.equals(OrderHelper.WAIT_SELLER_CONFIRM)) {
                            returnBtn.setVisibility(View.GONE);
                            returnTv.setVisibility(View.VISIBLE);
                            returnTv.setText(OrderHelper.getReturnStatus(refund));
                        }
                        returnBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(OrderDetailActivity.this, ReturnOrderActivity.class);
                                intent.putExtra("order_id", orderId);
                                intent.putExtra("product_id", myOrderItem.getProduct_id());
                                intent.putExtra("order_amount", myOrderItem.getPay());
                                startActivityForResult(intent,100);
                            }
                        });
                    } else {
                        productStatusLinear.setVisibility(View.GONE);
                    }
                    productListLinear.addView(itemLinear);
                }
            }
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            //刷新订单操作状态
            myOrderImpl.getOrderDetail(orderId);
        }
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.order_to_return_btn:
                intent = new Intent(this, ReturnOrderActivity.class);
                intent.putExtra("order_id", orderId);
                intent.putExtra("product_id", "");
                startActivity(intent);
                break;
            case R.id.order_to_delete_tv:
                delete();
                break;
            case R.id.order_to_cancel_btn:
                cancle();
                break;
            case R.id.order_to_comment_tv:
                intent = new Intent(this,CommentUploadActivity.class);
                intent.putExtra("order_id",currentOrderDetail.getOrder_id());
                intent.putExtra("comment_product_list",currentOrderDetail.getSuborders());
                startActivityForResult(intent, 100);
                break;
            case R.id.pay_imm_btn:
                intent = new Intent(this, CheckoutMainActivity.class);
                intent.putExtra("order_id", orderId);
                intent.putExtra("total_fee", currentOrderDetail.getTotal());
                intent.putExtra("from", "2");
                startActivity(intent);
                break;
        }
    }

    @Override
    public void onRefresh() {
        myOrderImpl.getOrderDetail(orderId);
    }

    @Override
    public void showOrderDetailFailedView(String msg) {
        scrollRefreshLayout.setRefreshing(false);
        ToastUtil.showShort(this, msg);
    }

    @Override
    public void showOrderDetail(MyOrderDetail data) {
        scrollRefreshLayout.setRefreshing(false);
        currentOrderDetail = data;
        orderName.setText(data.getProvider());
        orderCodeTv.setText(data.getOrder_id());
        creatTimeTv.setText(data.getCreated_time());
        preOrderTimeTv.setText(data.getPay_time());
        pointTv.setText("¥"+data.getPointPay());
        orderTotalPriceTv.setText(UIUtil.formatLablePrice(data.getTotal()));
        orderPriceTv.setText(UIUtil.formatLablePrice(data.getPay()));
        addProductToLinear();
        String status = data.getStatus();
        String refund = data.getRefund_status();
        statusTv.setText(OrderHelper.getOrderStatus(refund, status));
        if (refund.equals(OrderHelper.NO_REFUND)) {
            if (status.equals(OrderHelper.WAIT_BUYER_PAY)) {
                popBtn.setVisibility(View.VISIBLE);
                cancelBtn.setVisibility(View.VISIBLE);
//                returnBtn.setVisibility(View.GONE);
                deleteBtn.setVisibility(View.GONE);
                commentBtn.setVisibility(View.GONE);
            } else if (status.equals(OrderHelper.WAIT_FOR_EVALUATE)) {
                popBtn.setVisibility(View.GONE);
                cancelBtn.setVisibility(View.GONE);
//                returnBtn.setVisibility(View.GONE);
                deleteBtn.setVisibility(View.VISIBLE);
                commentBtn.setVisibility(View.VISIBLE);
            } else if (status.equals(OrderHelper.WAIT_FOR_SERVE)) {
                popBtn.setVisibility(View.GONE);
                cancelBtn.setVisibility(View.GONE);
//                returnBtn.setVisibility(View.GONE);
                deleteBtn.setVisibility(View.GONE);
                commentBtn.setVisibility(View.GONE);
            } else {
                popBtn.setVisibility(View.GONE);
                cancelBtn.setVisibility(View.GONE);
//                returnBtn.setVisibility(View.GONE);
                deleteBtn.setVisibility(View.VISIBLE);
                commentBtn.setVisibility(View.GONE);
            }
        } else {
            popBtn.setVisibility(View.GONE);
            cancelBtn.setVisibility(View.GONE);
//            returnBtn.setVisibility(View.GONE);
            deleteBtn.setVisibility(View.VISIBLE);
            commentBtn.setVisibility(View.GONE);
        }
    }

    /**
     * 取消订单
     */
    public void cancle() {
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
    public void delete() {

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
        ToastUtil.showShort(this, "取消订单失败");
    }

    @Override
    public void showCancelOrderView() {
        myOrderImpl.getOrderDetail(orderId);
    }

    @Override
    public void showDeleteOrderFailedView(String msg) {
        ToastUtil.showShort(this, "删除订单失败");
    }

    @Override
    public void showDeleteOrderView() {
//        myOrderImpl.getOrderDetail(orderId);
        setResult(RESULT_OK);
        finish();
    }
}
