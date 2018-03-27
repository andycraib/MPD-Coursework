package com.example.andycraib.mpdassessment;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by Andrew Craib on 19/03/2018.
 */

public class RoadworksActivity extends AppCompatActivity {

    private List<Traffic> roadworksList;
    private List<Traffic> selectedList;

    private ArrayAdapter<Traffic> roadworksAdapter;
    private ArrayAdapter<Traffic> selectedAdapter;

    private Button selectDateBtn;
    private Button getRoadworkBtn;

    private EditText enterNameTxt;
    private ListView roadworksListview;
    private ListView selectedListView;
    Date selectedDate;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.roadworks_layout);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        selectDateBtn = (Button) findViewById(R.id.selectDateBtn);
        getRoadworkBtn = (Button) findViewById(R.id.getRoadworkBtn);
        getRoadworkBtn.setInputType(0);
        enterNameTxt = (EditText) findViewById(R.id.enterNameTxt);

        roadworksListview = (ListView) findViewById(R.id.roadworksListView);
        selectedListView = (ListView) findViewById(R.id.selectedListView);

        selectedListView.setVisibility(View.INVISIBLE);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //Get back button

        roadworksList = (ArrayList<Traffic>)getIntent().getSerializableExtra("roadworksList");
        selectedList = new ArrayList<Traffic>();

        roadworksAdapter = new trafficArrayAdapter(RoadworksActivity.this, 0, roadworksList);
        roadworksListview.setAdapter(roadworksAdapter);

        roadworksListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView,
                                    View view, int position, long rowId) {

                Traffic traffic = roadworksList.get(position);

                Intent intent = new Intent(RoadworksActivity.this, DetailActivity.class);
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

        selectedListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView,
                                    View view, int position, long rowId) {

                Traffic traffic = roadworksList.get(position);

                Intent intent = new Intent(RoadworksActivity.this, DetailActivity.class);
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


        final Calendar myCalendar = Calendar.getInstance();
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                selectedList = new ArrayList<Traffic>(); //Reset each time a new date is selected
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                selectedDate = myCalendar.getTime();
                getSelectedDates();
                if (selectedList.size() > 0) {
                    selectedAdapter = new trafficArrayAdapter(RoadworksActivity.this, 0, selectedList);
                    selectedListView.setAdapter(selectedAdapter);
                    selectedListView.setVisibility(View.VISIBLE);
                    roadworksListview.setVisibility(View.INVISIBLE);
                }
                else
                {
                    displayErrorMessage();
                    selectedListView.setVisibility(View.INVISIBLE);
                    roadworksListview.setVisibility(View.VISIBLE);
                }
            }

        };

        selectDateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { //Open datepicker upon click
                new DatePickerDialog(RoadworksActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        getRoadworkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try  {
                    InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE); //Close the virtual keyboard when button is pressed
                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                } catch (Exception e) {

                }

                selectedList = new ArrayList<Traffic>(); //Reset each time a new date is selected
                String getTxtInput = enterNameTxt.getText().toString();
                getRoadworksWithName(getTxtInput);
                if (selectedList.size() > 0) {
                    selectedAdapter = new trafficArrayAdapter(RoadworksActivity.this, 0, selectedList);
                    selectedListView.setAdapter(selectedAdapter);
                    selectedListView.setVisibility(View.VISIBLE);
                    roadworksListview.setVisibility(View.INVISIBLE);
                }
                else
                {
                    displayErrorMessage();
                    selectedListView.setVisibility(View.INVISIBLE);
                    roadworksListview.setVisibility(View.VISIBLE);
                }
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

    public void displayErrorMessage() {
        Toast.makeText(RoadworksActivity.this,
                "No entries found!",
                Toast.LENGTH_LONG).show();
    }

    public void getRoadworksWithName(String searchTerm) {
        for (Traffic t : roadworksList) {
            String search = t.getTitle();
            if (search.toLowerCase().indexOf(searchTerm.toLowerCase()) != -1) {
                selectedList.add(t);
            }
        }
    }

    public void getSelectedDates() {
        for(Traffic t : roadworksList) {
            String pattern = "dd MMMM yyyy";
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);

            DateTime dateSelected = new DateTime(selectedDate);
            String[] parts = t.getDescription().split("<br />");
            String part1 = parts[0]; // 004
            String part2 = parts[1]; // 034556

            part1 = part1.substring(part1.indexOf(',') + 1, part1.indexOf('-')).substring(1);
            part2 = part2.substring(part2.indexOf(',') + 1, part2.indexOf('-')).substring(1);

            try {
                Date from = simpleDateFormat.parse(part1);
                Date to = simpleDateFormat.parse(part2);

                DateTime startDate = new DateTime(from);
                DateTime endDate = new DateTime(to);

                for(DateTime currentdate=startDate; currentdate.isBefore(endDate);currentdate=currentdate.plusDays(1)) {
                    String currentDatestr =currentdate.toString().substring(0,10);
                    String dateSelectedStr = dateSelected.toString().substring(0,10);
                    if (currentDatestr.equals(dateSelectedStr)) {
                        selectedList.add(t);
                    }
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }

}
