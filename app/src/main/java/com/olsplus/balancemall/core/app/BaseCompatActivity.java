package com.olsplus.balancemall.core.app;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.olsplus.balancemall.R;
import com.olsplus.balancemall.app.home.HomeActivity;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;


public abstract class BaseCompatActivity extends AppCompatActivity {

    private Toolbar toolbar;

    @Override
    protected final void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(getLayoutResId());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setupWindowAnimations();
        }
        initToolbar();
        getExtras();
        initUI();
        initData();
    }

    protected void getExtras() {
    }

    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            toolbar.setTitle("");
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_normal);
        }
    }

    protected void setTitle(String title) {

        if (toolbar != null) {
            TextView tvTitle = new TextView(this);
            tvTitle.setTextSize(20);// 20sp
            tvTitle.setTextColor(getResources().getColor(R.color.black));
            tvTitle.setText(title);
            Toolbar.LayoutParams layoutParams = new Toolbar.LayoutParams(Toolbar.LayoutParams.WRAP_CONTENT, Toolbar.LayoutParams.WRAP_CONTENT, Gravity.CENTER);
            toolbar.addView(tvTitle, layoutParams);
        }
    }

    /**
     * Return layout id of this Activity
     *
     * @return layout id
     */
    protected abstract int getLayoutResId();

    /**
     * Initialize Animations, only after API 21
     */
    protected abstract void setupWindowAnimations();

    /**
     * Initialize Views
     */
    protected abstract void initUI();

    /**
     * Initialize data, like getting network request
     */
    protected abstract void initData();

    public void startActivityAnimated(Activity activity, Intent intent, List<Pair<View, String>> sharedElement) {

        List<Pair<View, String>> pairs = new ArrayList<>();
        pairs.add(Pair.create(findViewById(android.R.id.statusBarBackground), Window.STATUS_BAR_BACKGROUND_TRANSITION_NAME));
        pairs.add(Pair.create(findViewById(android.R.id.navigationBarBackground), Window.NAVIGATION_BAR_BACKGROUND_TRANSITION_NAME));
        if (sharedElement != null && !sharedElement.isEmpty()) {
            pairs.addAll(sharedElement);
        }
        ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(activity, pairs.toArray(new Pair[pairs.size()]));
        ActivityCompat.startActivity(activity, intent, optionsCompat.toBundle());
    }

    public void startActivityAnimatedForResult(Activity activity, Intent intent, int requestCode, List<Pair<View, String>> sharedElement) {

        List<Pair<View, String>> pairs = new ArrayList<>();
        pairs.add(Pair.create(findViewById(android.R.id.statusBarBackground), Window.STATUS_BAR_BACKGROUND_TRANSITION_NAME));
        pairs.add(Pair.create(findViewById(android.R.id.navigationBarBackground), Window.NAVIGATION_BAR_BACKGROUND_TRANSITION_NAME));
        if (sharedElement != null && !sharedElement.isEmpty()) {
            pairs.addAll(sharedElement);
        }
        ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(activity, pairs.toArray(new Pair[pairs.size()]));
        ActivityCompat.startActivityForResult(activity, intent, requestCode, optionsCompat.toBundle());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_toolbar, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: // 顶栏返回键
                finish();
                break;
            case R.id.action_home:// 回到首页
                Intent intent = new Intent(this, HomeActivity.class);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 点击任意位置隐藏软键盘----------start------------
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {

        if (ev.getAction() == MotionEvent.ACTION_DOWN) {

            // 获得当前得到焦点的View，一般情况下就是EditText（特殊情况就是轨迹求或者实体案件会移动焦点）
            View v = getCurrentFocus();

            if (isShouldHideInput(v, ev)) {
                hideSoftInput(v.getWindowToken());
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    /**
     * 根据EditText所在坐标和用户点击的坐标相对比，来判断是否隐藏键盘，因为当用户点击EditText时没必要隐藏
     *
     * @param v
     * @param event
     * @return
     */
    private boolean isShouldHideInput(View v, MotionEvent event) {

        if (v != null && (v instanceof EditText)) {
            int[] l =
                    {0, 0};
            v.getLocationInWindow(l);
            int left = l[0], top = l[1], bottom = top + v.getHeight(), right = left + v.getWidth();
            if (event.getX() > left && event.getX() < right && event.getY() > top && event.getY() < bottom) {

                return false;// 点击EditText的事件，忽略它。
            } else {

                v.clearFocus();// 移除EditText焦点
                return true;
            }
        }
        // 如果焦点不是EditText则忽略，这个发生在视图刚绘制完，第一个焦点不在EditView上，和用户用轨迹球选择其他的焦点
        return false;
    }

    /**
     * 隐藏软键盘
     *
     * @param token
     */
    private void hideSoftInput(IBinder token) {

        if (token != null) {
            InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            im.hideSoftInputFromWindow(token, InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    /**
     * 点击任意位置隐藏软键盘---------end------------
     */

    /** 友盟统计 ----------------------start---------------------- */
    @Override
    protected void onResume() {
        MobclickAgent.onResume(this);
        super.onResume();
    }

    @Override
    protected void onPause() {
        MobclickAgent.onPause(this);
        super.onPause();
    }
    /** 友盟统计 ----------------------end---------------------- */
}
