package com.olsplus.balancemall.app.mystore.bean;


import java.io.Serializable;
import java.util.List;

public class CommentGroup implements Serializable {
    private static final long serialVersionUID = 1L;

    private String  order_id;
    private List<CommentItem> suborders;

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public List<CommentItem> getSuborders() {
        return suborders;
    }

    public void setSuborders(List<CommentItem> suborders) {
        this.suborders = suborders;
    }
}
