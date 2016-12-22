package com.olsplus.balancemall.app.mine.view;


import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.olsplus.balancemall.R;
import com.olsplus.balancemall.app.mine.bean.MessageCenterInfo;
import com.olsplus.balancemall.core.util.DateUtil;

import java.util.Date;

public class MessageViewHolder extends BaseViewHolder<MessageCenterInfo> {

    private ImageView messageIv;
    private TextView messageTv;
    private TextView reamrkMessageTv;
    private TextView timeTv;
    private ImageView message_dot;

    public MessageViewHolder(ViewGroup parent) {
        super(parent, R.layout.mystore_mymessage_list_item);
        messageIv = $(R.id.message_icon);
        messageTv = $(R.id.message_tv);
        reamrkMessageTv = $(R.id.message_sub_tv);
        timeTv = $(R.id.message_time_tv);
        message_dot = $(R.id.message_dot);
    }

    @Override
    public void setData(MessageCenterInfo data) {
        super.setData(data);
        if (data != null) {

            if (data.isRead()) {
                message_dot.setVisibility(View.GONE);
            } else {
                message_dot.setVisibility(View.VISIBLE);
            }

            Glide.with(getContext()).load(getMessageRes(data.getType())).into(messageIv);
            messageTv.setText(data.getTitle());
            reamrkMessageTv.setText(data.getInfo());
            String dateStr = data.getTime();
            boolean isSameDay = DateUtil.isSameDay(dateStr);
            boolean isSameYear = DateUtil.isSameYear(dateStr);
            Date date = DateUtil.parse(dateStr);
            if (isSameDay) {
                timeTv.setText(DateUtil.date2Str(date, DateUtil.FORMAT_HM));
            } else if (isSameYear) {
                timeTv.setText(DateUtil.date2Str(date, "MM-dd"));
            } else {
                timeTv.setText(DateUtil.date2Str(date, DateUtil.FORMAT_YMD));
            }
        }
    }

    private int getMessageRes(String type) {
        if (type.equals("NOTICE")) {
            return R.drawable.message_icon4;
        } else if (type.equals("POINT")) {
            return R.drawable.message_icon5;
        } else if (type.equals("VOUCHER")) {
            return R.drawable.message_icon2;
        } else if (type.equals("REMARK")) {
            return R.drawable.message_icon3;
        } else if (type.equals("ACTIVITY")) {
            return R.drawable.message_icon1;
        }
        return -1;
    }
}
