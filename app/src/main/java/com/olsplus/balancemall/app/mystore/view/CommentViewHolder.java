package com.olsplus.balancemall.app.mystore.view;

import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.olsplus.balancemall.R;
import com.olsplus.balancemall.app.mystore.bean.MyOrderInfo;
import com.olsplus.balancemall.core.util.ApiConst;

public class CommentViewHolder extends BaseViewHolder<MyOrderInfo> {

    private ImageView productIv;
    private TextView productTv;
    private EditText commentEt;

    public CommentViewHolder(ViewGroup parent) {
        super(parent, R.layout.comment_upload_item_group_view);
        productIv = $(R.id.product_img);
        productTv = $(R.id.comment_product_name);
        commentEt = $(R.id.product_comment_edit);
    }

    @Override
    public void setData(MyOrderInfo data) {
        super.setData(data);
        if (data != null) {
            Glide.with(getContext())
                    .load(ApiConst.BASE_IMAGE_URL+data.getThumbnail())
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .into(productIv);
            productTv.setText(data.getTitle());

        }
    }


}
