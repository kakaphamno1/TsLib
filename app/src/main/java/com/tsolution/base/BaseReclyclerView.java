package com.tsolution.base;


import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tsolution.base.listener.ILoadMore;

public class BaseReclyclerView extends RecyclerView {
    int lastVisibleItem,totalItemCount;
    int visibleThreshold=2;
    boolean isLoading;
    ILoadMore loadMore = null;

    public BaseReclyclerView(@NonNull Context context) {
        super(context);
        this.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                super.onScrolled(recyclerView, dx, dy);
                totalItemCount = linearLayoutManager.getItemCount();
                lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
                if (!isLoading && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
                    if (loadMore != null)
                        loadMore.onLoadMore();
                    isLoading = true;
                }

            }
        });
    }
    public void setLoadMore(ILoadMore loadMore) {
        this.loadMore = loadMore;
    }
}
