package com.example.booklisting;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Book>> {
    String keyword = "magic";
    BookAdapter adapter;
    ListView bookList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        LoaderManager loaderManager = getSupportLoaderManager();
        // loaderManager.initLoader(1, null, MainActivity.this);


        //Get the keyword that user entered
        Button searchButton = findViewById(R.id.search_button);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView searchView = findViewById(R.id.text_input);

                keyword = searchView.getText().toString().trim();
                if (keyword.length() == 0) {
                    keyword = " ";
                }

                loaderManager.restartLoader(1, null, MainActivity.this);


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
        adapter = new BookAdapter(this, (ArrayList<Book>) list);
        bookList = findViewById(R.id.book_list);

        bookList.setAdapter(adapter);
        bookList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Book currentBook = adapter.getItem(i);
                Uri webPage = Uri.parse(currentBook.getWebPage());
                Intent openPage = new Intent(Intent.ACTION_VIEW, webPage);
                startActivity(openPage);

            }
        });

    }

    @Override
    public void onLoaderReset(@NonNull Loader<List<Book>> loader) {
        adapter.clear();


    }
}