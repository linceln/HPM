package com.olsplus.balancemall.app.mystore.view;


import com.olsplus.balancemall.app.mystore.bean.MyOrderDetail;

public interface IShowMyOrderDetailView {

    void showOrderDetailFailedView(String msg);

    void showOrderDetail(MyOrderDetail data);
}
