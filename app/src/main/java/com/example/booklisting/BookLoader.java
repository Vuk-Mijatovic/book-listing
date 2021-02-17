package com.example.booklisting;
import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.loader.content.AsyncTaskLoader;
import java.util.ArrayList;
import java.util.List;

public class BookLoader extends AsyncTaskLoader <BookLoaderResult> {
    String keyword;
    int startIndex;
    BookAdapter adapter;

    public BookLoader(@NonNull Context context, String keyword, BookAdapter adapter) {
        super(context);
        this.keyword = keyword;
        this.startIndex = startIndex;
        this.adapter = adapter;
    }

    @Nullable
    @Override
    public BookLoaderResult loadInBackground() {
        if (keyword == null) { return null; }

        BookLoaderResult bookLoaderResult = new BookLoaderResult();
        try {
            List<Book> result = QueryUtils.extractBooks(keyword);
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