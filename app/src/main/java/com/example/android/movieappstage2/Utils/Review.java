package com.example.android.movieappstage2.Utils;

/**
 * Created by mina essam on 24-Apr-17.
 */

public class Review {
    private String author,content;
    public Review(String author,String content){
        this.author=author;
        this.content=content;
    }

    public String getAuthor() {
        return author;
    }

    public String getContent() {
        return content;
    }
}
