package com.example.booklisting;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.loader.content.AsyncTaskLoader;

import com.google.android.material.internal.ContextUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.example.booklisting.MainActivity.mainActivity;
import static com.google.android.material.internal.ContextUtils.*;

public class BookLoader extends AsyncTaskLoader<List<Book>> {
    String keyword;
    int startIndex;
    BookAdapter adapter;


    public BookLoader(@NonNull Context context, String keyword, int startIndex, BookAdapter adapter) {
        super(context);
        this.keyword = keyword;
        this.startIndex = startIndex;
        this.adapter = adapter;
    }


    @Nullable
    @Override
    public List<Book> loadInBackground() {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (keyword == null) {
            return null;
        }
        return QueryUtils.extractBooks(keyword, startIndex);
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }
}
