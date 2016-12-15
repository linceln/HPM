package com.olsplus.balancemall.app.mine.message.request;


import android.content.Context;

import com.olsplus.balancemall.app.mine.bean.MessageCenterInfoList;

public class MessageRequest implements IMessageRequest {

    private Context context;
    private MessageBuiness messageBuiness;
    private IShowMessageListView iShowMessageListView;

    public MessageRequest(Context context) {
        this.context = context;
        messageBuiness = new MessageBuiness(context);
    }

    public void setiShowMessageListView(IShowMessageListView iShowMessageListView) {
        this.iShowMessageListView = iShowMessageListView;
    }

    @Override
    public void getMessageList(int page, int count, final boolean isRefresh) {
        String pageStr = String.valueOf(page);
        String countStr = String.valueOf(count);
        messageBuiness.getMyMessageListData(pageStr, countStr, new OnGetMessageCallback() {
            @Override
            public void onGetMessageFailed(String msg) {
                if (iShowMessageListView != null) {
                    iShowMessageListView.showGetFailedView(msg);
                }
            }

            @Override
            public void onGetMessageSuccess(MessageCenterInfoList data) {
                if (iShowMessageListView != null) {
                    if (isRefresh) {
                        iShowMessageListView.showGetSuccessView(data.getMsgs());
                    } else {
                        iShowMessageListView.load(data.getMsgs());
                    }
                }
            }
        });

    }

    @Override
    public void readMessageRequest(String msgId) {
        messageBuiness.readMessageData(msgId, new OnReadMessageCallback() {
            @Override
            public void onReadFailed(String msg) {
                if (iShowMessageListView != null) {
                    iShowMessageListView.showReadFailedView(msg);
                }
            }

            @Override
            public void onReadSuccess() {
                if (iShowMessageListView != null) {
                    iShowMessageListView.showReadSuccessView();
                }
            }
        });
    }
}
