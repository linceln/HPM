package com.olsplus.balancemall.app.login;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.olsplus.balancemall.R;
import com.olsplus.balancemall.app.home.HomeActivity;
import com.olsplus.balancemall.app.login.request.LoginRequestImpl;
import com.olsplus.balancemall.app.login.view.IRegisterView;
import com.olsplus.balancemall.app.province.BuildCityActivity;
import com.olsplus.balancemall.app.web.WebActivity;
import com.olsplus.balancemall.core.app.BaseFragment;
import com.olsplus.balancemall.core.app.MainActivity;
import com.olsplus.balancemall.core.util.RegexUtil;
import com.olsplus.balancemall.core.util.ToastUtil;
import com.olsplus.balancemall.core.util.UIUtil;


public class RegisterActivity extends MainActivity implements IRegisterView {

    private LinearLayout registerLinear;
    private EditText mobileEt;
    private Button clearBtn;
    private CheckBox checkBox;
    private Button nextBtn;
    private LinearLayout codeLinear;
    private EditText smsCodeEt;
    private TextView smsAgainTv;
    private Button codeNextBtn;
    private RelativeLayout passwordLinear;
    private EditText newPasswordEt;
    private EditText nameEt;
    private Button registerBtn;

    private LoginRequestImpl loginRequest;
    private int time = 60;

