package com.example.booklisting;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.loader.content.AsyncTaskLoader;

import java.util.List;

public class BookLoader extends AsyncTaskLoader <BookLoaderResult> {
    String query;
    int startIndex;
    BookAdapter adapter;

    public BookLoader(@NonNull Context context, String query, BookAdapter adapter) {
        super(context);
        this.query = query;
        this.startIndex = startIndex;
        this.adapter = adapter;
    }

    @Nullable
    @Override
    public BookLoaderResult loadInBackground() {
        if (query == null) { return null; }

        BookLoaderResult bookLoaderResult = new BookLoaderResult();
        try {
            List<Book> result = QueryUtils.extractBooks(query);
            bookLoaderResult.setResult(result);
        } catch (Exception e) {
            bookLoaderResult.setException(e);
        }
        return bookLoaderResult;

    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }
}