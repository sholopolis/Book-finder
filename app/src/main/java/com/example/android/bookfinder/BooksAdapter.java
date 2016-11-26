package com.example.android.bookfinder;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;

import static android.R.attr.fingerprintAuthDrawable;
import static android.R.attr.layout_scale;
import static android.R.attr.resource;

/**
 * Created by arata on 24/11/2016.
 */

public class BooksAdapter extends ArrayAdapter<Book> {
    ArrayList<Book> books;
    public BooksAdapter(Context context, ArrayList<Book> books) {
        super(context, 0,books);
        this.books = books;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View list_item = convertView;
        ViewHolder viewHolder;
        // if list item view is null initialize a new one else get the reference to the view holder
        if(list_item == null){
            viewHolder = new ViewHolder();
            list_item = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
            viewHolder.author = (TextView)list_item.findViewById(R.id.book_author);
            viewHolder.image = (ImageView) list_item.findViewById(R.id.book_image);
            viewHolder.pages = (TextView)list_item.findViewById(R.id.num_pages);
            viewHolder.title = (TextView)list_item.findViewById(R.id.book_title);
            viewHolder.year = (TextView)list_item.findViewById(R.id.book_year);

            list_item.setTag(viewHolder);
        }
        else{
            viewHolder = (ViewHolder) list_item.getTag();
        }

        //get current book
        Book book = books.get(position);
        // storing book info in list_item
        new DownloadImageTask(viewHolder.image)
                .execute(book.getImageUrl());
        viewHolder.title.setText(book.getTitle());
        viewHolder.author.setText(book.getAuthor());
        viewHolder.year.setText("" + book.getYear());
        viewHolder.pages.setText(book.getNumOfPages()+ " pg");

        return list_item;
    }

    // returns image from url
    private Bitmap getImage(String imageUrl){
        URL url = null;
        Bitmap bmp = null;
        try {
            url = new URL(imageUrl);
            bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
        }
        catch (MalformedURLException e) {
            Log.e("URL","BookAdapter:getImage: malformed url");
        }
        catch(IOException e){
            Log.e("URL","BookAdapter:getImage: couldn't make the connection");
        }
        return bmp;
    }
    class ViewHolder{
        ImageView image;
        TextView title;
        TextView author;
        TextView year;
        TextView pages;
        TextView rating;
    }
    /**
     * helper function to round rating to 1 decimal place
     */
    private String formatRating(double rating){
        DecimalFormat decimalFormat = new DecimalFormat("0.0");
        return decimalFormat.format(rating);
    }

    //Image downloader
    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }
}
