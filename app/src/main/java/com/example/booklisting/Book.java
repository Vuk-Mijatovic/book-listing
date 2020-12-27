package com.example.booklisting;

public class Book {
    private final String author;
    private final String title;
    private final String webPage;


    public Book(String author, String title, String webPage) {
        this.author = author;
        this.title = title;
        this.webPage = webPage;
    }


    public String getAuthor() {
        return author;
    }

    public String getTitle() {
        return title;
    }

    public String getWebPage() {
        return webPage;
    }

}
