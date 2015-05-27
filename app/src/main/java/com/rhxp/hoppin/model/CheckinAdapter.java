package com.rhxp.hoppin.model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.rhxp.hoppin.R;

import java.util.ArrayList;

/**
 * Created by xavierprimus on 5/27/15.
 */
public class CheckinAdapter extends ArrayAdapter<Checkin> {
    public CheckinAdapter(Context context, ArrayList<Checkin> checkins) {
        super(context, 0, checkins);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Checkin checkin = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.checkin_item, parent, false);
        }
        // Lookup view for data population
        TextView cUserName = (TextView) convertView.findViewById(R.id.checkinUserName);
        TextView cText = (TextView) convertView.findViewById(R.id.checkinText);
        // Populate the data into the template view using the data object
        cUserName.setText(checkin.getUser().getScreen_name());
        cText.setText(checkin.getText());
        // Return the completed view to render on screen
        return convertView;
    }
}