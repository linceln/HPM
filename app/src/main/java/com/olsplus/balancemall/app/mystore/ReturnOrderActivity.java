package com.olsplus.balancemall.app.mystore;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.liuguangqiang.ipicker.IPicker;
import com.olsplus.balancemall.R;
import com.olsplus.balancemall.app.mystore.bean.MyOrderReturn;
import com.olsplus.balancemall.app.mystore.bean.RefundReason;
import com.olsplus.balancemall.app.mystore.request.MyOrderImpl;
import com.olsplus.balancemall.app.mystore.view.CommentImageView;
import com.olsplus.balancemall.app.mystore.view.IReturnOrderView;
import com.olsplus.balancemall.core.app.BaseFragment;
import com.olsplus.balancemall.core.app.MainActivity;
import com.olsplus.balancemall.core.util.SnackbarUtil;
import com.olsplus.balancemall.core.util.ToastUtil;

import java.util.ArrayList;
import java.util.List;


public class ReturnOrderActivity extends MainActivity implements IReturnOrderView, IPicker.OnSelectedListener {

    private Spinner reasonSp;
    private EditText amountEt;
    private EditText explianEt;
    private TextView returnAmountTv;
    private ImageView addImage;
    private LinearLayout picLinear;
    private TextView picInfoTv;
    private TextView sumitTv;
    private MyOrderImpl myOrderImpl;
    private String orderId;
    private String productId;
    private String amount;
    private String reasonId;
    private MyOrderReturn myOrderReturn;


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
        return R.layout.mystore_order_return;
    }

    @Override
    protected void handleIntent(Intent intent) {
        super.handleIntent(intent);
        orderId = intent.getStringExtra("order_id");
        productId = intent.getStringExtra("product_id");
        amount = intent.getStringExtra("order_amount");
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setActionBar();
        mTitleName.setText("退款");
        reasonSp = (Spinner) findViewById(R.id.return_product_detail_reason_sp);
        amountEt = (EditText) findViewById(R.id.return_order_amount_tv);
        amountEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                double pay = 0;
                if (!TextUtils.isEmpty(amount)) {
                    pay = Double.parseDouble(amount);
                }
                double input = 0;
                if (!TextUtils.isEmpty(editable.toString())) {
                    input = Double.parseDouble(editable.toString());
                }

                if (input > pay) {
                    amountEt.setText("");
                    Toast.makeText(ReturnOrderActivity.this, "退款金额不能大于" + amount, Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        });
        returnAmountTv = (TextView) findViewById(R.id.return_amount_tv);
        explianEt = (EditText) findViewById(R.id.return_order_explian_et);
        LinearLayout addImageLayout = (LinearLayout) findViewById(R.id.return_order_add_ll);
        addImage = (ImageView) findViewById(R.id.return_order_add_iv);
        picLinear = (LinearLayout) findViewById(R.id.return_order_pic_container);
        picInfoTv = (TextView) findViewById(R.id.return_order_add_pic_info);
        sumitTv = (TextView) findViewById(R.id.return_order_sumit);
        addImageLayout.setOnClickListener(this);
        addImage.setOnClickListener(this);
        sumitTv.setOnClickListener(this);
        initReasonSpiner();
        returnAmountTv.setText("退款金额" + "(¥" + amount + ")");
        myOrderReturn = new MyOrderReturn();
        myOrderImpl = new MyOrderImpl(this);
        myOrderImpl.setiReturnOrderView(this);
    }

    private void initReasonSpiner() {

        final List<RefundReason> refundReasonList = new ArrayList<RefundReason>();
        RefundReason refundReason1 = new RefundReason();
        refundReason1.setId("PRODUCT_DAMAGED");
        refundReason1.setReason("商品破损");

        RefundReason refundReason2 = new RefundReason();
        refundReason2.setId("PRODUCT_MISS");
        refundReason2.setReason("商品缺失");

        RefundReason refundReason3 = new RefundReason();
        refundReason3.setId("PRODUCT_MISSMATCH");
        refundReason3.setReason("商品与描述不符");

        RefundReason refundReason4 = new RefundReason();
        refundReason4.setId("PRODUCT_QUALITY");
        refundReason4.setReason("商品质量问题");

        RefundReason refundReason5 = new RefundReason();
        refundReason5.setId("NOT_RECEIVED");
        refundReason5.setReason("未收到商品");


        refundReasonList.add(refundReason1);
        refundReasonList.add(refundReason2);
        refundReasonList.add(refundReason3);
        refundReasonList.add(refundReason4);
        refundReasonList.add(refundReason5);

        ArrayList<String> reasonList = new ArrayList<>();
        for (int i = 0; i < refundReasonList.size(); i++) {
            reasonList.add(refundReasonList.get(i).getReason());
        }
        ArrayAdapter adapter = new ArrayAdapter<String>(this, R.layout.spinner_list_item, reasonList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        reasonId = refundReasonList.get(0).getId();
        reasonSp.setAdapter(adapter);
        reasonSp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                reasonId = refundReasonList.get(i).getId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    /**
     * 提交退款申请
     */
    private void sumitReturnData() {
//        MyOrderReturn myOrderReturn = new MyOrderReturn();
        String desc = explianEt.getText().toString();
        double amount = 0;
        try {
            amount = Double.parseDouble(amountEt.getText().toString());
        } catch (Throwable e) {
            e.printStackTrace();
        }
        myOrderReturn.setReason(reasonId);
        myOrderReturn.setDesc(desc);
        myOrderReturn.setTotal(amount);
        myOrderReturn.setOrder_id(orderId);
        myOrderReturn.setProduct_id(productId);
        if (myOrderImpl != null) {
            myOrderImpl.returnOrder(myOrderReturn);
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.return_order_sumit:
                sumitReturnData();
                break;
            case R.id.return_order_add_iv:
                showDialog();
                break;
            case R.id.return_order_add_ll:
                showDialog();
                break;

        }
    }

    private void showDialog() {

        IPicker.setLimit(3);
        IPicker.setOnSelectedListener(this);
        IPicker.open(this);
    }

    @Override
    public void showReturnOrderFailedView(String msg) {
        ToastUtil.showShort(this, msg);
    }

    @Override
    public void showReturnOrderView() {
        ToastUtil.showShort(this, "申请成功");
        setResult(Activity.RESULT_OK);
        finish();
    }

    /**
     * 上传图片失败回调
     *
     * @param msg
     * @param position
     */
    @Override
    public void updateReturnImgFail(String msg, int position) {
        SnackbarUtil.showShort(mTitleName, msg);
    }

    /**
     * 上传图片每一张成功的回调
     *
     * @param img
     */
    @Override
    public void updateReturnImgNext(String img) {
        myOrderReturn.getImgs().add(img);
    }

    /**
     * 上传图片完成回调
     */
    @Override
    public void updateReturnImgCompleted() {
    }

    /**
     * 选择图片回调
     *
     * @param paths
     */
    @Override
    public void onSelected(List<String> paths) {

        fillPhotoContainer(paths);
        if (myOrderImpl != null) {
            myOrderImpl.uploadReturnImg(paths);
        }
    }

    /**
     * 填充图片凭证
     */
    private void fillPhotoContainer(final List<String> paths) {
        picInfoTv.setVisibility(View.GONE);
        picLinear.setVisibility(View.VISIBLE);
        picLinear.removeAllViews();

        for (int i = 0; i < paths.size(); i++) {
            final CommentImageView imageView = new CommentImageView(this);
            final String pic = paths.get(i);
            imageView.getDelView().setTag(i);
            imageView.getDelView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = (Integer) view.getTag();
                    paths.remove(position);
                    picLinear.removeViewAt(position);
                    myOrderReturn.getImgs().remove(position);
                    if (picLinear.getChildCount() == 0) {
                        picLinear.setVisibility(View.GONE);
                        picInfoTv.setVisibility(View.VISIBLE);
                    }
                }
            });
            imageView.showImage(pic);
            picLinear.addView(imageView);
        }
    }
}
