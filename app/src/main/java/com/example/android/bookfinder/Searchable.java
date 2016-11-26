package com.example.android.bookfinder;

import android.app.LoaderManager;
import android.app.SearchManager;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class Searchable extends AppCompatActivity implements LoaderCallbacks<ArrayList<Book>> {

    ListView listView;
    BooksAdapter booksAdapter;
    String query;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchable);

        // reference to list view
        listView = (ListView) findViewById(R.id.list_view);

        // setting books custom adapter to listview
        booksAdapter = new BooksAdapter(this,new ArrayList<Book>());
        listView.setAdapter(booksAdapter);
        // empty view to display when no books where found
        listView.setEmptyView(findViewById(R.id.emptyTextView));
        // on item click listener to open more info about the book
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Book current =(Book) adapterView.getItemAtPosition(i);
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(current.getInfoUrl()));
                if(intent.resolveActivity(getPackageManager())!=null)
                    startActivity(intent);
            }
        });

        // getting the intent
        Intent intent = getIntent();
        // Loader
        LoaderManager loaderManager = getLoaderManager();

        // checking if the intent received is a search, and doing the search
        if(Intent.ACTION_SEARCH.equals(intent.getAction())){
            query = intent.getStringExtra(SearchManager.QUERY);

            // checking if connected to internet
            if(internetConnectionAvailable()){
                loaderManager.initLoader(0,null,this);
            }
            else{
                ((TextView)findViewById(R.id.emptyTextView)).setText("No internet connection :(");
            }
        }
    }


    /**
     * tells if connection to internet is available
     * @return
     */
    private boolean internetConnectionAvailable(){
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    @Override
    public Loader<ArrayList<Book>> onCreateLoader(int i, Bundle bundle) {
        // new book loader to handle the query
        return new BooksLoader(this,query);
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<Book>> loader, ArrayList<Book> books) {
        booksAdapter.clear();
        // set text for empty view in case no book was found
        ((TextView) findViewById(R.id.emptyTextView)).setText("No books found :(");

        // if books found add them to the adapter to be displayed
        if(books != null && !books.isEmpty()){
            booksAdapter.addAll(books);
        }
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<Book>> loader) {
        booksAdapter.clear();
    }

}
