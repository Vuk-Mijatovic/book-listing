package com.example.booklisting;

public class Book {
    private final String author;
    private final String title;
    private final String webPage;
    private final String imageUrl;


    public Book(String author, String title, String webPage, String imageLink) {
        this.author = author;
        this.title = title;
        this.webPage = webPage;
        this.imageUrl = imageLink;
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

    public String getImageUrl() {
        return imageUrl;
    }
}
