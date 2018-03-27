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

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private Context context;

    private String plndRoadworksUrl="http://trafficscotland.org/rss/feeds/plannedroadworks.aspx";
    private String curIncidentsUrl="http://trafficscotland.org/rss/feeds/currentincidents.aspx";

    private Button fetchIncidentsBtn;
    private Button fetchRoadworksBtn;
    private Button dateEnterBtn;

    private ArrayList<Traffic> incidentList;
    private ArrayList<Traffic> roadWorksList;

    private ArrayAdapter<Traffic> incidentAdapter;
    private ArrayAdapter<Traffic> roadworksAdapter;

    String pattern = " EEEE, dd MMMM yyyy";
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);

    ListView incidentsListView;
    ListView roadworksListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fetchIncidentsBtn = (Button) findViewById(R.id.fetchIncidentsBtn);
        fetchRoadworksBtn = (Button) findViewById(R.id.fetchRoadworksBtn);

        roadWorksList = new ArrayList<Traffic>();
        incidentList = new ArrayList<Traffic>();

        new FetchFeedTask().execute((Void) null);

        fetchIncidentsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // incidentsListView.setAdapter(incidentAdapter);
                // incidentsListView.setVisibility(View.VISIBLE);
                // roadworksListView.setVisibility(View.INVISIBLE);
                Intent myIntent = new Intent(MainActivity.this, IncidentsActivity.class);
                myIntent.putExtra("incidentsList", incidentList);
                MainActivity.this.startActivity(myIntent);

            }});

        fetchRoadworksBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(MainActivity.this, RoadworksActivity.class);
                myIntent.putExtra("roadworksList", roadWorksList);
                MainActivity.this.startActivity(myIntent);
            }});
    }

    public final class FetchFeedTask extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected void onPreExecute() {
            //mSwipeLayout.setRefreshing(true);
            //mFeedTitle = null;
            //mFeedLink = null;
            //mFeedDescription = null;
            //mFeedTitleTextView.setText("Feed Title: " + mFeedTitle);
            //mFeedDescriptionTextView.setText("Feed Description: " + mFeedDescription);
            //mFeedLinkTextView.setText("Feed Link: " + mFeedLink);
            //urlLink = curIncidentsUrl;
        }

        @Override
        protected Boolean doInBackground(Void... voids) {

            String urlLink = curIncidentsUrl;

            if (TextUtils.isEmpty(urlLink))
                return false;

            try {
                if(!urlLink.startsWith("http://") && !urlLink.startsWith("https://"))
                    urlLink = "http://" + urlLink;

                URL url = new URL(curIncidentsUrl);
                InputStream inputStream = url.openConnection().getInputStream();
                incidentList = MainActivity.parseFeed(inputStream);

                URL url2 = new URL(plndRoadworksUrl);
                InputStream inputStream2 = url2.openConnection().getInputStream();
                roadWorksList = MainActivity.parseFeed(inputStream2);


                return true;
            } catch (IOException e) {
                Log.e(TAG, "Error", e);
            } catch (XmlPullParserException e) {
                Log.e(TAG, "Error", e);
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean success) {
            // mSwipeLayout.setRefreshing(false);

            if (success) {
                //mFeedTitleTextView.setText("Feed Title: " + mFeedTitle);
                //mFeedDescriptionTextView.setText("Feed Description: " + mFeedDescription);
                //mFeedLinkTextView.setText("Feed Link: " + mFeedLink);
                // Fill RecyclerView
                incidentAdapter = new trafficArrayAdapter(MainActivity.this, 0, incidentList);
                roadworksAdapter = new trafficArrayAdapter(MainActivity.this, 0, roadWorksList);



            } else {
                //Toast.makeText(MainActivity.this,
                //  "Enter a valid Rss feed url",
                //Toast.LENGTH_LONG).show();
            }
        }
    }

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
                        //mFeedTitle = title;
                        //mFeedLink = link;
                        //mFeedDescription = description;
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
