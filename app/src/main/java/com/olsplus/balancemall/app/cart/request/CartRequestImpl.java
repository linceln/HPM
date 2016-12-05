package com.olsplus.balancemall.app.cart.request;


import android.content.Context;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.olsplus.balancemall.app.cart.bean.AddCartRequest;
import com.olsplus.balancemall.app.cart.bean.DeleteCartJsonEntity;
import com.olsplus.balancemall.app.cart.bean.DeleteCartRequest;
import com.olsplus.balancemall.app.cart.bean.ShoppingItemGroup;
import com.olsplus.balancemall.app.cart.bussiness.CartBussiness;
import com.olsplus.balancemall.app.cart.bussiness.OnAddCartCallback;
import com.olsplus.balancemall.app.cart.bussiness.OnDeleteCartCallback;
import com.olsplus.balancemall.app.cart.bussiness.OnGetCartDataCallback;
import com.olsplus.balancemall.app.cart.view.IAddCartView;
import com.olsplus.balancemall.app.cart.view.ICartView;

import java.util.List;

public class CartRequestImpl implements ICartRequest{

    private Context context;

    private CartBussiness cartBussiness;

    private IAddCartView iAddCartView;

    private ICartView iCartView;

    public CartRequestImpl(Context context) {
        this.context = context;
        cartBussiness = new CartBussiness(context);
    }


    public void setiAddCartView(IAddCartView showCartView) {
        this.iAddCartView = showCartView;
    }

    public void setiCartView(ICartView iCartView) {
        this.iCartView = iCartView;
    }

    @Override
    public void addCart(String addCartJson) {
        if(TextUtils.isEmpty(addCartJson)){
            return;
        }
        String jsonStr = addCartJson.substring(10,addCartJson.length());
        Gson gson=new Gson();
        AddCartRequest addCartRequest = gson.fromJson(jsonStr, AddCartRequest.class);
        if(addCartRequest!=null){
            if(cartBussiness!=null){
                String local_service_id = addCartRequest.getLocal_service_id();
                String product_id = addCartRequest.getProduct_id();
                String count = String.valueOf(addCartRequest.getCount());
                String price = String.valueOf(addCartRequest.getPrice());
                String sku_id = addCartRequest.getSku_id();
                String sku_value = addCartRequest.getSku_value();
                String schedule_time = addCartRequest.getSchedule_time();
                cartBussiness.addCart(local_service_id,product_id,count,price,sku_id,sku_value,schedule_time, new OnAddCartCallback() {
                    @Override
                    public void addCartSuccess() {
                        if(iAddCartView!= null){
                            iAddCartView.showAddCartSuccessView();
                        }
                    }

                    @Override
                    public void addCartFailed(String msg) {
                        if(iAddCartView!= null){
                            iAddCartView.showAddCartErrorView(msg);
                        }
                    }
                });
            }
        }


    }

    @Override
    public void deleteCart(List<DeleteCartJsonEntity> datas) {
        DeleteCartRequest deleteCartRequest = new DeleteCartRequest();
        deleteCartRequest.setCart(datas);
        Gson gson=new Gson();
        String result= gson.toJson(deleteCartRequest);
        if(TextUtils.isEmpty(result)){
            if(iCartView!=null){
                iCartView.showDeleteCartErrorView("数据出错了");
            }
            return;
        }
        cartBussiness.deleteCart(result, new OnDeleteCartCallback() {
            @Override
            public void deleteCartSuccess() {
                if(iCartView!=null){
                    iCartView.showDeleteCartSuccessView();
                }
            }

            @Override
            public void deleteCartFailed(String msg) {
                if(iCartView!=null){
                    iCartView.showDeleteCartErrorView(msg);
                }
            }
        });
    }

    @Override
    public void getCartListData(int page, int count,final boolean isRefresh) {
        String pageStr = String.valueOf(page);
        String countStr = String.valueOf(count);
        cartBussiness.getCartListData(pageStr, countStr, new OnGetCartDataCallback() {

            @Override
            public void getCartSuccess(List<ShoppingItemGroup> datas) {
                if(iCartView!= null){
                    if (isRefresh) {
                        iCartView.showGetCartDataView(datas);
                    } else {
                        iCartView.load(datas);
                    }
                }
            }

            @Override
            public void getCartFailed(String msg) {
                if(iCartView!= null){
                    iCartView.showCartDataErrorView(msg);
                }
            }
        });
    }
}
