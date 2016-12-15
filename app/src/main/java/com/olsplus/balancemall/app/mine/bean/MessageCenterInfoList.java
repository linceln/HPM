package com.olsplus.balancemall.app.mine.bean;


import com.olsplus.balancemall.core.bean.BaseResultEntity;

import java.util.List;


public class MessageCenterInfoList extends BaseResultEntity {

    private List<MessageCenterInfo> msgs;

    public List<MessageCenterInfo> getMsgs() {
        return msgs;
    }

    public void setMsgs(List<MessageCenterInfo> msgs) {
        this.msgs = msgs;
    }
}
