package com.olsplus.balancemall.app.mine.bean;


import java.io.Serializable;

public class CommentItem implements Serializable {
    private static final long serialVersionUID = 1L;

    private String product_id;
    private String remark;

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
