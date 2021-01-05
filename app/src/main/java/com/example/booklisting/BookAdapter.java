package com.example.booklisting;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class BookAdapter extends RecyclerView.Adapter<BookHolder> {

    private final ArrayList<Book> books;
    private final Context context;
    private int listItem;

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
            for (int i = 0; i < size; i++) {
                books.remove(0);
            }

            notifyItemRangeRemoved(0, size);
        }
    }
}

