package com.thirtysix.serendip;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.thirtysix.serendip.model.Venue;

import java.util.ArrayList;

public class LazyAdaptor extends BaseAdapter {
//    private Activity activity;
     Context c;
    private ArrayList<Venue> data;
    private static LayoutInflater inflater = null;


    public LazyAdaptor(Context _c, ArrayList<Venue> d) {
//        activity = a;
        c = _c;
        data = d;
//        inflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        View vi = convertView;
        if (vi == null) {
            inflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            vi = inflater.inflate(R.layout.location_custom_row, null);
        }


        TextView loc_name = (TextView) vi.findViewById(R.id.loc_name);
        TextView loc_street = (TextView) vi.findViewById(R.id.loc_street);

        Venue tempVenue = data.get(position);

        loc_name.setText(tempVenue.getName().toString());
        loc_street.setText(tempVenue.getStreetName().toString());

        return vi;
    }
}
