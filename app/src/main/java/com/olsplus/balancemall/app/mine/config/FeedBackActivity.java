package com.olsplus.balancemall.app.mine.config;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.olsplus.balancemall.R;
import com.olsplus.balancemall.app.mine.request.UserImpl;
import com.olsplus.balancemall.app.mine.view.IShowFeedbackView;
import com.olsplus.balancemall.core.app.BaseFragment;
import com.olsplus.balancemall.core.app.MainActivity;
import com.olsplus.balancemall.core.util.ToastUtil;
import com.olsplus.balancemall.core.util.UIUtil;


public class FeedBackActivity extends MainActivity implements TextWatcher,IShowFeedbackView {

    private EditText feedbackEt;
    private Button commitBtn;
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
        return R.layout.mystore_feedback;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setActionBar();
        mTitleName.setText("意见反馈");
        feedbackEt = (EditText) findViewById(R.id.feedback_edit);
        commitBtn = (Button) findViewById(R.id.feedback_commit);
        feedbackEt.addTextChangedListener(this);
        commitBtn.setOnClickListener(this);
        userImpl = new UserImpl(this);
        userImpl.setiShowFeedbackView(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.feedback_commit:
                String feedback = UIUtil.getText(feedbackEt);
                if (TextUtils.isEmpty(feedback)) {
                    ToastUtil.showShort(this, "评论不能为空");
                    return;
                }
                int length = feedback.length();
                if (length > 200) {
                    ToastUtil.showShort(this, "字数不能超过200个字以上");
                    return;
                }
                if(userImpl!=null){
                    userImpl.sumitFeedback(feedback);
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
        if (!TextUtils.isEmpty(UIUtil.getText(feedbackEt))) {
            commitBtn.setEnabled(true);
        } else {
            commitBtn.setEnabled(false);
        }
    }

    @Override
    public void showFeedbackFail(String msg) {
        ToastUtil.showShort(this,msg);
    }

    @Override
    public void showFeedbackSuccess() {
        ToastUtil.showShort(this,"提交成功");
        finish();
    }
}
