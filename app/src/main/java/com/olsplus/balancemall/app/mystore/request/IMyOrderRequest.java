package com.olsplus.balancemall.app.mystore.request;


import com.olsplus.balancemall.app.mystore.bean.CommentGroup;
import com.olsplus.balancemall.app.mystore.bean.MyOrderDetailResult;
import com.olsplus.balancemall.app.mystore.bean.MyOrderResult;
import com.olsplus.balancemall.app.mystore.bean.MyOrderReturn;

import java.util.List;

public interface IMyOrderRequest {

    interface  GetMyOrderListCallback{

        void onGetOrderListFailed(String msg);

        void onOrderListSuccess(MyOrderResult data);
    }

    interface  GetMyOrderDetailCallback{

        void onGetOrderDetailFailed(String msg);

        void onOrderDetailSuccess(MyOrderDetailResult data);
    }

    interface  OnOrderCancelCallback{

        void onFailed(String msg);

        void onSuccess();
    }

    interface  OnOrderDeleteCallback{

        void onFailed(String msg);

        void onSuccess();
    }

    interface UpoadReturnImgCallback {

        void onUploadFailed(String msg);

        void onUploadSuccess(String img);
    }



    interface  OnReturnOrderCallback{

        void onRequestFailed(String msg);

        void onRequestSuccess();
    }

    interface  OnSumitCommentCallback{

        void onSumitFailed(String msg);

        void onSumitSuccess();
    }


    void getOrderList(String orderType,int page,int count,boolean isRefresh);

    void getOrderDetail(String orderId);

    void cancelOrder(String orderId);

    void deleteOrder(String orderId);

//    void uploadReturnImg(String pic,int position);
    void uploadReturnImg(List<String> paths);

    void returnOrder(MyOrderReturn data);

    void sumitComment(CommentGroup data);
}
