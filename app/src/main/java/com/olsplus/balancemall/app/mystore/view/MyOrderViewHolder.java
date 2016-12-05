package com.olsplus.balancemall.app.mystore.view;


import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.olsplus.balancemall.R;
import com.olsplus.balancemall.app.mystore.ReturnOrderActivity;
import com.olsplus.balancemall.app.mystore.bean.MyOrderInfo;
import com.olsplus.balancemall.app.mystore.bean.MyOrderItem;
import com.olsplus.balancemall.app.mystore.util.OrderHelper;
import com.olsplus.balancemall.core.util.ApiConst;
import com.olsplus.balancemall.core.util.UIUtil;

import java.util.List;

public class MyOrderViewHolder extends BaseViewHolder<MyOrderItem> {

    private TextView noTv;
    private TextView statusTv;
    private LinearLayout productLayout;
    private TextView numTv;
    private TextView totalPriceTv;
    private Button returnBtn;
    private Button deleteBtn;
    private Button cancelBtn;
    private Button commentBtn;
    private Button popBtn;

    public MyOrderViewHolder(ViewGroup parent) {
        super(parent, R.layout.mystore_myorder_list_item);
        noTv = $(R.id.order_no_tv);
        statusTv = $(R.id.order_status_textview);
        productLayout = $(R.id.products_layout);
        numTv = $(R.id.myorder_products);
        totalPriceTv = $(R.id.order_price_textview);
        returnBtn = $(R.id.order_to_return_btn);
        deleteBtn = $(R.id.order_to_delete_btn);
        cancelBtn = $(R.id.order_to_cancel_btn);
        commentBtn = $(R.id.order_to_comment_tv);
        popBtn = $(R.id.pay_imm_pop_btn);
    }

    @Override
    public void setData(final MyOrderItem data) {
        super.setData(data);
        if (data != null) {
            noTv.setText(data.getProvider());
            String status = data.getStatus();
            String refund = data.getRefund_status();
            statusTv.setText(OrderHelper.getOrderStatus(refund, status));
            String priceStr = "合计:" + UIUtil.formatLablePrice(data.getTotal());
            UIUtil.addColorSpan(totalPriceTv, priceStr, 3, priceStr.length(), "#ffff3c25");
            int allNum = 0;
            for (MyOrderInfo myOrderInfo : data.getSuborders()) {
                allNum = allNum + myOrderInfo.getProduct_count();
            }
            numTv.setText("共" + allNum + "件商品");

            if (refund.equals(OrderHelper.NO_REFUND)) {
                if (status.equals(OrderHelper.WAIT_BUYER_PAY)) {
                    popBtn.setVisibility(View.VISIBLE);
                    cancelBtn.setVisibility(View.VISIBLE);
                    returnBtn.setVisibility(View.GONE);
                    deleteBtn.setVisibility(View.GONE);
                    commentBtn.setVisibility(View.GONE);
                } else if (status.equals(OrderHelper.WAIT_FOR_EVALUATE)) {
                    popBtn.setVisibility(View.GONE);
                    cancelBtn.setVisibility(View.GONE);
                    returnBtn.setVisibility(View.GONE);
                    deleteBtn.setVisibility(View.VISIBLE);
                    commentBtn.setVisibility(View.VISIBLE);
                } else if (status.equals(OrderHelper.WAIT_FOR_SERVE)) {
                    popBtn.setVisibility(View.GONE);
                    cancelBtn.setVisibility(View.GONE);
                    returnBtn.setVisibility(View.GONE);
                    deleteBtn.setVisibility(View.GONE);
                    commentBtn.setVisibility(View.GONE);
                } else {
                    popBtn.setVisibility(View.GONE);
                    cancelBtn.setVisibility(View.GONE);
                    returnBtn.setVisibility(View.GONE);
                    deleteBtn.setVisibility(View.VISIBLE);
                    commentBtn.setVisibility(View.GONE);
                }
            } else {
                popBtn.setVisibility(View.GONE);
                cancelBtn.setVisibility(View.GONE);
                returnBtn.setVisibility(View.GONE);
                deleteBtn.setVisibility(View.VISIBLE);
                commentBtn.setVisibility(View.GONE);
            }

            returnBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getContext(), ReturnOrderActivity.class);
                    intent.putExtra("order_id", data.getOrder_id());
                    intent.putExtra("product_id", "");
                    getContext().startActivity(intent);
                }
            });
//            commentBtn.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Intent intent = new Intent(getContext(),CommentUploadActivity.class);
//                    intent.putExtra("order_id",data.getOrder_id());
//                    intent.putExtra("comment_product_list",data.getSuborders());
//                    getContext().startActivity(intent);
//                }
//            });
        }

        productLayout.removeAllViews();
        List<MyOrderInfo> suborders = data.getSuborders();
        if (suborders != null && !suborders.isEmpty()) {
            for (int i = 0; i < suborders.size(); i++) {
                MyOrderInfo myOrderItem = suborders.get(i);
                if (myOrderItem != null) {
                    LinearLayout localLinearLayout = (LinearLayout) LayoutInflater.from(getContext()).inflate(R.layout.mystore_order_product_detail_item, productLayout, false);
                    ImageView productIv = (ImageView) localLinearLayout.findViewById(R.id.order_product_imageview);
                    TextView productTv = (TextView) localLinearLayout.findViewById(R.id.order_product_name_textview);
                    TextView numTv = (TextView) localLinearLayout.findViewById(R.id.order_product_num_textview);
                    TextView priceTv = (TextView) localLinearLayout.findViewById(R.id.order_product_price_textview);
                    TextView subTv = (TextView) localLinearLayout.findViewById(R.id.order_product_sub_textview);
                    TextView timeTv = (TextView) localLinearLayout.findViewById(R.id.order_time_textview);
                    Glide.with(getContext())
                            .load(ApiConst.BASE_IMAGE_URL + myOrderItem.getThumbnail())
                            .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                            .into(productIv);
                    productTv.setText(myOrderItem.getTitle());
                    numTv.setText("x" + myOrderItem.getProduct_count());
                    priceTv.setText(UIUtil.formatLablePrice(myOrderItem.getProduct_price()));
                    if (!TextUtils.isEmpty(myOrderItem.getSku_value())) {
                        subTv.setText(myOrderItem.getSku_value());
                    }
                    if (!TextUtils.isEmpty(myOrderItem.getSchedule_time())) {
                        timeTv.setText("预约时间:" + myOrderItem.getSchedule_time());
                    }
                    productLayout.addView(localLinearLayout);
                }
            }
        }

    }

}
