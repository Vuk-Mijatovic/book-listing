package com.example.booklisting;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public abstract class EndlessOnScrollListener extends RecyclerView.OnScrollListener {

    LinearLayoutManager layoutManager;
    int previousTotalItemCount = 0;
    boolean loading = true;
    int visibleThreshold = 10;

    public EndlessOnScrollListener(LinearLayoutManager layoutManager) {
        super();
        this.layoutManager = layoutManager;
    }

    @Override
    public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);

        int lastVisibleItemPosition = layoutManager.findLastCompletelyVisibleItemPosition();
        int totalItemCount = layoutManager.getItemCount();

        if (loading && (totalItemCount > previousTotalItemCount)) {
            loading = false;
            previousTotalItemCount = totalItemCount;
        }

        if (!loading && (lastVisibleItemPosition + visibleThreshold == totalItemCount)) {
            onLoadMore();
            loading = true;
        }
    }

    public abstract void onLoadMore();

}


