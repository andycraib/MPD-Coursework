package com.example.andycraib.mpdassessment;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.*;
import android.util.Property;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

// Andrew Craib S1628364

//This is a custom ArrayAdapter
class trafficArrayAdapter extends ArrayAdapter<Traffic> implements Serializable {

    private Context context;
    private ArrayList<Traffic> detailProperties;

    //This is a constructor, that is called on creation
    public trafficArrayAdapter(Context context, int resource, List<Traffic> objects) {
        super(context, resource, objects);

        this.context = context;
        this.detailProperties = (ArrayList)objects;
    }

    //This is called when rendering the list
    public View getView(int position, View convertView, ViewGroup parent) {

        //This gets the property to be displayed
        Traffic traffic = detailProperties.get(position);

        //This gets the inflater and is used to inflate the XML layout for each item
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.property_layout, null);

        //Parses the dates, finds out the length in between start and end dates assigns them to a colour
        if (traffic.getDescription().contains("Start Date")) {
            String pattern = " EEEE, dd MMMM yyyy";
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);

            String[] parts = traffic.getDescription().split("<br />");

            String part1 = parts[0];
            String part2 = parts[1];

            part1 = part1.substring(part1.indexOf(':') + 1, part1.indexOf('-'));
            part2 = part2.substring(part2.indexOf(':') + 1, part2.indexOf('-'));

            try {
                Date date = simpleDateFormat.parse(part1);
                Date date2 = simpleDateFormat.parse(part2);
                long diff = Math.abs(date.getTime() - date2.getTime());
                long diffDays = diff / (24 * 60 * 60 * 1000);
                if (diffDays != 1) {
                    diffDays = diffDays + 1;
                }

                //Less than or equal to 1 = Green Background
                if (diffDays <= 1) {
                    view.setBackgroundColor(Color.GREEN);
                }
                //Less than or equal to 3 & more than or equal to 1 = Yellow Background
                else if(diffDays >= 1 && diffDays <= 3) {
                    view.setBackgroundColor(Color.YELLOW);
                }
                //Anything else or anything above 3 = Red Background
                else
                {
                    view.setBackgroundColor(Color.RED);
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        //This links the TextViews to the XML layout
        TextView title = (TextView) view.findViewById(R.id.title);
        TextView description = (TextView) view.findViewById(R.id.description);
        TextView link = (TextView) view.findViewById(R.id.link);

        //This links the image view to the XML file
        ImageView image = (ImageView) view.findViewById(R.id.image);

        //Getters and Setters
        title.setText(traffic.getTitle());
        description.setText(traffic.getDescription());
        link.setText(traffic.getLink());

        //This displays the trimmed excerpt for description
        int descriptionLength = traffic.getDescription().length();
        if(descriptionLength >= 100){
            String descriptionTrim = traffic.getDescription().substring(0, 100) + "...";
            description.setText(descriptionTrim);
        }else{
            description.setText(traffic.getDescription());
        }

        //This gets the roadwork image from the drawable file
        int imageID = context.getResources().getIdentifier("roadwork", "drawable", context.getPackageName());
        image.setImageResource(imageID);

        return view;
    }
}
