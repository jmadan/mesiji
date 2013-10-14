package com.thirtysix.serendip;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.thirtysix.serendip.model.Message;

import java.util.ArrayList;
import java.util.List;

public class MessageAdaptor extends BaseAdapter {

    private List<Message> _data;
    Context _c;

    public MessageAdaptor(List<Message> data, Context c){
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
        ViewHolder holder = new ViewHolder();
        View v = view;
        if (v == null){
            LayoutInflater vi = (LayoutInflater) _c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(R.layout.message_custom_row, null);
            holder.msg = (TextView) v.findViewById(R.id.msg_title);
            holder.msg_owner = (TextView) v.findViewById(R.id.message_owner);
            v.setTag(holder);
            Message msgs = _data.get(i);
            holder.msg.setText(msgs.getMessage().toString());
//            holder.msg_owner.setText(msgs.user.getName().toString());
        }
        return v;
    }

    static class ViewHolder {
        TextView msg;
        TextView msg_owner;
    }
}