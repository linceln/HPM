package com.olsplus.balancemall.app.login;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.liuguangqiang.permissionhelper.PermissionHelper;
import com.olsplus.balancemall.BuildConfig;
import com.olsplus.balancemall.R;
import com.olsplus.balancemall.app.home.HomeActivity;
import com.olsplus.balancemall.app.login.bean.LoginResultEntity;
import com.olsplus.balancemall.app.login.request.LoginRequestImpl;
import com.olsplus.balancemall.app.login.view.ILoginView;
import com.olsplus.balancemall.app.province.BuildCityActivity;
import com.olsplus.balancemall.core.app.BaseFragment;
import com.olsplus.balancemall.core.app.MainActivity;
import com.olsplus.balancemall.core.util.SPUtil;
import com.olsplus.balancemall.core.util.ToastUtil;
import com.olsplus.balancemall.core.util.UIUtil;
import com.umeng.analytics.MobclickAgent;

import static com.olsplus.balancemall.component.dialog.LoadingDialogManager.showLoading;
import static com.olsplus.balancemall.component.dialog.LoadingDialogManager.dismissLoading;


public class LoginActivity extends MainActivity implements ILoginView, TextWatcher {

    private EditText userNameEt;
    private EditText passwordEt;
    private TextView registerTv;
    private TextView forgetTv;
    private Button landBtn;

    private LoginRequestImpl loginRequest;


    @Override
    protected BaseFragment getFirstFragment() {
        return null;
    }

    @Override
    protected int getContentViewId() {
        return R.layout.login_login_activity;
    }

    @Override
    protected int getFragmentContentId() {
        return 0;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        boolean isFirst = (Boolean) SPUtil.get(this, SPUtil.IS_FRIST, true);
        if (!isFirst) {
            goHome();
            return;
        }
        initView();
        loginRequest = new LoginRequestImpl(this);
        loginRequest.setLoginView(this);
        PermissionHelper.getInstance().requestPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
    }

    private void initView() {
        userNameEt = ((EditText) findViewById(R.id.username_editText));
        passwordEt = ((EditText) findViewById(R.id.password_editText));

        if (BuildConfig.DEBUG) {
            userNameEt.setText("15985846810");
            passwordEt.setText("123456");
        }

        UIUtil.setClearText(userNameEt, findViewById(R.id.username_txt_clear));
        UIUtil.setClearText(passwordEt, findViewById(R.id.password_txt_clear));
        userNameEt.addTextChangedListener(this);
        passwordEt.addTextChangedListener(this);
        registerTv = (TextView) findViewById(R.id.register);
        forgetTv = (TextView) findViewById(R.id.forget_password);
        landBtn = ((Button) findViewById(R.id.userland_button_land));
        registerTv.setOnClickListener(this);
        forgetTv.setOnClickListener(this);
        landBtn.setEnabled(true);
        landBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.userland_button_land:
                final String accout = userNameEt.getText().toString();
                final String password = passwordEt.getText().toString();
                if (accout.isEmpty()) {
                    ToastUtil.showShort(this, R.string.module_login_username_isempty);
                    break;
                }
                if (password.isEmpty()) {
                    ToastUtil.showShort(this, R.string.module_login_password_isempty);
                    break;
                }
                showLoading(this, getString(R.string.loading_login));
                loginRequest.login(accout, password);
                break;
            case R.id.register:
//                Intent registerIntent = new Intent(this, RegisterActivity.class);
                Intent registerIntent = new Intent(this, RegisterMobileActivity.class);
                startActivity(registerIntent);
                break;
            case R.id.forget_password:
                Intent forgetPwdIntent = new Intent(this, ForgetPasswordActivity.class);
                startActivity(forgetPwdIntent);
                break;

        }
    }


    @Override
    public void showLoginError(String msg) {
        dismissLoading();
        ToastUtil.showShort(this, msg);
    }

    @Override
    public void onLoginSuccess(LoginResultEntity data) {

        dismissLoading();
        SPUtil.put(this, SPUtil.IS_FRIST, false);
//        ToastUtil.showShort(this, R.string.upomp_lthj_save_account);
        if (data != null) {
            String building_id = data.getBuilding_id();

//        String building_name = data.getBuilding_name();
//        String cityId = (String)SPUtil.get(this,SPUtil.CITY_ID,"");
            if (building_id.equals("-1")) {
                goProvinceSwitch();
            } else {
                goHome();
            }

            // 友盟账号统计
            MobclickAgent.onProfileSignIn(data.getUid());
        }
    }

    private void goProvinceSwitch() {
//        Intent intent = new Intent(this, ProvinceSwitchActivity.class);
//        startActivity(intent);
//        finish();
        Intent intent = new Intent(this, BuildCityActivity.class);
        intent.putExtra("city_id", "1");
        intent.putExtra("city_name", "厦门");
        intent.putExtra("from", "");
        startActivity(intent);
    }

    private void goHome() {
        Intent intent = new Intent(this, HomeActivity.class);
        intent.putExtra("dispatch_url", "xb://home");
        startActivity(intent);
        finish();
    }


    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if (!TextUtils.isEmpty(UIUtil.getText(userNameEt)) && !TextUtils.isEmpty(UIUtil.getText(passwordEt))) {
            landBtn.setEnabled(true);
        } else {
            landBtn.setEnabled(false);
        }
    }
}
