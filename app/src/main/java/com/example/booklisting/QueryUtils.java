package com.example.booklisting;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class QueryUtils {

    //Tag for the log messages
    public static final String LOG_TAG = QueryUtils.class.getSimpleName();

    //Private constructor, because no one should create instances of this class,
    // it is meant only for static helper methods
    private QueryUtils() {
    }

    public static ArrayList<Book> extractBooks(String keyword) {
        if (TextUtils.isEmpty(keyword)) {
            return null;
        }
        ArrayList<Book> books = new ArrayList<>();


        URL url = createURL(keyword);
        String JSONresponse = null;
        try {
            JSONresponse = makeAHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making http connection.");
        }

        try {
            JSONObject root = new JSONObject(JSONresponse);
            JSONArray items = root.optJSONArray("items");
            for (int i = 0; i < items.length(); i++) {
                JSONObject item = items.optJSONObject(i);
                JSONObject volumeInfo = item.optJSONObject("volumeInfo");
                JSONArray authors = volumeInfo.optJSONArray("authors");
                String author = authors.optString(0);
                String title = volumeInfo.optString("title");
                books.add(new Book(author, title));
            }
        } catch (JSONException e) {
            Log.e(LOG_TAG, "Problem parsing JSON response.");
        }

        return books;


    }

    //method to make a http request
    private static String makeAHttpRequest(URL url) throws IOException {
        String JSONresponse = "";
        if (url == null) return JSONresponse;


        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;

        // create http conection
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.connect();


            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();

                JSONresponse = readFromStream(inputStream);


            } else {
                Log.e(LOG_TAG, "Error code:" + urlConnection.getResponseCode());
            }


        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving JSON response.");
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }

        return JSONresponse;
    }

    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }


    //Method to create URL using text entered in search field
    private static URL createURL(String keyword) {

        String query = "https://www.googleapis.com/books/v1/volumes?q=" + keyword + "&maxResults=8&key=AIzaSyAQ_cswvQ3PenOYLnuTZ4VORlEp3tfnXtE";
        URL url = null;
        try {
            url = new URL(query);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Error with creating URL.");
        }
        Log.i(LOG_TAG, "Query: " + url.toString());
        return url;

    }


}



