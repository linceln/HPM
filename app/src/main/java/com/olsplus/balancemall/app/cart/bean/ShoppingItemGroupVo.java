package com.olsplus.balancemall.app.cart.bean;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ShoppingItemGroupVo implements Serializable {
    private static final long serialVersionUID = 7354715381405289288L;
    private long store_id;
    private String store_name;
    private String free_freight_list;
    private List<ShoppingCartItemVo> goods = new ArrayList<ShoppingCartItemVo>();

    public long getStore_id() {
        return store_id;
    }

    public void setStore_id(long store_id) {
        this.store_id = store_id;
    }

    public String getStore_name() {
        return store_name;
    }

    public void setStore_name(String store_name) {
        this.store_name = store_name;
    }

    public String getFree_freight_list() {
        return free_freight_list;
    }

    public void setFree_freight_list(String free_freight_list) {
        this.free_freight_list = free_freight_list;
    }

    public List<ShoppingCartItemVo> getGoods() {
        return goods;
    }

    public void setGoods(List<ShoppingCartItemVo> goods) {
        this.goods = goods;
    }
}
