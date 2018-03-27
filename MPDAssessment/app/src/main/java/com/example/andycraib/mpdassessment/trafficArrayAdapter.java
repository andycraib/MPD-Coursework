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

/**
 * Created by Andrew Craib on 22/03/2018.
 */

//custom ArrayAdapter
class trafficArrayAdapter extends ArrayAdapter<Traffic> implements Serializable {

    private Context context;
    private ArrayList<Traffic> rentalProperties;

    //constructor, call on creation
    public trafficArrayAdapter(Context context, int resource, List<Traffic> objects) {
        super(context, resource, objects);

        this.context = context;
        this.rentalProperties = (ArrayList)objects;
    }


    //called when rendering the list
    public View getView(int position, View convertView, ViewGroup parent) {

        //get the property we are displaying
        Traffic traffic = rentalProperties.get(position);

        //get the inflater and inflate the XML layout for each item
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.property_layout, null);

        if (traffic.getDescription().contains("Start Date")) {
            String pattern = " EEEE, dd MMMM yyyy";
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);

            String[] parts = traffic.getDescription().split("<br />");
            String part1 = parts[0]; // 004
            String part2 = parts[1]; // 034556

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

                if (diffDays <= 1) {
                    view.setBackgroundColor(Color.GREEN);
                } else if(diffDays >= 1 && diffDays <= 3) {
                    view.setBackgroundColor(Color.YELLOW);
                }
                else
                {
                    view.setBackgroundColor(Color.RED);
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }



        }

        TextView title = (TextView) view.findViewById(R.id.title);
        TextView description = (TextView) view.findViewById(R.id.description);
        TextView link = (TextView) view.findViewById(R.id.link);

        //TextView bedroom = (TextView) view.findViewById(R.id.bedroom);
        //TextView bathroom = (TextView) view.findViewById(R.id.bathroom);
        //TextView carspot = (TextView) view.findViewById(R.id.carspot);
        ImageView image = (ImageView) view.findViewById(R.id.image);

        //set address and description
        //String completeAddress = incident.getTitle() + " " + incident.getDescription() + ", " + incident.getLink() + ", " + incident.getAuthor();
        //address.setText(completeAddress);

        title.setText(traffic.getTitle());
        description.setText(traffic.getDescription());
        link.setText(traffic.getLink());



        //display trimmed excerpt for description
        int descriptionLength = traffic.getDescription().length();
        if(descriptionLength >= 100){
            String descriptionTrim = traffic.getDescription().substring(0, 100) + "...";
            description.setText(descriptionTrim);
        }else{
            description.setText(traffic.getDescription());
        }

        //set price and rental attributes
        // price.setText("$" + String.valueOf(property.getPrice()));
        //bedroom.setText("Bed: " + String.valueOf(property.getBedrooms()));
        //bathroom.setText("Bath: " + String.valueOf(property.getBathrooms()));
        //carspot.setText("Car: " + String.valueOf(property.getCarspots()));

        //get the image associated with this property

        int imageID = context.getResources().getIdentifier("roadwork", "drawable", context.getPackageName());
        image.setImageResource(imageID);

        return view;
    }
}
