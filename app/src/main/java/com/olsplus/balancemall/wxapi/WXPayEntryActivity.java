package com.olsplus.balancemall.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.olsplus.balancemall.R;
import com.olsplus.balancemall.core.event.WxPayEvent;
import com.olsplus.balancemall.core.pay.config.PayConstants;
import com.olsplus.balancemall.core.util.ToastUtil;

import org.greenrobot.eventbus.EventBus;


public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {
    private IWXAPI api;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_result);
        api = WXAPIFactory.createWXAPI(this, PayConstants.WX_APP_ID);
        api.handleIntent(getIntent(), this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq req) {
    }

    @Override
    public void onResp(BaseResp resp) {
        if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
            if (BaseResp.ErrCode.ERR_USER_CANCEL == resp.errCode) {// 微信支付取消回调
                Toast.makeText(this, "支付失败", Toast.LENGTH_SHORT).show();
            } else if (BaseResp.ErrCode.ERR_OK == resp.errCode) {// 微信支付成功回调
                setResult(RESULT_OK);
                finish();
                EventBus.getDefault().post(new WxPayEvent(1));
            } else {
                ToastUtil.showShort(this, "支付失败");
            }
        }
        finish();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

}