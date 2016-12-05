package com.olsplus.balancemall.app.cart.request;


import com.olsplus.balancemall.app.cart.bean.DeleteCartJsonEntity;

import java.util.List;

public interface ICartRequest {

    void addCart(String addCartJson);

    void deleteCart(List<DeleteCartJsonEntity> datas);

    void getCartListData(int page,int count,boolean isRefresh);

}
