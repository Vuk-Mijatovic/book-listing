package com.example.booklisting;

import android.text.TextUtils;
import com.facebook.stetho.okhttp3.StethoInterceptor;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class QueryUtils {

    //Private constructor, because no one should create instances of this class,
    // it is meant only for static helper methods
    private QueryUtils() {
    }

    public static ArrayList<Book> extractBooks(String keyword, int startIndex) throws Exception {
        String author;
        String imageUrl;
        String description;
        ArrayList<Book> books = new ArrayList<>();
        if (TextUtils.isEmpty(keyword)) {
            return null;
        }

        URL url = createURL(keyword, startIndex);
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

    //Method to create URL using text entered in search field
    private static URL createURL(String keyword, int startIndex) throws MalformedURLException {
        String query = "https://www.googleapis.com/books/v1/volumes?q=" + keyword + "&startIndex=" + startIndex + "&maxResults=40&key=AIzaSyAQ_cswvQ3PenOYLnuTZ4VORlEp3tfnXtE";
        URL url = null;
        url = new URL(query);
        return url;
    }

}



