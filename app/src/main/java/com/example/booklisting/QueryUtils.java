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
//NOT SURE HOW TO HANDLE POSSIBLE NULLPOINTEREXEPTIONS

    //Tag for the log messages
    public static final String LOG_TAG = QueryUtils.class.getSimpleName();

    //Private constructor, because no one should create instances of this class,
    // it is meant only for static helper methods
    private QueryUtils() {
    }


    public static ArrayList<Book> extractBooks(String keyword, int startIndex, ArrayList<Book> books) {
        String author;
        String imageUrl;
        if (TextUtils.isEmpty(keyword)) {
            return null;
        }


        URL url = createURL(keyword, startIndex);
        String JSONResponse = "";
        try {
            JSONResponse = makeAHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making http connection.");
        }
        Log.i(LOG_TAG, "Searching for: " + url);
        try {
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
                    JSONObject imageLinks = volumeInfo.optJSONObject("imageLinks");
                    if (imageLinks != null) {
                        imageUrl = imageLinks.optString("smallThumbnail");
                    } else
                        imageUrl = "https://media-exp1.licdn.com/dms/image/C4D0BAQGpUHuSqzqVkw/company-logo_200_200/0/1550857067943?e=2159024400&v=beta&t=WOiC7IyHvC9I46NruMrRcLdcOz66V6JcuejZjzEpzZk";
                    books.add(new Book(author, title, webPage, imageUrl));
                }
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
    private static URL createURL(String keyword, int startIndex) {

        String query = "https://www.googleapis.com/books/v1/volumes?q=" + keyword + "&startIndex=" + startIndex + "&maxResults=40&key=AIzaSyAQ_cswvQ3PenOYLnuTZ4VORlEp3tfnXtE";
        URL url = null;
        try {
            url = new URL(query);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Error with creating URL.");
        }
        return url;

    }


}



