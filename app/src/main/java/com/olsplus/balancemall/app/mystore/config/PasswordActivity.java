package com.olsplus.balancemall.app.mystore.config;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.olsplus.balancemall.R;
import com.olsplus.balancemall.app.mystore.request.UserImpl;
import com.olsplus.balancemall.app.mystore.view.IShowUpdatePwdView;
import com.olsplus.balancemall.core.app.BaseFragment;
import com.olsplus.balancemall.core.app.MainActivity;
import com.olsplus.balancemall.core.util.ToastUtil;
import com.olsplus.balancemall.core.util.UIUtil;


public class PasswordActivity extends MainActivity implements TextWatcher,IShowUpdatePwdView {

    private EditText oldpasswordEt;
    private EditText passwordEt1;
    private EditText passwordEt2;
    private Button nextBtn;
    private UserImpl userImpl;

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
        return R.layout.mystore_password_activity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setActionBar();
        mTitleName.setText("修改密码");
        oldpasswordEt = ((EditText) findViewById(R.id.old_password_editText));
        UIUtil.setClearText(oldpasswordEt, findViewById(R.id.old_password_txt_clear_1));

        this.passwordEt1 = ((EditText) findViewById(R.id.new_password_editText));
        this.passwordEt2 = ((EditText) findViewById(R.id.new_password_editText_2));
        UIUtil.setClearText(passwordEt1, findViewById(R.id.password_txt_clear_1));
        UIUtil.setClearText(passwordEt2, findViewById(R.id.password_txt_clear_2));

        oldpasswordEt.addTextChangedListener(this);
        passwordEt1.addTextChangedListener(this);
        passwordEt2.addTextChangedListener(this);
        nextBtn = ((Button) findViewById(R.id.password_btn_next));
        nextBtn.setOnClickListener(this);
        userImpl = new UserImpl(this);
        userImpl.setiShowUpdatePwdView(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.password_btn_next:
                String oldPassword = oldpasswordEt.getText().toString();
                String password1 = passwordEt1.getText().toString();
                String password2 = passwordEt1.getText().toString();
                if (oldPassword.isEmpty()) {
                    ToastUtil.showShort(this,"旧密码不能为空");
                    break;
                }
                if (password1.isEmpty()) {
                    ToastUtil.showShort(this,R.string.module_login_password_isempty);
                    break;
                }

                if (password2.isEmpty()) {
                    ToastUtil.showShort(this,R.string.module_login_password_isempty);
                    break;
                }
                if (!password2.equals(password1)) {
                    ToastUtil.showShort(this,"密码不一样");
                    break;
                }
                if(userImpl!=null){
                    userImpl.updatePassword(oldPassword,password1);
                }
                break;
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if(!TextUtils.isEmpty(UIUtil.getText(passwordEt1))&& !TextUtils.isEmpty(UIUtil.getText(passwordEt2))&& !TextUtils.isEmpty(UIUtil.getText(oldpasswordEt))){
            nextBtn.setEnabled(true);
        }else{
            nextBtn.setEnabled(false);
        }
    }

    @Override
    public void showUpdatePwdFail(String msg) {
        ToastUtil.showShort(this,msg);
    }

    @Override
    public void showUpdatePwdSuccess() {
        ToastUtil.showShort(this,"修改成功");
        finish();
    }
}
