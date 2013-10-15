package com.thirtysix.serendip;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.thirtysix.serendip.model.Conversation;
import java.util.List;

public class ConversationAdaptor extends BaseAdapter{

    private List<Conversation> _data;
    Context _c;

    public ConversationAdaptor(List<Conversation> data, Context c){
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
            v = vi.inflate(R.layout.conversation_custom_row, null);
        }

        TextView convTitle = (TextView) v.findViewById(R.id.conversation_title);
        TextView convUser = (TextView) v.findViewById(R.id.conversation_user);
        Conversation cons = _data.get(i);
        convTitle.setText(cons.getTitle());
//        convUser.setText(cons.getUser().getHandle().toString());

        return v;
    }

}
