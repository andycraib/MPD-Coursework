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

// Andrew Craib S1628364

public class RoadworksActivity extends AppCompatActivity {

    //Lists for Roadworks Activity
    private List<Traffic> plannedRoadworksList;
    private List<Traffic> selectedList;

    //Adapters for Roadworks Activity
    private ArrayAdapter<Traffic> plannedRoadworksAdapter;
    private ArrayAdapter<Traffic> selectedDetailAdapter;

    //Declaring the buttons for Roadworks Activity
    private Button selectDateButton;
    private Button getPlannedRoadworksButton;

    //Declaring EditText and ListViews for Roadworks Activity
    private EditText enterNameText;
    private ListView plannedRoadworksListView;
    private ListView selectedListView;
    Date selectedDate;

    //Shows the Roadworks Layout
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.roadworks_layout);


        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        //Link buttons and text to XML layout
        selectDateButton = (Button) findViewById(R.id.selectDateButton);
        getPlannedRoadworksButton = (Button) findViewById(R.id.getPlannedRoadworksButton);
        getPlannedRoadworksButton.setInputType(0);
        enterNameText = (EditText) findViewById(R.id.enterNameText);

        //Link ListViews to XML layout
        plannedRoadworksListView = (ListView) findViewById(R.id.plannedRoadworksListView);
        selectedListView = (ListView) findViewById(R.id.selectedListView);

        selectedListView.setVisibility(View.INVISIBLE);

        //To get the back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //To attach roadworks list to ArrayList
        plannedRoadworksList = (ArrayList<Traffic>)getIntent().getSerializableExtra("plannedRoadworksList");
        selectedList = new ArrayList<Traffic>();

        //To attach adapter to roadworksList
        plannedRoadworksAdapter = new trafficArrayAdapter(RoadworksActivity.this, 0, plannedRoadworksList);
        plannedRoadworksListView.setAdapter(plannedRoadworksAdapter);

        //This gets the details required for the roadworks list
        plannedRoadworksListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView,
                                    View view, int position, long rowId) {

                Traffic traffic = plannedRoadworksList.get(position);

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

        //This gets the details required for the detailed selection
        selectedListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView,
                                    View view, int position, long rowId) {

                Traffic traffic = plannedRoadworksList.get(position);

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


        //This generates the calendar for use
        final Calendar myCalendar = Calendar.getInstance();
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                //This resets each time a new date is selected
                selectedList = new ArrayList<Traffic>();
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                selectedDate = myCalendar.getTime();
                getSelectedDates();
                if (selectedList.size() > 0) {
                    selectedDetailAdapter = new trafficArrayAdapter(RoadworksActivity.this, 0, selectedList);
                    selectedListView.setAdapter(selectedDetailAdapter);
                    selectedListView.setVisibility(View.VISIBLE);
                    plannedRoadworksListView.setVisibility(View.INVISIBLE);
                }
                else
                {
                    displayErrorMessage();
                    selectedListView.setVisibility(View.INVISIBLE);
                    plannedRoadworksListView.setVisibility(View.VISIBLE);
                }
            }

        };

        //This opens the calendar when the button is clicked
        selectDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(RoadworksActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        //This closes the virtual keyboard when the button is clicked
        getPlannedRoadworksButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try  {
                    InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                } catch (Exception e) {

                }

                //This resets each time a new date is chosen
                selectedList = new ArrayList<Traffic>();
                String getTxtInput = enterNameText.getText().toString();
                getRoadworksWithName(getTxtInput);
                if (selectedList.size() > 0) {
                    selectedDetailAdapter = new trafficArrayAdapter(RoadworksActivity.this, 0, selectedList);
                    selectedListView.setAdapter(selectedDetailAdapter);
                    selectedListView.setVisibility(View.VISIBLE);
                    plannedRoadworksListView.setVisibility(View.INVISIBLE);
                }
                else
                {
                    displayErrorMessage();
                    selectedListView.setVisibility(View.INVISIBLE);
                    plannedRoadworksListView.setVisibility(View.VISIBLE);
                }
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

    //This displays an error message when necessary
    public void displayErrorMessage() {
        Toast.makeText(RoadworksActivity.this,
                "No entries found!",
                Toast.LENGTH_LONG).show();
    }

    //This takes the value from the edit text bar
    public void getRoadworksWithName(String searchTerm) {
        for (Traffic t : plannedRoadworksList) {
            String search = t.getTitle();
            if (search.toLowerCase().indexOf(searchTerm.toLowerCase()) != -1) {
                selectedList.add(t);
            }
        }
    }

    //This parses the details of the selected dates chosen from the calendar
    public void getSelectedDates() {
        for(Traffic t : plannedRoadworksList) {
            String pattern = "dd MMMM yyyy";
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);

            DateTime dateSelected = new DateTime(selectedDate);
            String[] parts = t.getDescription().split("<br />");
            String part1 = parts[0];
            String part2 = parts[1];

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
