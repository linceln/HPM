package com.olsplus.balancemall.component.recycler;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.List;


public abstract class BaseAdapter<T> extends RecyclerView.Adapter<CustomViewHolder> {

    protected Context context;
    protected List<T> list;

    public BaseAdapter(Context context, List<T> list) {

        this.context = context;
        this.list = list;
    }


    @Override
    public abstract CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType);

    @Override
    public abstract void onBindViewHolder(CustomViewHolder holder, int position);

    @Override
    public final int getItemCount() {
        return list == null ? 0 : list.size();
    }
}
