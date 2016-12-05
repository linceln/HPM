package com.olsplus.balancemall.app.web;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.olsplus.balancemall.R;
import com.olsplus.balancemall.app.cart.request.CartRequestImpl;
import com.olsplus.balancemall.app.cart.view.IAddCartView;
import com.olsplus.balancemall.app.pay.CheckoutMainActivity;
import com.olsplus.balancemall.core.app.BaseFragment;
import com.olsplus.balancemall.core.app.MainActivity;
import com.olsplus.balancemall.core.util.ToastUtil;


public class WebActivity extends MainActivity {

    private TextView h5Title;
    private ProgressBar webViewProgress;
    private WebView webView;

    private String pageUrl;
    private String title;


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
        return R.layout.module_web_action_bar_title;
    }

    @Override
    protected int getContentViewId() {
        return R.layout.module_web_container_fragment;
    }

    @Override
    protected void handleIntent(Intent intent) {
        super.handleIntent(intent);
        pageUrl = intent.getStringExtra("url");
        title= intent.getStringExtra("title");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        h5Title = (TextView) findViewById(R.id.title);
        webViewProgress = (ProgressBar) findViewById(R.id.webview_progressbar);
        webView = (WebView) findViewById(R.id.webView);
        initWebSetting();
        setH5Title(title);
        findViewById(R.id.home).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (webView.canGoBack()) {
                    webView.goBack();// 返回前一个页面
                }else{
                    finish();
                }
            }
        });

    }

    private void initWebSetting(){
        if (Build.VERSION.SDK_INT >= 9) {
            this.webView.setOverScrollMode(2);
        }
        WebSettings ws = this.webView.getSettings();
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int mDensity = metrics.densityDpi;
        if (mDensity == 120) {
            ws.setDefaultZoom(WebSettings.ZoomDensity.CLOSE);
        }else if (mDensity == 160) {
            ws.setDefaultZoom(WebSettings.ZoomDensity.MEDIUM);
        }else if (mDensity == 240) {
            ws.setDefaultZoom(WebSettings.ZoomDensity.FAR);
        }
        ws.setCacheMode(WebSettings.LOAD_NO_CACHE);
        ws.setJavaScriptEnabled(true);
        ws.setBuiltInZoomControls(false);
        ws.setSupportZoom(true);
        ws.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        ws.setUseWideViewPort(true);
        ws.setLoadWithOverviewMode(true);
        webView.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onProgressChanged(WebView arg3, int progress) {
                webViewProgress.setProgress(progress);
                if (progress != 100) {
                    webViewProgress.setVisibility(View.VISIBLE);
                } else {
                    webViewProgress.setVisibility(View.GONE);
                }
            }

            @Override
            public void onReceivedTitle(WebView arg4, String title) {
//                setH5Title(title);
            }

            @Override
            @SuppressLint(value = {"NewApi"})
            public boolean onShowFileChooser(WebView webView, ValueCallback
                    valueCallback, WebChromeClient.FileChooserParams fileChooserParams) {

                return true;
            }
        });

        webView.setWebViewClient(new WebViewClient(){
            @Override
            public void onLoadResource(WebView webView, String s) {
                super.onLoadResource(webView, s);
            }

            @Override
            public void onPageFinished(WebView webView, String s) {
                super.onPageFinished(webView, s);
            }

            @Override
            public void onPageStarted(WebView webView, String s, Bitmap bitmap) {
                super.onPageStarted(webView, s, bitmap);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView webView, String url) {
//                if(url.startsWith(ApiConst.BASE_IMAGE_URL)){
//                    webView.loadUrl(url);
//                }
                if(url.startsWith("addtocart")){
                    addCartRequest(url);
                }
                else if(url.startsWith("http://")){
                    webView.loadUrl(url);
                }
                else{
                    Intent intent = new Intent(WebActivity.this, CheckoutMainActivity.class);
                    intent.putExtra("sumit_order_parmas",url);
                    intent.putExtra("from","0");
                    startActivity(intent);
                    finish();
                }
                return true;
            }
        });
        webView.loadUrl(pageUrl);
    }

    private void setH5Title(String titele) {
        if (titele == null) {
            return;
        }
        if(titele.contains("blank")){
            return;
        }
        this.h5Title.setText(titele);
    }

    @Override
    public void onClick(View v) {

    }


    /**
     * 添加购物车请求
     */
    private void addCartRequest(String addCartJson){
        CartRequestImpl cartRequest = new CartRequestImpl(this);
        cartRequest.setiAddCartView(new IAddCartView() {
            @Override
            public void showAddCartSuccessView() {
//                Intent intent = new Intent(WebActivity.this, CartActivity.class);
//                intent.putExtra("from","0");
//                startActivity(intent);
                ToastUtil.showShort(WebActivity.this,"添加购物车成功");
            }

            @Override
            public void showAddCartErrorView(String msg) {
                ToastUtil.showShort(WebActivity.this,"添加购物车失败");
            }

        });
        cartRequest.addCart(addCartJson);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && webView.canGoBack()) {
            webView.goBack();// 返回前一个页面
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}
