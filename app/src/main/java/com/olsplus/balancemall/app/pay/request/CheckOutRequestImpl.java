package com.olsplus.balancemall.app.pay.request;


import android.content.Context;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.olsplus.balancemall.app.pay.bean.ShopingOrderSubmitResultEntity;
import com.olsplus.balancemall.app.pay.bean.OrderDataEntity;
import com.olsplus.balancemall.app.pay.bean.OrderProductEntity;
import com.olsplus.balancemall.app.pay.bean.ShoppingCartEntity;
import com.olsplus.balancemall.app.pay.bean.ShoppingCartGroupEntity;
import com.olsplus.balancemall.app.pay.bean.ShoppingPayRequestEntity;
import com.olsplus.balancemall.app.pay.bean.ShoppingPayResultEntity;
import com.olsplus.balancemall.app.pay.bean.ShoppingProductEntity;
import com.olsplus.balancemall.app.pay.bean.ShoppingResultByZero;
import com.olsplus.balancemall.app.pay.bean.ShoppingVoucherEntity;
import com.olsplus.balancemall.app.pay.bean.ShoppingWxPayResult;
import com.olsplus.balancemall.app.pay.business.CheckOutBussiness;
import com.olsplus.balancemall.app.pay.view.ICheckOutView;
import com.olsplus.balancemall.app.pay.view.IShowVoucherView;
import com.olsplus.balancemall.core.util.LogUtil;

import java.util.ArrayList;
import java.util.List;

public class CheckOutRequestImpl implements ICheckOutRequest {

    private Context context;

    private CheckOutBussiness checkOutBussiness;

    private ICheckOutView checkOutView;

    private IShowVoucherView showVoucherView;

    public CheckOutRequestImpl(Context context) {
        this.context = context;
        checkOutBussiness = new CheckOutBussiness(context);
    }

    public void setShowCheckOutView(ICheckOutView checkOutView){
        this.checkOutView = checkOutView;
    }

    public void setShowVoucherView(IShowVoucherView showVoucherView){
        this.showVoucherView = showVoucherView;
    }


    @Override
    public void sumitOrder(List<ShoppingCartEntity> services) {
        String shoppingCartJson = PraseShoppingCartToJson(services);
        if(TextUtils.isEmpty(shoppingCartJson)){
            if(checkOutView!=null){
                checkOutView.showSumitOrderFailed("数据出错了");
            }
            return;
        }
        checkOutBussiness.sumitOrder(shoppingCartJson, new SumitOrderCallback() {
            @Override
            public void onSumitOrderSuccess(ShopingOrderSubmitResultEntity shopingOrderSubmitResultEntity) {
                if(checkOutView!=null){
                    checkOutView.onSumitOrderSuccess(shopingOrderSubmitResultEntity);
                }
            }

            @Override
            public void onSumitOrderFailed(String msg) {
                if(checkOutView!=null){
                    checkOutView.showSumitOrderFailed(msg);
                }
            }
        });
    }

    @Override
    public void sumitOrder(String orderData) {
        List<ShoppingCartEntity> services = parseOrderResutData(orderData);
        if(services==null || services.isEmpty()){
            if(checkOutView!=null){
                checkOutView.showSumitOrderFailed("数据出错了");
            }
            return;
        }
        String shoppingCartJson = PraseShoppingCartToJson(services);
        if(TextUtils.isEmpty(shoppingCartJson)){
            if(checkOutView!=null){
                checkOutView.showSumitOrderFailed("数据出错了");
            }
            return;
        }
        checkOutBussiness.sumitOrder(shoppingCartJson, new SumitOrderCallback() {
            @Override
            public void onSumitOrderSuccess(ShopingOrderSubmitResultEntity shopingOrderSubmitResultEntity) {
                if(checkOutView!=null){
                    checkOutView.onSumitOrderSuccess(shopingOrderSubmitResultEntity);
                }
            }

            @Override
            public void onSumitOrderFailed(String msg) {
                if(checkOutView!=null){
                    checkOutView.showSumitOrderFailed(msg);
                }
            }
        });
    }

