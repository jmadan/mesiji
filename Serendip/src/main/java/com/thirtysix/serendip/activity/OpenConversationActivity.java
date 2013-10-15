package com.thirtysix.serendip.activity;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.thirtysix.serendip.Constants;
import com.thirtysix.serendip.MesijiClient;
import com.thirtysix.serendip.MessageAdaptor;
import com.thirtysix.serendip.R;
import com.thirtysix.serendip.model.Conversation;
import com.thirtysix.serendip.model.Message;
import com.thirtysix.serendip.model.User;

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
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class OpenConversationActivity extends Activity {

    String con_id = null;
    String con_title = null;
    List<Message> messages;
    User mesijiUser;
    Conversation conversation;

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.conversation_menu, menu);
        return true;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.conversation_detail_view);
        ActionBar bar = getActionBar();
        bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#006868")));
        readIntent();
//        TextView conTitle = (TextView) findViewById(R.id.con_title);
//        conTitle.setText(conversation.getTitle());
//        final ListView MESSAGE_LIST_VIEW = (ListView) findViewById(R.id.messages_list);
//
//        messages = conversation.getMessages();
//        MessageAdaptor messageAdaptor = new MessageAdaptor(messages, getApplicationContext());
//        MESSAGE_LIST_VIEW.setAdapter(messageAdaptor);

//            MesijiClient.get("/message/conversation/" + conversation.getId(), new AsyncHttpResponseHandler() {
//                @Override
//                public void onSuccess(String response) {
//                    getMessages(response, message_list);
//                    if (!message_list.isEmpty()) {
//                        MessageAdaptor messageAdaptor = new MessageAdaptor(message_list, getApplicationContext());
//                        MESSAGE_LIST_VIEW.setAdapter(messageAdaptor);
//                    } else {
//                        setContentView(R.layout.create_message);
//                    }
//                }
//            });



    }

    private void readIntent() {
        Intent intent = getIntent();
        Bundle b = intent.getExtras();
        if (b.isEmpty()) {
            Log.e(Constants.LOG, "Selected Conversation is missing");
            Intent homeIntent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(homeIntent);
        } else {
            conversation = b.getParcelable("conversation");
        }
        System.out.println("Parcelable Conversation>>>>>>>>>>"+conversation.getId() + conversation.getTitle()+conversation.user.getHandle());
    }

    private void getMessages(String response, ArrayList<Message> message_list) {
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
        JSONArray msgs = null;
        try {
            json = new JSONObject(output);
            msgs = json.getJSONObject("data").getJSONObject("json_data").getJSONArray("messages");
//            msgs = json.getJSONObject("data").getJSONArray("json_data");
            System.out.println("I am here....LOOK FOR ME " + msgs.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        int i;
        for (i = 0; i < msgs.length(); i++) {
            try {
                final JSONObject jsonMessage = msgs.getJSONObject(i);
//                System.out.println(jsonMessage.get("msg_text").toString()+"----"+jsonMessage.get("user_id").toString());
                message_list.add(new Message(jsonMessage.get("_id").toString(),
                        jsonMessage.get("msg_text").toString(),
                        jsonMessage.get("user_id").toString(),
                        new SimpleDateFormat("MM/dd/yyy HH:MM:SS a").parse(jsonMessage.get("created_on").toString())));
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }

    public void saveMessage(){
        Log.e(Constants.LOG, "trying to save a message!!!");
        setContentView(R.layout.save_message_view);
        TextView msg_heading = (TextView) findViewById(R.id.save_msg_heading);
        msg_heading.setText(con_title);
    }

    public void createMessage(View view) {
        EditText msg_text = (EditText) findViewById(R.id.msg_text);

        JSONObject jsonMessageObject = new JSONObject();
        JSONArray jsonMessagesObject = new JSONArray();
        try {
            //Message JSON Object
            jsonMessageObject.put("msg_text", msg_text.getText().toString());
            jsonMessageObject.put("user_id", mesijiUser.get_id());
            jsonMessagesObject.put(jsonMessageObject);
        } catch (Exception e) {
            Log.e(Constants.LOG, e.toString());
        }

        RequestParams params = new RequestParams();
        params.put("message", jsonMessageObject.toString());
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
                Log.e(Constants.LOG, "response: " + response);
                try {
                    JSONObject res = new JSONObject(response);
                    Log.e(Constants.LOG, "json object of response: "+ res.toString());
                    if (res.getJSONObject("data").getString("status").toString().equals("201")) {
                        Intent intent = new Intent(getApplicationContext(), OpenConversationActivity.class);
                        Bundle b = new Bundle();
                        b.putString("conversation_id", con_id);
                        b.putString("conversation_title", con_title);
                        intent.putExtras(b);
                        startActivity(intent);
                    } else {
                        Log.e(Constants.LOG, res.getJSONObject("data").getString("error_msg").toString());
//                        startActivity(intent);
                    }
                    System.out.println(res);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        int i = item.getItemId();
        if (i == R.id.leave_message) {
            saveMessage();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    public User getUserById(String user_id) {
        final User[] user = {new User()};
        MesijiClient.get("/user/" + user_id, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(String response) {
                user[0] = getUserFromJson(response);
            }
        });
        return user[0];
    }

    private User getUserFromJson(String response) {
        User user = null;
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
        JSONObject jUser = null;
        try {
            json = new JSONObject(output);
            jUser = json.getJSONObject("data").getJSONObject("json_data");
            user._id = jUser.getString("_id");
            user.name = jUser.getString("name");
            user.email = jUser.getString("email");
            user.handle = jUser.getString("handle");
            user.userId = jUser.getInt("userid");
        } catch (JSONException e) {
            e.printStackTrace();
        }
//        try {
//            final JSONObject jsonMessage = msgs.getJSONObject(i);
//            User.getUserById(jsonMessage.get("user_id").toString());
//            System.out.println(jsonMessage.get("msg_text").toString()+"----"+jsonMessage.get("user_id").toString());
//            message_list.add(new Message(jsonMessage.get("msg_text").toString(), jsonMessage.get("user_id").toString()));
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
        return user;
    }

}
