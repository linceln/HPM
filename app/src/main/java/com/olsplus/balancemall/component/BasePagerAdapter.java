package com.olsplus.balancemall.component;


import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import java.util.List;

public class BasePagerAdapter<T> extends PagerAdapter {

    private boolean isCycleFlow = false;
    protected final List<T> viewCache;

    public BasePagerAdapter(List viewCache)
    {
        this.viewCache = viewCache;
    }

    public void isCycleFlow(boolean isCycleFlow) {
        this.isCycleFlow = isCycleFlow;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View)object);
    }

    @Override
    public int getItemPosition(Object object) {
        return super.getItemPosition(object);
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        if(viewCache==null){
            return 0;
        }
        return viewCache.size();
    }

    public final int getRealCount()
    {
        return this.viewCache.size();
    }

    public final int getRealPosition(int paramInt)
    {
        return paramInt % getRealCount();
    }


    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        position =  getRealPosition(position);
        if (position<0){
            position = viewCache.size()+position;
        }
        Object object = viewCache.get(position);
        if(object instanceof  View) {
            ViewParent vp = ((View)object).getParent();
            if (vp != null) {
                ViewGroup parent = (ViewGroup) vp;
                parent.removeView(((View)object));
            }
            container.addView(((View)object));
            return object;
        }
        return null;
    }

    @Override
    public boolean isViewFromObject(View view, Object obj) {
        return view == obj;
    }
}
