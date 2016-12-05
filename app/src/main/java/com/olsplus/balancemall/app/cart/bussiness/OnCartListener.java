package com.olsplus.balancemall.app.cart.bussiness;



import com.olsplus.balancemall.app.cart.bean.ShoppingBagVo;


public  interface OnCartListener {

    public void onCartCheckChange(boolean isChecked);

    public void onCartDelete();

    public void onCartBalance();

    public void onCartChildCheckChange(boolean isChecked);

    public void onCartEditChildCheckChange(boolean isChecked);

    public void onBottomCheckChanged(boolean isChecked);

    public void onBottomEditCheckChanged(boolean isChecked);

    public String getCartItemSelected(ShoppingBagVo shoppingBagVo);


}
