package com.thirtysix.serendip;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.thirtysix.serendip.model.Conversation;

import java.util.ArrayList;

public class ConversationAdaptor extends BaseAdapter{

    private ArrayList<Conversation> _data;
    Context _c;

    public ConversationAdaptor(ArrayList<Conversation> data, Context c){
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
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder = new ViewHolder();;
        View v = view;
        if (v == null){
            LayoutInflater vi = (LayoutInflater) _c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(R.layout.conversation_custom_row, null);
//        TextView locName = (TextView) v.findViewById(R.id.cTitle);
            holder.locName = (TextView) v.findViewById(R.id.cTitle);
            v.setTag(holder);
            Conversation cons = _data.get(i);
            holder.locName.setText(cons.getTitle());
        }


        return v;
    }

    static class ViewHolder {
        TextView locName;
    }
}
