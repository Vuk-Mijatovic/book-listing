package com.example.booklisting;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.loader.content.AsyncTaskLoader;

import java.util.List;

public class BookLoader extends AsyncTaskLoader<List<Book>> {
    String keyword;

    public BookLoader(@NonNull Context context, String keyword) {
        super(context);
        this.keyword = keyword;
    }

    @Nullable
    @Override
    public List<Book> loadInBackground() {
        if (keyword == null) {
            return null;
        }
        return QueryUtils.extractBooks(keyword);

    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }
}
