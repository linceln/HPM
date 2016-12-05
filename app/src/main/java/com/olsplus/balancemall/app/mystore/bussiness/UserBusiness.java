package com.olsplus.balancemall.app.mystore.bussiness;


import android.content.Context;
import android.text.TextUtils;

import com.olsplus.balancemall.R;
import com.olsplus.balancemall.app.merchant.goods.bean.ImageUploadEntity;
import com.olsplus.balancemall.app.mystore.bean.AvatarResultEntity;
import com.olsplus.balancemall.app.mystore.bean.FeedBackResult;
import com.olsplus.balancemall.app.mystore.bean.MySeesionEntity;
import com.olsplus.balancemall.app.mystore.bean.UpdateGenderResultEntity;
import com.olsplus.balancemall.app.mystore.request.IMyStoreDataRequest;
import com.olsplus.balancemall.app.mystore.request.UserService;
import com.olsplus.balancemall.core.bean.BaseResultEntity;
import com.olsplus.balancemall.core.http.HttpManager;
import com.olsplus.balancemall.core.http.HttpResultObserver;
import com.olsplus.balancemall.core.http.HttpUtil;
import com.olsplus.balancemall.core.http.RequestCallback;
import com.olsplus.balancemall.core.util.ApiConst;
import com.olsplus.balancemall.core.util.DateUtil;
import com.olsplus.balancemall.core.util.EncrypUtil;
import com.olsplus.balancemall.core.util.LogUtil;
import com.olsplus.balancemall.core.util.SPUtil;
import com.olsplus.balancemall.core.util.StrConst;
import com.olsplus.balancemall.core.util.UrlConst;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class UserBusiness {

    private Context context;

    public UserBusiness(Context context) {
        this.context = context;
    }

    /**
     * 获取个人信息
     *
     * @param callback
     */
    public void getMyseesionData(final IMyStoreDataRequest.GetMySeesionCallback callback) {
        String url = ApiConst.BASE_URL + "v1/user/info";
        String timestamp = String.valueOf(DateUtil.getCurrentTimeInLong());
        String uid = (String) SPUtil.get(context, SPUtil.UID, "");
        String token = (String) SPUtil.get(context, SPUtil.TOKEN, "");
        LogUtil.d("yongyuan.w", "token=" + token);
        String sign = parseGetMySeesionSign(url, uid, token, timestamp);
        HttpResultObserver respObserver = new HttpResultObserver<MySeesionEntity>() {

            @Override
            public void prepare() {
            }

            @Override
            public void reConnect() {
                getMyseesionData(callback);
            }

            @Override
            public void handleSuccessResp(MySeesionEntity data) {
                if (data == null) {
                    callback.onGetMySeesionFailed("数据出错了");
                    return;
                }
                callback.onGetMySeesionSuccess(data);
            }

            @Override
            public void handleFailedResp(String msg) {
                LogUtil.d("yongyuan,w", "getMySeesionData failed");
                callback.onGetMySeesionFailed(msg);
            }
        };
        HttpManager.getRetrofit()
                .create(UserService.class)
                .getMySeesionData(uid, token, timestamp, sign)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(respObserver);
    }

    /**
     * 得到上传头像的token
     *
     * @param uploadfile
     * @return
     */
    public Observable<ImageUploadEntity> getAvatarToken(final String uploadfile) {
        String uid = (String) SPUtil.get(context, SPUtil.UID, "");
        String token = (String) SPUtil.get(context, SPUtil.TOKEN, "");
        String timestamp = String.valueOf(DateUtil.getCurrentTimeInLong());

//        Map<String, String> paramMap = new HashMap<>();
//        paramMap.put("uid", uid);
//        paramMap.put("token", token);
//        paramMap.put("timestamp", timestamp);
//        paramMap.put("uploadfile", uploadfile);
//        String sign = HttpUtil.sign(HttpUtil.POST, UrlConst.merchant.avatar_upload, paramMap);

        String sign = parseUploadAvatarSign(UrlConst.merchant.avatar_upload, uid, token, timestamp, uploadfile);

        return HttpManager.getRetrofit()
                .create(UserService.class)
                .getAvatarToken(uid, token, timestamp, sign, uploadfile)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(new Func1<ImageUploadEntity, Observable<ImageUploadEntity>>() {
                    @Override
                    public Observable<ImageUploadEntity> call(ImageUploadEntity imageUploadEntity) {

                        // 异常处理
                        if (imageUploadEntity.getRet() == 0) {
                            imageUploadEntity.setCompressedPath(uploadfile);
                            return Observable.just(imageUploadEntity);
                        } else {
                            return Observable.error(new Exception(imageUploadEntity.getMsg()));
                        }
                    }
                });
    }

//    /**
//     * 上传头像
//     *
//     * @param picPath
//     * @param callback
//     */
//    public void uploadAvatar(final String picPath, final IMyStoreDataRequest.UpoadAvaterCallback callback) {
//        if (TextUtils.isEmpty(picPath)) {
//            return;
//        }
//        File file = new File(picPath.replace("file://", ""));
//        RequestBody requestFile =
//                RequestBody.create(MediaType.parse("multipart/form-data"), file);
////        MultipartBody.Part body = MultipartBody.Part.createFormData("uploadfile", file.getName(), requestFile);
//        String url = ApiConst.BASE_URL + "v1/upload/avatar";
//        String timestamp = String.valueOf(DateUtil.getCurrentTimeInLong());
//        String uid = (String) SPUtil.get(context, SPUtil.UID, "");
//        String token = (String) SPUtil.get(context, SPUtil.TOKEN, "");
//        String sign = parseUploadAvatarSign(url, uid, token, timestamp);
////        String requestUrl = "v1/upload/avatar?uid="+uid+"&token="+token+"&timestamp="+timestamp+"&sign="+sign;
//        HttpResultObserver respObserver = new HttpResultObserver<AvatarResultEntity>() {
//
//            @Override
//            public void prepare() {
//            }
//
//            @Override
//            public void reConnect() {
//                uploadAvatar(picPath.replace("file://", ""), callback);
//            }
//
//            @Override
//            public void handleSuccessResp(AvatarResultEntity data) {
//                if (data == null) {
//                    callback.onUploadFailed("数据出错了");
//                    return;
//                }
//                callback.onUploadSuccess(data.getAvatar());
//            }
//
//            @Override
//            public void handleFailedResp(String msg) {
//                callback.onUploadFailed(msg);
//            }
//        };
//        HttpManager.getRetrofit()
//                .create(UserService.class)
//                .getAvatarToken(uid, token, timestamp, sign, requestFile)
////                .uploadAvatar(body)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(respObserver);
//    }

    /**
     * 设置性别
     *
     * @param gender
     * @param callback
     */
    public void updateGender(final String gender, final IMyStoreDataRequest.UpdateGenderCallback callback) {
        String url = ApiConst.BASE_URL + "v1/gender";
        String timestamp = String.valueOf(DateUtil.getCurrentTimeInLong());
        String uid = (String) SPUtil.get(context, SPUtil.UID, "");
        String token = (String) SPUtil.get(context, SPUtil.TOKEN, "");
        String sign = parseUpdateGenderSign(url, gender, uid, token, timestamp);
        HttpResultObserver respObserver = new HttpResultObserver<UpdateGenderResultEntity>() {

            @Override
            public void prepare() {

            }

            @Override
            public void reConnect() {
                updateGender(gender, callback);
            }

            @Override
            public void handleSuccessResp(UpdateGenderResultEntity data) {
                if (data == null) {
                    callback.onUpdateGenderFailed("数据出错了");
                    return;
                }
                callback.onUpdateGenderSuccess(data.getGender());
            }

            @Override
            public void handleFailedResp(String msg) {
                LogUtil.d("yongyuan,w", "updateGender failed");
                callback.onUpdateGenderFailed(msg);
            }
        };
        HttpManager.getRetrofit()
                .create(UserService.class)
                .updateGender(uid, token, gender, timestamp, sign)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(respObserver);
    }


    /**
     * 修改密码
     *
     * @param oldPwd
     * @param newPwd
     * @param callback
     */
    public void updatePassword(final String oldPwd, final String newPwd, final IMyStoreDataRequest.UpdatePwdCallback callback) {
        String url = ApiConst.BASE_URL + "v1/passwd";
        String timestamp = String.valueOf(DateUtil.getCurrentTimeInLong());
        String uid = (String) SPUtil.get(context, SPUtil.UID, "");
        String token = (String) SPUtil.get(context, SPUtil.TOKEN, "");
        String oldPassword = EncrypUtil.eccryptSHA1(oldPwd);
        String newPassword = EncrypUtil.eccryptSHA1(newPwd);
        String sign = parseUpdatePwdSign(url, oldPassword, newPassword, uid, token, timestamp);
        HttpResultObserver respObserver = new HttpResultObserver<BaseResultEntity>() {

            @Override
            public void prepare() {

            }

            @Override
            public void reConnect() {
                updatePassword(oldPwd, newPwd, callback);
            }

            @Override
            public void handleSuccessResp(BaseResultEntity data) {
                if (data == null) {
                    callback.onUpdatePwdFailed("数据出错了");
                    return;
                }
                callback.onUpdatePwdSuccess();
            }

            @Override
            public void handleFailedResp(String msg) {
                LogUtil.d("yongyuan,w", "updatePassword failed");
                callback.onUpdatePwdFailed(msg);
            }
        };
        HttpManager.getRetrofit()
                .create(UserService.class)
                .updatePassword(uid, token, oldPassword, newPassword, timestamp, sign)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(respObserver);
    }

    /**
     * 校验密码
     *
     * @param pwd
     * @param callback
     */
    public void checkPassword(final String pwd, final IMyStoreDataRequest.CheckPwdCallback callback) {
        String url = ApiConst.BASE_URL + "v1/passwd";
        String timestamp = String.valueOf(DateUtil.getCurrentTimeInLong());
        String uid = (String) SPUtil.get(context, SPUtil.UID, "");
        String token = (String) SPUtil.get(context, SPUtil.TOKEN, "");
        String password = EncrypUtil.eccryptSHA1(pwd);
        String sign = parseCheckPwdSign(url, password, uid, token, timestamp);
        HttpResultObserver respObserver = new HttpResultObserver<BaseResultEntity>() {

            @Override
            public void prepare() {

            }

            @Override
            public void reConnect() {
                checkPassword(pwd, callback);
            }

            @Override
            public void handleSuccessResp(BaseResultEntity data) {
                callback.onCheckPwdSuccess();
            }

            @Override
            public void handleFailedResp(String msg) {
                LogUtil.d("yongyuan,w", "checkPasswd failed");
                callback.onCheckPwdFailed(msg);
            }
        };
        HttpManager.getRetrofit()
                .create(UserService.class)
                .checkPasswd(uid, token, timestamp, sign, password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(respObserver);
    }

    /**
     * 绑定手机
     *
     * @param phone
     * @param sms
     * @param callback
     */
    public void bindPhone(final String phone, final String sms, final IMyStoreDataRequest.BindPhoneCallback callback) {
        String url = ApiConst.BASE_URL + "v1/change_phone";
        String timestamp = String.valueOf(DateUtil.getCurrentTimeInLong());
        String uid = (String) SPUtil.get(context, SPUtil.UID, "");
        String token = (String) SPUtil.get(context, SPUtil.TOKEN, "");
        String sign = parseBindPhoneSign(url, phone, sms, uid, token, timestamp);
        HttpResultObserver respObserver = new HttpResultObserver<BaseResultEntity>() {

            @Override
            public void prepare() {

            }

            @Override
            public void reConnect() {
                bindPhone(phone, sms, callback);
            }

            @Override
            public void handleSuccessResp(BaseResultEntity data) {
                callback.onBinduccess();
            }

            @Override
            public void handleFailedResp(String msg) {
                LogUtil.d("yongyuan,w", "bindPhone failed");
                callback.onBindFailed(msg);
            }
        };
        HttpManager.getRetrofit()
                .create(UserService.class)
                .bindPhone(uid, token, phone, sms, timestamp, sign)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(respObserver);
    }

    /**
     * 绑定手机发送验证码
     *
     * @param phone
     * @param callback
     */
    public void sendSms(final String phone, final IMyStoreDataRequest.SendsmsCallback callback) {
        String timestamp = String.valueOf(DateUtil.getCurrentTimeInLong());
        String url = ApiConst.BASE_URL + "v1/phone/change_sms";
        String uid = (String) SPUtil.get(context, SPUtil.UID, "");
        String token = (String) SPUtil.get(context, SPUtil.TOKEN, "");
        String sign = parseSendsmsSign(url, phone, uid, token, timestamp);
        HttpResultObserver respObserver = new HttpResultObserver<BaseResultEntity>() {

            @Override
            public void prepare() {

            }

            @Override
            public void reConnect() {
                sendSms(phone, callback);
            }

            @Override
            public void handleSuccessResp(BaseResultEntity data) {
                callback.onSendsuccess();
            }

            @Override
            public void handleFailedResp(String msg) {
                LogUtil.d("yongyuan.w", "sendSms");
                callback.onSendFailed(msg);
            }
        };
        HttpManager.getRetrofit()
                .create(UserService.class)
                .sendSms(uid, token, phone, timestamp, sign)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(respObserver);
    }

    /**
     * 提交建议
     *
     * @param suggestion
     * @param callback
     */
    public void sumitFeedback(final String suggestion, final IMyStoreDataRequest.FeedbackCallback callback) {
        String url = ApiConst.BASE_URL + "v1/feedback";
        String timestamp = String.valueOf(DateUtil.getCurrentTimeInLong());
        String uid = (String) SPUtil.get(context, SPUtil.UID, "");
        String token = (String) SPUtil.get(context, SPUtil.TOKEN, "");
        String sign = parseFeedbcakSign(url, uid, token, suggestion, timestamp);
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), suggestion);
        String requestUrl = "v1/feedback?uid=" + uid + "&token=" + token + "&timestamp=" + timestamp + "&sign=" + sign;
        HttpResultObserver respObserver = new HttpResultObserver<FeedBackResult>() {

            @Override
            public void prepare() {

            }

            @Override
            public void reConnect() {
                sumitFeedback(suggestion, callback);
            }

            @Override
            public void handleSuccessResp(FeedBackResult data) {
                callback.onFeedbacksuccess();
            }

            @Override
            public void handleFailedResp(String msg) {
                LogUtil.d("yongyuan,w", "sumitFeedback failed");
                callback.onFeedbackFailed(msg);
            }
        };
        HttpManager.getRetrofit()
                .create(UserService.class)
                .sumitFeedback(requestUrl, body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(respObserver);
    }


    /**
     * 生成获取个人信息签名
     *
     * @param url
     * @param uid
     * @param token
     * @param timestamp
     * @return
     */
    private String parseGetMySeesionSign(String url, String uid, String token, String timestamp) {
        Map<String, String> paramMap = new HashMap<String, String>();
        paramMap.put("uid", uid);
        paramMap.put("token", token);
        paramMap.put("timestamp", timestamp);
        String sign = HttpUtil.sign(HttpUtil.GET, url, paramMap);
        return sign;
    }


    /**
     * 生成上传头像签名
     *
     * @param url
     * @param uid
     * @param token
     * @param timestamp
     * @return
     */
    private String parseUploadAvatarSign(String url, String uid, String token, String timestamp, String uploadfile) {
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("uid", uid);
        paramMap.put("token", token);
        paramMap.put("timestamp", timestamp);
        paramMap.put("uploadfile", uploadfile);
        String sign = HttpUtil.sign(HttpUtil.POST, url, paramMap);
        return sign;
    }

    private String parseUpdateGenderSign(String url, String gender, String uid, String token, String timestamp) {
        Map<String, String> paramMap = new HashMap<String, String>();
        paramMap.put("gender", gender);
        paramMap.put("uid", uid);
        paramMap.put("token", token);
        paramMap.put("timestamp", timestamp);
        String sign = HttpUtil.sign(HttpUtil.POST, url, paramMap);
        return sign;
    }

    private String parseUpdatePwdSign(String url, String oldPwd, String newPwd, String uid, String token, String timestamp) {
        Map<String, String> paramMap = new HashMap<String, String>();
        paramMap.put("oldpwd", oldPwd);
        paramMap.put("newpwd", newPwd);
        paramMap.put("uid", uid);
        paramMap.put("token", token);
        paramMap.put("timestamp", timestamp);
        String sign = HttpUtil.sign(HttpUtil.POST, url, paramMap);
        return sign;
    }

    private String parseCheckPwdSign(String url, String pwd, String uid, String token, String timestamp) {
        Map<String, String> paramMap = new HashMap<String, String>();
        paramMap.put("pwd", pwd);
        paramMap.put("uid", uid);
        paramMap.put("token", token);
        paramMap.put("timestamp", timestamp);
        String sign = HttpUtil.sign(HttpUtil.GET, url, paramMap);
        return sign;
    }

    private String parseBindPhoneSign(String url, String phone, String sms, String uid, String token, String timestamp) {
        Map<String, String> paramMap = new HashMap<String, String>();
        paramMap.put("phone", phone);
        paramMap.put("sms", sms);
        paramMap.put("uid", uid);
        paramMap.put("token", token);
        paramMap.put("timestamp", timestamp);
        String sign = HttpUtil.sign(HttpUtil.POST, url, paramMap);
        return sign;
    }

    private String parseSendsmsSign(String url, String phone, String uid, String token, String timestamp) {
        Map<String, String> paramMap = new HashMap<String, String>();
        paramMap.put("phone", phone);
        paramMap.put("uid", uid);
        paramMap.put("token", token);
        paramMap.put("timestamp", timestamp);
        String sign = HttpUtil.sign(HttpUtil.POST, url, paramMap);
        return sign;
    }

    private String parseFeedbcakSign(String url, String uid, String token, String suggestion, String timestamp) {
        Map<String, String> paramMap = new HashMap<String, String>();
        paramMap.put("uid", uid);
        paramMap.put("token", token);
        paramMap.put("timestamp", timestamp);
        String sign = HttpUtil.signWithJson(HttpUtil.POST, url, paramMap, suggestion);
        return sign;
    }
}
