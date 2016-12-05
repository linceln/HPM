package com.olsplus.balancemall.app.mystore;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.liuguangqiang.ipicker.IPicker;
import com.olsplus.balancemall.R;
import com.olsplus.balancemall.app.mystore.config.BindPhoneActivity;
import com.olsplus.balancemall.app.mystore.request.UserImpl;
import com.olsplus.balancemall.app.mystore.view.IUserView;
import com.olsplus.balancemall.component.dialog.BottomActionDialog;
import com.olsplus.balancemall.component.view.CircleImageView;
import com.olsplus.balancemall.core.app.BaseFragment;
import com.olsplus.balancemall.core.app.MainActivity;
import com.olsplus.balancemall.core.image.ImageHelper;
import com.olsplus.balancemall.core.util.ApiConst;
import com.olsplus.balancemall.core.util.SPUtil;
import com.olsplus.balancemall.core.util.SnackbarUtil;
import com.olsplus.balancemall.core.util.ToastUtil;

import java.util.List;


public class UserInfoActivity extends MainActivity implements IUserView, IPicker.OnSelectedListener {

    private CircleImageView photoImage;
    private TextView gendarTv;
    private TextView phoneTv;
    private BottomActionDialog dialog;
    private String picPath;
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
        return R.layout.mystore_user_info;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setActionBar();
        mTitleName.setText("个人资料");
        initView();
    }

    private void initView() {
        findViewById(R.id.linearAvatar).setOnClickListener(this);
        RelativeLayout photoLinear = ((RelativeLayout) findViewById(R.id.myaccount_userinfo__userpic));
        LinearLayout gendarLinear = ((LinearLayout) findViewById(R.id.myaccount_gendar_layout));
        LinearLayout bindPhoneLinear = ((LinearLayout) findViewById(R.id.myaccount_bind_phone_layout));
        photoImage = (CircleImageView) findViewById(R.id.myaccount_user_photo);
        gendarTv = (TextView) findViewById(R.id.myaccount_gendar);
        phoneTv = (TextView) findViewById(R.id.myaccount_bind_phone);
        photoLinear.setOnClickListener(this);
        gendarLinear.setOnClickListener(this);
        bindPhoneLinear.setOnClickListener(this);
        initData();
    }

    private void initData() {
        userImpl = new UserImpl(this);
        userImpl.setUserView(this);
        picPath = Environment.getExternalStorageDirectory() + "/xiaobai/temp.jpg";
        String avatar = (String) SPUtil.get(this, SPUtil.AVATAR, "");
        String imageUrl = ApiConst.BASE_IMAGE_URL + avatar;
        Glide.with(this)
                .load(imageUrl)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(photoImage);

        gendarTv.setText((String) SPUtil.get(this, SPUtil.GENDER, ""));
        phoneTv.setText((String) SPUtil.get(this, SPUtil.PHONE, ""));
    }

    private void updateAvatar() {
        if (userImpl != null) {
//            showLoading(this, getString(R.string.loading_upload));
            userImpl.upLoadAvatar(picPath);
        }
    }

    @Override
    public void onSelected(List<String> paths) {
        if (paths != null && !paths.isEmpty()) {
            picPath = paths.get(0);
            updateAvatar();
        }
    }


    //    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == 333) {
//            File tempPicFile = new File(picPath);
//            try {
//                Intent v0 = new Intent("com.android.camera.action.CROP");
//                v0.setDataAndType(Uri.fromFile(tempPicFile), "image/*");
//                v0.putExtra("crop", "true");
//                v0.putExtra("aspectX", 1);
//                v0.putExtra("aspectY", 1);
//                v0.putExtra("outputX", 200);
//                v0.putExtra("outputY", 200);
//                v0.putExtra("scale", true);
//                v0.putExtra("output", Uri.fromFile(tempPicFile));
//                v0.putExtra("return-data", false);
//                v0.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
//                v0.putExtra("noFaceDetection", true);
//                startActivityForResult(v0, 444);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        } else if (requestCode == 444) {
//            if (resultCode == RESULT_OK) {
//                updateAvatar();
//            }
//        } else if (requestCode == 777) {
//            Uri uri = null;
//            if (data == null) {
//                return;
//            }
//            if (data.getData() != null) {
//                uri = data.getData();
//            }
//            if (uri == null) {
//                return;
//            }
//            File tempPicFile = new File(picPath);
//            Intent intent = new Intent("com.android.camera.action.CROP");
//            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
//                String url = FileUtil.getPath(this, uri);
//                intent.setDataAndType(Uri.fromFile(new File(url)), "image/*");
//            } else {
//                intent.setDataAndType(uri, "image/*");
//            }
//            intent.putExtra("crop", "true");
//            intent.putExtra("aspectX", 1);
//            intent.putExtra("aspectY", 1);
//            intent.putExtra("outputX", 200);
//            intent.putExtra("outputY", 200);
//            intent.putExtra("scale", true);
//            intent.putExtra("output", Uri.fromFile(tempPicFile));
//            intent.putExtra("return-data", false);
//            intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
//            intent.putExtra("noFaceDetection", true);
//            startActivityForResult(intent, 444);
//        }
//    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.linearAvatar:

                IPicker.setLimit(1);
                // TODO 头像大小200x200
//                IPicker.setCropEnable(true);
                IPicker.open(this);
                IPicker.setOnSelectedListener(this);
                break;
            case R.id.myaccount_gendar_layout:
                if (dialog != null) {
                    dialog.dismiss();
                    dialog = null;
                }
                dialog = new BottomActionDialog.Builder(this)
                        .addMenu("男", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                                if (userImpl != null) {
                                    userImpl.updateGender("男");
                                }
                            }
                        }).addMenu("女", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                                if (userImpl != null) {
                                    userImpl.updateGender("女");
                                }
                            }
                        }).create();

                dialog.show();
                break;
            case R.id.myaccount_bind_phone_layout:
                Intent intent = new Intent(this, BindPhoneActivity.class);
                startActivity(intent);
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (dialog != null) {
            dialog.dismiss();
            dialog = null;
        }
    }

    @Override
    public void updateAvatarFail(String msg) {
//        dismissLoading();
        SnackbarUtil.showShort(photoImage, msg);
    }

    @Override
    public void updateAvatarSuccess(String avatar) {
//        dismissLoading();
        ImageHelper.displayNoHolder(this, photoImage, avatar);
        SnackbarUtil.showShort(photoImage, "头像已更新");
    }

    @Override
    public void updateGenderFail(String msg) {
        ToastUtil.showShort(this, msg);
    }

    @Override
    public void updateGenderSuccess(String gender) {
        gendarTv.setText(gender);
    }


    @Override
    public void updatePasswordFail(String msg) {
        ToastUtil.showShort(this, msg);
    }

    @Override
    public void updatePasswordSuccess() {
        ToastUtil.showShort(this, "修改成功");
    }
}
