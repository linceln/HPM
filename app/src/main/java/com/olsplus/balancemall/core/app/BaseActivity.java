package com.olsplus.balancemall.core.app;

import android.content.res.Resources;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.ViewStub;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.olsplus.balancemall.R;
import com.olsplus.balancemall.app.bottom.BottomNavigateFragment;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;
import com.umeng.analytics.MobclickAgent;

public abstract class BaseActivity extends RxAppCompatActivity implements View.OnClickListener {

    private int titleResId = -1;
    private View titleView = null;
    private View bodyView = null;
    private BottomNavigateFragment bottomFragment;


    protected ImageView mLeftOperationImageView;
    protected LinearLayout mLeftOperationLinear;
    protected TextView mProvinceName;
    protected RelativeLayout mRightLayout;
    protected TextView mRightOperationDes;
    protected ImageView mRightOperationImageView;
    protected TextView mTitleName;
    protected TextView msgCountTv;

    //标题栏ID
    protected abstract int getTitleViewId();

    //布局文件ID
    protected abstract int getContentViewId();

    //布局中Fragment的ID
    protected abstract int getFragmentContentId();

    protected <T extends View> T $(int id) {
        return (T) super.findViewById(id);
    }

    public void setContentView(int layoutId) {
        super.setContentView(R.layout.activity_main_ui_frame);
        ViewStub titleStub = (ViewStub) findViewById(R.id.title_stub);
        if (titleResId != -1) {
            titleStub.setLayoutResource(this.titleResId);
            titleView = titleStub.inflate();
        }
        if (layoutId != -1) {
            ViewStub bodyStub = (ViewStub) findViewById(R.id.body_stub);
            bodyStub.setLayoutResource(layoutId);
            this.bodyView = bodyStub.inflate();
        }
        int bottomId = this.getResources().getIdentifier("bottom_stub", "id", this.getPackageName());
        Fragment bottomFragment = this.getSupportFragmentManager().findFragmentById(bottomId);
        if (bottomFragment != null) {
            this.bottomFragment = ((BottomNavigateFragment) bottomFragment);
        } else {
            FrameLayout frameLayout = (FrameLayout) this.findViewById(bottomId);
            if (this.bottomFragment != null) {
                FragmentTransaction fragmentTransaction = this.getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(bottomId, this.bottomFragment);
                fragmentTransaction.commit();
            } else {
                frameLayout.setVisibility(View.GONE);
            }
        }
    }

    public void setBottomFragment(BottomNavigateFragment bottomNavigateFragment) {
        this.bottomFragment = bottomNavigateFragment;
    }

    public void setTitleResId(int titleResId) {
        this.titleResId = titleResId;
    }

    public View getCommonTitle() {
        return this.titleView;
    }


    /**
     * 普通公共titlebar初始化,使用前需提前调用
     **/
    public void setActionBar() {
        Resources localResources = getResources();
        this.mProvinceName = ((TextView) titleView.findViewById(localResources.getIdentifier("actionbar_province_tv", "id", this.getPackageName())));
        this.mTitleName = ((TextView) titleView.findViewById(localResources.getIdentifier("actionbar_title_tv", "id", this.getPackageName())));

        this.mLeftOperationLinear = ((LinearLayout) titleView.findViewById(localResources.getIdentifier("left_operation_ll", "id", this.getPackageName())));
        this.mLeftOperationImageView = ((ImageView) titleView.findViewById(localResources.getIdentifier("left_operation_iv", "id", this.getPackageName())));
        this.mRightOperationImageView = ((ImageView) titleView.findViewById(localResources.getIdentifier("right_operation_iv", "id", this.getPackageName())));
        this.mRightOperationDes = ((TextView) titleView.findViewById(localResources.getIdentifier("right_operation_tv", "id", this.getPackageName())));
        this.msgCountTv = ((TextView) titleView.findViewById(localResources.getIdentifier("actionbar_message_count_tv", "id", this.getPackageName())));
        this.mRightLayout = ((RelativeLayout) titleView.findViewById(localResources.getIdentifier("right_operation_rl", "id", this.getPackageName())));
        this.mLeftOperationLinear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    //添加fragment
    protected void addFragment(BaseFragment fragment) {
        if (fragment != null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(getFragmentContentId(), fragment, fragment.getClass().getSimpleName())
//                    .addToBackStack(fragment.getClass().getSimpleName())
                    .commitAllowingStateLoss();
        }
    }

    //移除fragment
    protected void removeFragment() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 1) {
            getSupportFragmentManager().popBackStack();
        } else {
            finish();
        }
    }

    /* 友盟统计 ----------------------start---------------------- */
    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }
    /* 友盟统计 ----------------------end---------------------- */
}
