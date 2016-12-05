package com.olsplus.balancemall.app.mystore.view;


import com.olsplus.balancemall.core.util.BaseView;

public interface IUserView extends BaseView{

    void updateAvatarFail(String msg);

    void updateAvatarSuccess(String avater);

    void updateGenderFail(String msg);

    void updateGenderSuccess(String gender);

    void updatePasswordFail(String msg);

    void updatePasswordSuccess();

}
