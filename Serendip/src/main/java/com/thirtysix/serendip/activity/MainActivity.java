package com.thirtysix.serendip.activity;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import com.loopj.android.http.PersistentCookieStore;
import com.thirtysix.serendip.Constants;
import com.thirtysix.serendip.R;
import com.thirtysix.serendip.Register;
import com.thirtysix.serendip.model.User;

import org.apache.http.cookie.Cookie;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class MainActivity extends Activity {

    public LocationManager locationManager;
    PersistentCookieStore mesijiCookieStore;
    User mesijiUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar bar = getActionBar();
        bar.hide();
        CheckForData();
    }

    @Override
    public void onResume(){
        super.onResume();
        CheckForData();
    }

    @Override
    public void onPause(){
        super.onPause();
        if(locationManager != null){
            locationManager = null;
        }
    }

    private void CheckForData() {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            CheckUserState();
        } else {
            setContentView(R.layout.nodata);
        }
    }

    private void CheckUserState() {
        mesijiCookieStore = new PersistentCookieStore(getApplicationContext());
        List<Cookie> cookies = mesijiCookieStore.getCookies();
        Log.e(Constants.LOG, String.valueOf(cookies.size()));
        for (Cookie c : cookies) {
            if (c.getName().equals("mesiji.userInfo")) {
                try {
                    JSONObject userJson = new JSONObject(c.getValue());
                    System.out.println("userJson: " + userJson.toString());
                    mesijiUser = User.getUserFromJson(userJson);
                    UserAlreadyLoggedIn(mesijiUser);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        setContentView(R.layout.startup);
    }

    private void UserAlreadyLoggedIn(User user) {
        Log.e(Constants.LOG, user.getUserId().toString());
        final Intent intent = new Intent(this, LocationActivity.class);
        intent.putExtra("user", user);
        startActivity(intent);
    }

    public void ShowLogin(View view){
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    public void ShowRegister(View view) {
        Intent intent = new Intent(this, Register.class);
        startActivity(intent);
    }

}