package com.olsplus.balancemall.app.pay;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.olsplus.balancemall.R;
import com.olsplus.balancemall.app.mystore.OrderDetailActivity;
import com.olsplus.balancemall.app.pay.bean.ShopingOrderChildItem;
import com.olsplus.balancemall.app.pay.bean.ShopingPayQueryEntity;
import com.olsplus.balancemall.app.pay.bean.ShoppingOrderItem;
import com.olsplus.balancemall.app.pay.bean.ShoppingSuccessResult;
import com.olsplus.balancemall.app.pay.bean.ShoppingTrade;
import com.olsplus.balancemall.app.pay.request.QueryPaymentRequestImpl;
import com.olsplus.balancemall.app.pay.view.IQueryPaymentView;
import com.olsplus.balancemall.core.app.BaseFragment;
import com.olsplus.balancemall.core.app.MainActivity;
import com.olsplus.balancemall.core.util.ApiConst;
import com.olsplus.balancemall.core.util.ToastUtil;
import com.olsplus.balancemall.core.util.UIUtil;

import java.util.List;


public class PaySuccessActivity extends MainActivity implements IQueryPaymentView {

    private TextView orderNoTv;
    private TextView orderAmountTv;
    private TextView pointTv;
    private TextView voucherTv;
    private LinearLayout voucherLayout;
    private TextView realPayTv;
    private TextView orderStatusTv;
    private LinearLayout orderLayout;

    private double pointRule;
    private ShopingPayQueryEntity shopingPayQueryEntity;
    private QueryPaymentRequestImpl queryPaymentRequest;

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
        return R.layout.pay_success_activity;
    }

    @Override
    protected void handleIntent(Intent intent) {
        super.handleIntent(intent);
        shopingPayQueryEntity = (ShopingPayQueryEntity) intent.getSerializableExtra("query_parmas");
        pointRule = intent.getDoubleExtra("point_rule",0);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setActionBar();
        mTitleName.setText("支付成功");
        mLeftOperationImageView.setBackgroundResource(R.drawable.back_normal);
        initView();
    }

    private void initView() {
        orderNoTv = (TextView) findViewById(R.id.order_no_tv);
        orderAmountTv = (TextView) findViewById(R.id.order_amount_tv);
        voucherTv= (TextView) findViewById(R.id.order_voucher_tv);
        voucherLayout= (LinearLayout) findViewById(R.id.order_voucher);
        pointTv = (TextView) findViewById(R.id.order_point_tv);
        realPayTv = (TextView) findViewById(R.id.order_pay_price_tv);
        orderStatusTv = (TextView) findViewById(R.id.order_status_tv);
        orderLayout = (LinearLayout) findViewById(R.id.order_detail_layout);
        initData();
    }

    private void initData() {
        queryPaymentRequest = new QueryPaymentRequestImpl(this);
        queryPaymentRequest.setiQueryPaymentView(this);
        queryPaymentRequest.queryPayment(shopingPayQueryEntity);
    }


    @Override
    public void onClick(View v) {

    }

    @Override
    public void showQueryPaymentFailed(String msg) {
        ToastUtil.showShort(this,msg);
    }

    @Override
    public void showQueryPaymentSuccess(final ShoppingSuccessResult shoppingSuccessResult) {
        ShoppingTrade shoppingTrade = shoppingSuccessResult.getTrade();
        orderNoTv.setText(shoppingTrade.getTid());
        orderAmountTv.setText(UIUtil.formatLablePrice(shoppingTrade.getTotal()));
        String pointUsed = shoppingTrade.getPointsUsed();
        if(!TextUtils.isEmpty(pointUsed)){
            int point = Integer.parseInt(pointUsed);
            pointTv.setText(UIUtil.formatLablePrice(point*pointRule));
        }
        String voucher = shoppingTrade.getVoucherUsed();
        if(!TextUtils.isEmpty(voucher)){
            voucherLayout.setVisibility(View.VISIBLE);
            double voucherDouble = Double.parseDouble(voucher);
            voucherTv.setText(UIUtil.formatLablePrice(voucherDouble));
        }else {
            voucherLayout.setVisibility(View.GONE);
        }
        double pay = 0;
        try {
            pay = Double.parseDouble(shoppingTrade.getPay());
        } catch (Throwable e) {
            e.printStackTrace();
        }

        realPayTv.setText(UIUtil.formatLablePrice(pay));
        LayoutInflater localLayoutInflater = LayoutInflater.from(this);
        List<ShoppingOrderItem> orders = shoppingTrade.getOrders();
        if (orders != null) {
            orderLayout.removeAllViews();
            int count = orders.size();
            for (int i = 0; i < count; i++) {
                final ShoppingOrderItem shoppingOrderItem = orders.get(i);
                if (shoppingOrderItem != null) {
                    LinearLayout orderLinear = (LinearLayout) localLayoutInflater.inflate(R.layout.pay_order_product_detail, orderLayout, false);
                    TextView providerTv = ((TextView) orderLinear.findViewById(R.id.product_provider));
                    LinearLayout productLayout = (LinearLayout)orderLinear .findViewById(R.id.pay_product_layout);
                    View drive = orderLinear .findViewById(R.id.pay_drive);
                    providerTv.setText(shoppingOrderItem.getProvider());
                    List<ShopingOrderChildItem> suborders = shoppingOrderItem.getSuborders();
                    if (suborders != null) {
                        int subCount = suborders.size();
                        for (int j = 0; j < subCount; j++) {
                            ShopingOrderChildItem shopingOrderChildItem = suborders.get(j);
                            LinearLayout itemLinear = (LinearLayout) localLayoutInflater.inflate(R.layout.pay_order_product_detail_item, productLayout, false);

                            TextView priceTv = ((TextView) itemLinear.findViewById(R.id.tv_price));
                            TextView countTv = (TextView) itemLinear.findViewById(R.id.tv_count);
                            TextView nameTv = ((TextView) itemLinear.findViewById(R.id.tv_name));
                            ImageView productIv = (ImageView) itemLinear.findViewById(R.id.iv_pic);
                            priceTv.setText(UIUtil.formatLablePrice(shopingOrderChildItem.getProduct_price()));
                            countTv.setText("x"+shopingOrderChildItem.getProduct_count());
                            nameTv.setText(shopingOrderChildItem.getTitle());
                            Glide.with(this)
                                    .load(ApiConst.BASE_IMAGE_URL+shopingOrderChildItem.getThumbnail())
                                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                                    .into(productIv);
                            itemLinear.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(PaySuccessActivity.this, OrderDetailActivity.class);
                                    intent.putExtra("order_id", shoppingOrderItem.getOrder_id());
                                    startActivity(intent);
                                    finish();
                                }
                            });
                            productLayout.addView(itemLinear);

                        }
                        if(i == count-1){
                            drive.setVisibility(View.GONE);
                        }
                        orderLayout.addView(orderLinear);
                    }

                }
            }
        }


    }
}
