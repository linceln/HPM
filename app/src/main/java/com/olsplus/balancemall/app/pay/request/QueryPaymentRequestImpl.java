package com.olsplus.balancemall.app.pay.request;


import android.content.Context;

import com.google.gson.Gson;
import com.olsplus.balancemall.app.pay.bean.ShopingPayQueryEntity;
import com.olsplus.balancemall.app.pay.bean.ShoppingSuccessResult;
import com.olsplus.balancemall.app.pay.business.QueryPayReusltBussiness;
import com.olsplus.balancemall.app.pay.view.IQueryPaymentView;
import com.olsplus.balancemall.core.util.LogUtil;

public class QueryPaymentRequestImpl {

    private Context context;

    private QueryPayReusltBussiness queryPayReusltBussiness;

    private IQueryPaymentView iQueryPaymentView;

    public QueryPaymentRequestImpl(Context context) {
        this.context = context;
        queryPayReusltBussiness = new QueryPayReusltBussiness(context);
    }

    public void setiQueryPaymentView(IQueryPaymentView iQueryPaymentView) {
        this.iQueryPaymentView = iQueryPaymentView;
    }

    public void queryPayment(ShopingPayQueryEntity data){
        if(data == null){
            return;
        }
        String json = praseQueryPayToJson(data);
        queryPayReusltBussiness.queryPayment(json, new IQueryPayReusltRequest.QueryCallback() {
            @Override
            public void onQueryPayReusltSuccess(ShoppingSuccessResult data) {
                if(iQueryPaymentView!=null){
                    iQueryPaymentView.showQueryPaymentSuccess(data);
                }
            }

            @Override
            public void onQueryPayReusltFailed(String msg) {
                if(iQueryPaymentView!=null){
                    iQueryPaymentView.showQueryPaymentFailed(msg);
                }
            }
        });
    }

    private String praseQueryPayToJson(  ShopingPayQueryEntity data){
        Gson gson=new Gson();
        String result= gson.toJson(data);
        LogUtil.d("yongyuan.w","praseQueryPayToJson--"+result);
        return result;
    }

}
