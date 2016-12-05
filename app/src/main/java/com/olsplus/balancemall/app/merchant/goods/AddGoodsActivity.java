package com.olsplus.balancemall.app.merchant.goods;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.jakewharton.rxbinding.view.RxView;
import com.jude.easyrecyclerview.decoration.DividerDecoration;
import com.liuguangqiang.ipicker.IPicker;
import com.olsplus.balancemall.R;
import com.olsplus.balancemall.app.merchant.goods.adapter.AddGoodsAdapter;
import com.olsplus.balancemall.app.merchant.goods.bean.EditGoodsEntity;
import com.olsplus.balancemall.app.merchant.goods.bean.GoodsDetail;
import com.olsplus.balancemall.app.merchant.goods.business.GoodsBusiness;
import com.olsplus.balancemall.core.app.BaseCompatActivity;
import com.olsplus.balancemall.core.bean.BaseResultEntity;
import com.olsplus.balancemall.core.http.RequestCallback;
import com.olsplus.balancemall.core.image.ImageHelper;
import com.olsplus.balancemall.core.util.Base64Util;
import com.olsplus.balancemall.core.util.DateUtil;
import com.olsplus.balancemall.core.util.DensityUtil;
import com.olsplus.balancemall.core.util.LogUtil;
import com.olsplus.balancemall.core.util.SPUtil;
import com.olsplus.balancemall.core.util.StrConst;
import com.olsplus.balancemall.core.util.ToastUtil;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Subscriber;
import rx.functions.Action1;

import static com.olsplus.balancemall.core.util.LoadingDialogManager.dismissLoading;
import static com.olsplus.balancemall.core.util.LoadingDialogManager.showLoading;
import static com.olsplus.balancemall.core.util.StrConst.extra.goods_edit_type;
import static com.olsplus.balancemall.core.util.StrConst.input.adding;
import static com.olsplus.balancemall.core.util.StrConst.input.editing;
import static com.olsplus.balancemall.core.util.UploadManager.uploadGoodsImage;


