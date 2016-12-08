package com.olsplus.balancemall.app.mystore.request;


import android.content.Context;

import com.google.gson.Gson;
import com.olsplus.balancemall.app.mystore.bean.CommentGroup;
import com.olsplus.balancemall.app.mystore.bean.MyOrderDetailResult;
import com.olsplus.balancemall.app.mystore.bean.MyOrderResult;
import com.olsplus.balancemall.app.mystore.bean.MyOrderReturn;
import com.olsplus.balancemall.app.mystore.bussiness.MyOrderBussiness;
import com.olsplus.balancemall.app.mystore.view.ICancelOrderView;
import com.olsplus.balancemall.app.mystore.view.ICommentView;
import com.olsplus.balancemall.app.mystore.view.IDeleteOrderView;
import com.olsplus.balancemall.app.mystore.view.IReturnOrderView;
import com.olsplus.balancemall.app.mystore.view.IShowMyOrderDetailView;
import com.olsplus.balancemall.app.mystore.view.IShowMyOrderListView;
import com.olsplus.balancemall.core.util.LogUtil;
import com.olsplus.balancemall.core.util.UploadManager;

import java.util.List;

import rx.Subscriber;

public class MyOrderImpl implements IMyOrderRequest {

    private Context context;
    private MyOrderBussiness myOrderBussiness;
    private IShowMyOrderListView iShowMyOrderListView;
    private IShowMyOrderDetailView iShowMyOrderDetailView;
    private ICancelOrderView iCancelOrderView;
    private IDeleteOrderView iDeleteOrderView;
    private IReturnOrderView iReturnOrderView;
    private ICommentView iCommentView;

    public MyOrderImpl(Context context) {
        this.context = context;
        myOrderBussiness = new MyOrderBussiness(context);
    }

    public void setiShowMyOrderListView(IShowMyOrderListView iShowMyOrderListView) {
        this.iShowMyOrderListView = iShowMyOrderListView;
    }

    public void setiShowMyOrderDetailView(IShowMyOrderDetailView iShowMyOrderDetailView) {
        this.iShowMyOrderDetailView = iShowMyOrderDetailView;
    }

    public void setiCancelOrderView(ICancelOrderView iCancelOrderView) {
        this.iCancelOrderView = iCancelOrderView;
    }

    public void setiDeleteOrderView(IDeleteOrderView iDeleteOrderView) {
        this.iDeleteOrderView = iDeleteOrderView;
    }

    public void setiReturnOrderView(IReturnOrderView iReturnOrderView) {
        this.iReturnOrderView = iReturnOrderView;
    }

    public void setiCommentView(ICommentView iCommentView) {
        this.iCommentView = iCommentView;
    }

    @Override
    public void getOrderList(String orderType, int page, int count, final boolean isRefresh) {
        if (myOrderBussiness != null) {
            String pageStr = String.valueOf(page);
            String countStr = String.valueOf(count);
            myOrderBussiness.getMyOrderListData(orderType, pageStr, countStr, new GetMyOrderListCallback() {
                @Override
                public void onGetOrderListFailed(String msg) {
                    if (iShowMyOrderListView != null) {
                        iShowMyOrderListView.showOrderListFailedView(msg);
                    }
                }

                @Override
                public void onOrderListSuccess(MyOrderResult data) {
                    if (iShowMyOrderListView != null) {
                        if (isRefresh) {
                            iShowMyOrderListView.showOrderList(data.getOrders());
                        } else {
                            iShowMyOrderListView.load(data.getOrders());
                        }

                    }
                }
            });
        }
    }

    @Override
    public void getOrderDetail(String orderId) {
        if (myOrderBussiness != null) {
            myOrderBussiness.getMyOrderDetailData(orderId, new GetMyOrderDetailCallback() {
                @Override
                public void onGetOrderDetailFailed(String msg) {
                    if (iShowMyOrderDetailView != null) {
                        iShowMyOrderDetailView.showOrderDetailFailedView(msg);
                    }
                }

                @Override
                public void onOrderDetailSuccess(MyOrderDetailResult data) {
                    if (iShowMyOrderDetailView != null) {
                        iShowMyOrderDetailView.showOrderDetail(data.getOrder_detail());
                    }
                }
            });
        }
    }

    @Override
    public void cancelOrder(String orderId) {
        if (myOrderBussiness != null) {
            myOrderBussiness.cancelOrder(orderId, new OnOrderCancelCallback() {

                @Override
                public void onFailed(String msg) {
                    if (iCancelOrderView != null) {
                        iCancelOrderView.showCancelOrderFailedView(msg);
                    }
                }

                @Override
                public void onSuccess() {
                    if (iCancelOrderView != null) {
                        iCancelOrderView.showCancelOrderView();
                    }
                }
            });
        }
    }

    @Override
    public void deleteOrder(String orderId) {
        if (myOrderBussiness != null) {
            myOrderBussiness.deleteOrder(orderId, new OnOrderDeleteCallback() {

                @Override
                public void onFailed(String msg) {
                    if (iDeleteOrderView != null) {
                        iDeleteOrderView.showDeleteOrderFailedView(msg);
                    }
                }

                @Override
                public void onSuccess() {
                    if (iDeleteOrderView != null) {
                        iDeleteOrderView.showDeleteOrderView();
                    }
                }
            });
        }
    }

    @Override
    public void uploadReturnImg(List<String> paths) {

        UploadManager.uploadProof(context, paths, new Subscriber<String>() {
            @Override
            public void onCompleted() {
                if (iReturnOrderView != null) {
                    iReturnOrderView.updateReturnImgCompleted();
                }
            }

            @Override
            public void onError(Throwable e) {
                if (iReturnOrderView != null) {
                    iReturnOrderView.updateReturnImgFail(e.getMessage(), 0);
                }
            }

            @Override
            public void onNext(String url) {
                if (iReturnOrderView != null) {
                    iReturnOrderView.updateReturnImgNext(url);
                }
            }
        });
    }

    @Override
    public void returnOrder(MyOrderReturn data) {
        String json = praseReturnOrderRequestToJson(data);
        if (myOrderBussiness != null) {
            myOrderBussiness.requestReturnOrder(json, new OnReturnOrderCallback() {

                @Override
                public void onRequestFailed(String msg) {
                    if (iReturnOrderView != null) {
                        iReturnOrderView.showReturnOrderFailedView(msg);
                    }
                }

                @Override
                public void onRequestSuccess() {
                    if (iReturnOrderView != null) {
                        iReturnOrderView.showReturnOrderView();
                    }
                }
            });
        }
    }

    @Override
    public void sumitComment(CommentGroup data) {
        String json = praseCommentToJson(data);
        if (myOrderBussiness != null) {
            myOrderBussiness.sumitComment(json, new OnSumitCommentCallback() {

                @Override
                public void onSumitFailed(String msg) {
                    if (iCommentView != null) {
                        iCommentView.showSumitCommentFailedView(msg);
                    }
                }

                @Override
                public void onSumitSuccess() {
                    if (iCommentView != null) {
                        iCommentView.showSumitCommentView();
                    }
                }
            });
        }
    }

    private String praseReturnOrderRequestToJson(MyOrderReturn myOrderReturn) {
        Gson gson = new Gson();
        String result = gson.toJson(myOrderReturn);
        LogUtil.d("yongyuan.w", "praseReturnOrderRequestToJson--" + result);
        return result;
    }

    private String praseCommentToJson(CommentGroup data) {
        Gson gson = new Gson();
        String result = gson.toJson(data);
        LogUtil.d("yongyuan.w", "praseCommentToJson--" + result);
        return result;
    }
}
