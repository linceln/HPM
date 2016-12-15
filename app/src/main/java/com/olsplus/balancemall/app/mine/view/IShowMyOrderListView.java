package com.olsplus.balancemall.app.mine.view;

import com.olsplus.balancemall.app.mine.bean.MyOrderItem;

import java.util.List;


public interface IShowMyOrderListView {

    void showOrderListFailedView(String msg);

    void showOrderList(List<MyOrderItem> data);

    void load(List<MyOrderItem> data);
}
