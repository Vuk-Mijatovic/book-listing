package com.example.booklisting;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

public class BookHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    private final TextView authorView;
    private final TextView titleView;
    private final ImageView imageView;

    private Book currentBook;
    private Context context;




    public BookHolder(Context context, @NonNull View itemView) {
        super(itemView);

        this.context = context;

        this.authorView = itemView.findViewById(R.id.author_view);
        this.titleView = itemView.findViewById(R.id.title_view);
        this.imageView = itemView.findViewById(R.id.imageView);

        itemView.setOnClickListener(this);

    }

    public void bindBook (Book currentBook) {
        this.currentBook = currentBook;
        this.authorView.setText(currentBook.getAuthor());
        this.titleView.setText(currentBook.getTitle());
        Picasso.get().load(currentBook.getImageUrl()).into(this.imageView);
    }



    @Override
    public void onClick(View view) {

        Uri webPage = Uri.parse(currentBook.getWebPage());
        Intent openPage = new Intent(Intent.ACTION_VIEW, webPage);
        context.startActivity(openPage);


    }
}
