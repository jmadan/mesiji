package com.thirtysix.serendip;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.PersistentCookieStore;
import com.loopj.android.http.RequestParams;
import com.thirtysix.serendip.activity.LocationActivity;
import com.thirtysix.serendip.model.User;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.entity.StringEntity;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

public class Register extends Activity {
    AlertDialogManager alert;
    AsyncHttpClient mesijiClient = new AsyncHttpClient();
    PersistentCookieStore mesijiCookieStore;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
	}

	public void signUp(View view){
		Context context = getApplicationContext();
        JSONObject newUser = new JSONObject();

		EditText userHandle = (EditText)findViewById(R.id.handle);
		EditText userEmail = (EditText)findViewById(R.id.email);
		EditText userName = (EditText)findViewById(R.id.name);
		EditText userPassword = (EditText)findViewById(R.id.password);
        try {
            newUser.put("name", userName.getText().toString());
            newUser.put("email", userEmail.getText().toString());
            newUser.put("handle", userHandle.getText().toString());
            newUser.put("password", userPassword.getText().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestParams params = new RequestParams();
		params.put("new_user", newUser.toString());
        StringEntity se = null;
        try {
            se = new StringEntity(params.toString());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        mesijiClient.setCookieStore(mesijiCookieStore);
        final User[] u = {null};

        final Intent intent = new Intent(this, LocationActivity.class);
		MesijiClient.post(context, "/user", se, "application/json", new AsyncHttpResponseHandler(){
			
			@Override
			public void onSuccess(String response){
                System.out.println(response);

                try {
                    JSONObject res = new JSONObject(response);
                    JSONObject json_data = res.getJSONObject("json_data");
                    if (json_data.get("userid").toString().equals("0")) {
                        alert = new AlertDialogManager(getApplicationContext());
                        alert.showAlertDialog(Register.this, "Oops...", "Something went wrong. Please try again.");
                    } else {
                        u[0] = User.getUserFromJson(res);
                        BasicClientCookie mesijiCookie = new BasicClientCookie("mesiji.userInfo", res.toString());
                        mesijiCookie.setDomain("msgstory.com");
                        mesijiCookie.setPath("/");
                        mesijiCookie.setVersion(1);
                        mesijiCookieStore.addCookie(mesijiCookie);
//                        Bundle bundle = new Bundle();
//                        bundle.putParcelable("user", u[0]);
                        intent.putExtra("user", u[0]);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
			}
		});
	}
}