package com.thirtysix.serendip.activity;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.thirtysix.serendip.AlertDialogManager;
import com.thirtysix.serendip.Constants;
import com.thirtysix.serendip.ConversationAdaptor;
import com.thirtysix.serendip.MesijiClient;
import com.thirtysix.serendip.R;
import com.thirtysix.serendip.model.Conversation;
import com.thirtysix.serendip.model.Message;
import com.thirtysix.serendip.model.User;
import com.thirtysix.serendip.model.Venue;

import org.apache.http.entity.StringEntity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;

public class ConversationActivity extends Activity {

    Venue atVenue;
    AlertDialogManager alert;
    User mesijiuser;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.conversation_menu, menu);
        return true;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.conversation_list_view);
        ActionBar bar = getActionBar();
        bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#006868")));

        Intent intent = getIntent();
        Bundle b = intent.getExtras();
        atVenue = b.getParcelable("venueObj");
        mesijiuser = b.getParcelable("user");
        final ArrayList<Conversation> CHATTER = new ArrayList<Conversation>();
        if (atVenue.id.isEmpty()) {
            alert.showAlertDialog(getApplicationContext(), "Location Error", "Somehow we lost the location you selected! ");
            Log.e(Constants.LOG, "Venue is missing");
        } else {
            TextView venue_name = (TextView) findViewById(R.id.venue_name);
            venue_name.setText(atVenue.getName().toString());
            final ListView CONVERSATION_LIST_VIEW = (ListView) findViewById(R.id.conversation_list);
            MesijiClient.get("/conversation/all/" + atVenue.getId().toString(), new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(String response) {
                    GetConversations(response, CHATTER);
                    if (CHATTER.isEmpty()) {
                        setContentView(R.layout.create_conversation_view);
                    } else {
                        ConversationAdaptor adapter = new ConversationAdaptor(CHATTER, getApplicationContext());
                        CONVERSATION_LIST_VIEW.setAdapter(adapter);
                        CONVERSATION_LIST_VIEW.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                Conversation selectedConversation = (Conversation) adapterView.getItemAtPosition(i);
                                Log.e(Constants.LOG, ">>>>>>>>>>"+selectedConversation.getTitle().toString());
                                Intent intent = new Intent(getApplicationContext(), OpenConversationActivity.class);
                                Bundle b = new Bundle();
//                                b.putString("conversation_title", selectedConversation.getTitle());
//                                b.putString("conversation_id", selectedConversation.getId());
//                                b.putParcelable("user", mesijiuser);
                                b.putParcelable("conversation", selectedConversation);
                                intent.putExtras(b);
                                startActivity(intent);
                            }
                        });
                    }
                }
            });

        }

    }

    public void createConversation(View view) {
        EditText con_title = (EditText) findViewById(R.id.conversation_title);
        EditText msg = (EditText) findViewById(R.id.message);

        JSONObject jsonConversationObject = new JSONObject();
        final JSONObject jsonMessageObject = new JSONObject();
        JSONObject jsonVenueObject = new JSONObject();
        JSONArray jsonMessagesObject = new JSONArray();
        JSONObject jsonUserObject = new JSONObject();
        try {
            //User JSON Object
            //{"_id":"522d8746156b5751b1000001","userid":100,"name":"Test User","email":"test@test.com","handle":"testuser","phone":"9008307311","relations":{"messages":null},"created_on":"0001-01-01T00:00:00Z"}
            jsonUserObject.put("_id", mesijiuser.get_id().toString());
            jsonUserObject.put("name", mesijiuser.getName().toString());
            jsonUserObject.put("email", mesijiuser.getEmail().toString());
            jsonUserObject.put("handle", mesijiuser.getHandle().toString());

            //Message JSON Object
            jsonMessageObject.put("msg_text", msg.getText().toString());
            jsonMessageObject.put("user_id", mesijiuser.get_id().toString());
            jsonMessagesObject.put(jsonMessageObject);

            //Venue JSON Object
            jsonVenueObject.put("four_id", atVenue.getId().toString());
            jsonVenueObject.put("name", atVenue.getName().toString());
            jsonVenueObject.put("lat", atVenue.locationDetails.getLat());
            jsonVenueObject.put("lng", atVenue.locationDetails.getLng());

            // Conversation JSON Object
            jsonConversationObject.put("title", con_title.getText().toString());
//            jsonConversationObject.put("messages", jsonMessagesObject);
            jsonConversationObject.put("venue", jsonVenueObject);
            jsonConversationObject.put("user", jsonUserObject);
            jsonConversationObject.put("is_approved", true);
        } catch (Exception e) {
            Log.e(Constants.LOG, e.toString());
        }

        RequestParams params = new RequestParams();
        Log.e(Constants.LOG, jsonConversationObject.toString());
        params.put("conversation", jsonConversationObject.toString());
//        params.put("message", jsonMessageObject.toString());
        Log.e(Constants.LOG, params.toString());
        StringEntity se = null;
        try{
            se = new StringEntity(params.toString());
        }catch (UnsupportedEncodingException e){
            e.printStackTrace();
        }
//        final Intent intent = new Intent(this, LocationActivity.class);
        MesijiClient.post(getBaseContext(), "/conversation/", se, "application/json", new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(String response) {
                Log.e(Constants.LOG, "response: " + response);
                try {
                    JSONObject res = new JSONObject(response);
                    Log.e(Constants.LOG, "json object of response: "+ res.toString());
                    if (res.getJSONObject("data").getString("status").toString().equals("201")) {
                        if (SaveMessage(jsonMessageObject, res.getJSONObject("data").getJSONObject("json_data").getString("_id").toString())) {
                            finish();
                            startActivity(getIntent());
                        }
//                        Intent intent = new Intent(getApplicationContext(), ConversationActivity.class);
//                        Bundle b = new Bundle();
//                        b.putParcelable("venueObj", atVenue);
//                        intent.putExtras(b);
                    } else {
                        Log.e(Constants.LOG, ">>>>>>>>>>>>>>>"+res.getJSONObject("data").getString("error_msg").toString());
//                        startActivity(intent);
                    }
                    System.out.println(res);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private boolean SaveMessage(JSONObject jsonMsg, String con_id){
        final boolean[] status = new boolean[1];
        status[0]=true;
        RequestParams params = new RequestParams();
        params.put("message", jsonMsg.toString());
        Log.e(Constants.LOG, params.toString());
        StringEntity se = null;
        try{
            se = new StringEntity(params.toString());
        }catch (UnsupportedEncodingException e){
            e.printStackTrace();
        }

        MesijiClient.post(getBaseContext(), "/message/conversation/"+con_id, se, "application/json", new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(String response) {
                status[0]=true;
                Log.e(Constants.LOG, "response: " + response);
                try {
                    JSONObject res = new JSONObject(response);
                    Log.e(Constants.LOG, "json object of response: "+ res.toString());
                    if (!res.getJSONObject("data").getString("status").toString().equals("201")) {
                        status[0] = false;
                        Log.e(Constants.LOG, ">>>>>>>>>>>>>>>"+res.getJSONObject("data").getString("error_msg").toString());
                    }
                    System.out.println(res);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        return status[0];
    }

    private void GetConversations(String response, ArrayList<Conversation> chatter) {
        InputStream is = new ByteArrayInputStream(response.getBytes());
        StringBuilder sb = new StringBuilder();
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        try {
            for (String line; null != (line = reader.readLine()); ) {
                sb.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        String output = sb.toString();
        JSONObject json = null;
        JSONArray conversations = null;
        try {
            json = new JSONObject(output);
            conversations = json.getJSONObject("data").getJSONArray("json_data");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        int i;
        for (i = 0; i < conversations.length(); i++) {
            try {
                final JSONObject jsonConversation = conversations.getJSONObject(i);
                if (jsonConversation.get("is_approved").toString().equals("true")){
                    JSONObject userObject = jsonConversation.getJSONObject("user");
                    chatter.add(new Conversation(jsonConversation.getString("_id"),
                            jsonConversation.getString("title"),
                            jsonConversation.getBoolean("is_approved"),
                            new String[]{jsonConversation.getJSONArray("circles").toString()},
                            new User(userObject.getString("_id"), userObject.getInt("userid"),
                                    userObject.getString("name"), userObject.getString("email"),
                                    userObject.getString("handle")),
                            new ArrayList<Message>())
                            );
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void GetVenueDetail(String response) {
        InputStream is = new ByteArrayInputStream(response.getBytes());
        StringBuilder sb = new StringBuilder();
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        try {
            for (String line; null != (line = reader.readLine()); ) {
                sb.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        String output = sb.toString();
        JSONObject json = null;
        JSONObject venue = null;
        try {
            json = new JSONObject(output);
            venue = json.getJSONObject("response").getJSONObject("venues");
            atVenue.setId(venue.getString("id"));
            atVenue.setName(venue.getString("name"));
            atVenue.contact.setPhone(venue.getString("phone"));
            atVenue.contact.setFormattedPhone(venue.getString("formattedPhone"));
            atVenue.locationDetails.setAddress(venue.getString("address"));
            atVenue.locationDetails.setLat(venue.getDouble("lat"));
            atVenue.locationDetails.setLng(venue.getDouble("lng"));
            atVenue.locationDetails.setCity(venue.getString("city"));
            atVenue.locationDetails.setPostalCode(venue.getString("postalCode"));
            atVenue.locationDetails.setState(venue.getString("state"));
            atVenue.locationDetails.setCountry(venue.getString("country"));

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void show_messages(){
        Log.e(Constants.LOG, "I am here to my surprise!");
    }
}
