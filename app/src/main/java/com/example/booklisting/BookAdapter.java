package com.example.booklisting;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class BookAdapter extends ArrayAdapter<Book> {


    public BookAdapter(@NonNull Activity context, @NonNull ArrayList<Book> books) {
        super(context, 0, books);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        Book currentBook = getItem(position);


        View listItem = convertView;
        if (convertView == null) {
            listItem = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }

        TextView authorView = listItem.findViewById(R.id.author_view);
        authorView.setText(currentBook.getAuthor());

        TextView titleView = listItem.findViewById(R.id.title_view);
        titleView.setText(currentBook.getTitle());


        ImageView imageView = listItem.findViewById(R.id.imageView);
        Picasso.get().load(currentBook.getImageUrl()).into(imageView);

        return listItem;

    }



}

