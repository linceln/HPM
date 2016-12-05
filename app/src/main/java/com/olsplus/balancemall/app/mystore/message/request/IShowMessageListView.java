package com.olsplus.balancemall.app.mystore.message.request;


import com.olsplus.balancemall.app.mystore.bean.MessageCenterInfo;

import java.util.List;

public interface IShowMessageListView {

    void showGetFailedView(String msg);

    void showGetSuccessView(List<MessageCenterInfo> datas);

    void load(List<MessageCenterInfo> data);

    void showReadFailedView(String msg);

    void showReadSuccessView();
}
