package com.thirtysix.serendip;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.thirtysix.serendip.model.Venue;

import java.util.ArrayList;

public class LocationAdaptor extends BaseAdapter {

    private ArrayList<Venue> _data;
    Context _c;

    public LocationAdaptor (ArrayList<Venue> data, Context c){
        _data = data;
        _c = c;
    }

    @Override
    public int getCount() {
        return _data.size();
    }

    @Override
    public Object getItem(int i) {
        return _data.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        View v = convertView;
        if (v == null){
            LayoutInflater vi = (LayoutInflater) _c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(R.layout.location_custom_row, null);
        }

//        ImageView locIcon = (ImageView) v.findViewById(R.id.loc_icon);
//        TextView locId = (TextView) v.findViewById(R.id.loc_id);
        TextView locName = (TextView) v.findViewById(R.id.loc_name);
        TextView locStreet = (TextView) v.findViewById(R.id.loc_street);
//        ImageView locConvo = (ImageView) v.findViewById(R.id.loc_convo);

        Venue locs = _data.get(i);
//        locIcon.setImageResource(R.drawable.location_4_icon);
//        locId.setText(locs.id);
        locName.setText(locs.getName());
        locStreet.setText(locs.getStreetName());

//        locConvo.setImageResource(R.drawable.megaphone_icon);

        return v;
    }
}
