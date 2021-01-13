package com.example.booklisting;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class BookAdapter extends RecyclerView.Adapter<BookHolder> {

    private final ArrayList<Book> books;
    private final Context context;
    private final int listItem;

    public BookAdapter(Context context, int listItem, ArrayList<Book> books) {
        this.context = context;
        this.listItem = listItem;
        this.books = books;
    }


    @NonNull
    @Override
    public BookHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        // Inflate the view and return the new ViewHolder
        View view = LayoutInflater.from(parent.getContext())
                .inflate(this.listItem, parent, false);
        return new BookHolder(this.context, view);
    }


    @Override
    public void onBindViewHolder(@NonNull BookHolder holder, int position) {
        //  Use position to access the correct Book object
        Book currentBook = this.books.get(position);
        //  Bind the book object to the holder
        holder.bindBook(currentBook);

    }

    @Override
    public int getItemCount() {
        return this.books.size();
    }

    public void clear() {
        int size = books.size();
        if (size > 0) {
            books.subList(0, size).clear();

            notifyItemRangeRemoved(0, size);
        }
    }
}

