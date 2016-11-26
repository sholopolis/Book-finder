package com.example.android.bookfinder;

/**
 * Created by arata on 24/11/2016.
 */


public class Book {
    private String title;
    private String author;
    private int year;
    private String imageUrl;
    private int numOfPages;
    private String infoUrl;
    // constructor
    public Book(String title, String author, int year, String imageUrl, int numOfPages, String infoUrl){
        this.title = title;
        this.author = author;
        this.year = year;
        this.imageUrl = imageUrl;
        this.numOfPages = numOfPages;
        this.infoUrl = infoUrl;
    }
    //title getter
    public String getTitle(){
        return  title;
    }
    //author getter
    public String getAuthor(){
        return  author;
    }
    //imageUrl getter
    public String getImageUrl(){
        return  imageUrl;
    }
    //getter
    public String getInfoUrl(){
        return  infoUrl;
    }
    //year getter
    public int getYear(){
        return  year;
    }
    //numOfPages getter
    public int getNumOfPages(){
        return  numOfPages;
    }
}
