package com.olsplus.balancemall.app.mystore.view;

import com.olsplus.balancemall.app.mystore.bean.MyOrderItem;

import java.util.List;


public interface IShowMyOrderListView {

    void showOrderListFailedView(String msg);

    void showOrderList(List<MyOrderItem> data);

    void load(List<MyOrderItem> data);
}
