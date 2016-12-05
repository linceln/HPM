package com.olsplus.balancemall.component.recycler;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

public class EndlessRecyclerOnScrollListener extends RecyclerView.OnScrollListener {

    private int previousTotal = 0;

    private boolean loading = true;

    int firstVisibleItem, visibleItemCount, totalItemCount;

    private int currentPage = 1;

    private LinearLayoutManager mLinearLayoutManager;
    private LoadMoreListener listener;

    public EndlessRecyclerOnScrollListener(LinearLayoutManager linearLayoutManager, LoadMoreListener listener) {

        this.mLinearLayoutManager = linearLayoutManager;
        this.listener = listener;
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {

        super.onScrolled(recyclerView, dx, dy);

        visibleItemCount = recyclerView.getChildCount();
        totalItemCount = mLinearLayoutManager.getItemCount();
        firstVisibleItem = mLinearLayoutManager.findFirstVisibleItemPosition();

        if (loading) {
            if (totalItemCount > previousTotal) {
                loading = false;
                previousTotal = totalItemCount;
            }
        }
        if (!loading && (totalItemCount - visibleItemCount) <= firstVisibleItem) {
            currentPage++;
//            onLoadMore(currentPage);
            if (listener != null) {
                listener.onLoadMore(currentPage);
            }
            loading = true;
        }
    }

    public interface LoadMoreListener {
        void onLoadMore(int currentPage);
    }
//    public abstract void onLoadMore(int currentPage);
}