    Runnable timeRunnable = new Runnable() {
        @Override
        public void run() {
            time--;
            smsAgainTv.setText(time + "s 重新获取");
            if (time == 0) {
                smsAgainTv.setText("获取验证码");
//                smsAgainTv.setTextColor(Color.parseColor("#333333"));
                smsAgainTv.setOnClickListener(RegisterActivity.this);
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
        return R.layout.action_bar_view;
    }

    @Override
    protected int getContentViewId() {
        return R.layout.login_register_activity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setActionBar();
        mTitleName.setText("注册");
        mLeftOperationImageView.setBackgroundResource(R.drawable.back_normal);
        initView();
        loginRequest = new LoginRequestImpl(this);
        loginRequest.setRegisterView(this);
    }

    private void initView() {
        registerLinear = (LinearLayout) findViewById(R.id.register_linear);
        mobileEt = (EditText) findViewById(R.id.mobile_editText);
        clearBtn = (Button) findViewById(R.id.mobile_txt_clear);
        UIUtil.setClearText(mobileEt, clearBtn);
        checkBox = (CheckBox) findViewById(R.id.register_checkBox);
        nextBtn = (Button) findViewById(R.id.userland_button_next);
        mobileEt.addTextChangedListener(phoneWatcher);
        TextView privateTv = (TextView) findViewById(R.id.register_private);
        privateTv.setOnClickListener(this);
        nextBtn.setOnClickListener(this);

        codeLinear = (LinearLayout) findViewById(R.id.register_linear_code);
        smsCodeEt = (EditText) findViewById(R.id.sms_code);
        smsAgainTv = (TextView) findViewById(R.id.sms_code_again);
        codeNextBtn = (Button) findViewById(R.id.userland_button_code_next);
        smsCodeEt.addTextChangedListener(codeWatcher);
        codeNextBtn.setOnClickListener(this);

        passwordLinear = (RelativeLayout) findViewById(R.id.register_linear_password);
        newPasswordEt = (EditText) findViewById(R.id.new_password_editText);
        nameEt = (EditText) findViewById(R.id.name_editText);
        UIUtil.setClearText(newPasswordEt, findViewById(R.id.new_password_txt_clear));
        UIUtil.setClearText(nameEt, findViewById(R.id.name_txt_clear));
        newPasswordEt.addTextChangedListener(passwordWatcher);
        nameEt.addTextChangedListener(passwordWatcher);
        registerBtn = (Button) findViewById(R.id.userland_button_register);
        registerBtn.setOnClickListener(this);
    }

    private String mobileEditString() {
        return mobileEt.getText().toString().trim();
    }

    /**
     * 开始验证码倒计时
     */
    private void startCountDown() {
        this.time = 60;
        this.smsAgainTv.postDelayed(this.timeRunnable, 1000L);
        this.smsAgainTv.setText(time + "s 重新获取");
        this.smsAgainTv.setOnClickListener(null);
    }

    /**
     * 跳转到验证码界面
     */
    private void goNextChanged() {
        registerLinear.setVisibility(View.GONE);
        nextBtn.setVisibility(View.GONE);
        codeLinear.setVisibility(View.VISIBLE);
        codeNextBtn.setVisibility(View.VISIBLE);
        this.passwordLinear.setVisibility(View.GONE);
        if (loginRequest != null) {
            loginRequest.sendSms(mobileEditString());
        }
        startCountDown();
    }

    /**
     * 跳转到输入密码界面
     */
    private void goNextRegister() {
        this.registerLinear.setVisibility(View.GONE);
        this.nextBtn.setVisibility(View.GONE);
        this.codeLinear.setVisibility(View.GONE);
        this.codeNextBtn.setVisibility(View.GONE);
        this.passwordLinear.setVisibility(View.VISIBLE);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.register_private:
                Intent intent = new Intent(this, WebActivity.class);
                intent.putExtra("url", "https://www.olsplus.com/agreement.html");
                intent.putExtra("title", "使用条款和隐私政策");
                startActivity(intent);
                break;
            case R.id.userland_button_next:
                if (nextBtn.isEnabled()) {
                    if (!checkBox.isChecked()) {
                        ToastUtil.showShort(this, "请勾选服务协议");
                        return;
                    }
                    goNextChanged();
                }

                break;
            case R.id.userland_button_code_next:
                if (codeNextBtn.isEnabled()) {
                    smsAgainTv.removeCallbacks(timeRunnable);
                    smsAgainTv.setText("再次发送验证码");
                    smsAgainTv.setTextColor(Color.parseColor("#333333"));
                    smsAgainTv.setOnClickListener(this);
                    goNextRegister();
                }
                break;

            case R.id.userland_button_register:
                final String newPassword = newPasswordEt.getText().toString();
                final String name = nameEt.getText().toString();
                if (newPassword.isEmpty() || name.isEmpty()) {
                    ToastUtil.showShort(this, "昵称或者密码不能为空");
                    break;
                }
                String phone = mobileEditString();
                loginRequest.register(phone, name, newPassword);

                break;

            case R.id.sms_code_again:
                if (loginRequest != null) {
                    loginRequest.sendSms(mobileEditString());
                    startCountDown();
                }
                break;
        }
    }


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
            String s = mobileEditString();

            if (TextUtils.isEmpty(s)) {
                nextBtn.setEnabled(false);
                nextBtn.setTextColor(getResources().getColor(R.color.white));
                return;
            }

            // 电话号码判断
            if (RegexUtil.isMobileNum(s)) {
                nextBtn.setEnabled(true);
                nextBtn.setTextColor(getResources().getColor(R.color.white));
            } else {
                nextBtn.setEnabled(false);
                nextBtn.setTextColor(getResources().getColor(R.color.white));
                if (s.length() == 11) {
                    Toast.makeText(RegisterActivity.this, "请输入正确的手机号码", Toast.LENGTH_SHORT).show();
                }
            }
        }
    };

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
            if (!TextUtils.isEmpty(s)) {
                codeNextBtn.setEnabled(true);
            } else {
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
            if (!TextUtils.isEmpty(UIUtil.getText(newPasswordEt)) && !TextUtils.isEmpty(UIUtil.getText(nameEt))) {
                registerBtn.setEnabled(true);
            } else {
                registerBtn.setEnabled(false);
            }
        }
    };

    @Override
    public void showRegisterError(String msg) {
        ToastUtil.showShort(this, msg);
    }

    @Override
    public void onRegisterSuccess() {
        ToastUtil.showShort(this, R.string.upomp_lthj_quick_bind_prompt);
//        goHome();
        goProvinceSwitch();
    }

    @Override
    public void showSendsmsFailedView(String msg) {
        ToastUtil.showShort(this, msg);
    }

    @Override
    public void showSendsmsSuccess() {

    }

    private void goHome() {
        Intent intent = new Intent(this, HomeActivity.class);
        intent.putExtra("dispatch_url", "xb://home");
        startActivity(intent);
        finish();
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
        finish();
    }
}
