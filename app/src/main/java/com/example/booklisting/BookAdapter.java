package com.example.booklisting;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import static com.google.android.material.internal.ContextUtils.getActivity;
import static java.security.AccessController.getContext;

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.BookHolder> {


    private static final int VIEW_TYPE_ITEM = 1;
    private static final int VIEW_TYPE_LOADING = 0;
    private final ArrayList<Book> books;
    private final Context context;
    private final int listItem;

    public BookAdapter(Context context, int listItem, ArrayList<Book> books) {
        this.context = context;
        this.listItem = listItem;
        this.books = books;
    }


    @Override
    public int getItemViewType(int position) {
        if (books.get(position) != null) {
            return VIEW_TYPE_ITEM;
        } else {
            return VIEW_TYPE_LOADING;
        }
    }


    @NonNull
    @Override
    public BookHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = null;
        if (viewType == VIEW_TYPE_LOADING) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.raw_progress, parent, false);
            return new ProgressViewHolder(context, view);
        } else {
            // Inflate the view and return the new ViewHolder
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_item, parent, false);
            return new DataViewHolder(context, view);
        }

    }


    @Override
    public void onBindViewHolder(@NonNull BookHolder holder, int position) {
        if (holder instanceof DataViewHolder) {
            //  Use position to access the correct Book object
            Book currentBook = this.books.get(position);
            //  Bind the book object to the holder
            holder.bindBook(currentBook);
        }

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

    public void addNullData() {
        books.add(null);
        notifyItemInserted(books.size() - 1);
    }

    public void removeNullData() {
        books.remove(books.size() - 1);
        notifyItemRemoved(books.size());
    }


    class BookHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final TextView authorView;
        private final TextView titleView;
        private final ImageView imageView;
        private final TextView descriptionView;

        private Book currentBook;
        private Context context;


        public BookHolder(Context context, @NonNull View itemView) {
            super(itemView);

            this.context = context;

            this.authorView = itemView.findViewById(R.id.author_view);
            this.titleView = itemView.findViewById(R.id.title_view);
            this.imageView = itemView.findViewById(R.id.imageView);
            this.descriptionView = itemView.findViewById(R.id.description_view);

            itemView.setOnClickListener(this);

        }


        public void bindBook(Book currentBook) {
            this.currentBook = currentBook;
            this.authorView.setText(currentBook.getAuthor());
            this.titleView.setText(currentBook.getTitle());
            this.descriptionView.setText(currentBook.getDescription());
            Picasso.get().load(currentBook.getImageUrl()).into(this.imageView);

        }


        @Override
        public void onClick(View view) {

            Uri webPage = Uri.parse(currentBook.getWebPage());
            Intent openPage = new Intent(Intent.ACTION_VIEW, webPage);
            context.startActivity(openPage);
        }
    }

    private class DataViewHolder extends BookAdapter.BookHolder {


        public DataViewHolder(Context context, @NonNull View itemView) {
            super(context, itemView);
        }
    }

    private class ProgressViewHolder extends BookAdapter.BookHolder {


        public ProgressViewHolder(Context context, @NonNull View itemView) {
            super(context, itemView);
        }
    }


}

