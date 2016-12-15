package com.olsplus.balancemall.app.mine.request;


import android.content.Context;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.olsplus.balancemall.app.mine.bean.FeedBackEntity;
import com.olsplus.balancemall.app.mine.bean.MySeesionEntity;
import com.olsplus.balancemall.app.mine.bussiness.UserBusiness;
import com.olsplus.balancemall.app.mine.view.IShowBindPhoneView;
import com.olsplus.balancemall.app.mine.view.IShowFeedbackView;
import com.olsplus.balancemall.app.mine.view.IShowMytoreView;
import com.olsplus.balancemall.app.mine.view.IShowUpdatePwdView;
import com.olsplus.balancemall.app.mine.view.IUserView;
import com.olsplus.balancemall.core.http.HttpUtil;
import com.olsplus.balancemall.core.util.AppUtil;
import com.olsplus.balancemall.core.util.LogUtil;
import com.olsplus.balancemall.core.util.UploadManager;

import rx.Subscriber;

public class UserImpl implements IMyStoreDataRequest {

    private Context context;

    private UserBusiness userBusiness;
    private IUserView userView;
    private IShowMytoreView iShowMytoreView;
    private IShowBindPhoneView iShowBindPhoneView;
    private IShowFeedbackView iShowFeedbackView;
    private IShowUpdatePwdView iShowUpdatePwdView;

    public UserImpl(Context context) {
        this.context = context;
        userBusiness = new UserBusiness(context);
    }

    public void setUserView(IUserView userView) {
        this.userView = userView;
    }

    public void setIShowMytoreView(IShowMytoreView iShowMytoreView) {
        this.iShowMytoreView = iShowMytoreView;
    }

    public void setiShowBindPhoneView(IShowBindPhoneView iShowBindPhoneView) {
        this.iShowBindPhoneView = iShowBindPhoneView;
    }

    public void setiShowFeedbackView(IShowFeedbackView iShowFeedbackView) {
        this.iShowFeedbackView = iShowFeedbackView;
    }

    public void setiShowUpdatePwdView(IShowUpdatePwdView iShowUpdatePwdView) {
        this.iShowUpdatePwdView = iShowUpdatePwdView;
    }

    @Override
    public void getMySeesionData() {

        userBusiness.getMyseesionData(new GetMySeesionCallback() {
            @Override
            public void onGetMySeesionFailed(String msg) {
                if (iShowMytoreView != null) {
                    iShowMytoreView.showError(msg);
                }
            }

            @Override
            public void onGetMySeesionSuccess(MySeesionEntity data) {
                if (iShowMytoreView != null) {
                    iShowMytoreView.showMySeesionView(data);
                }
            }
        });

    }

    @Override
    public void upLoadAvatar(String picPath) {
        UploadManager.uploadAvatar(context, picPath, new Subscriber<String>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if (userView != null) {
                    userView.updateAvatarFail(e.getMessage());
                }
            }

            @Override
            public void onNext(String avatar) {
                if (userView != null) {
                    userView.updateAvatarSuccess(avatar);
                }
            }
        });
    }

    @Override
    public void updateGender(String gender) {
        userBusiness.updateGender(gender, new UpdateGenderCallback() {

            @Override
            public void onUpdateGenderFailed(String msg) {
                if (userView != null) {
                    userView.updateGenderFail(msg);
                }
            }

            @Override
            public void onUpdateGenderSuccess(String gender) {
                if (userView != null) {
                    userView.updateGenderSuccess(gender);
                }
            }
        });
    }

    @Override
    public void checkPassword(String pwd) {
        userBusiness.checkPassword(pwd, new CheckPwdCallback() {

            @Override
            public void onCheckPwdFailed(String msg) {
                if (iShowBindPhoneView != null) {
                    iShowBindPhoneView.checkPasswordFail(msg);
                }
            }

            @Override
            public void onCheckPwdSuccess() {
                if (iShowBindPhoneView != null) {
                    iShowBindPhoneView.checkPasswordSuccess();
                }
            }
        });
    }


    @Override
    public void sumitFeedback(String suggestion) {
        FeedBackEntity feedBackEntity = new FeedBackEntity();
        feedBackEntity.setChannel(HttpUtil.CHANNEL);
        feedBackEntity.setPlatform(HttpUtil.PLATFORM);
        feedBackEntity.setVersion(AppUtil.getVersionName(context));
        feedBackEntity.setSuggestion(suggestion);
        String json = praseFeedbackRequestToJson(feedBackEntity);
        if (TextUtils.isEmpty(json)) {
            if (iShowFeedbackView != null) {
                iShowFeedbackView.showFeedbackFail("数据出错了");
            }
            return;
        }
        userBusiness.sumitFeedback(json, new FeedbackCallback() {

            @Override
            public void onFeedbackFailed(String msg) {
                if (iShowFeedbackView != null) {
                    iShowFeedbackView.showFeedbackFail(msg);
                }
            }

            @Override
            public void onFeedbacksuccess() {
                if (iShowFeedbackView != null) {
                    iShowFeedbackView.showFeedbackSuccess();
                }
            }
        });
    }

    @Override
    public void bindPhone(String phone, String sms) {
        userBusiness.bindPhone(phone, sms, new BindPhoneCallback() {

            @Override
            public void onBindFailed(String msg) {
                if (iShowBindPhoneView != null) {
                    iShowBindPhoneView.showBindPhoneFail(msg);
                }
            }

            @Override
            public void onBinduccess() {
                if (iShowBindPhoneView != null) {
                    iShowBindPhoneView.showBindPhoneSuccess();
                }
            }
        });
    }

    @Override
    public void sendSms(String phone) {
        userBusiness.sendSms(phone, new SendsmsCallback() {

            @Override
            public void onSendFailed(String msg) {
                if (iShowBindPhoneView != null) {
                    iShowBindPhoneView.showSendsmFailedView(msg);
                }
            }

            @Override
            public void onSendsuccess() {
                if (iShowBindPhoneView != null) {
                    iShowBindPhoneView.showSendsms();
                }
            }

        });
    }

    @Override
    public void updatePassword(String oldpwd, String newpwd) {
        userBusiness.updatePassword(oldpwd, newpwd, new UpdatePwdCallback() {

            @Override
            public void onUpdatePwdFailed(String msg) {
                if (iShowUpdatePwdView != null) {
                    iShowUpdatePwdView.showUpdatePwdFail(msg);
                }
            }

            @Override
            public void onUpdatePwdSuccess() {
                if (iShowUpdatePwdView != null) {
                    iShowUpdatePwdView.showUpdatePwdSuccess();
                }
            }

        });
    }


    private String praseFeedbackRequestToJson(FeedBackEntity feedBackEntity) {
        Gson gson = new Gson();
        String result = gson.toJson(feedBackEntity);
        LogUtil.d("yongyuan.w", "praseFeedbackRequestToJson--" + result);
        return result;
    }
}
