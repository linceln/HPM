package com.olsplus.balancemall.app.mystore.request;


import com.olsplus.balancemall.app.mystore.bean.MySeesionEntity;

public interface IMyStoreDataRequest {

    interface  GetMySeesionCallback{

        void onGetMySeesionFailed(String msg);

        void onGetMySeesionSuccess(MySeesionEntity data);
    }

    interface UpoadAvaterCallback {

        void onUploadFailed(String msg);

        void onUploadSuccess(String avater);
    }

    interface UpdateGenderCallback {

        void onUpdateGenderFailed(String msg);

        void onUpdateGenderSuccess(String gender);
    }

    interface UpdatePwdCallback {

        void onUpdatePwdFailed(String msg);

        void onUpdatePwdSuccess();
    }

    interface CheckPwdCallback {

        void onCheckPwdFailed(String msg);

        void onCheckPwdSuccess();
    }

    interface BindPhoneCallback {

        void onBindFailed(String msg);

        void onBinduccess();
    }

    interface SendsmsCallback {

        void onSendFailed(String msg);

        void onSendsuccess();
    }

    interface FeedbackCallback {

        void onFeedbackFailed(String msg);

        void onFeedbacksuccess();
    }


    void getMySeesionData();

    void upLoadAvatar(String picPath);

    void updateGender(String gender);

    void checkPassword(String pwd);

    void sumitFeedback(String suggestion);

    void bindPhone(String phone,String sms);

    void sendSms(String phone);

    void updatePassword(String oldpwd,String newpwd);

}
