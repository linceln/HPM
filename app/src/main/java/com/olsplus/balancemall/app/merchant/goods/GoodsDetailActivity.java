package com.olsplus.balancemall.app.merchant.goods;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.olsplus.balancemall.R;
import com.olsplus.balancemall.app.merchant.goods.business.GoodsBusiness;
import com.olsplus.balancemall.component.dialog.GeneralDialogFragment;
import com.olsplus.balancemall.core.app.BaseCompatActivity;
import com.olsplus.balancemall.core.bean.BaseResultEntity;
import com.olsplus.balancemall.core.http.RequestCallback;
import com.olsplus.balancemall.core.util.ApiConst;
import com.olsplus.balancemall.core.util.SPUtil;
import com.olsplus.balancemall.core.util.StrConst;
import com.olsplus.balancemall.core.util.ToastUtil;

import org.greenrobot.eventbus.EventBus;

import static com.olsplus.balancemall.component.dialog.LoadingDialog.dismissLoading;
import static com.olsplus.balancemall.component.dialog.LoadingDialog.showLoading;

public class GoodsDetailActivity extends BaseCompatActivity implements View.OnClickListener {

    private static final String TAG = "GoodsDetailActivity";
    private static final int REQUEST_EDIT = 0;
    private long goods_id;
    private WebView webViewGoodsDetail;
    private String goodsType;


    @Override
    protected int getLayoutResId() {
        return R.layout.activity_goods_detail;
    }

    @Override
    protected void setupWindowAnimations() {
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void initUI() {
        // 标题栏
        setTitle("商品详情");

        TextView tvGoodsDetailEdit = (TextView) findViewById(R.id.tvGoodsDetailEdit);
        tvGoodsDetailEdit.setOnClickListener(this);
        TextView tvGoodsDetailOffSale = (TextView) findViewById(R.id.tvGoodsDetailOffSale);
        tvGoodsDetailOffSale.setOnClickListener(this);
        TextView tvGoodsDetailDelete = (TextView) findViewById(R.id.tvGoodsDetailDelete);
        tvGoodsDetailDelete.setOnClickListener(this);

        if (goodsType.equals(StrConst.goods.OFF_SALE)) {
            tvGoodsDetailOffSale.setText("上架");
        } else {
            tvGoodsDetailOffSale.setText("下架");
        }

        webViewGoodsDetail = (WebView) findViewById(R.id.webViewGoodsDetail);
        WebSettings webSettings = webViewGoodsDetail.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setBlockNetworkImage(false);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // Lollipop 之后不能混合用http和https
            webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        webViewGoodsDetail.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                dismissLoading();
                super.onPageFinished(view, url);
            }
        });
        webViewGoodsDetail.setWebChromeClient(new WebChromeClient());
    }

    @Override
    protected void getExtras() {
        goodsType = getIntent().getStringExtra(StrConst.extra.goods_type);
        goods_id = getIntent().getLongExtra(StrConst.extra.goods_id, 0);
    }

    @Override
    protected void initData() {

        String res_path = (String) SPUtil.get(this, SPUtil.RES_PATH, "");
        String local_service_id = (String) SPUtil.get(this, SPUtil.LOCAL_SERVICE_ID, "");

        String path = getString(R.string.merchant_goods_detail, ApiConst.BASE_HTML_URL, res_path, local_service_id, String.valueOf(goods_id));

        showLoading(this, getString(R.string.loading_default));
        webViewGoodsDetail.loadUrl(path);
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {

            case R.id.tvGoodsDetailEdit:// 编辑
                intent = new Intent(this, AddGoodsActivity.class);
                intent.putExtra(StrConst.extra.goods_edit_type, StrConst.input.editing);
                intent.putExtra(StrConst.extra.goods_id, goods_id);
                startActivityForResult(intent, REQUEST_EDIT);
                break;
            case R.id.tvGoodsDetailOffSale:// 上下架

                if (goodsType.equals(StrConst.goods.OFF_SALE)) {
                    showDialog("确定上架商品？", new GeneralDialogFragment.OnPositiveClickListener() {
                        @Override
                        public void onClick() {
                            //上架
                            GoodsBusiness.onSale(GoodsDetailActivity.this, goods_id, new GoodsCallback());

                        }
                    });
                } else {
                    showDialog("确定下架商品？", new GeneralDialogFragment.OnPositiveClickListener() {
                        @Override
                        public void onClick() {
                            //下架
                            GoodsBusiness.offSale(GoodsDetailActivity.this, goods_id, new GoodsCallback());

                        }
                    });
                }
                break;
            case R.id.tvGoodsDetailDelete:// 删除
                showDialog("确定删除商品？", new GeneralDialogFragment.OnPositiveClickListener() {
                    @Override
                    public void onClick() {
                        // 删除
                        GoodsBusiness.delete(GoodsDetailActivity.this, goods_id, new GoodsCallback());
                    }
                });
                break;
        }
    }

    private void showDialog(String tag, GeneralDialogFragment.OnPositiveClickListener listener) {
        GeneralDialogFragment dialogFragment = new GeneralDialogFragment();
        dialogFragment.setMessage(tag);
        dialogFragment.setOnPositiveClickListener(listener);
        dialogFragment.show(getSupportFragmentManager(), tag);
    }

    private class GoodsCallback implements RequestCallback<BaseResultEntity> {

        @Override
        public void onSuccess(BaseResultEntity baseResultEntity) {
            ToastUtil.showShort(GoodsDetailActivity.this, baseResultEntity.getMsg());
            EventBus.getDefault().post("From GoodsDetailActivity");
            finish();
        }

        @Override
        public void onError(String msg) {
            ToastUtil.showShort(GoodsDetailActivity.this, msg);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_EDIT && resultCode == RESULT_OK) {
            // 编辑成功刷新页面
            initData();
        }
    }
}
