package com.thirtysix.serendip.activity;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.PersistentCookieStore;
import com.loopj.android.http.RequestParams;
import com.thirtysix.serendip.AlertDialogManager;
import com.thirtysix.serendip.Constants;
import com.thirtysix.serendip.MesijiClient;
import com.thirtysix.serendip.R;
import com.thirtysix.serendip.model.User;

import org.apache.http.entity.StringEntity;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

public class LoginActivity extends Activity {

    public LocationManager locationManager;
    AlertDialogManager alert;
    AsyncHttpClient mesijiClient = new AsyncHttpClient();
    PersistentCookieStore mesijiCookieStore;
    User mesijiUser;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        ActionBar bar = getActionBar();
        bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#006868")));
        mesijiCookieStore = new PersistentCookieStore(getApplicationContext());
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        final boolean gpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        final boolean wifiEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        if (!gpsEnabled && !wifiEnabled) {
            Log.e(Constants.LOG, "Network is not available");
            setContentView(R.layout.no_network);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.signout:
                logoutUser();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void logoutUser() {
        mesijiCookieStore.clear();
        System.out.println("LOGGING OUT.......OR TRYING......");
        finish();
        startActivity(getIntent());
    }

    public void LoginUser(View view) {
        EditText userEmail = (EditText) findViewById(R.id.email_text);
        EditText userPass = (EditText) findViewById(R.id.password_text);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("email", userEmail.getText().toString());
            jsonObject.put("password", userPass.getText().toString());
        } catch (Exception e) {
        }

        RequestParams params = new RequestParams();
        params.put("formData", jsonObject.toString());
        StringEntity se = null;
        try {
            se = new StringEntity(params.toString());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        mesijiClient.setCookieStore(mesijiCookieStore);
        final User[] u = {null};

        final Intent intent = new Intent(this, LocationActivity.class);
        MesijiClient.post(getBaseContext(), "/auth/login", se, "application/json", new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(String response) {
                try {
                    JSONObject res = new JSONObject(response);
                    if (res.get("userid").toString().equals("0")) {
//                        Toast toast = Toast.makeText(getBaseContext(), "Invalid Username/Password combination", 3000);
//                        toast.show();
                        alert = new AlertDialogManager(getApplicationContext());
                        alert.showAlertDialog(LoginActivity.this, "Login failed...", "Username/Password is incorrect");
                    } else {
                        u[0] = User.getUserFromJson(res);
                        BasicClientCookie mesijiCookie = new BasicClientCookie("mesiji.userInfo", res.toString());
                        mesijiCookie.setDomain("msgstory.com");
                        mesijiCookie.setPath("/");
                        mesijiCookie.setVersion(1);
                        mesijiCookieStore.addCookie(mesijiCookie);
                        Bundle bundle = new Bundle();
                        bundle.putParcelable("user", u[0]);
                        intent.putExtras(bundle);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                startActivity(intent);
            }
        });
    }
}