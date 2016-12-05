package com.olsplus.balancemall.app.mystore;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;

import com.jude.easyrecyclerview.EasyRecyclerView;
import com.olsplus.balancemall.R;
import com.olsplus.balancemall.app.mystore.adapter.CommentUploadAdapter;
import com.olsplus.balancemall.app.mystore.bean.CommentGroup;
import com.olsplus.balancemall.app.mystore.bean.CommentItem;
import com.olsplus.balancemall.app.mystore.bean.MyOrderInfo;
import com.olsplus.balancemall.app.mystore.request.MyOrderImpl;
import com.olsplus.balancemall.app.mystore.view.ICommentView;
import com.olsplus.balancemall.core.app.BaseFragment;
import com.olsplus.balancemall.core.app.MainActivity;
import com.olsplus.balancemall.core.util.ToastUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class CommentUploadActivity extends MainActivity implements ICommentView{

    private EasyRecyclerView listView;
    private CommentUploadAdapter commentUploadAdapter;
    private Button submitBtn;

    private ArrayList<MyOrderInfo> myOrderInfoArrayList;
    private String orderId;

    private MyOrderImpl myOrderImpl;

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
        return  R.layout.action_bar_view;
    }

    @Override
    protected int getContentViewId() {
        return  R.layout.comment_upload_list_activity;
    }

    @Override
    protected void handleIntent(Intent intent) {
        super.handleIntent(intent);
        myOrderInfoArrayList = (ArrayList<MyOrderInfo>)intent.getSerializableExtra("comment_product_list");
        orderId = intent.getStringExtra("order_id");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setActionBar();
        mTitleName.setText("发表评论");
        listView = (EasyRecyclerView)findViewById(R.id.comment_listview);
        listView.setLayoutManager(new LinearLayoutManager(this));
//        DividerDecoration itemDecoration = new DividerDecoration(getResources().getColor(R.color.divider_line_color), DensityUtil.dp2px(this, 10f), 0, 0);
//        listView.addItemDecoration(itemDecoration);
        commentUploadAdapter = new CommentUploadAdapter(this);
        listView.setAdapterWithProgress(commentUploadAdapter);
        submitBtn = (Button) findViewById(R.id.submit_btn);
        submitBtn.setOnClickListener(this);
        initData();
    }

    private void initData(){
        myOrderImpl = new MyOrderImpl(this);
        myOrderImpl.setiCommentView(this);
        if(myOrderInfoArrayList!=null){
            commentUploadAdapter.addAll(myOrderInfoArrayList);
        }

    }



    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.submit_btn:
                if(myOrderInfoArrayList!=null){
                    Map<Integer, String> commentMap = commentUploadAdapter.getCommentMap();
                    int count = myOrderInfoArrayList.size();
                    CommentGroup commentGroup = new CommentGroup();
                    List<CommentItem> suborders = new ArrayList<CommentItem>();
                    for (int i = 0; i < count; i++) {
                        String text = commentMap.get(i);
                        if (TextUtils.isEmpty(text)) {
                            ToastUtil.showShort(this,"还没有输入评价内容~");
                            return;
                        }
                        MyOrderInfo myOrderInfo = myOrderInfoArrayList.get(i);
                        CommentItem commentItem = new CommentItem();
                        commentItem.setProduct_id(myOrderInfo.getProduct_id());
                        commentItem.setRemark(text);
                        suborders.add(commentItem);
                    }
                    commentGroup.setOrder_id(orderId);
                    commentGroup.setSuborders(suborders);
                    if(myOrderImpl!=null){
                        myOrderImpl.sumitComment(commentGroup);
                    }
                }


                break;
        }
    }

    @Override
    public void showSumitCommentFailedView(String msg) {
        ToastUtil.showShort(this,"提交失败");
    }

    @Override
    public void showSumitCommentView() {
        ToastUtil.showShort(this,"提交成功");
        setResult(RESULT_OK);
        finish();
    }
}
