package com.olsplus.balancemall.app.login;

import android.content.Intent;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Menu;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.jakewharton.rxbinding.view.RxView;
import com.olsplus.balancemall.R;
import com.olsplus.balancemall.app.login.bean.RegisterBean;
import com.olsplus.balancemall.app.login.bussiness.LoginBusiness;
import com.olsplus.balancemall.app.login.request.ILoginRequest;
import com.olsplus.balancemall.app.province.BuildCityActivity;
import com.olsplus.balancemall.core.app.BaseCompatActivity;
import com.olsplus.balancemall.core.util.StrConst;
import com.olsplus.balancemall.core.util.ToastUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.concurrent.TimeUnit;

import rx.functions.Action1;

public class RegisterPwdActivity extends BaseCompatActivity implements Action1 {


    private EditText etNickname;
    private EditText etPsw;
    private RegisterBean bean;
    private Button btnNext;
    private LoginBusiness loginBusiness;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_register_pwd;
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
        etNickname = (EditText) findViewById(R.id.etNickname);
        etNickname.addTextChangedListener(new DefaultTextWatcher());
        etPsw = (EditText) findViewById(R.id.etPsw);
        etPsw.addTextChangedListener(new DefaultTextWatcher());
        btnNext = (Button) findViewById(R.id.btnNext);
        RxView.clicks(btnNext)
                .throttleWithTimeout(1, TimeUnit.SECONDS)
                .subscribe(this);
    }

    @Override
    protected void initData() {
    }

    // 注册
    @Override
    public void call(Object o) {
        if (loginBusiness == null) {
            loginBusiness = new LoginBusiness(this);
        }
        loginBusiness.register(bean.getMobile(), bean.getNickname(), bean.getPassword(), new ILoginRequest.RegisterCallback() {
            @Override
            public void onRegisterSuccess() {
                ToastUtil.showShort(RegisterPwdActivity.this, R.string.upomp_lthj_quick_bind_prompt);
                selectMall();
            }

            @Override
            public void onRegisterFailed(String msg) {
                Toast.makeText(RegisterPwdActivity.this, msg, Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 跳转选择写字楼
     */
    private void selectMall() {

        Intent intent = new Intent(this, BuildCityActivity.class);
        intent.putExtra("city_id", "1");
        intent.putExtra("city_name", "厦门");
        intent.putExtra("from", "");
        startActivity(intent);
        // 关闭所有注册页面
        EventBus.getDefault().post("finish");
    }

    private class DefaultTextWatcher implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            String nickname = etNickname.getText().toString().trim();
            String psw = etPsw.getText().toString().trim();
            if (TextUtils.isEmpty(nickname) || TextUtils.isEmpty(psw)) {
                btnNext.setEnabled(false);
                return;
            }

            if (psw.length() >= 6) {
                btnNext.setEnabled(true);
                bean.setNickname(nickname);
                bean.setPassword(psw);
            } else {
                btnNext.setEnabled(false);
            }
        }
    }

    @Subscribe
    public void onRegisterFinished(String str) {
        finish();
    }

    @Override
    protected void onDestroy() {
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