public class AddGoodsActivity extends BaseCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener,
        IPicker.OnSelectedListener, View.OnClickListener {

    private static final int REQUEST_RICH_TEXT = 0;
    private List<String> originalPaths = new ArrayList<>();
    private String mainImage;
    private List<GoodsDetail.SkuInfo> list = new ArrayList<>();
    private AddGoodsAdapter adapter;
    private EditText etGoodsName;
    private ImageView ivGoodsImage;
    private String html = "";// 商品描述
    private String time;// 开始出售时间
    private TextView tvStartSaleTime;
    private TextView tvRichText;
    private int type;
    private long goods_id;
    private String goodsName;


    @Override
    protected int getLayoutResId() {
        return R.layout.activity_add_goods;
    }

    @Override
    protected void setupWindowAnimations() {
    }

    @Override
    protected void getExtras() {
        // 区分添加商品和编辑商品
        type = getIntent().getIntExtra(goods_edit_type, adding);
        // 编辑商品时得到id请求数据
        goods_id = getIntent().getLongExtra(StrConst.extra.goods_id, 0);
    }

    @Override
    protected void initUI() {

        if (type == adding) {
            setTitle("添加商品");
        } else if (type == editing) {
            setTitle("编辑商品");
        }
        etGoodsName = (EditText) findViewById(R.id.etGoodsName);
        ivGoodsImage = (ImageView) findViewById(R.id.ivGoodsImage);
        ivGoodsImage.setOnClickListener(this);
        TextView tvCancel = (TextView) findViewById(R.id.tvCancel);
        tvCancel.setOnClickListener(this);
        // 完成
        TextView tvComplete = (TextView) findViewById(R.id.tvComplete);
        RxView.clicks(tvComplete)
                .throttleFirst(1, TimeUnit.SECONDS)// 防抖
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void v) {
                        if (type == adding) {
                            commitAdd();
                        } else if (type == editing) {
                            commitEdit();
                        }
                    }
                });
        TextView tvAddSpec = (TextView) findViewById(R.id.tvAddSpec);
        tvAddSpec.setOnClickListener(this);

        findViewById(R.id.linearRichEdit).setOnClickListener(this);
        findViewById(R.id.linearStartSaleTime).setOnClickListener(this);
        tvRichText = (TextView) findViewById(R.id.tvRichText);
        tvStartSaleTime = (TextView) findViewById(R.id.tvStartSaleTime);

        RecyclerView recyclerAddGoods = (RecyclerView) findViewById(R.id.recyclerAddGoods);
        recyclerAddGoods.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerAddGoods.addItemDecoration(new DividerDecoration(getResources().getColor(android.R.color.transparent), DensityUtil.dp2px(this, 20)));
        recyclerAddGoods.setItemAnimator(new DefaultItemAnimator());
        // 默认有一个
        list.add(new GoodsDetail.SkuInfo());
        adapter = new AddGoodsAdapter(this, list);
        recyclerAddGoods.setAdapter(adapter);
    }

    @Override
    protected void initData() {

        if (type == editing) {
            showLoading(this, getString(R.string.loading_default));
            // 编辑商品
            adapter.setInput(editing);
            list.clear();
            GoodsBusiness.editGoods(this, goods_id, new RequestCallback<BaseResultEntity>() {
                @Override
                public void onSuccess(BaseResultEntity baseResultEntity) {
                    dismissLoading();
                    EditGoodsEntity entity = (EditGoodsEntity) baseResultEntity;
                    EditGoodsEntity.ProductBean product = entity.getProduct();

                    etGoodsName.setText(product.getTitle());
                    if (!TextUtils.isEmpty(product.getMainImage())) {
                        // 保存用大图
                        mainImage = product.getMainImage();
                    }
                    if (!TextUtils.isEmpty(product.getThumbnail())) {
                        // 显示用缩略图
                        ImageHelper.display(AddGoodsActivity.this, ivGoodsImage, product.getThumbnail());
                    }
                    tvStartSaleTime.setText(DateUtil.getSecond(product.getStart_time()));
                    if (TextUtils.isEmpty(product.getDesc())) {
                        tvRichText.setText("未添加");
                    } else {
                        html = product.getDesc().replace("=", "");
                        tvRichText.setText("已添加");
                    }
                    time = DateUtil.getSecond(product.getStart_time() / 1000);

                    List<EditGoodsEntity.ProductBean.SkuInfoBean> skuInfoBean = entity.getProduct().getSku_info();
                    if (!skuInfoBean.isEmpty()) {
                        for (EditGoodsEntity.ProductBean.SkuInfoBean info : skuInfoBean) {
                            GoodsDetail.SkuInfo skuInfo = new GoodsDetail.SkuInfo();
                            skuInfo.setPrice(info.getPrice());
                            skuInfo.setInventory(info.getInventory());
                            if (!TextUtils.isEmpty(info.getProperty_value())) {
                                skuInfo.setProperty(info.getProperty_value());
                            } else {
                                skuInfo.setProperty("");
                            }
                            list.add(skuInfo);
                        }
                        adapter.notifyDataSetChanged();
                    } else {
                        GoodsDetail.SkuInfo skuInfo = new GoodsDetail.SkuInfo();
                        skuInfo.setPrice(product.getPrice());
                        skuInfo.setInventory(product.getInventory());
                        skuInfo.setProperty("");
                        list.add(skuInfo);
                        adapter.notifyDataSetChanged();
                    }
                }

                @Override
                public void onError(String msg) {
                    dismissLoading();
                    Toast.makeText(AddGoodsActivity.this, msg, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == REQUEST_RICH_TEXT && data != null) {
            // 富文本
            String html = data.getStringExtra(StrConst.extra.rich_text);
            this.html = Base64Util.encode(html);
            tvRichText.setText("已添加");
            LogUtil.e("返回的富文本", html + "\n" + this.html);
        }
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.ivGoodsImage:// 添加图片并上传
                IPicker.setLimit(1);
                IPicker.open(this);
                IPicker.setOnSelectedListener(this);
                break;
            case R.id.tvAddSpec:// 添加商品规格
                list.add(new GoodsDetail.SkuInfo());
                adapter.notifyItemInserted(list.size());
                break;
            case R.id.linearRichEdit: // 富文本

                intent = new Intent(this, RichTextActivity.class);
                intent.putExtra(StrConst.extra.html, html);
                startActivityForResult(intent, REQUEST_RICH_TEXT);
                break;
            case R.id.linearStartSaleTime: // 开始销售时间
                Calendar now = Calendar.getInstance();
                DatePickerDialog dpd = DatePickerDialog.newInstance(
                        AddGoodsActivity.this,
                        now.get(Calendar.YEAR),
                        now.get(Calendar.MONTH),
                        now.get(Calendar.DAY_OF_MONTH)
                );
                dpd.show(getFragmentManager(), "DatePickerDialog");
                break;

            case R.id.tvCancel: // 取消
                finish();
                break;
        }
    }

    private boolean isInputEmpty() {
        goodsName = etGoodsName.getText().toString().trim();
        if (TextUtils.isEmpty(goodsName)) {
            ToastUtil.showShort(this, "请输入商品名称");
            return true;
        }

        if (originalPaths.isEmpty()) {
            ToastUtil.showShort(this, "请选择商品图片");
            return true;
        }

        for (GoodsDetail.SkuInfo skuInfo : list) {

            if (skuInfo.getPrice() == 0) {
                ToastUtil.showShort(this, "请输入商品价格");
                return true;
            }

            if (skuInfo.getInventory() == 0) {
                ToastUtil.showShort(this, "请输入商品库存");
                return true;
            }

//            if (TextUtils.isInputEmpty(skuInfo.getProperty())) {
//                ToastUtil.showShort(this, "请输入商品规格");
//                return;
//            }
        }

        if (TextUtils.isEmpty(time)) {
            Toast.makeText(this, "请选择开始出售时间", Toast.LENGTH_SHORT).show();
            return true;
        }
        return false;
    }

    /**
     * 提交新增商品
     */
    private void commitAdd() {

        if (isInputEmpty()) {
            return;
        }

        // 上传图片再提交
        upload2Add();

    }

    /**
     * 提交编辑商品
     */
    private void commitEdit() {

//        if (isInputEmpty()) {
//            return;
//        }

        if (imageNotUpdate()) {// 图片未改动，直接调更新商品接口
            showLoading(this, getString(R.string.loading_upload));
            updateGoods(mainImage);
        } else { // 图片有改动，先上传图片

            // 上传图片再更新
            upload2Update();
        }
    }

    private void upload2Update() {
        uploadGoodsImage(this, originalPaths, new Subscriber<String>() {

            @Override
            public void onStart() {
                showLoading(AddGoodsActivity.this, getString(R.string.loading_upload));
            }

            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
                dismissLoading();
                Toast.makeText(AddGoodsActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNext(String path) {
                updateGoods(path);
            }
        });
    }

    private void upload2Add() {
        uploadGoodsImage(this, originalPaths, new Subscriber<String>() {

            @Override
            public void onStart() {
                showLoading(AddGoodsActivity.this, getString(R.string.loading_upload));
            }

            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
                dismissLoading();
                Toast.makeText(AddGoodsActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNext(String path) {
                addGoods(path);
            }
        });
    }

    private void addGoods(String url) {
        GoodsBusiness.addGoods(this, generateGoodsDetail(url), new RequestCallback<BaseResultEntity>() {
            @Override
            public void onSuccess(BaseResultEntity baseResultEntity) {
                dismissLoading();
                Toast.makeText(AddGoodsActivity.this, baseResultEntity.getMsg(), Toast.LENGTH_SHORT).show();
                EventBus.getDefault().post("添加成功");
                finish();
            }

            @Override
            public void onError(String msg) {
                dismissLoading();
                Toast.makeText(AddGoodsActivity.this, msg, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateGoods(String url) {
        GoodsBusiness.updateGoods(this, generateGoodsDetail(url), new RequestCallback<BaseResultEntity>() {
            @Override
            public void onSuccess(BaseResultEntity baseResultEntity) {
                dismissLoading();
                Toast.makeText(AddGoodsActivity.this, baseResultEntity.getMsg(), Toast.LENGTH_SHORT).show();
                EventBus.getDefault().post("添加成功");
                finish();
            }

            @Override
            public void onError(String msg) {
                dismissLoading();
                Toast.makeText(AddGoodsActivity.this, msg, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @NonNull
    private GoodsDetail generateGoodsDetail(String url) {
        GoodsDetail goodsDetail = new GoodsDetail();
        goodsDetail.setDetail(html);
        goodsDetail.setInventory(list.get(0).getInventory());
        goodsDetail.setPrice(list.get(0).getPrice());
        goodsDetail.setImg(url);
        goodsDetail.setStart_time(DateUtil.getTimestamp(time));
        goodsDetail.setTitle(goodsName);
        goodsDetail.setSku_info(list);
        if (type == editing) {
            goodsDetail.setLocal_service_id((String) SPUtil.get(this, SPUtil.LOCAL_SERVICE_ID, ""));
            goodsDetail.setProduct_id(goods_id);
        }
        return goodsDetail;
    }

    private boolean imageNotUpdate() {
        return originalPaths.isEmpty() && !TextUtils.isEmpty(mainImage);
    }

    /**
     * 选择图片回调
     *
     * @param paths
     */
    @Override
    public void onSelected(List<String> paths) {

        if (!paths.isEmpty()) {

            // 显示
            ImageHelper.displayLocal(this, ivGoodsImage, paths.get(0));

            this.originalPaths.clear();
            this.originalPaths.addAll(paths);
        }
    }

    /**
     * 日期选择回调
     *
     * @param view
     * @param year
     * @param monthOfYear
     * @param dayOfMonth
     */
    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {

        time = DateUtil.addZero(year) + "-" + DateUtil.addZero(monthOfYear) + "-" + DateUtil.addZero(dayOfMonth) + " ";

        Calendar now = Calendar.getInstance();
        TimePickerDialog tpd = TimePickerDialog.newInstance(
                AddGoodsActivity.this,
                now.get(Calendar.HOUR),
                now.get(Calendar.MINUTE),
                now.get(Calendar.SECOND),
                true
        );
        tpd.show(getFragmentManager(), "TimePickerDialog");
    }

    /**
     * 时间选择回调
     *
     * @param view
     * @param hourOfDay
     * @param minute
     * @param second
     */
    @Override
    public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second) {

        time += DateUtil.addZero(hourOfDay) + ":" + DateUtil.addZero(minute) + ":" + DateUtil.addZero(second);
        tvStartSaleTime.setText(time);
    }
}
