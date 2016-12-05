package com.olsplus.balancemall.app.bottom.request;



import com.olsplus.balancemall.app.bottom.view.IBottomView;

public class BottomImpl {

    private IBottomView view;

    public BottomImpl() {
    }

    public void setView(IBottomView view){
        this.view = view;
    }

    public void initBottomMenuView(){
        view.showBottomView();
    }

}
