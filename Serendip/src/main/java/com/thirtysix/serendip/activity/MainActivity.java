package com.thirtysix.serendip.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.PersistentCookieStore;
import com.loopj.android.http.RequestParams;
import com.thirtysix.serendip.AlertDialogManager;
import com.thirtysix.serendip.Constants;
import com.thirtysix.serendip.MesijiClient;
import com.thirtysix.serendip.R;
import com.thirtysix.serendip.Register;
import com.thirtysix.serendip.model.User;

import org.apache.http.cookie.Cookie;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.List;

public class MainActivity extends Activity {

    public LocationManager locationManager;
    AlertDialogManager alert;
    AsyncHttpClient mesijiClient = new AsyncHttpClient();
    PersistentCookieStore mesijiCookieStore;
    User mesijiUser;

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e(Constants.LOG, "Resumed");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.startup);
        mesijiCookieStore = new PersistentCookieStore(getApplicationContext());
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        final boolean gpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        final boolean wifiEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        if (!gpsEnabled && !wifiEnabled) {
            Log.e(Constants.LOG, "Network is not available");
            setContentView(R.layout.no_network);
        } else {
            List<Cookie> cookies = mesijiCookieStore.getCookies();
            Log.e(Constants.LOG, String.valueOf(cookies.size()));
            for (Cookie c : cookies) {
                if (c.getName().equals("mesiji.userInfo")) {
                    Log.e(Constants.LOG, c.getName());
                    try {
                        JSONObject userJson = new JSONObject(c.getValue());
                        System.out.println(userJson.toString());
                        mesijiUser = new User(userJson.getString("email"),userJson.getString("handle"), userJson.getString("userid"),userJson.getInt("uid"));
                        UserAlreadyLoggedIn(mesijiUser);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
            setContentView(R.layout.activity_main);
        }
    }

    private void UserAlreadyLoggedIn(User mesijiUser) {
        final Intent intent = new Intent(this, LocationActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable("user", mesijiUser);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Toast.makeText(this, "menu>>>>>>>>>>>>>>>>>>" + item.getTitle().toString(), Toast.LENGTH_SHORT).show();
        return true;
    }

    public void loginUser(View view) {
        Context context = getApplicationContext();
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

        final Intent intent = new Intent(this, LocationActivity.class);
        MesijiClient.post(getBaseContext(), "/auth/login", se, "application/json", new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(String response) {
                try {
                    JSONObject res = new JSONObject(response);
                    if (res.get("uid").toString().equals("0")) {
//                        Toast toast = Toast.makeText(getBaseContext(), "Invalid Username/Password combination", 3000);
//                        toast.show();
                        alert = new AlertDialogManager(getApplicationContext());
                        alert.showAlertDialog(MainActivity.this, "Login failed...", "Username/Password is incorrect");
                    } else {
                        BasicClientCookie mesijiCookie = new BasicClientCookie("mesiji.userInfo", res.toString());
                        mesijiCookie.setDomain("msgstory.com");
                        mesijiCookie.setPath("/");
                        mesijiCookie.setVersion(1);
                        mesijiCookieStore.addCookie(mesijiCookie);
                        Log.e(Constants.LOG, res.get("uid").toString());

                    }
                    System.out.println(res);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        startActivity(intent);
    }

    public void registerForm(View view) {
        Intent intent = new Intent(this, Register.class);
        startActivity(intent);
    }

}