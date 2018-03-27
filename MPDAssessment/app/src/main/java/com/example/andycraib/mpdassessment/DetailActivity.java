package com.example.andycraib.mpdassessment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

// Andrew Craib S1628364

public class DetailActivity extends AppCompatActivity {
    @Override
    //Shows the Detail Layout
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_layout);

        //This sets up the back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //This finds all of the view components
        ImageView imageV = (ImageView) findViewById(R.id.image);
        TextView titleView = (TextView) findViewById(R.id.title);
        TextView descriptionView = (TextView) findViewById(R.id.description);
        TextView linkView = (TextView) findViewById(R.id.link);
        TextView georssView = (TextView) findViewById(R.id.georss);
        TextView authorView = (TextView) findViewById(R.id.author);
        TextView commentsView = (TextView) findViewById(R.id.comments);
        TextView pubDateView = (TextView) findViewById(R.id.pubDate);


        //This collects the intent and populates the layout
        Intent intent = getIntent();

        String title = intent.getStringExtra("title");
        String description = intent.getStringExtra("description");
        String link = intent.getStringExtra("link");
        String georss = intent.getStringExtra("georss");
        String author = intent.getStringExtra("author");
        String comments = intent.getStringExtra("comments");
        String pubDate = intent.getStringExtra("pubDate");

        //This gets the image
        Integer imageID = this.getResources().getIdentifier("roadwork", "drawable", this.getPackageName());
        String image = intent.getStringExtra("image");

        //This sets the elements
        imageV.setImageResource(imageID);
        titleView.setText(title);
        descriptionView.setText(description);
        linkView.setText("Link: " + link);
        georssView.setText("Georss: " + georss);
        authorView.setText("Author: " + author);
        commentsView.setText("Comments: " + comments);
        pubDateView.setText("Date Published: " + pubDate);

    }

    @Override
    //This goes back to the previous page
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}