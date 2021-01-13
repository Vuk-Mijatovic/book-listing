package com.example.booklisting;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.loader.content.AsyncTaskLoader;

import java.util.ArrayList;
import java.util.List;

public class BookLoader extends AsyncTaskLoader<List<Book>> {
    String keyword;
    int startIndex;
    ArrayList<Book> books;

    public BookLoader(@NonNull Context context, String keyword, int startIndex, ArrayList<Book> books) {
        super(context);
        this.keyword = keyword;
        this.startIndex = startIndex;
        this.books = books;
    }



    @Nullable
    @Override
    public List<Book> loadInBackground() {
        if (keyword == null) {
            return null;
        }
        return QueryUtils.extractBooks(keyword, startIndex, books);

    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }
}
