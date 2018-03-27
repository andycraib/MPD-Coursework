package com.example.andycraib.mpdassessment;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Xml;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.text.SimpleDateFormat;

// Andrew Craib S1628364

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private Context context;

    //The URL for Current Incidents and Planned Roadworks RSS
    private String currentIncidentsUrl="http://trafficscotland.org/rss/feeds/currentincidents.aspx";
    private String plannedRoadworksUrl="http://trafficscotland.org/rss/feeds/plannedroadworks.aspx";

    //This gives the button IDs
    private Button currentIncidentsButton;
    private Button plannedRoadworksButton;
    private Button dateEnterBtn;

    //The ArrayLists for Current Incidents and Planned Roadworks
    private ArrayList<Traffic> currentIncidentsList;
    private ArrayList<Traffic> plannedRoadworksList;

    //The ArrayLists for Current Incidents and Planned Roadworks
    private ArrayAdapter<Traffic> currentIncidentsAdapter;
    private ArrayAdapter<Traffic> plannedRoadworksAdapter;

    String pattern = " EEEE, dd MMMM yyyy";
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);

    ListView currentIncidentsListView;
    ListView plannedRoadworksListView;

    @Override
    //Shows the Activity Main Layout
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //This links the buttons to the XML layout
        currentIncidentsButton = (Button) findViewById(R.id.currentIncidentsButton);
        plannedRoadworksButton = (Button) findViewById(R.id.plannedRoadworksButton);

        //This creates a new ArrayList
        currentIncidentsList = new ArrayList<Traffic>();
        plannedRoadworksList = new ArrayList<Traffic>();


        new FetchFeedTask().execute((Void) null);

        //This starts the activity of the Current Incidents when the related button is clicked
        currentIncidentsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(MainActivity.this, IncidentsActivity.class);
                myIntent.putExtra("currentIncidentsList", currentIncidentsList);
                MainActivity.this.startActivity(myIntent);

            }});

        //This starts the activity of the Planned Roadworks when the related button is clicked
        plannedRoadworksButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(MainActivity.this, RoadworksActivity.class);
                myIntent.putExtra("plannedRoadworksList", plannedRoadworksList);
                MainActivity.this.startActivity(myIntent);
            }});
    }

    public final class FetchFeedTask extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected void onPreExecute() {

        }

        @Override
        //This parses the related URL to the correct page
        protected Boolean doInBackground(Void... voids) {

            String urlLink = currentIncidentsUrl;

            if (TextUtils.isEmpty(urlLink))
                return false;

            try {
                if(!urlLink.startsWith("http://") && !urlLink.startsWith("https://"))
                    urlLink = "http://" + urlLink;

                URL url = new URL(currentIncidentsUrl);
                InputStream inputStream = url.openConnection().getInputStream();
                currentIncidentsList = MainActivity.parseFeed(inputStream);

                URL url2 = new URL(plannedRoadworksUrl);
                InputStream inputStream2 = url2.openConnection().getInputStream();
                plannedRoadworksList = MainActivity.parseFeed(inputStream2);


                return true;
            } catch (IOException e) {
                Log.e(TAG, "Error", e);
            } catch (XmlPullParserException e) {
                Log.e(TAG, "Error", e);
            }
            return false;
        }

        //This populates the ListViews for the respective activities
        @Override
        protected void onPostExecute(Boolean success) {

            if (success) {

                currentIncidentsAdapter = new trafficArrayAdapter(MainActivity.this, 0, currentIncidentsList);
                plannedRoadworksAdapter = new trafficArrayAdapter(MainActivity.this, 0, plannedRoadworksList);

            } else {

            }
        }
    }

    //This sets the variables for the ArrayList to null if no value is found
    public static ArrayList<Traffic> parseFeed(InputStream inputStream) throws XmlPullParserException, IOException {
        String title = null;
        String link = null;
        String description = null;
        String georss = null;
        String author = null;
        String comments = null;
        String pubDate = null;
        boolean isItem = false;
        ArrayList<Traffic> items = new ArrayList<>();

        //This parses the details and puts them into the ArrayList if they can be found
        try {
            XmlPullParser xmlPullParser = Xml.newPullParser();
            xmlPullParser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            xmlPullParser.setInput(inputStream, null);

            xmlPullParser.nextTag();
            while (xmlPullParser.next() != XmlPullParser.END_DOCUMENT) {
                int eventType = xmlPullParser.getEventType();

                String name = xmlPullParser.getName();
                if (name == null)
                    continue;

                if (eventType == XmlPullParser.END_TAG) {
                    if (name.equalsIgnoreCase("item")) {
                        isItem = false;
                    }
                    continue;
                }

                if (eventType == XmlPullParser.START_TAG) {
                    if (name.equalsIgnoreCase("item")) {
                        isItem = true;
                        continue;
                    }
                }

                Log.d("MainActivity", "Parsing name ==> " + name);
                String result = "";
                if (xmlPullParser.next() == XmlPullParser.TEXT) {
                    result = xmlPullParser.getText();
                    xmlPullParser.nextTag();
                }

                if (name.equalsIgnoreCase("title")) {
                    title = result;
                } else if (name.equalsIgnoreCase("description")) {
                    description = result;
                } else if (name.equalsIgnoreCase("link")) {
                    link = result;
                } else if (name.equalsIgnoreCase("georss:point")) {
                    georss = result;
                } else if (name.equalsIgnoreCase("author")) {
                    author = result;
                    if (author == "") {
                        author = "N/A";
                    }
                } else if (name.equalsIgnoreCase("comments")) {
                    comments = result;
                    if (comments == "") {
                        comments = "N/A";
                    }
                } else if (name.equalsIgnoreCase("pubDate")) {
                    pubDate = result;
                }


                if (description != null && description.contains("<br\\s*/>")) {
                    description.replace("<br\\s*/>", " ");
                }

                if (title != null && link != null && description != null && georss != null) {
                    if (isItem) {
                        Traffic item = new Traffic(title, description, link, georss, author, comments, pubDate);
                        items.add(item);
                    } else {

                    }

                    title = null;
                    link = null;
                    description = null;
                    isItem = false;
                }
            }

            return items;
        } finally {
            inputStream.close();
        }
    }
}
