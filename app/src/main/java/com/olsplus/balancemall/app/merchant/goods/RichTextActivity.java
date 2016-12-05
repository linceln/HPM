package com.olsplus.balancemall.app.merchant.goods;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.design.widget.Snackbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.widget.TextView;

import com.github.mr5.icarus.Callback;
import com.github.mr5.icarus.Icarus;
import com.github.mr5.icarus.TextViewToolbar;
import com.github.mr5.icarus.button.Button;
import com.github.mr5.icarus.button.TextViewButton;
import com.github.mr5.icarus.entity.Options;
import com.liuguangqiang.ipicker.IPicker;
import com.olsplus.balancemall.R;
import com.olsplus.balancemall.core.app.BaseCompatActivity;
import com.olsplus.balancemall.core.util.ApiConst;
import com.olsplus.balancemall.core.util.Base64Util;
import com.olsplus.balancemall.core.util.LogUtil;
import com.olsplus.balancemall.core.util.StrConst;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import rx.Subscriber;

import static com.olsplus.balancemall.core.util.UploadManager.uploadGoodsImage;
import static com.olsplus.balancemall.core.util.LoadingDialogManager.showLoading;
import static com.olsplus.balancemall.core.util.LoadingDialogManager.dismissLoading;

public class RichTextActivity extends BaseCompatActivity implements IPicker.OnSelectedListener {


    private WebView editor;
    private Icarus icarus;
    private List<String> paths;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_rich_text;
    }

    @Override
    protected void setupWindowAnimations() {
    }

    @Override
    protected void initUI() {


        setTitle("商品描述");

        editor = (WebView) findViewById(R.id.editor);

        TextViewToolbar toolbar = new TextViewToolbar();
        Options options = new Options();
        options.setPlaceholder("请输入...");
        options.addAllowedAttributes("img", Arrays.asList("data-type", "data-id", "class", "src", "alt", "width", "height", "data-non-image"));
        options.addAllowedAttributes("iframe", Arrays.asList("data-type", "data-id", "class", "src", "width", "height"));
        options.addAllowedAttributes("a", Arrays.asList("data-type", "data-id", "class", "href", "target", "title"));

        icarus = new Icarus(toolbar, options, editor);
        prepareToolbar(toolbar, icarus);
        icarus.loadCSS("file:///android_asset/editor.css");
        icarus.loadJs("file:///android_asset/test.js");
        icarus.render();
    }

    @Override
    protected void initData() {
        String html = getIntent().getStringExtra(StrConst.extra.html);
        if (!TextUtils.isEmpty(html)) {
            icarus.setContent(Base64Util.decode(html));
            LogUtil.e("RichText html", Base64Util.decode(html));
        }
    }

    private void prepareToolbar(TextViewToolbar toolbar, final Icarus icarus) {
        Typeface iconFont = Typeface.createFromAsset(getAssets(), "Simditor.ttf");
        TextView imageButtonTextView = (TextView) findViewById(R.id.button_image);
        imageButtonTextView.setTypeface(iconFont);
        ImageButton imageButton = new ImageButton(imageButtonTextView, icarus);
        imageButton.setName(Button.NAME_IMAGE);
//        imageButton.setPopover(new ImagePopoverImpl(imageButtonTextView, icarus));
        toolbar.addButton(imageButton);
    }

    private class ImageButton extends TextViewButton {

        public ImageButton(TextView textView, Icarus icarus) {
            super(textView, icarus);
        }

        @Override
        public void command() {
            IPicker.setLimit(1);
//            IPicker.setCropEnable(true);
            IPicker.open(RichTextActivity.this);
            IPicker.setOnSelectedListener(RichTextActivity.this);
        }
    }


    @Override
    public void onSelected(List<String> paths) {

        // 选好图之后在onResume()中做上传操作，因为有DialogFragment，如果没走生命周期会报Can not perform this action after onSaveInstanceState
        this.paths = new ArrayList<>();
        this.paths.addAll(paths);
    }

    @Override
    protected void onResume() {

        super.onResume();
        if (paths != null && !paths.isEmpty()) {
            // 上传
            uploadGoodsImage(this, paths, new Subscriber<String>() {

                @Override
                public void onStart() {
                    showLoading(RichTextActivity.this, getString(R.string.loading_insert));
                }

                @Override
                public void onCompleted() {
                }

                @Override
                public void onError(Throwable e) {
                    dismissLoading();
                    Snackbar.make(editor, e.getMessage(), Snackbar.LENGTH_SHORT).show();
//                    Toast.makeText(RichTextActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onNext(String path) {
                    dismissLoading();
                    icarus.insertHtml("<img src=\"" + ApiConst.BASE_IMAGE_URL + path + "\" />");
                    LogUtil.e("RichText insertImage", "<img src=\"" + ApiConst.BASE_IMAGE_URL + path + "\" />");
                }
            });
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_rich_edit, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:// 返回
                finish();
                break;
            case R.id.action_complete:// 完成

                icarus.getContent(new Callback() {
                    @Override
                    public void run(String params) {
                        if (!TextUtils.isEmpty(params)) {
                            LogUtil.e("RichText getContent", params);

                            String content = "";
                            try {
                                JSONObject jsonObject = new JSONObject(params);
                                content = jsonObject.getString("content");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            LogUtil.e("RichText getHtml", content);
                            Intent data = new Intent();
                            data.putExtra(StrConst.extra.rich_text, content);
                            setResult(RESULT_OK, data);
                        }
                        finish();
                    }
                });

                break;
        }
        return true;
    }

    @Override
    protected void onDestroy() {
        hideSoftInput();
        super.onDestroy();
    }

    /**
     * 隐藏软键盘
     */
    private void hideSoftInput() {
        InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        im.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }
}
