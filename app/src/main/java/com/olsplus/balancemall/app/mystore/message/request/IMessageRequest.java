package com.olsplus.balancemall.app.mystore.message.request;


import com.olsplus.balancemall.app.mystore.bean.MessageCenterInfoList;

public interface IMessageRequest {

    interface  OnGetMessageCallback{

        void onGetMessageFailed(String msg);

        void onGetMessageSuccess(MessageCenterInfoList data);
    }

    interface  OnReadMessageCallback{

        void onReadFailed(String msg);

        void onReadSuccess();
    }

    void getMessageList(int page,int count,boolean isRefresh);

    void readMessageRequest(String msgId);
}
