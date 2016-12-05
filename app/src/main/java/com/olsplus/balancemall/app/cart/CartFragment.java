package com.olsplus.balancemall.app.cart;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.olsplus.balancemall.R;
import com.olsplus.balancemall.app.cart.bean.DeleteCartItem;
import com.olsplus.balancemall.app.cart.bean.DeleteCartJsonEntity;
import com.olsplus.balancemall.app.cart.bean.ShoppingBagVo;
import com.olsplus.balancemall.app.cart.bean.ShoppingCartItem;
import com.olsplus.balancemall.app.cart.bean.ShoppingItemGroup;
import com.olsplus.balancemall.app.cart.bussiness.OnCartListener;
import com.olsplus.balancemall.app.cart.request.CartRequestImpl;
import com.olsplus.balancemall.app.cart.view.CartBottomView;
import com.olsplus.balancemall.app.cart.view.CartItemGroupView;
import com.olsplus.balancemall.app.cart.view.ICartView;
import com.olsplus.balancemall.app.pay.CheckoutMainActivity;
import com.olsplus.balancemall.app.pay.bean.ShoppingCartEntity;
import com.olsplus.balancemall.app.pay.bean.ShoppingProductEntity;
import com.olsplus.balancemall.core.app.BaseFragment;
import com.olsplus.balancemall.core.util.ToastUtil;

import java.util.ArrayList;
import java.util.List;


public class CartFragment extends BaseFragment implements OnCartListener,ICartView {

    private RelativeLayout rootView;
    private TextView rightOperationTv;
    private TextView refreshTv;
    private CartBottomView cartBottomView;
    private LinearLayout cartPartView;
    private LinearLayout bagLinear;
    private LinearLayout promotionParentLinear;
    private LinearLayout promotionLinear;

    private String addCartJson;
    private CartRequestImpl cartRequestImpl;
    private List<ShoppingItemGroup> shoppingItemGroups;


    // 当前界面的状态
    private boolean isEdit = false;

    private int pageNo = 1;// 第几页
    private int pageSize = 3;// 每页多少条数据

//    public static CartFragment getInstance(String json) {
//        CartFragment  cartFragment = new CartFragment();
//        Bundle bundle = new Bundle();
//        bundle.putString("add_cart_parmas", json);
//        cartFragment.setArguments(bundle);
//        return cartFragment;
//    }

