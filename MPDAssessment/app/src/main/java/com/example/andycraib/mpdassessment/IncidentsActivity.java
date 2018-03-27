package com.example.andycraib.mpdassessment;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.ListView;

import org.joda.time.DateTime;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

// Andrew Craib S1628364

public class IncidentsActivity extends AppCompatActivity {

    private Context context;

    //The URL for Current Incidents RSS
    private String curIncidentsUrl="https://trafficscotland.org/rss/feeds/currentincidents.aspx";

    //List and Array
    private List<Traffic> currentIncidentList;
    private ArrayAdapter<Traffic> currentIncidentAdapter;

    //The ListView for current incidents
    private ListView currentIncidentsListView;

    //Shows the Incidents Layout
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.incidents_layout);

        //This links the ListView to the XML layout
        currentIncidentsListView = (ListView) findViewById(R.id.currentIncidentsListView);

        //This grabs the parsed data from the Activity Main
        currentIncidentList = (ArrayList<Traffic>)getIntent().getSerializableExtra("currentIncidentsList");

        //This grabs all the parsed incidents data
        currentIncidentAdapter = new trafficArrayAdapter(IncidentsActivity.this, 0, currentIncidentList); //Grab all parsed incidents data

        //This generates a back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //This gets all the details for the Incident ListView
        currentIncidentsListView.setAdapter(currentIncidentAdapter);
        currentIncidentsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView,
                                    View view, int position, long rowId) {

                Traffic traffic = currentIncidentList.get(position);

                Intent intent = new Intent(IncidentsActivity.this, DetailActivity.class);
                intent.putExtra("title", traffic.getTitle());
                intent.putExtra("description", traffic.getDescription());
                intent.putExtra("link", traffic.getLink());
                intent.putExtra("georss", traffic.getGeorss());
                intent.putExtra("author", traffic.getAuthor());
                intent.putExtra("comments", traffic.getComments());
                intent.putExtra("pubDate", traffic.getPubDate());
                startActivity(intent);
            }
        });
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