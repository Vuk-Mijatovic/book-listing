package com.example.booklisting;

import android.app.Activity;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.loader.content.AsyncTaskLoader;

import java.util.ArrayList;
import java.util.List;

import static com.example.booklisting.MainActivity.mainActivity;
import static com.google.android.material.internal.ContextUtils.getActivity;

public class BookLoader extends AsyncTaskLoader<List<Book>> {
    String keyword;
    int startIndex;
    ArrayList<Book> books;
    BookAdapter adapter;


    public BookLoader(@NonNull Context context, String keyword, int startIndex,
                      ArrayList<Book> books, BookAdapter adapter) {
        super(context);
        this.keyword = keyword;
        this.startIndex = startIndex;
        this.books = books;
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

        if ((adapter != null) && (adapter.getItemCount() > 0)) {

            mainActivity.getInstance().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    adapter.removeNullData();

                }
            });
        }
        return QueryUtils.extractBooks(keyword, startIndex, books);

    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }
}