    public static CartFragment getInstance() {
        CartFragment  cartFragment = new CartFragment();
        return cartFragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.cart_fragment_root;
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        ((TextView) view.findViewById(R.id.actionbar_title_tv)).setText("购物车");
        rightOperationTv = ((TextView) view.findViewById(R.id.right_operation_tv));
        refreshTv = ((TextView) view.findViewById(R.id.tv_refresh));
        cartBottomView = ((CartBottomView) view.findViewById(R.id.cart_bottom_linear));
        cartBottomView.setOnCartListenerr(this);
        ScrollView scrollView = ((ScrollView) view.findViewById(R.id.cart_scroll));
        scrollView.setBackgroundResource(R.color.cart_background_gray);
        scrollView.setFadingEdgeLength(0);
        cartPartView = (LinearLayout) mInflater.inflate(R.layout.cart_parent_view, null);
        bagLinear = ((LinearLayout) cartPartView.findViewById(R.id.layout_bags));
        promotionParentLinear = ((LinearLayout) cartPartView.findViewById(R.id.cart_cross_promotion_parent));
        promotionLinear = ((LinearLayout) cartPartView.findViewById(R.id.cart_cross_promotion_layout));
        scrollView.addView(cartPartView);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
//        Bundle bundle = getArguments();
//        if (bundle != null) {
//            addCartJson = bundle.getString("add_cart_parmas");
//        }

        rightOperationTv.setTextColor(getResources().getColor(R.color.black));
        rightOperationTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isEdit = !isEdit;
                initRightOperationDes();
                addShopItemGroup(shoppingItemGroups);
                refreshCartModeStatus(isEdit);
            }
        });
        initRightOperationDes();

        cartRequestImpl = new CartRequestImpl(mActivity);
        cartRequestImpl.setiCartView(this);
        cartRequestImpl.getCartListData(pageNo,pageSize,true);
    }

    private void initRightOperationDes() {
        if (isEdit) {
            rightOperationTv.setText("完成");
        } else {
            rightOperationTv.setText("编辑");
        }
    }

    /**
     * 添加数据到购物车
     *
     * @param shoppingItemGroupVoList
     */
    private void addShopItemGroup(List<ShoppingItemGroup> shoppingItemGroupVoList){
        if (shoppingItemGroupVoList == null) {
            return;
        }
        bagLinear.removeAllViews();
        int count = shoppingItemGroupVoList.size();
        for(int i = 0;i<count;i++){
            ShoppingItemGroup shoppingItemGroup = shoppingItemGroupVoList.get(i);
            CartItemGroupView cartItemGroupView = new CartItemGroupView(getContext());
            cartItemGroupView.setOnCartListener(this);
            cartItemGroupView.addShoppingItemGroup(shoppingItemGroup,isEdit);
            bagLinear.addView(cartItemGroupView);
        }
    }

    /**
     * 设置编辑或者完成状态下的购物车界面变化
     * @param isEdit
     */
    public void refreshCartModeStatus(boolean isEdit) {
        this.isEdit = isEdit;
        cartBottomView.notifyViewByStatus(isEdit);
    }
    /**
     * 刷新底部结算界面
     */
    private void setBottomTotalPrice() {
        cartBottomView.fillBottomView(shoppingItemGroups);
    }



    @Override
    public void onCartCheckChange( boolean isChecked) {
        int childCout = bagLinear.getChildCount();
        for (int i = 0; i < childCout; i++) {
            if (bagLinear.getChildAt(i) instanceof CartItemGroupView) {
                CartItemGroupView cartItemGroupView = (CartItemGroupView) bagLinear.getChildAt(i);
                cartItemGroupView.refreshGroupSelected(isChecked);
            }
        }
    }


    @Override
    public void onCartDelete() {
        List<DeleteCartJsonEntity> deleteCartJsonEntityList = new ArrayList<DeleteCartJsonEntity>();
        int count = shoppingItemGroups.size();
        for(int i = 0;i<count;i++){
            ShoppingItemGroup shoppingItemGroup = shoppingItemGroups.get(i);
            if(shoppingItemGroup!=null){
                DeleteCartJsonEntity deleteCartJsonEntity = new DeleteCartJsonEntity();
                deleteCartJsonEntity.setLocal_service_id(shoppingItemGroup.getLocal_service_id());
                List<ShoppingCartItem> shoppingCartItems = shoppingItemGroup.getProducts();
                int itemCount = shoppingCartItems.size();
                List<DeleteCartItem> deleteCartItemList = new ArrayList<DeleteCartItem>();
                for(int j = 0;j<itemCount;j++){
                    ShoppingCartItem shoppingCartItem = shoppingCartItems.get(j);
                    if (shoppingCartItem.isEditChecked()) {
                        DeleteCartItem deleteCartItem = new DeleteCartItem();
                        deleteCartItem.setId(shoppingCartItem.getId());
                        if(!TextUtils.isEmpty(shoppingCartItem.getSku_id())){
                            deleteCartItem.setSku_id(shoppingCartItem.getSku_id());
                        }
                        if(!TextUtils.isEmpty(shoppingCartItem.getSku_value())){
                            deleteCartItem.setSku_value(shoppingCartItem.getSku_value());
                        }
                        deleteCartItemList.add(deleteCartItem);
                    }
                }
                if(!deleteCartItemList.isEmpty()){
                    deleteCartJsonEntity.setProducts(deleteCartItemList);
                    deleteCartJsonEntityList.add(deleteCartJsonEntity);
                }

            }
        }
        if(deleteCartJsonEntityList.isEmpty()){
            ToastUtil.showShort(mActivity, "请先选商品");
            return;
        }
        if(cartRequestImpl!=null){
            cartRequestImpl.deleteCart(deleteCartJsonEntityList);
        }
    }

    @Override
    public void onCartBalance() {
        ArrayList<ShoppingCartEntity> services = new ArrayList<ShoppingCartEntity>();
        int count = shoppingItemGroups.size();
        for(int i = 0;i<count;i++){
            ShoppingItemGroup shoppingItemGroup = shoppingItemGroups.get(i);
            if(shoppingItemGroup!=null){
                ShoppingCartEntity shoppingCartEntity = new ShoppingCartEntity();
                shoppingCartEntity.setLocal_service_id(shoppingItemGroup.getLocal_service_id());
                List<ShoppingCartItem> shoppingCartItems = shoppingItemGroup.getProducts();
                int itemCount = shoppingCartItems.size();
                List<ShoppingProductEntity> products = new ArrayList<ShoppingProductEntity>();
                for(int j = 0;j<itemCount;j++){
                    ShoppingCartItem shoppingCartItem = shoppingCartItems.get(j);
                    if (shoppingCartItem.isChecked()) {
                        ShoppingProductEntity shoppingProductEntity = new ShoppingProductEntity();
                        shoppingProductEntity.setId(shoppingCartItem.getId());
//                        if(!TextUtils.isEmpty(shoppingCartItem.getSku_id())){
//                            shoppingProductEntity.setSku_id(shoppingCartItem.getSku_id());
//                        }
//                        shoppingProductEntity.setSchedule_time("2016-10-16 18:30");
                        shoppingProductEntity.setCount(shoppingCartItem.getCount());
                        products.add(shoppingProductEntity);
                    }
                }
                if(!products.isEmpty()){
                    shoppingCartEntity.setProducts(products);
                    services.add(shoppingCartEntity);
                }
            }
        }
        if(services.isEmpty()){
            ToastUtil.showShort(mActivity,"请先选商品");
            return;
        }
        Intent intent = new Intent(mActivity, CheckoutMainActivity.class);
        intent.putExtra("service",services);
        intent.putExtra("from","1");
        startActivity(intent);


    }

    @Override
    public void onCartChildCheckChange(boolean isChecked) {
        //刷新商店是否选择
        boolean isAllChecked = true;
        int childCout = bagLinear.getChildCount();
        for (int i = 0; i < childCout; i++) {
            if (bagLinear.getChildAt(i) instanceof CartItemGroupView) {
                CartItemGroupView cartBagView = (CartItemGroupView) bagLinear.getChildAt(i);
                boolean isCheck = cartBagView.getCartGroupCheckStatus();
                if (!isCheck) {
                    isAllChecked = false;
                }
            }
        }
        //刷新底部是否全选
        this.cartBottomView.setAllCartCheck(isAllChecked);
        this.cartBottomView.refreshTotalPrice(shoppingItemGroups);
    }

    @Override
    public void onCartEditChildCheckChange(boolean isChecked) {
        //刷新商店是否选择
        boolean isAllChecked = true;
        int childCout = bagLinear.getChildCount();
        for (int i = 0; i < childCout; i++) {
            if (bagLinear.getChildAt(i) instanceof CartItemGroupView) {
                CartItemGroupView cartBagView = (CartItemGroupView) bagLinear.getChildAt(i);
                boolean isCheck = cartBagView.getEditCartGroupCheckStatus();
                if (!isCheck) {
                    isAllChecked = false;
                }
            }
        }

        //刷新底部是否全选
        this.cartBottomView.setEditCartCheck(isAllChecked);
    }


    /**
     * 购物车结算状态下，底部全选点击界面刷新
     *
     * @param isCheck
     */
    @Override
    public void onBottomCheckChanged(boolean isCheck) {
        int count = bagLinear.getChildCount();
        for(int i = 0;i<count;i++){
            CartItemGroupView cartItemGroupView = (CartItemGroupView)bagLinear.getChildAt(i);
            cartItemGroupView.refreshGroupSelected(isCheck);
        }
    }

    /**
     * 购物车编辑状态下，底部全选点击界面刷新
     *
     * @param isCheck
     */
    @Override
    public void onBottomEditCheckChanged(boolean isCheck) {
        int count = bagLinear.getChildCount();
        for(int i = 0;i<count;i++){
            CartItemGroupView cartItemGroupView = (CartItemGroupView)bagLinear.getChildAt(i);
            cartItemGroupView.refreshGroupSelected(isCheck);
        }
    }

    @Override
    public String getCartItemSelected(ShoppingBagVo shoppingBagVo) {
        return null;
    }

    @Override
    public void showDeleteCartSuccessView() {
        if(cartRequestImpl!=null) {
            cartRequestImpl.getCartListData(pageNo, pageSize, true);
        }
    }

    @Override
    public void showDeleteCartErrorView(String msg) {
        ToastUtil.showShort(mActivity,"删除失败");
    }

    @Override
    public void showGetCartDataView(List<ShoppingItemGroup> datas) {
        shoppingItemGroups = datas;
        addShopItemGroup(datas);
        rightOperationTv.setVisibility(View.VISIBLE);
        refreshTv.setVisibility(View.GONE);
        cartBottomView.setVisibility(View.VISIBLE);
        refreshCartModeStatus(isEdit);
        setBottomTotalPrice();
    }

    @Override
    public void showCartDataErrorView(String msg) {
        ToastUtil.showShort(mActivity,msg);
    }

    @Override
    public void load(List<ShoppingItemGroup> datas) {

    }
}
