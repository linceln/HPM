package com.olsplus.balancemall.app.mine.message;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.LinearLayout;

import com.jude.easyrecyclerview.EasyRecyclerView;
import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.jude.easyrecyclerview.decoration.DividerDecoration;
import com.olsplus.balancemall.R;
import com.olsplus.balancemall.app.mine.adapter.MessageAdapter;
import com.olsplus.balancemall.app.mine.bean.MessageCenterInfo;
import com.olsplus.balancemall.app.mine.message.request.MessageRequest;
import com.olsplus.balancemall.app.mine.message.request.IShowMessageListView;
import com.olsplus.balancemall.app.pay.voucher.MyCouponActivity;
import com.olsplus.balancemall.app.web.WebActivity;
import com.olsplus.balancemall.core.app.BaseFragment;
import com.olsplus.balancemall.core.app.MainActivity;
import com.olsplus.balancemall.core.exception.layout.DefaultExceptionListener;
import com.olsplus.balancemall.core.exception.layout.ExceptionManager;
import com.olsplus.balancemall.core.util.DensityUtil;
import com.olsplus.balancemall.core.util.ToastUtil;

import java.util.List;

import io.realm.Realm;


public class MyMessageActivity extends MainActivity implements SwipeRefreshLayout.OnRefreshListener,
        RecyclerArrayAdapter.OnLoadMoreListener, IShowMessageListView, MessageAdapter.OnMessageItemClickListener {

    private LinearLayout nullLinearLayout;
    private EasyRecyclerView messageListView;
    private MessageAdapter messageAdapter;

    private MessageRequest messageRequest;
    private int pageNo = 1;// 第几页
    private int pageSize = 10;// 每页多少条数据
    private ExceptionManager manager;
    private Realm realm;

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
        return R.layout.mystore_mymessage;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Realm.init(this);
        realm = Realm.getDefaultInstance();
        setActionBar();
        mTitleName.setText("消息");
        nullLinearLayout = (LinearLayout) findViewById(R.id.my_message_null_linear);
        messageListView = (EasyRecyclerView) findViewById(R.id.my_message_listview);
        messageListView.setLayoutManager(new LinearLayoutManager(this));
        DividerDecoration itemDecoration = new DividerDecoration(getResources().getColor(R.color.divider_line_color), DensityUtil.dp2px(this, 1f), DensityUtil.dp2px(this, 60f), 0);
        messageListView.addItemDecoration(itemDecoration);
        messageListView.setErrorView(R.layout.error_layout);
        messageAdapter = new MessageAdapter(this, realm);
        messageAdapter.setMore(R.layout.load_more_layout, this);
        messageAdapter.setOnMessageItemClickListener(this);
        messageListView.setAdapterWithProgress(messageAdapter);
        messageListView.setRefreshListener(this);
        manager = ExceptionManager.initialize(messageListView, new DefaultExceptionListener(this));
        initData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.close();
    }

    private void initData() {
        messageRequest = new MessageRequest(this);
        messageRequest.setiShowMessageListView(this);
        messageRequest.getMessageList(pageNo, pageSize, true);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.retry:
            case R.id.empty:
                onRefresh();
                break;
        }
    }

    @Override
    public void onRefresh() {
        pageNo = 1;
        messageRequest.getMessageList(pageNo, pageSize, true);
    }

    @Override
    public void showGetFailedView(String msg) {
        ToastUtil.showShort(this, msg);
//        messageListView.showError();
        manager.showRetry();

    }

    @Override
    public void showGetSuccessView(List<MessageCenterInfo> datas) {
        nullLinearLayout.setVisibility(View.GONE);
        messageAdapter.clear();
        messageAdapter.addAll(datas);
        if (messageAdapter.getAllData().size() == 0) {
            manager.showEmpty();
        } else {
            manager.hide();
        }
    }

    @Override
    public void onLoadMore() {
        pageNo++;
        messageRequest.getMessageList(pageNo, pageSize, false);
    }

    @Override
    public void load(List<MessageCenterInfo> data) {
        data.addAll(data);
        messageAdapter.addAll(data);
    }

    @Override
    public void showReadFailedView(String msg) {
        ToastUtil.showShort(this, msg);
    }

    @Override
    public void showReadSuccessView() {
        //读取消息成功则刷新界面
        if (messageRequest != null) {
            messageRequest.getMessageList(pageNo, pageSize, true);
        }
    }

    @Override
    public void onItemClick(int position, BaseViewHolder holder) {
        MessageCenterInfo messageCenterInfo = messageAdapter.getItem(position);
        if (messageCenterInfo != null) {
            String link = messageCenterInfo.getLink();
            if (link.startsWith("http://") || link.startsWith("https://")) {
                Intent newIntent = new Intent(this, WebActivity.class);
                newIntent.putExtra("url", link);
                startActivity(newIntent);
            } else if (link.startsWith("app://")) {
                if (link.contains("voucher")) {
                    Intent newIntent = new Intent(this, MyCouponActivity.class);
                    startActivity(newIntent);
                }
            }
            // 是否已读
//            if (messageRequest != null) {
//                messageRequest.readMessageRequest(messageCenterInfo.getId());
//            }
        }
    }
}
