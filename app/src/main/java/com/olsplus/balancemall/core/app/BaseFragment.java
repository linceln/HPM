package com.olsplus.balancemall.core.app;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.olsplus.balancemall.core.util.DialogHelper;
import com.trello.rxlifecycle.components.support.RxFragment;


public abstract class BaseFragment extends RxFragment {

    protected DialogHelper mDialogHelper;

    protected BaseActivity mActivity;

    protected ViewGroup rootView;

    protected  LayoutInflater mInflater;

    protected abstract void initView(View view, Bundle savedInstanceState);

    //获取fragment布局文件ID
    protected abstract int getLayoutId();

    //获取宿主Activity
    protected BaseActivity getHoldingActivity() {
        return mActivity;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.mActivity = (BaseActivity) activity;
    }

    //添加fragment
    protected void addFragment(BaseFragment fragment) {
        if (null != fragment) {
            getHoldingActivity().addFragment(fragment);
        }
    }

    //移除fragment
    protected void removeFragment() {
        getHoldingActivity().removeFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mDialogHelper = new DialogHelper(mActivity);
        View view = inflater.inflate(getLayoutId(), container, false);
        rootView = (ViewGroup)view;
        mInflater = inflater;
        initView(view, savedInstanceState);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }


}
