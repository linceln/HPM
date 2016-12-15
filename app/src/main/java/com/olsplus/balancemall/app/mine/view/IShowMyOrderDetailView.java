package com.olsplus.balancemall.app.mine.view;


import com.olsplus.balancemall.app.mine.bean.MyOrderDetail;

public interface IShowMyOrderDetailView {

    void showOrderDetailFailedView(String msg);

    void showOrderDetail(MyOrderDetail data);
}
