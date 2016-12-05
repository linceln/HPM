package com.olsplus.balancemall.app.mystore.message.request;


import android.content.Context;

import com.olsplus.balancemall.app.mystore.bean.MessageCenterInfoList;
import com.olsplus.balancemall.core.bean.BaseResultEntity;
import com.olsplus.balancemall.core.http.HttpManager;
import com.olsplus.balancemall.core.http.HttpResultObserver;
import com.olsplus.balancemall.core.http.HttpUtil;
import com.olsplus.balancemall.core.util.ApiConst;
import com.olsplus.balancemall.core.util.DateUtil;
import com.olsplus.balancemall.core.util.LogUtil;
import com.olsplus.balancemall.core.util.SPUtil;

import java.util.HashMap;
import java.util.Map;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MessageBuiness {

    private Context context;

    public MessageBuiness(Context context) {
        this.context = context;
    }

    /**
     * 获取消息列表
     * @param page
     * @param count
     * @param callback
     */
    public void getMyMessageListData(final String page,final String count,final IMessageRequest.OnGetMessageCallback callback){
        String url = ApiConst.BASE_URL + "v1/message/list";
        String timestamp = String.valueOf(DateUtil.getCurrentTimeInLong());
        String uid = (String) SPUtil.get(context, SPUtil.UID, "");
        String token = (String) SPUtil.get(context, SPUtil.TOKEN, "");
        String sign = parseGetMessageListSign(url, uid, token,timestamp,page,count);
        HttpResultObserver respObserver = new HttpResultObserver<MessageCenterInfoList>() {

            @Override
            public void prepare() {

            }

            @Override
            public void reConnect() {
                getMyMessageListData(page, count, callback);
            }

            @Override
            public void handleSuccessResp(MessageCenterInfoList data) {
                if (data == null) {
                    callback.onGetMessageFailed("数据出错了");
                    return;
                }
                if (data.getMsgs() == null) {
                    callback.onGetMessageFailed("数据出错了");
                    return;
                }
                callback.onGetMessageSuccess(data);
            }

            @Override
            public void handleFailedResp(String msg) {
                LogUtil.d("yongyuan,w", "getMessageList failed");
                callback.onGetMessageFailed(msg);
            }
        };
        HttpManager.getRetrofit()
                .create(MessageService.class)
                .getMessageList(uid,token,timestamp,sign,page,count)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(respObserver);
    }

    /**
     * 阅读消息
     * @param msg_id
     * @param callback
     */
    public void readMessageData(final String msg_id,final IMessageRequest.OnReadMessageCallback callback){
        String url = ApiConst.BASE_URL + "v1/message/read";
        String timestamp = String.valueOf(DateUtil.getCurrentTimeInLong());
        String uid = (String) SPUtil.get(context, SPUtil.UID, "");
        String token = (String) SPUtil.get(context, SPUtil.TOKEN, "");
        String sign = parseReadMessageSign(url, uid, token,timestamp,msg_id);
        HttpResultObserver respObserver = new HttpResultObserver<BaseResultEntity>() {

            @Override
            public void prepare() {

            }

            @Override
            public void reConnect() {
                readMessageData(msg_id, callback);
            }

            @Override
            public void handleSuccessResp(BaseResultEntity data) {
                if (data == null) {
                    callback.onReadFailed("数据出错了");
                    return;
                }
                callback.onReadSuccess();
            }

            @Override
            public void handleFailedResp(String msg) {
                LogUtil.d("yongyuan,w", "readMessage failed");
                callback.onReadFailed(msg);
            }
        };
        HttpManager.getRetrofit()
                .create(MessageService.class)
                .readMessage(uid,token,timestamp,sign,msg_id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(respObserver);
    }


    /**
     * 生成获取消息列表签名
     * @param url
     * @param uid
     * @param token
     * @param timestamp
     * @param page
     * @param count
     * @return
     */
    private String parseGetMessageListSign(String url, String uid, String token,String timestamp,String page,String count){
        Map<String, String> paramMap = new HashMap<String, String>();
        paramMap.put("uid", uid);
        paramMap.put("token", token);
        paramMap.put("timestamp", timestamp);
        paramMap.put("page", page);
        paramMap.put("count", count);
        String sign = HttpUtil.sign(HttpUtil.GET, url, paramMap);
        return sign;
    }

    private String parseReadMessageSign(String url, String uid, String token,String timestamp,String msg_id){
        Map<String, String> paramMap = new HashMap<String, String>();
        paramMap.put("uid", uid);
        paramMap.put("token", token);
        paramMap.put("timestamp", timestamp);
        paramMap.put("msg_id", msg_id);
        String sign = HttpUtil.sign(HttpUtil.POST, url, paramMap);
        return sign;
    }
}
