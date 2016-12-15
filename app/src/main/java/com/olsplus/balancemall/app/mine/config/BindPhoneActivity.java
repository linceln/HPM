package com.olsplus.balancemall.app.mine.config;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.olsplus.balancemall.R;
import com.olsplus.balancemall.app.mine.request.UserImpl;
import com.olsplus.balancemall.app.mine.view.IShowBindPhoneView;
import com.olsplus.balancemall.core.app.BaseFragment;
import com.olsplus.balancemall.core.app.MainActivity;
import com.olsplus.balancemall.core.util.ToastUtil;
import com.olsplus.balancemall.core.util.UIUtil;


public class BindPhoneActivity extends MainActivity implements IShowBindPhoneView {

    private LinearLayout passwordLayout;
    private EditText pwdEt;
    private Button nextBtn;

    private LinearLayout phoneLayout;
    private EditText phoneEt;
    private EditText codeEt;
    private TextView sendCodeTv;
    private Button sureBtn;

    private UserImpl userImpl;

    private int time = 60;
    Runnable timeRunnable = new Runnable() {
        @Override
        public void run() {
            time--;
            sendCodeTv.setText(time+"s 重新获取");
            if(time == 0){
                sendCodeTv.setText("获取验证码");
                sendCodeTv.setTextColor(Color.parseColor("#333333"));
                sendCodeTv.setOnClickListener(BindPhoneActivity.this);
                return;
            }
            sendCodeTv.postDelayed(this, 1000);
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
    protected int getContentViewId() {
        return R.layout.mystore_bind_phone_activity;
    }

    @Override
    protected int getTitleViewId() {
        return R.layout.action_bar_view;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setActionBar();
        mTitleName.setText("绑定手机");
        initView();
    }

    private void initView() {
        passwordLayout = ((LinearLayout) findViewById(R.id.password_layout));
        phoneLayout = ((LinearLayout) findViewById(R.id.bind_phone_linear_code));
        pwdEt = (EditText) findViewById(R.id.password_editText);
        nextBtn = (Button) findViewById(R.id.bind_phone_button_next);
        UIUtil.setClearText(pwdEt,findViewById(R.id.mobile_txt_clear));
        pwdEt.addTextChangedListener(passwordWatcher);

        phoneEt = (EditText) findViewById(R.id.phone_editText);
        UIUtil.setClearText(phoneEt,findViewById(R.id.bind_mobile_txt_clear));
        codeEt = (EditText) findViewById(R.id.sms_code);
        sendCodeTv = (TextView)findViewById(R.id.sms_code_again);
        sureBtn = (Button)findViewById(R.id.bind_phone_sure);
        phoneEt.addTextChangedListener(phoneWatcher);
        codeEt.addTextChangedListener(phoneWatcher);
        nextBtn.setOnClickListener(this);
        sendCodeTv.setOnClickListener(this);
        sureBtn.setOnClickListener(this);
        initData();
    }

    private void initData(){
        userImpl = new UserImpl(this);
        userImpl.setiShowBindPhoneView(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bind_phone_button_next:
                String pwd = pwdEt.getText().toString().trim();
                if(TextUtils.isEmpty(pwd)){
                    ToastUtil.showShort(this,"请输入密码");
                    return;
                }
                userImpl.checkPassword(pwd);
                break;
            case R.id.sms_code_again:
                if(userImpl!=null){
                    String phone = phoneEt.getText().toString();
                    if(TextUtils.isEmpty(phone)){
                        ToastUtil.showShort(this,"请输入手机号码");
                        return;
                    }
                    userImpl.sendSms(phone);
                    startCountDown();
                }
                break;

            case R.id.bind_phone_sure:
                String phone = phoneEt.getText().toString().trim();
                if(TextUtils.isEmpty(phone)){
                    ToastUtil.showShort(this,"请输入手机号码");
                    return;
                }
                String code = codeEt.getText().toString().trim();
                if(TextUtils.isEmpty(code)){
                    ToastUtil.showShort(this,"请输入验证码");
                    return;
                }
                userImpl.bindPhone(phone,code);
                break;
        }
    }

    /**
     * 开始验证码倒计时
     */
    private void  startCountDown(){
        this.time = 60;
        this.sendCodeTv.postDelayed(this.timeRunnable, 1000L);
        this.sendCodeTv.setText(time+"s 重新获取");
        this.sendCodeTv.setOnClickListener(null);
    }

    @Override
    public void checkPasswordFail(String msg) {
        ToastUtil.showShort(this,msg);
    }

    @Override
    public void checkPasswordSuccess() {
        passwordLayout.setVisibility(View.GONE);
        phoneLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void showBindPhoneFail(String msg) {
        ToastUtil.showShort(this,msg);
    }

    @Override
    public void showBindPhoneSuccess() {
        ToastUtil.showShort(this,"修改成功");
        finish();
    }

    @Override
    public void showSendsms() {
        ToastUtil.showShort(this,"验证码已发送");
    }

    @Override
    public void showSendsmFailedView(String msg) {
        ToastUtil.showShort(this,msg);
    }

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
            if(!TextUtils.isEmpty(UIUtil.getText(pwdEt))&& !TextUtils.isEmpty(UIUtil.getText(pwdEt))){
                nextBtn.setEnabled(true);
            }else{
                nextBtn.setEnabled(false);
            }
        }
    };


    /**
     * 手机号码输入监听
     */
    private TextWatcher phoneWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            String s = phoneEt.getText().toString().trim();
            String code = codeEt.getText().toString().trim();
            if(!TextUtils.isEmpty(s) && !TextUtils.isEmpty(code) ){
                sureBtn.setEnabled(true);
                sureBtn.setTextColor(getResources().getColor(R.color.white));
            }else{
                sureBtn.setEnabled(false);
                sureBtn.setTextColor(getResources().getColor(R.color.white));
            }
        }
    };
}
