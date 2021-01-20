package com.example.booklisting;

public class Book {
    private final String author;
    private final String title;
    private final String webPage;
    private final String imageUrl;
    private final String description;


    public Book(String author, String title, String webPage, String imageLink, String description) {
        this.author = author;
        this.title = title;
        this.webPage = webPage;
        this.imageUrl = imageLink;
        this.description = description;
    }

    public String getDescription() { return description; }

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
