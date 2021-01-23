package com.example.booklisting;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
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
    int startIndex;
    LoaderManager loaderManager;
    LinearLayoutManager layoutManager;
    ArrayList<Book> books = new ArrayList<>();
    static MainActivity mainActivity;
    EndlessOnScrollListener scrollListener;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        progressBar = findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.GONE);
        mainActivity = this;

        //Get the keyword that user entered
        Button searchButton = findViewById(R.id.search_button);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputMethodManager inputMethodManager = (InputMethodManager) MainActivity.this.getSystemService(INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(),0);
                if (!(adapter == null)) {
                    adapter.clear();
                }
                books.clear();
                startIndex = 0;
                emptyView = findViewById(R.id.empty_list_item);
                emptyView.setVisibility(View.GONE);
                progressBar.setVisibility(View.VISIBLE);
                TextView searchView = findViewById(R.id.text_input);
                loaderManager = getSupportLoaderManager();
                bookList = findViewById(R.id.book_list);
                layoutManager = new LinearLayoutManager(MainActivity.this);
                bookList.setLayoutManager(layoutManager);
                bookList.clearOnScrollListeners();

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

                scrollListener = new EndlessOnScrollListener(layoutManager) {
                    @Override
                    public void onLoadMore() {
                        startIndex = startIndex + 40;
                        if ((books.get(books.size() - 1) != null)) {
                            books.add(null);
                        }
                        adapter.notifyDataSetChanged();
                        loadMore();
                    }
                };
                bookList.addOnScrollListener(scrollListener);
            }
        });
    }


    @NonNull
    @Override
    public Loader<List<Book>> onCreateLoader(int id, @Nullable Bundle args) {
        return new BookLoader(MainActivity.this, keyword, startIndex, adapter);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<List<Book>> loader, List<Book> list) {
        if ((adapter == null) || (adapter.getItemCount() == 0)) {

            if (list.isEmpty()) {
                emptyView.setVisibility(View.VISIBLE);
                emptyView.setText(R.string.no_books_found);
            } else {
                emptyView.setVisibility(View.GONE);
                progressBar.setVisibility(View.GONE);

                books.addAll(list);
                adapter = new BookAdapter(this, R.layout.list_item, (ArrayList<Book>) books);
                adapter.setStateRestorationPolicy(RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY);
                bookList.setAdapter(adapter);
            }
        } else {
            progressBar.setVisibility(View.GONE);
            if (books.get(books.size() - 1) == null) {
                books.remove(books.size() - 1);
            }
            books.addAll(list);
            adapter.notifyDataSetChanged();
        }



    }

    public void loadMore() {
        loaderManager.restartLoader(1, null, MainActivity.this);
    }


    @Override
    public void onLoaderReset(@NonNull Loader<List<Book>> loader) {
        adapter.clear();
    }
    public static MainActivity getInstance() {
        return mainActivity;
    }


}

