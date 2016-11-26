package com.example.android.bookfinder;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.content.Loader;

import java.util.ArrayList;

/**
 * Created by arata on 24/11/2016.
 */

public class BooksLoader extends AsyncTaskLoader<ArrayList<Book>> {

    final static String request_string = "https://www.googleapis.com/books/v1/volumes?q=";
    String query;
    public BooksLoader(Context context,String query) {
        super(context);
        // change all white spaces for +
        this.query = query.replaceAll(" ","+");
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public ArrayList<Book> loadInBackground() {
        if(query.isEmpty())
            return null;
        else{
            ArrayList<Book> books = QueryUtils.fetchBooks(request_string + query);
            return books;
        }
    }
}
