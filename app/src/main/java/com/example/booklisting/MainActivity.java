package com.example.booklisting;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<BookLoaderResult> {
    String keyword;
    BookAdapter adapter;
    RecyclerView bookList;
    View progressBar;
    TextView emptyView;
    int startIndex;
    LoaderManager loaderManager;
    LinearLayoutManager layoutManager;
    ArrayList<Book> books = new ArrayList<>();
    EndlessOnScrollListener scrollListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        progressBar = findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.GONE);
        Button searchButton = findViewById(R.id.search_button);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputMethodManager inputMethodManager = (InputMethodManager) MainActivity.this.getSystemService(INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
                searchBooks();
            }
        });
    }

    private void searchBooks() {
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
            progressBar.setVisibility(View.GONE);
            emptyView.setVisibility(View.VISIBLE);
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

    @NonNull
    @Override
    public Loader<BookLoaderResult> onCreateLoader(int id, @Nullable Bundle args) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String orderBy = sharedPreferences.getString(getString(R.string.settings_order_by_key),
                getString(R.string.settings_order_by_default));

        Uri baseUri = Uri.parse("https://www.googleapis.com/books/v1/volumes?");
        Uri.Builder uriBuilder = baseUri.buildUpon();
        uriBuilder.appendQueryParameter("q", keyword);
        uriBuilder.appendQueryParameter("startIndex", String.valueOf(startIndex));
        uriBuilder.appendQueryParameter("maxResults", "40");
        uriBuilder.appendQueryParameter("orderBy", orderBy);
        uriBuilder.appendQueryParameter("key", "AIzaSyAQ_cswvQ3PenOYLnuTZ4VORlEp3tfnXtE");
        String query = uriBuilder.toString();

        return new BookLoader(MainActivity.this, query, adapter);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<BookLoaderResult> loader, BookLoaderResult loaderResult) {
        if (loaderResult == null) {
            showAlert();
            return;
        }

        if ((adapter == null) || (adapter.getItemCount() == 0)) {
            if (loaderResult.getException() != null) {
                progressBar.setVisibility(View.GONE);
                emptyView.setVisibility(View.GONE);
                showAlert();
            } else {

                if (loaderResult.getResult().isEmpty())  {
                    progressBar.setVisibility(View.GONE);
                    emptyView.setVisibility(View.VISIBLE);
                    emptyView.setText(R.string.no_books_found);
                } else {
                    emptyView.setVisibility(View.GONE);
                    progressBar.setVisibility(View.GONE);
                    books.addAll(loaderResult.getResult());
                    adapter = new BookAdapter(this, R.layout.list_item, (ArrayList<Book>) books);
                    adapter.setStateRestorationPolicy(RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY);
                    bookList.setAdapter(adapter);
                }
            }
        } else {
            if (loaderResult.getResult().isEmpty()) {
                emptyView.setVisibility(View.GONE);
                progressBar.setVisibility(View.GONE);
                if (books.get(books.size() - 1) == null) {
                    books.remove(books.size() - 1);
                    adapter.notifyDataSetChanged();
                }
            } else {
                progressBar.setVisibility(View.GONE);
                books.remove(books.size() - 1);
                books.addAll(loaderResult.getResult());
                adapter.notifyDataSetChanged();
            }
        }
    }

    public void loadMore() {
        loaderManager.restartLoader(1, null, MainActivity.this);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<BookLoaderResult> loader) {
        adapter.clear();
    }

    private void showAlert(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setMessage("Something went wrong! Please, try again.");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                searchBooks();
            }
        });
        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                TextView searchView =  findViewById(R.id.text_input);
                searchView.setText("");
            }
        });
        AlertDialog alert = builder.create();
        alert.show();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.action_settings){
            Intent actionIntent = new Intent(this, SettingsActivity.class);
            startActivity(actionIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    Log.i("THis is new line", "Ovo je nova linija");
}

