package com.olsplus.balancemall.app.merchant.earning.business;


import android.content.Context;

import com.olsplus.balancemall.app.merchant.earning.bean.EarningListEntity;
import com.olsplus.balancemall.app.merchant.earning.service.EarningService;
import com.olsplus.balancemall.core.http.DoTransform;
import com.olsplus.balancemall.core.http.HttpManager;
import com.olsplus.balancemall.core.http.HttpResultObserver;
import com.olsplus.balancemall.core.http.HttpUtil;
import com.olsplus.balancemall.core.util.DateUtil;
import com.olsplus.balancemall.core.util.SPUtil;
import com.olsplus.balancemall.core.util.UrlConst;

import java.util.HashMap;
import java.util.Map;

public class EarningBusiness {

    private static EarningService service = HttpManager.getRetrofit().create(EarningService.class);

    /**
     * 收入列表
     *
     * @param context
     * @param page
     * @param count
     * @param type
     * @param observer
     */
    public static void getEarning(Context context, int page, int count, String type, HttpResultObserver<EarningListEntity> observer) {

        String uid = (String) SPUtil.get(context, SPUtil.UID, "");
        String token = (String) SPUtil.get(context, SPUtil.TOKEN, "");
        String timestamp = String.valueOf(DateUtil.getCurrentTimeInLong());
        String local_service_id = (String) SPUtil.get(context, SPUtil.LOCAL_SERVICE_ID, "");

        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("uid", uid);
        paramMap.put("token", token);
        paramMap.put("page", String.valueOf(page));
        paramMap.put("count", String.valueOf(count));
        paramMap.put("type", type);
        paramMap.put("local_service_id", local_service_id);
        paramMap.put("timestamp", timestamp);
        String sign = HttpUtil.sign(HttpUtil.GET, UrlConst.merchant.merchant_earning, paramMap);

        service.getEarning(uid, token, local_service_id, type, timestamp, page, count, sign)
                .compose(DoTransform.<EarningListEntity>applyScheduler())
                .subscribe(observer);
    }
}
