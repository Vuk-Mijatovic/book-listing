package com.example.booklisting;

import java.util.List;

public class BookLoaderResult  {
    private  List<Book> result;
    private  Exception exception;


    public BookLoaderResult() { }

    public void setResult(List<Book> result) {
        this.result = result;
    }

    public void setException(Exception exception) {
        this.exception = exception;
    }

    public List<Book> getResult() {
        return result;
    }

    public Exception getException() {
        return exception;
    }

}
