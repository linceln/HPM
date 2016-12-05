package com.olsplus.balancemall.app.login;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.olsplus.balancemall.R;
import com.olsplus.balancemall.app.login.request.LoginRequestImpl;
import com.olsplus.balancemall.app.login.view.IForgetPwdView;
import com.olsplus.balancemall.core.app.BaseFragment;
import com.olsplus.balancemall.core.app.MainActivity;
import com.olsplus.balancemall.core.util.ToastUtil;
import com.olsplus.balancemall.core.util.UIUtil;


public class ForgetPasswordActivity extends MainActivity implements IForgetPwdView{
    private LinearLayout mobileLayout;
    private RelativeLayout mobileLinear;
    private EditText mobileEt;
    private LinearLayout codeLinear;
    private EditText smsCodeEt;
    private TextView smsAgainTv;
    private Button codeNextBtn;
    private RelativeLayout passwordLinear;
    private EditText newPasswordEt;
    private EditText surePasswordEt;
    private Button sureBtn;

    private LoginRequestImpl loginRequestImpl;

    private int time = 60;

    Runnable timeRunnable = new Runnable() {
        @Override
        public void run() {
            time--;
            smsAgainTv.setText(time+"s 重新获取");
            if(time == 0){
                smsAgainTv.setText("获取验证码");
//                smsAgainTv.setTextColor(Color.parseColor("#333333"));
                smsAgainTv.setOnClickListener(ForgetPasswordActivity.this);
                return;
            }
            smsAgainTv.postDelayed(this, 1000);
        }
    };

    @Override
    protected BaseFragment getFirstFragment() {
        return null;
    }

    @Override
    protected int getFragmentContentId() {
        return 0;
    }

    @Override
    protected int getTitleViewId() {
        return  R.layout.action_bar_view;
    }

    @Override
    protected int getContentViewId() {
        return  R.layout.login_forget_pwd;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setActionBar();
        mTitleName.setText("忘记密码");
        mLeftOperationImageView.setBackgroundResource(R.drawable.back_normal);
        initView();
        loginRequestImpl = new LoginRequestImpl(this);
        loginRequestImpl.setiForgetPwdView(this);
//        initData();
    }

    private void initView(){
        mobileLayout = (LinearLayout)findViewById(R.id.mobile_layout);
        mobileLinear = (RelativeLayout)findViewById(R.id.usermobile);
        mobileEt = (EditText)findViewById(R.id.mobile_editText);
        mobileEt.addTextChangedListener(codeWatcher);

        codeLinear = (LinearLayout)findViewById(R.id.pwd_linear_code);
        smsCodeEt = (EditText)findViewById(R.id.pwd_sms_code);
        smsAgainTv = (TextView)findViewById(R.id.pwd_sms_code_again);
        codeNextBtn = (Button)findViewById(R.id.pwd_userland_button_code_next);
        smsCodeEt.addTextChangedListener(codeWatcher);
        codeNextBtn.setOnClickListener(this);
        smsAgainTv.setOnClickListener(this);

        passwordLinear = (RelativeLayout)findViewById(R.id.register_linear_password);
        newPasswordEt = (EditText)findViewById(R.id.new_password_editText);
        surePasswordEt = (EditText)findViewById(R.id.sure_password_editText);
        UIUtil.setClearText(newPasswordEt, findViewById(R.id.new_password_txt_clear));
        UIUtil.setClearText(surePasswordEt,findViewById(R.id.sure_password_txt_clear));
        newPasswordEt.addTextChangedListener(passwordWatcher);
        surePasswordEt.addTextChangedListener(passwordWatcher);
        sureBtn = (Button)findViewById(R.id.userland_button_register);
        sureBtn.setOnClickListener(this);
    }

//    private void  initData(){
//        if(loginRequestImpl!=null){
//            loginRequestImpl.sendForgetPwdSms(mobileEditString());
//        }
//        startCountDown();
//    }

    /**
     * 开始验证码倒计时
     */
    private void  startCountDown(){
        this.time = 60;
        this.smsAgainTv.postDelayed(this.timeRunnable, 1000L);
        this.smsAgainTv.setText(time+"s 重新获取");
        this.smsAgainTv.setOnClickListener(null);
    }

    private String mobileEditString(){
        return mobileEt.getText().toString().trim();
    }

    /**
     * 跳转到输入密码界面
     */
    private void goNextRegister(){
        this.mobileLayout.setVisibility(View.GONE);
        this.passwordLinear.setVisibility(View.VISIBLE);
    }


    /**
     * 验证码输入监听
     */
    private TextWatcher codeWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            String s = smsCodeEt.getText().toString().trim();
            if(!TextUtils.isEmpty(s) && !TextUtils.isEmpty(mobileEditString()) ){
                codeNextBtn.setEnabled(true);
            }else{
                codeNextBtn.setEnabled(false);
            }
        }
    };



    /**
     * 密码输入监听
     */
    private TextWatcher passwordWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            if(!TextUtils.isEmpty(UIUtil.getText(newPasswordEt))&& !TextUtils.isEmpty(UIUtil.getText(surePasswordEt))){
                sureBtn.setEnabled(true);
            }else{
                sureBtn.setEnabled(false);
            }
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.pwd_userland_button_code_next:
                if(codeNextBtn.isEnabled()){
                    smsAgainTv.removeCallbacks(timeRunnable);
                    smsAgainTv.setText("再次发送验证码");
                    smsAgainTv.setTextColor(Color.parseColor("#333333"));
                    smsAgainTv.setOnClickListener(this);
                    goNextRegister();
                };
                break;
            case R.id.userland_button_register:
                final String newPassword = newPasswordEt.getText().toString();
                final String surePassword = surePasswordEt.getText().toString();
                if (newPassword.isEmpty()||surePassword.isEmpty()) {
                    ToastUtil.showShort(this,R.string.module_login_password_isempty);
                    break;
                }
                String phone = mobileEditString();
                if(loginRequestImpl!=null){
                    loginRequestImpl.forgetPwd(phone,newPassword);
                }

                break;

            case R.id.pwd_sms_code_again:
                if(loginRequestImpl!=null){
                    loginRequestImpl.sendForgetPwdSms(mobileEditString());
                }
                startCountDown();
                break;
        }
    }

    @Override
    public void showSendsmsFailedView(String msg) {
        ToastUtil.showShort(this,"验证码发送失败");
        smsAgainTv.removeCallbacks(timeRunnable);
        smsAgainTv.setText("获取验证码");
        smsAgainTv.setOnClickListener(ForgetPasswordActivity.this);

    }

    @Override
    public void showSendsmsSuccess() {

    }

    @Override
    public void showForgetPwdFailedView(String msg) {
        ToastUtil.showShort(this,msg);
    }

    @Override
    public void showForgetPwdSuccess() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
