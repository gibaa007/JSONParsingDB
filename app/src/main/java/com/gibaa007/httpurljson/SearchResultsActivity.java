package com.gibaa007.httpurljson;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;

/**
 * Created by gibaa007 on 12/3/16.
 */
public class SearchResultsActivity extends Activity {
    private SimpleDraweeView image;
    private Movie movie = new Movie();
    private DatabaseHandler db;
    private TextView title, rating, releaseYear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = new DatabaseHandler(this);
        setContentView(R.layout.activity_result);
        image = (SimpleDraweeView) findViewById(R.id.image_view);
        title = (TextView) findViewById(R.id.title);
        rating = (TextView) findViewById(R.id.rating);
        releaseYear = (TextView) findViewById(R.id.releaseYear);
        if (Intent.ACTION_SEARCH.equals(getIntent().getAction())) {
            String query = getIntent().getStringExtra(SearchManager.QUERY);
            movie = db.getContact(query);
            if (movie.getTitle() != null) {
                Uri uri = Uri.parse(movie.getThumbnailUrl());
                image.setImageURI(uri);
                title.setText("Title: " + movie.getTitle());
                rating.setText("Rating: " + movie.getRating());
                releaseYear.setText("Release Year: " + movie.getYear());
            } else {
                Toast.makeText(this, "No Search Results", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }
}