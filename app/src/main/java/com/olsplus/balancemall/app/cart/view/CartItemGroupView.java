package com.olsplus.balancemall.app.cart.view;


import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.olsplus.balancemall.R;
import com.olsplus.balancemall.app.cart.bean.ShoppingCartItem;
import com.olsplus.balancemall.app.cart.bean.ShoppingItemGroup;
import com.olsplus.balancemall.app.cart.bussiness.OnCartListener;

import java.util.List;

public class CartItemGroupView  extends LinearLayout {

    private LinearLayout itemLinear;

    private TextView merchantnameTv;

    private boolean isEdit;

    private OnCartListener mOnCartListener;

    public CartItemGroupView(Context context) {
        this(context, null);
    }

    public CartItemGroupView(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.cart_itemgroup_view, this, true);
        if (isInEditMode()) {
            return;
        }
        itemLinear = ((LinearLayout)findViewById(R.id.layout_items));
        merchantnameTv = (TextView)findViewById(R.id.tv_merchantname);
    }

    public void setOnCartListener(OnCartListener mOnCartListener){
        this.mOnCartListener = mOnCartListener;
    }

    public void  addShoppingItemGroup(ShoppingItemGroup shoppingItemGroup, boolean isEdit){
        this.isEdit = isEdit;
        if(shoppingItemGroup == null){
            return;
        }
        merchantnameTv.setText(shoppingItemGroup.getLocal_service_name());
        List<ShoppingCartItem> shoppingCartItems = shoppingItemGroup.getProducts();
        if(shoppingCartItems == null || shoppingCartItems.isEmpty()){
            return;
        }
        itemLinear.removeAllViews();
        int count = shoppingCartItems.size();
        for(int i = 0;i<count;i++){
            ShoppingCartItem shoppingCartItem = shoppingCartItems.get(i);
            CartItemChildView cartItemChildView = new CartItemChildView(getContext());
            cartItemChildView.setOnCartListener(mOnCartListener);
            cartItemChildView.initView(shoppingCartItem);
            cartItemChildView.setEditType(isEdit);
            itemLinear.addView(cartItemChildView);
        }

    }

    /**
     * 全选或者店铺勾选界面响应
     * @param isSelected
     */
   public void refreshGroupSelected(boolean isSelected){
      int count =  itemLinear.getChildCount();
      for(int i = 0; i<count;i++){
          CartItemChildView childView = (CartItemChildView)itemLinear.getChildAt(i);
          childView.setMearchanSelected(isSelected);
      }
   }

    /**
     * 在完成状态下，获取店铺底下的商品选择状态
     */
    public boolean  getCartGroupCheckStatus(){
        boolean isAllCheck = true;
        int count = itemLinear.getChildCount();
        for(int i = 0; i<count;i++){
            CartItemChildView childView = (CartItemChildView)itemLinear.getChildAt(i);
            boolean isChecked = childView.getCartChildIsSelected();
            if(!isChecked){
                isAllCheck = false;
            }
        }
        return isAllCheck;
    }

    /**
     * 在编辑状态下，获取店铺底下的商品选择状态
     */
    public boolean  getEditCartGroupCheckStatus(){
        boolean isAllCheck = true;
        int count =  itemLinear.getChildCount();
        for(int i = 0; i<count;i++){
            CartItemChildView childView = (CartItemChildView)itemLinear.getChildAt(i);
            boolean isChecked = childView.getEditCartChildIsSelected();
            if(!isChecked){
                isAllCheck = false;
            }
        }
        return isAllCheck;
    }

}
