package com.olsplus.balancemall.app.login;

import android.content.Intent;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.olsplus.balancemall.R;
import com.olsplus.balancemall.app.login.bean.RegisterBean;
import com.olsplus.balancemall.app.login.bussiness.LoginBusiness;
import com.olsplus.balancemall.app.login.request.ILoginRequest;
import com.olsplus.balancemall.core.app.BaseCompatActivity;
import com.olsplus.balancemall.core.bean.BaseResultEntity;
import com.olsplus.balancemall.core.http.RequestCallback;
import com.olsplus.balancemall.core.util.CountDown;
import com.olsplus.balancemall.core.util.StrConst;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import static com.olsplus.balancemall.component.dialog.LoadingDialogManager.showLoading;
import static com.olsplus.balancemall.component.dialog.LoadingDialogManager.dismissLoading;


import static com.olsplus.balancemall.core.util.StrConst.checkSms.REGISTER;

public class RegisterVerifActivity extends BaseCompatActivity implements View.OnClickListener {


    private EditText etVerif;
    private Button btnNext;
    private RegisterBean bean;
    private TextView tvGetVerif;
    private CountDown countDown;
    private LoginBusiness loginBusiness;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_register_verif;
    }

    @Override
    protected void setupWindowAnimations() {
    }

    @Override
    protected void getExtras() {
        bean = (RegisterBean) getIntent().getSerializableExtra(StrConst.extra.register);
    }

    @Override
    protected void initUI() {

        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }

        setTitle("注册");

        tvGetVerif = (TextView) findViewById(R.id.tvGetVerif);
        tvGetVerif.setOnClickListener(this);
        btnNext = (Button) findViewById(R.id.btnNext);
        btnNext.setOnClickListener(this);
        etVerif = (EditText) findViewById(R.id.etVerif);
        etVerif.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if (TextUtils.isEmpty(s.toString())) {
                    btnNext.setEnabled(false);
                    return;
                }


                if (s.toString().length() == 4) {
                    btnNext.setEnabled(true);
                } else {
                    btnNext.setEnabled(false);
                }
            }
        });
    }

    @Override
    protected void initData() {
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvGetVerif: // 倒计时
                countDown();
                break;
            case R.id.btnNext:
                if (btnNext.isEnabled()) {
                    String verificationCode = etVerif.getText().toString().trim();
                    checkSms(verificationCode);
                }
                break;
        }
    }

    private void checkSms(String verificationCode) {
        showLoading(this, getString(R.string.loading_check));
        if (loginBusiness == null) {
            loginBusiness = new LoginBusiness(this);
        }
        loginBusiness.checkSms(bean.getMobile(), verificationCode, REGISTER, new RequestCallback<BaseResultEntity>() {
            @Override
            public void onSuccess(BaseResultEntity baseResultEntity) {
                dismissLoading();
                Intent intent = new Intent(RegisterVerifActivity.this, RegisterPwdActivity.class);
                intent.putExtra(StrConst.extra.register, bean);
                startActivity(intent);
            }

            @Override
            public void onError(String msg) {
                dismissLoading();
                Toast.makeText(RegisterVerifActivity.this, msg, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void countDown() {
        if (countDown == null) {
            countDown = new CountDown();
        }
        countDown.setTextOnCounting("", "后可重新发送");
        countDown.setTextOnFinished("再次获取验证码");
        countDown.execute(tvGetVerif, 60, new CountDown.Callback() {
            @Override
            public void onStart() {// 发送短信验证码
                sendSms();
            }

            @Override
            public void onFinish() {

            }
        });
    }

    private void sendSms() {
        if (loginBusiness == null) {
            loginBusiness = new LoginBusiness(RegisterVerifActivity.this);
        }
        loginBusiness.sendSms(bean.getMobile(), new ILoginRequest.SendsmsCallback() {
            @Override
            public void onSendSuccess() {
                Toast.makeText(RegisterVerifActivity.this, "验证码发送成功", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSendFailed(String msg) {
                Toast.makeText(RegisterVerifActivity.this, msg, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Subscribe
    public void onRegisterFinished(String str) {

        finish();
    }

    @Override
    protected void onDestroy() {
        if (countDown != null) {
            countDown.cancel();
        }
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }
}
