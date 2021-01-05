package com.example.booklisting;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Book>> {
    String keyword;
    BookAdapter adapter;
    RecyclerView bookList;
    View progressBar;
    TextView emptyView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        progressBar = findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.GONE);


        //Get the keyword that user entered
        Button searchButton = findViewById(R.id.search_button);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!(adapter == null)) {
                    adapter.clear();
                }
                emptyView = findViewById(R.id.empty_list_item);
                emptyView.setVisibility(View.GONE);
                progressBar.setVisibility(View.VISIBLE);
                TextView searchView = findViewById(R.id.text_input);

                bookList = findViewById(R.id.book_list);
                LoaderManager loaderManager = getSupportLoaderManager();


                keyword = searchView.getText().toString().trim();
                if (keyword.length() == 0) {
                    keyword = " ";
                }
                ConnectivityManager cm =
                        (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
                boolean isConnected = activeNetwork != null &&
                        activeNetwork.isConnectedOrConnecting();

                if (isConnected) {
                    loaderManager.restartLoader(1, null, MainActivity.this);
                } else {

                    emptyView.setText(getResources().getString(R.string.no_internet_connection));
                }
            }
        });


    }

    @NonNull
    @Override
    public Loader<List<Book>> onCreateLoader(int id, @Nullable Bundle args) {
        return new BookLoader(MainActivity.this, keyword);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<List<Book>> loader, List<Book> list) {


        emptyView.setVisibility(View.GONE);
        progressBar.setVisibility(View.GONE);


        adapter = new BookAdapter(this, R.layout.list_item, (ArrayList<Book>) list);
        bookList = (RecyclerView) findViewById(R.id.book_list);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);

        bookList.setLayoutManager(layoutManager);
        bookList.setAdapter(adapter);
        if (list.isEmpty()) {
            emptyView = findViewById(R.id.empty_list_item);
            emptyView.setVisibility(View.VISIBLE);
            emptyView.setText(R.string.no_books_found);
        }


    }

    @Override
    public void onLoaderReset(@NonNull Loader<List<Book>> loader) {
        adapter.clear();


    }
}