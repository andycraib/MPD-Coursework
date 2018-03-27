package com.example.andycraib.mpdassessment;



import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;

import org.joda.time.DateTime;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Andy Craib on 22/03/2018.
 */

public class IncidentsActivity extends AppCompatActivity {

    private String curIncidentsUrl="https://trafficscotland.org/rss/feeds/currentincidents.aspx";

    private List<Traffic> incidentList;
    private ArrayAdapter<Traffic> incidentAdapter;

    private ListView incidentsListView;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.incidents_layout);

        incidentsListView = (ListView) findViewById(R.id.incidentsListView);
        incidentList = (ArrayList<Traffic>)getIntent().getSerializableExtra("incidentsList"); //Grab the parsed data from ActivityMain
        incidentAdapter = new trafficArrayAdapter(IncidentsActivity.this, 0, incidentList); //Grab all parsed incidents data

        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //Generate back button

        incidentsListView.setAdapter(incidentAdapter);
        incidentsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView,
                                    View view, int position, long rowId) {

                Traffic traffic = incidentList.get(position);

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