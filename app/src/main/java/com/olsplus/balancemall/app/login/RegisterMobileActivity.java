package com.olsplus.balancemall.app.login;

import android.content.Intent;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.olsplus.balancemall.R;
import com.olsplus.balancemall.app.login.bean.RegisterBean;
import com.olsplus.balancemall.app.web.WebActivity;
import com.olsplus.balancemall.core.app.BaseCompatActivity;
import com.olsplus.balancemall.core.util.RegexUtil;
import com.olsplus.balancemall.core.util.SnackbarUtil;
import com.olsplus.balancemall.core.util.StrConst;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

public class RegisterMobileActivity extends BaseCompatActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    private EditText etMobile;
    private Button btnNext;
    private CheckBox checkBoxClause;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_register_mobile;
    }

    @Override
    protected void setupWindowAnimations() {
    }

    @Override
    protected void initUI() {

        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }

        setTitle("注册");

        checkBoxClause = (CheckBox) findViewById(R.id.checkBoxClause);
        checkBoxClause.setOnCheckedChangeListener(this);
        btnNext = (Button) findViewById(R.id.btnNext);
        btnNext.setOnClickListener(this);
        TextView tvClause = (TextView) findViewById(R.id.tvClause);
        tvClause.setOnClickListener(this);
        etMobile = (EditText) findViewById(R.id.etMobile);
        etMobile.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                // 输入为空判断
                if (TextUtils.isEmpty(s.toString())) {
                    btnNext.setEnabled(false);
                    return;
                }

                // 电话号码判断
                if (RegexUtil.isMobileNum(s.toString())) {
                    btnNext.setEnabled(true);
                } else {
                    btnNext.setEnabled(false);
                    if (s.toString().length() == 11) {
                        Toast.makeText(RegisterMobileActivity.this, "请输入正确的手机号码", Toast.LENGTH_SHORT).show();
                    }
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
            case R.id.tvClause:// 条款
                Intent intent = new Intent(this, WebActivity.class);
                intent.putExtra("url", "https://www.olsplus.com/agreement.html");
                intent.putExtra("title", "使用条款和隐私政策");
                startActivity(intent);
                break;
            case R.id.btnNext:// 下一步
                next();
                break;
        }
    }

    private void next() {
        if (btnNext.isEnabled()) {
            if (!checkBoxClause.isChecked()) {
                SnackbarUtil.showShort(btnNext, "请勾选使用条款和隐私协议");
                return;
            }

            String mobile = etMobile.getText().toString().trim();

            RegisterBean bean = new RegisterBean();
            bean.setMobile(mobile);

            Intent intent = new Intent(this, RegisterVerifActivity.class);
            intent.putExtra(StrConst.extra.register, bean);
            startActivity(intent);
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked && RegexUtil.isMobileNum(etMobile.getText().toString().trim())) {
            btnNext.setEnabled(true);
        } else {
            btnNext.setEnabled(false);
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
