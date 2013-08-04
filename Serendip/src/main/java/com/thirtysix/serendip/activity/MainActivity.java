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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar bar = getActionBar();
        bar.hide();
        mesijiCookieStore = new PersistentCookieStore(getApplicationContext());
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        List<Cookie> cookies = mesijiCookieStore.getCookies();
        Log.e(Constants.LOG, String.valueOf(cookies.size()));
        for (Cookie c : cookies) {
            if (c.getName().equals("mesiji.userInfo")) {
                Log.e(Constants.LOG, c.getName());
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
        Bundle bundle = new Bundle();
        bundle.putParcelable("user", user);
        intent.putExtras(bundle);
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