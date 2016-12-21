package com.olsplus.balancemall.app.mine.config;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.olsplus.balancemall.BuildConfig;
import com.olsplus.balancemall.R;
import com.olsplus.balancemall.app.web.WebActivity;
import com.olsplus.balancemall.core.app.BaseFragment;
import com.olsplus.balancemall.core.app.MainActivity;
import com.olsplus.balancemall.core.util.AppUtil;

public class AboutNewActivity extends MainActivity {

    private TextView versionTv;
    private LinearLayout commentLl;
    private LinearLayout privateLl;
    private LinearLayout feedbackLl;

    @Override
    protected BaseFragment getFirstFragment() {
        return null;
    }

    @Override
    protected int getTitleViewId() {
        return R.layout.action_bar_view;
    }

    @Override
    protected int getContentViewId() {
        return R.layout.mystore_about_new;
    }

    @Override
    protected int getFragmentContentId() {
        return 0;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setActionBar();
        mTitleName.setText(getString(R.string.about));
        initView();
    }

    private void initView() {
        versionTv = (TextView) findViewById(R.id.aboutnew_version);
        commentLl = (LinearLayout) findViewById(R.id.aboutnew_comment);
        privateLl = (LinearLayout) findViewById(R.id.aboutnew_private);
        feedbackLl = (LinearLayout) findViewById(R.id.aboutnew_feedback);
        commentLl.setOnClickListener(this);
        privateLl.setOnClickListener(this);
        feedbackLl.setOnClickListener(this);
        if (BuildConfig.DEBUG) {
            versionTv.setText(String.valueOf(AppUtil.getVersionName(this) + " Debug"));
        } else {
            versionTv.setText(String.valueOf(AppUtil.getVersionName(this)));
        }
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.aboutnew_feedback:
                intent = new Intent(this, FeedBackActivity.class);
                startActivity(intent);
                break;
            case R.id.aboutnew_private:
                intent = new Intent(this, WebActivity.class);
                intent.putExtra("url", "https://www.olsplus.com/agreement.html");
                intent.putExtra("title", "使用条款和隐私政策");
                startActivity(intent);
                break;
        }
    }
}
