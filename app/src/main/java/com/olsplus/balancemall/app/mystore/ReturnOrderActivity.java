package com.olsplus.balancemall.app.mystore;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.olsplus.balancemall.R;
import com.olsplus.balancemall.app.mystore.bean.MyOrderReturn;
import com.olsplus.balancemall.app.mystore.bean.RefundReason;
import com.olsplus.balancemall.app.mystore.request.MyOrderImpl;
import com.olsplus.balancemall.app.mystore.view.CommentImageView;
import com.olsplus.balancemall.app.mystore.view.IReturnOrderView;
import com.olsplus.balancemall.component.dialog.BottomActionDialog;
import com.olsplus.balancemall.core.app.BaseFragment;
import com.olsplus.balancemall.core.app.MainActivity;
import com.olsplus.balancemall.core.util.BitmapUtil;
import com.olsplus.balancemall.core.util.FileUtil;
import com.olsplus.balancemall.core.util.ToastUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class ReturnOrderActivity extends MainActivity implements IReturnOrderView {

    private Spinner reasonSp;
    private EditText amountEt;
    private EditText explianEt;
    private TextView returnAmountTv;
    private ImageView addImage;
    private LinearLayout picLinear;
    private TextView picInfoTv;
    private TextView sumitTv;
    private BottomActionDialog dialog;

    private MyOrderImpl myOrderImpl;
    private String orderId;
    private String productId;
    private String amount;
    private String reasonId;
    private File file;
    private List<String> imagePathList = new ArrayList<String>();
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
        amount= intent.getStringExtra("order_amount");
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setActionBar();
        mTitleName.setText("退款");
        reasonSp = (Spinner) findViewById(R.id.return_product_detail_reason_sp);
        amountEt = (EditText) findViewById(R.id.return_order_amount_tv);
        returnAmountTv = (TextView)findViewById(R.id.return_amount_tv);
        explianEt = (EditText) findViewById(R.id.return_order_explian_et);
        LinearLayout addImageLayout = (LinearLayout) findViewById(R.id.return_order_add_ll);
        addImage = (ImageView) findViewById(R.id.return_order_add_iv);
        picLinear = (LinearLayout) findViewById(R.id.return_order_pic_container);
        picInfoTv  = (TextView) findViewById(R.id.return_order_add_pic_info);
        sumitTv = (TextView) findViewById(R.id.return_order_sumit);
        addImageLayout.setOnClickListener(this);
        addImage.setOnClickListener(this);
        sumitTv.setOnClickListener(this);
        initReasonSpiner();
        returnAmountTv.setText("退款金额"+"(¥"+amount+")");
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
        if(myOrderImpl!=null){
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
                showDioalg();
                break;
            case R.id.return_order_add_ll:
                showDioalg();
                break;

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 36) {
                FileUtil.compressImageFromFile(this, file);
                imagePathList.add(file.getAbsolutePath());
//                uploadRefundImage(file.getAbsolutePath());
                int count = imagePathList.size();
                if(myOrderImpl!=null && count <= 3){
                    myOrderImpl.uploadReturnImg(file.getAbsolutePath(),count-1);
                }
                fillPhotoContainer();

//                if (data != null) {
//                    Bundle extras = data.getExtras();
//                    Bitmap bitmap = (Bitmap) extras.get("data");
//                    fillPhotoContainer(bitmap, null);
//                }
            }
            if (requestCode == 37) {
                if (data != null) {
                    Uri uri = data.getData();
                    Bitmap bitmap = BitmapUtil.getBitmapFromUri(uri);
                    if (bitmap != null) {
                        try {
                            file = FileUtil.createPicCacheFile();
                            FileOutputStream outputStream = new FileOutputStream(file); // 文件输出流
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 70, outputStream);
                            try {
                                if (outputStream != null) {
                                    outputStream.close();
                                }
                                FileUtil.compressImageFromFile(this, file);
                                imagePathList.add(file.getAbsolutePath());
//                                uploadRefundImage(file.getAbsolutePath());
                                int count = imagePathList.size();
                                if(myOrderImpl!=null && count <= 3){
                                    myOrderImpl.uploadReturnImg(file.getAbsolutePath(),count-1);
                                }
                                fillPhotoContainer();
                            } catch (IOException e) {

                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }

            }
        }
    }

    /**
     * 填充图片凭证
     */
    private void fillPhotoContainer() {
        picInfoTv.setVisibility(View.GONE);
        picLinear.setVisibility(View.VISIBLE);
        int picCount = picLinear.getChildCount();
        if (picCount >= 3) {
            ToastUtil.showShort(this, "最多上传3张照片");
            return;
        }
        picLinear.removeAllViews();
//        int size = DensityUtil.setDensityMarge(this, 34f);
        int pathCount = imagePathList.size();
        for (int i = 0; i < pathCount; i++) {
            final CommentImageView imageView = new CommentImageView(this);
//            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(size, size);
//            layoutParams.setMargins(0, 0, 15, 0);
//            imageView.setLayoutParams(layoutParams);
//            imageView.setPadding(1, 1, 1, 1);
////            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
//            imageView.setBackgroundDrawable(this.getResources().getDrawable(R.drawable.comment_border));
            final String pic = imagePathList.get(i);
            imageView.getDelView().setTag(i);
            imageView.getDelView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = (Integer) view.getTag();
                    imagePathList.remove(position);
                    picLinear.removeViewAt(position);
                    myOrderReturn.getImgs().remove(position);
                    if(picLinear.getChildCount() == 0){
                        picLinear.setVisibility(View.GONE);
                        picInfoTv.setVisibility(View.VISIBLE);
                    }
                }
            });
            imageView.showImage(pic);
            picLinear.addView(imageView);
        }


    }

    private void showDioalg() {
//        View view = LayoutInflater.from(this).inflate(R.layout.comment_choose_photo_dialog,
//                null);
//        TextView v0 = (TextView) view.findViewById(R.id.camera_tv);
//        TextView v1 = (TextView) view.findViewById(R.id.gallery_tv);
//        final AlertDialog dialog = new AlertDialog.Builder(this).setView(view).show();
//        v0.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                file = FileUtil.createPicCacheFile();
//                Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
//                intent.putExtra("output", Uri.fromFile(file));
//                intent.putExtra("android.intent.extra.videoQuality", 0);
//                startActivityForResult(intent, 36);
//                dialog.dismissLoading();
//
//            }
//        });
//        v1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent();
//                intent.setType("image/*");
//                intent.setAction(Intent.ACTION_GET_CONTENT);
//                startActivityForResult(intent, 37);
//                dialog.dismissLoading();
//            }
//        });
        if (dialog != null) {
            dialog.dismiss();
            dialog = null;
        }
          dialog = new BottomActionDialog.Builder(this)
                .addMenu("拍照", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        file = FileUtil.createPicCacheFile();
                        Intent v2 = new Intent("android.media.action.IMAGE_CAPTURE");
                        v2.putExtra("output", Uri.fromFile(file));
                        v2.putExtra("android.intent.extra.videoQuality", 0);
                        startActivityForResult(v2, 36);
                        if (dialog != null) {
                            dialog.dismiss();
                        }
                    }
                }).addMenu("从手机相册选择", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent();
                        intent.setType("image/*");
                        intent.setAction(Intent.ACTION_GET_CONTENT);
                        startActivityForResult(intent, 37);
                        if (dialog != null) {
                            dialog.dismiss();
                        }
                    }
                }).create();

        dialog.show();
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

    @Override
    public void updateReturnImgFail(String msg,int position) {
        ToastUtil.showShort(this,"上传图片失败");
        picLinear.removeViewAt(position);
    }

    @Override
    public void updateReturnImgSuccess(String img) {
        myOrderReturn.getImgs().add(img);
    }
}
