package com.example.android.bookfinder;

import android.content.Loader;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;

import static java.lang.Integer.parseInt;
import static junit.runner.Version.id;

/**
 * Created by arata on 24/11/2016.
 */

public class QueryUtils {
    /**
     * creates URL object given a String
     */
    private static URL createUrl(String urlString){
        try {
            URL url = new URL(urlString);
            return url;
        } catch (MalformedURLException e) {
            Log.e("URL", "QueryUtils: problems creating the url object");
            return null;
        }
    }

    /**
     * gets all books found in google books given a httprequest url
     */
    public static ArrayList<Book> fetchBooks(String httpRequestUrl){
        URL url = createUrl(httpRequestUrl);

        String json = makeHttpRequest(url);

        ArrayList<Book> books = extractBooksFromJson(json);
        return books;
    }

    /**
     * makes http request and returns the server response in a string
     * @param url
     * @return
     */
    private static String makeHttpRequest(URL url){
        String respone= "";
        HttpURLConnection httpURLConnection= null;
        InputStream inputStream = null;
        if(url == null){
            return null;
        }
        else {
            try {
                // setting up the connection
                httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setConnectTimeout(10000);
                httpURLConnection.setReadTimeout(10000);
                httpURLConnection.setRequestMethod("GET");
                httpURLConnection.connect();
                // if response code is "200 = OK" read from inputstream
                if(httpURLConnection.getResponseCode() == 200){
                    inputStream = httpURLConnection.getInputStream();
                    respone = readFromInputStream(inputStream);
                }
                else{
                    Log.e("URL","QueryUtils:MakehttpRequest: response code " + httpURLConnection.getResponseCode());
                }
            } catch (IOException e) {
                Log.e("URL","QueryUtils:MakehttpRequest: failed opening connection");
            }
            finally {
                if(httpURLConnection != null){
                    httpURLConnection.disconnect();
                }
                if(inputStream != null ) {
                    try {
                        inputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return respone;
        }
    }
    /**
     * extracts Books from Json string
     */
    private static ArrayList<Book> extractBooksFromJson(String jsonString){
        ArrayList<Book> books = new ArrayList<>();
        int year=0;
        int pages = 0;
        String inforUrl="";
        String imageUrl ="";
        String author ="";
        if(!jsonString.isEmpty()){
            try {
                // get the whole response
                JSONObject entire_response = new JSONObject(jsonString);
                // get the array of books
                JSONArray jsonBooks = entire_response.getJSONArray("items");
                for(int i =0; i < jsonBooks.length(); i++){
                    // for each item in the list add a new book to books

                    JSONObject current = jsonBooks.getJSONObject(i).getJSONObject("volumeInfo");
                    String title = current.getString("title");
                    if(current.has("authors")) {
                        author = getAuthor(current.getJSONArray("authors"));
                    }
                    if(current.has("publishedDate")) {
                         year = parseInt(current.getString("publishedDate").substring(0, 4));
                    }
                    if(current.has("pageCount")) {
                        pages = current.getInt("pageCount");
                    }

                    if(current.has("infoLink")) {
                         inforUrl = current.getString("infoLink");
                    }

                    if(current.has("imageLinks")) {
                        imageUrl = extractImageUrl(current.getJSONObject("imageLinks"));
                    }
                    books.add(new Book(title,author,year,imageUrl,pages,inforUrl));
                }
            } catch (JSONException e) {
                Log.e("URL","QueryUtils:extractbooksFromJson: malformed json string");
                return null;
            }
        }
        return books;
    }
    /**
     * extracts thumbnail image url from image links jsonobject
     * !!!
     */
    private  static String extractImageUrl(JSONObject jsonObject){
        try {
            return jsonObject.getString("thumbnail");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
    /**
     * extract authors from book and returns it in one string
     */
    private static String getAuthor(JSONArray authors) throws JSONException {
        String authorsString = "";
        for(int i = 0 ; i < authors.length(); i ++){
            if(i > 0) {
                authorsString += " & " + authors.getString(i);
            }
            authorsString +=  authors.getString(i);
        }
        return authorsString;
    }

    /**
     * reads a json string from an input stream
     */
    private static String readFromInputStream(InputStream inputStream){
        StringBuilder response = new StringBuilder();
        String line;
        // wrap the input stream in a inputstramreader and the inputstreamreader in a bufferedreader to read line by line and append it to a string builder
        if(inputStream != null) {
            InputStreamReader reader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader bufferedReader = new BufferedReader(reader);
            try {
                line = bufferedReader.readLine();
                while(line != null){
                    response.append(line);
                    line = bufferedReader.readLine();
                }
            } catch (IOException e) {
                Log.e("ERROR","QueryUtils:readFromInputStream: error while reading input stream");
            }
        }
        return response.toString();
    }
}
