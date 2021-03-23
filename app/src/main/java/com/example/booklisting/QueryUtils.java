package com.example.booklisting;

import android.text.TextUtils;

import com.facebook.stetho.okhttp3.StethoInterceptor;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class QueryUtils {

    //Tag for the log messages
    public static final String LOG_TAG = QueryUtils.class.getSimpleName();

    //Private constructor, because no one should create instances of this class,
    // it is meant only for static helper methods
    private QueryUtils() {
    }

    public static ArrayList<Book> extractBooks(String query) throws Exception {
        String author;
        String imageUrl;
        String description;
        ArrayList<Book> books = new ArrayList<>();
        if (TextUtils.isEmpty(query)) { return null; }

        URL url = new URL(query);
        String JSONResponse = "";
            JSONResponse = makeAHttpRequest(url);
            JSONObject root = new JSONObject(JSONResponse);
            if (root.length() == 0) {
                return new ArrayList<>();
            }
            JSONArray items = root.optJSONArray("items");
            if (items != null) {
                for (int i = 0; i < items.length(); i++) {
                    JSONObject item = items.optJSONObject(i);
                    JSONObject volumeInfo = item.optJSONObject("volumeInfo");
                    JSONArray authors = volumeInfo.optJSONArray("authors");
                    if (authors == null) {
                        author = "N/A";
                    } else {
                        if (authors.length() == 1) {
                            author = authors.optString(0);
                        } else if (authors.length() == 2) {
                            author = authors.optString(0) + ", " + authors.optString(1);
                        } else {
                            author = authors.optString(0) + ", " + authors.optString(1) + " and others";
                        }
                    }
                    String title = volumeInfo.optString("title");
                    String webPage = volumeInfo.optString("infoLink");
                    description = volumeInfo.optString("description");
                    JSONObject imageLinks = volumeInfo.optJSONObject("imageLinks");
                    if (imageLinks != null) {
                        imageUrl = imageLinks.optString("smallThumbnail");
                    } else
                        imageUrl = "https://media-exp1.licdn.com/dms/image/C4D0BAQGpUHuSqzqVkw/company-logo_200_200/0/1550857067943?e=2159024400&v=beta&t=WOiC7IyHvC9I46NruMrRcLdcOz66V6JcuejZjzEpzZk";
                    books.add(new Book(author, title, webPage, imageUrl, description));
                }
            }
            return books;
    }

    //method to make a http request
    private static String makeAHttpRequest(URL url) throws IOException {
        String JSONresponse = "";
        OkHttpClient client = new OkHttpClient.Builder()
                .addNetworkInterceptor(new StethoInterceptor()).build();
        Request request = new Request.Builder().url(url).build();
        Response response = client.newCall(request).execute();
        JSONresponse = response.body().string();
        return JSONresponse;
    }

}



