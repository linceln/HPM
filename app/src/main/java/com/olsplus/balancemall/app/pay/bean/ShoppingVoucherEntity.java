package com.olsplus.balancemall.app.pay.bean;



import java.io.Serializable;

public class ShoppingVoucherEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;
    private double voucher_value;
    private int min_spend;
    private String apply_service_name;
    private long start_time;
    private long end_time;
    private boolean isSelected;

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public double getVoucher_value() {
        return voucher_value;
    }

    public void setVoucher_value(double voucher_value) {
        this.voucher_value = voucher_value;
    }

    public int getMin_spend() {
        return min_spend;
    }

    public void setMin_spend(int min_spend) {
        this.min_spend = min_spend;
    }

    public String getApply_service_name() {
        return apply_service_name;
    }

    public void setApply_service_name(String apply_service_name) {
        this.apply_service_name = apply_service_name;
    }

    public long getStart_time() {
        return start_time;
    }

    public void setStart_time(long start_time) {
        this.start_time = start_time;
    }

    public long getEnd_time() {
        return end_time;
    }

    public void setEnd_time(long end_time) {
        this.end_time = end_time;
    }
}