    @Override
    public void startPay(double totalFee, double needPayFee, int pointsUsed, String voucher, String pay_type, List<String> orders) {
        ShoppingPayRequestEntity payRequestEntity = new ShoppingPayRequestEntity();
        payRequestEntity.setTotal_fee(totalFee);
        payRequestEntity.setPay(needPayFee);
        payRequestEntity.setPointsUsed(pointsUsed);
        payRequestEntity.setVoucher(voucher);
        payRequestEntity.setPay_type(pay_type);
        payRequestEntity.setOrders(orders);
        String requestData = prasePayRequestToJson(payRequestEntity);
        if(TextUtils.isEmpty(requestData)){
            if(checkOutView!=null){
                checkOutView.showPayFailed("数据出错了");
            }
            return;
        }
        if(pay_type.equals("ZHIFUBAO")){
            checkOutBussiness.startAliBuyStep(requestData, new PayCallback() {
                @Override
                public void onPaySuccess(ShoppingPayResultEntity shoppingPayResultEntity) {
                    if(checkOutView!=null){
                        checkOutView.showPaySuccess(shoppingPayResultEntity);
                    }
                }

                @Override
                public void onPayFailed(String msg) {
                    if(checkOutView!=null){
                        checkOutView.showPayFailed(msg);
                    }
                }
            });
        }else if(pay_type.equals("WEIXIN")){
            checkOutBussiness.startWxBuyStep(requestData, new WxPayCallback() {

                @Override
                public void onPaySuccess(ShoppingWxPayResult data) {
                    if(checkOutView!=null){
                        checkOutView.showWxPaySuccess(data);
                    }
                }

                @Override
                public void onPayFailed(String msg) {
                    if(checkOutView!=null){
                        checkOutView.showPayFailed(msg);
                    }
                }
            });
        }
        else{
            checkOutBussiness.startBuyStep(requestData, new SpecialPayCallback() {

                @Override
                public void onPaySuccess(ShoppingResultByZero data) {
                    if(checkOutView!=null){
                        checkOutView.showSpecialPaySuccess(data);
                    }
                }

                @Override
                public void onPayFailed(String msg) {
                    if(checkOutView!=null){
                        checkOutView.showPayFailed(msg);
                    }
                }
            });
        }

    }

    @Override
    public void getVouchers() {
        checkOutBussiness.getVouchers(new GetVoucherCallback() {
            @Override
            public void onVoucherSuccess(List<ShoppingVoucherEntity> datas) {
                if(showVoucherView!=null){
                    showVoucherView.showVoucherView(datas);
                }
            }

            @Override
            public void onVoucherFailed(String msg) {
                if(showVoucherView!=null){
                    showVoucherView.showVoucherError(msg);
                }
            }
        });
    }


    /**
     * 解析网页提交订单返回的url
     * @param orderJson
     */
    private  List<ShoppingCartEntity> parseOrderResutData(String orderJson){
        if(TextUtils.isEmpty(orderJson)){
            return null;
        }
        try {
            int index =  orderJson.indexOf(":");
            String dataStr = orderJson.substring(index+1,orderJson.length());
            if(TextUtils.isEmpty(dataStr)){
                return null;
            }

            Gson dataGson = new Gson();
            OrderDataEntity orderDataEntity = dataGson.fromJson(dataStr, OrderDataEntity.class);
            List<ShoppingCartEntity> services = new ArrayList<ShoppingCartEntity>();
            if(orderDataEntity!=null){
                ShoppingCartEntity shoppingCartEntity = new ShoppingCartEntity();
                shoppingCartEntity.setLocal_service_id(orderDataEntity.getLocal_service_id());
                String time = orderDataEntity.getTime();
                List<ShoppingProductEntity> cartProducts = new ArrayList<ShoppingProductEntity>();
                List<OrderProductEntity> products =orderDataEntity.getProduct();
                for(OrderProductEntity orderProductEntity :products){
                    ShoppingProductEntity shoppingProductEntity = new ShoppingProductEntity();
                    shoppingProductEntity.setId(orderProductEntity.getId());
                    String sku_id = orderProductEntity.getSku_info().getSku_id();
                    int  count = orderProductEntity.getSku_info().getCount();
                    if(!TextUtils.isEmpty(sku_id)){
                        shoppingProductEntity.setSku_id(sku_id);
                    }
                    if(!TextUtils.isEmpty(time)){
                        shoppingProductEntity.setSchedule_time(time);
                    }
                    shoppingProductEntity.setCount(count);
                    cartProducts.add(shoppingProductEntity);
                }
                shoppingCartEntity.setProducts(cartProducts);
                services.add(shoppingCartEntity);
            }
            return services;
        } catch (Throwable e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * 把提交订单的请求转化成json格式
     * @param services
     * @return
     */
    private String PraseShoppingCartToJson( List<ShoppingCartEntity> services ){
        ShoppingCartGroupEntity shoppingCartGroupEntity = new ShoppingCartGroupEntity();
        shoppingCartGroupEntity.setServices(services);
        Gson gson=new Gson();
        String result= gson.toJson(shoppingCartGroupEntity);
        LogUtil.d("yongyuan.w","PraseShoppingCartToJson--"+result);
        return result;
    }


    /**
     * 把订单支付的请求转化成json格式
     * @param payRequestEntity
     * @return
     */
    private String prasePayRequestToJson( ShoppingPayRequestEntity payRequestEntity){
        Gson gson=new Gson();
        String result= gson.toJson(payRequestEntity);
        LogUtil.d("yongyuan.w","PrasePayRequestToJson--"+result);
        return result;
    }


}
