package com.olsplus.balancemall.app.cart.view;


import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.olsplus.balancemall.R;
import com.olsplus.balancemall.app.cart.bean.ShoppingCartItem;
import com.olsplus.balancemall.app.cart.bussiness.OnCartListener;
import com.olsplus.balancemall.core.util.ApiConst;
import com.olsplus.balancemall.core.util.UIUtil;


public class CartItemChildView extends RelativeLayout implements View.OnClickListener {

    private View anchorCheck;
    private View anchorPicName;
    private CheckBox checkCb;
    private CheckBox deleteCb;
    private LinearLayout priceLinear;
    private TextView priceTv;
    private TextView nameTv;
    private ImageView picIv;
    private TextView countTv;
    private boolean isEdit;

    //    protected ShoppingCartItemVo currentShopCartItemVo;
    protected ShoppingCartItem currentShopCartItem;

    private OnCartListener mOnCartListener;

    public CartItemChildView(Context context) {
        this(context, null);
    }

    public CartItemChildView(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.cart_item_single_view, this, true);
        anchorCheck = findViewById(R.id.anchor_check);
        anchorPicName = findViewById(R.id.anchor_pic_name);
        checkCb = ((CheckBox) findViewById(R.id.cb_checked));
        deleteCb = ((CheckBox) findViewById(R.id.btn_delete));
        priceLinear = (LinearLayout) findViewById(R.id.layout_price);
        priceTv = ((TextView) findViewById(R.id.tv_price));
        countTv = (TextView) findViewById(R.id.tv_count);
        nameTv = ((TextView) findViewById(R.id.tv_name));
        nameTv.setOnClickListener(this);
        picIv = ((ImageView) findViewById(R.id.iv_pic));
        checkCb.setOnClickListener(this);
        deleteCb.setOnClickListener(this);
    }


    public void setOnCartListener(OnCartListener mOnCartListener) {
        this.mOnCartListener = mOnCartListener;
    }

    public void initView(ShoppingCartItem shoppingCartItem) {
        currentShopCartItem = shoppingCartItem;
        checkCb.setChecked(shoppingCartItem.isChecked());
        nameTv.setText(shoppingCartItem.getTitle());
        priceTv.setText(UIUtil.formatLablePrice(shoppingCartItem.getPrice()));
        countTv.setText("x" + shoppingCartItem.getCount());
        Glide.with(getContext())
                .load(ApiConst.BASE_IMAGE_URL + shoppingCartItem.getImg())
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(picIv);
//        ImageLoaderUtils.displayWebImage(shoppingCartItemVo.getGoods_image_url(),this.picIv,null);
    }


    /**
     * 编辑状态和完成状态下
     *
     * @param isEdit
     */
    public void setEditType(boolean isEdit) {
        this.isEdit = isEdit;
        if (isEdit) {
            checkCb.setVisibility(INVISIBLE);
            deleteCb.setVisibility(VISIBLE);
        } else {
            checkCb.setVisibility(VISIBLE);
            deleteCb.setVisibility(INVISIBLE);
        }
    }

    /**
     * 当整个店铺被选上，则商品附带选择
     *
     * @param isSelected
     */
    public void setMearchanSelected(boolean isSelected) {
        checkCb.setChecked(isSelected);
        if (isEdit) {
            currentShopCartItem.setEditChecked(isSelected);
            deleteCb.setChecked(isSelected);
        } else {
            currentShopCartItem.setChecked(isSelected);
            checkCb.setChecked(isSelected);
        }
    }

    /**
     * 在完成状态下，获取商品是否选择
     */
    public boolean getCartChildIsSelected() {
        return checkCb.isChecked();
    }

    /**
     * 在编辑状态下，获取商品是否选择
     */
    public boolean getEditCartChildIsSelected() {
        return deleteCb.isChecked();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.cb_checked:
                currentShopCartItem.setChecked(checkCb.isChecked());
                if (mOnCartListener != null) {
                    mOnCartListener.onCartChildCheckChange(checkCb.isChecked());
                }

                break;
            case R.id.btn_delete:
                currentShopCartItem.setEditChecked(deleteCb.isChecked());
                if (mOnCartListener != null) {
                    mOnCartListener.onCartEditChildCheckChange(checkCb.isChecked());
                }
                break;
            case R.id.tv_name:
                break;
        }
    }


}
