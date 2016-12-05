package com.olsplus.balancemall.core.pay.wxpay;


import android.app.Activity;
import android.text.TextUtils;
import android.widget.Toast;


import com.tencent.mm.sdk.constants.Build;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.olsplus.balancemall.core.pay.config.PayConstants;



public class WXPayUtil {

    private static WXPayUtil instance;
    // IWXAPI 是第三方app和微信通信的openapi接口
    private IWXAPI api;
    private static Object lock = new Object();
    private Activity mActivity;

    private boolean debug = false;

    private WXPayUtil(Activity activity) {
        this.mActivity = activity;
        // 通过WXAPIFactory工厂，获取IWXAPI的实例
        api = WXAPIFactory.createWXAPI(activity, PayConstants.WX_APP_ID,
                false);
    }

    public static WXPayUtil getInstance(Activity activity) {
        synchronized (lock) {
            if (instance == null) {
                instance = new WXPayUtil(activity);
            }
            return instance;
        }
    }

    //"datas":{"appid":"wxf7f687e1a687a34a","noncestr":"b435d382806216066fbb82567e789447","package":"Sign=WXPay","partnerid":"1361730302","prepayid":"wx2016071217203553dce4da9c0429067621","timestamp":1468315234,"sign":"5786902121FC01EBF9890BEE7D04D91C"}

    public void startPayReq(String appid,String noncestr,String partnerid,String prepayid,String timestamp,String sign) {
        if (TextUtils.isEmpty(prepayid)) {
            return;
        }
        PayReq req = new PayReq();
        req.appId = appid;
        req.partnerId = partnerid;
        req.prepayId = prepayid;
        req.packageValue = "Sign=WXPay";
        req.nonceStr = noncestr;
        req.timeStamp = timestamp;

        req.sign = sign;
        if (!api.isWXAppInstalled()) {
            Toast.makeText(mActivity, "请先安装微信才能支付",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        boolean isPaySupported = api.getWXAppSupportAPI() >= Build.PAY_SUPPORTED_SDK_INT;
        if (!isPaySupported) {
            Toast.makeText(mActivity, "请先升级您的微信版本",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        api.registerApp(PayConstants.WX_APP_ID);
        api.sendReq(req);

    }

}
