package com.olsplus.balancemall.app.cart.view;


import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.olsplus.balancemall.R;
import com.olsplus.balancemall.app.cart.bean.ShoppingCartItem;
import com.olsplus.balancemall.app.cart.bean.ShoppingItemGroup;
import com.olsplus.balancemall.app.cart.bussiness.OnCartListener;
import com.olsplus.balancemall.core.util.UIUtil;

import java.util.List;


public class CartBottomView extends LinearLayout implements View.OnClickListener {

    private TextView priceTv;
    private TextView pointCountTv;
    private TextView plusTv;
    private TextView pointPlusTv;
    private TextView freightTv;
    private Button sumitBtn;
    private Button deleteBtn;
    private Button addFavoriteBtn;
    private RelativeLayout editLayout;
    private LinearLayout noEditLayout;
    private CheckBox checkBox;
    private CheckBox editCheckBox;
    private TextView priceTv1;

    private OnCartListener mOnCartListener;

    public CartBottomView(Context context) {
        super(context);
        init();
    }

    public CartBottomView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.cart_bottom_view, this, true);
        this.priceTv = ((TextView) findViewById(R.id.cart_total_price));
        this.pointCountTv = ((TextView) findViewById(R.id.cart_point_count));
        this.plusTv = ((TextView) findViewById(R.id.cart_plus));
        this.pointPlusTv = ((TextView) findViewById(R.id.cart_plus2));
        this.freightTv = ((TextView) findViewById(R.id.cart_total_freight_tv));
        this.sumitBtn = ((Button) findViewById(R.id.cart_submit_btn));
        this.editLayout = ((RelativeLayout) findViewById(R.id.bottom_edit_mode));
        this.noEditLayout = ((LinearLayout) findViewById(R.id.bottom_not_edit_mode));
        this.deleteBtn = ((Button) findViewById(R.id.cart_delete_btn));
        this.addFavoriteBtn = ((Button) findViewById(R.id.cart_add_favorite_btn));
        this.checkBox = ((CheckBox) findViewById(R.id.all_checkbox));
        this.editCheckBox = ((CheckBox) findViewById(R.id.all_edit_checkbox));
        this.priceTv1 = ((TextView) findViewById(R.id.cart_total_price_1));
        this.checkBox.setOnClickListener(this);
        this.editCheckBox.setOnClickListener(this);
        this.sumitBtn.setOnClickListener(this);
        this.deleteBtn.setOnClickListener(this);
    }


    public void setOnCartListenerr(OnCartListener mOnCartListener) {
        this.mOnCartListener = mOnCartListener;
    }


    /**
     * 填充底部结算view
     */
    public void fillBottomView(List<ShoppingItemGroup> shoppingItemGroups) {
        if(shoppingItemGroups == null){
            return;
        }
        double sum = 0;
        for(int i = 0;i<shoppingItemGroups.size();i++){
            ShoppingItemGroup shoppingItemGroup = shoppingItemGroups.get(i);
            if(shoppingItemGroup!=null){
                List<ShoppingCartItem> shoppingCartItems = shoppingItemGroup.getProducts();
                if(shoppingCartItems!=null){
                    int count = shoppingCartItems.size();
                    for(int j = 0;j<count;j++){
                        ShoppingCartItem shoppingCartItem = shoppingCartItems.get(j);
                        if(shoppingCartItem!=null){
                            double current = shoppingCartItem.getPrice()*shoppingCartItem.getCount();
                            sum = sum+current;
                        }

                    }
                }
            }
        }
        this.priceTv1.setText(UIUtil.formatLablePrice(sum));
    }

    /**
     * 购物车结算界面，底部全选选择状态更新
     *
     * @param isCheck
     */
    public void setAllCartCheck(boolean isCheck) {
        checkBox.setChecked(isCheck);
    }

    /**
     * 刷新底部总价格
     */
    public void refreshTotalPrice(List<ShoppingItemGroup> shoppingItemGroups) {
        double totalPrice = 0;
        for(int i = 0;i<shoppingItemGroups.size();i++){
            ShoppingItemGroup shoppingItemGroup = shoppingItemGroups.get(i);
            if(shoppingItemGroup!=null){
                List<ShoppingCartItem> shoppingCartItems = shoppingItemGroup.getProducts();
                if(shoppingCartItems!=null){
                    int count = shoppingCartItems.size();
                    for(int j = 0;j<count;j++){
                        ShoppingCartItem shoppingCartItem = shoppingCartItems.get(j);
                        if(shoppingCartItem!=null){
                            double current = shoppingCartItem.getPrice()*shoppingCartItem.getCount();
                            if(shoppingCartItem.isChecked()) {
                                totalPrice = totalPrice + current;
                            }
                        }

                    }
                }
            }
        }
        this.priceTv1.setText(UIUtil.formatLablePrice(totalPrice));
    }

    /**
     * 购物车删除界面，底部全选选择状态更新
     *
     * @param isCheck
     */
    public void setEditCartCheck(boolean isCheck) {
        editCheckBox.setChecked(isCheck);
    }

    public void notifyViewByStatus(boolean isEdit) {
        if (isEdit) {
            this.editLayout.setVisibility(VISIBLE);
            this.noEditLayout.setVisibility(GONE);

        } else {
            this.editLayout.setVisibility(GONE);
            this.noEditLayout.setVisibility(VISIBLE);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.cart_submit_btn:
                if (mOnCartListener != null) {
                    mOnCartListener.onCartBalance();
                }
                break;
            case R.id.cart_delete_btn:
                if (mOnCartListener != null) {
                    mOnCartListener.onCartDelete();
                }
                break;
            case R.id.all_checkbox:
                if (mOnCartListener != null) {
                    mOnCartListener.onBottomCheckChanged(checkBox.isChecked());
                }
                break;
            case R.id.all_edit_checkbox:
                if (mOnCartListener != null) {
                    mOnCartListener.onBottomEditCheckChanged(editCheckBox.isChecked());
                }
                break;

        }
    }
}
