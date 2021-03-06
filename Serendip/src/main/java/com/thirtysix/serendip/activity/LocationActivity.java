package com.thirtysix.serendip.activity;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.PersistentCookieStore;
import com.thirtysix.serendip.AlertDialogManager;
import com.thirtysix.serendip.Constants;
import com.thirtysix.serendip.LocationAdaptor;
import com.thirtysix.serendip.MesijiClient;
import com.thirtysix.serendip.model.LocationDetails;
import com.thirtysix.serendip.model.User;
import com.thirtysix.serendip.model.Venue;
import com.thirtysix.serendip.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class LocationActivity extends Activity {

    LocationManager locationManager;
    AlertDialogManager alert;
    PersistentCookieStore mesijiCookieStore;
    User mesijiUser;
    String latlng = null;
    private static long back_pressed;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.default_menu, menu);
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.location_list_view);
        ActionBar bar = getActionBar();
        bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#006868")));
        ReadIntent();
        latlng = getLatLng();
        ShowLocationListView(latlng);
    }

    @Override
    public void onResume(){
        super.onResume();
        latlng = getLatLng();
    }

    @Override
    public void onPause(){
        super.onPause();
    }

    @Override
    public void onBackPressed()
    {
        if (back_pressed + 2000 > System.currentTimeMillis()) {
            super.onBackPressed();
        }
        else {
            Toast.makeText(getBaseContext(), "Press once again to exit!", Toast.LENGTH_SHORT).show();
        }
        back_pressed = System.currentTimeMillis();
    }

    private void ReadIntent() {
        Intent intent = getIntent();
        mesijiUser = (User) intent.getSerializableExtra("user");
        mesijiCookieStore = new PersistentCookieStore(getBaseContext());
    }

    private void ShowLocationListView(String latlng) {
        final ListView LOCATION_LIST_VIEW = (ListView) findViewById(R.id.location_list);
        if (latlng != null) {
            MesijiClient.get("/location/coordinates/" + latlng, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(String response) {
                    final List<Venue> locationList = GetLocations(response);
                    LocationAdaptor adapter = new LocationAdaptor(locationList, getBaseContext());
                    LOCATION_LIST_VIEW.setAdapter(adapter);
                    LOCATION_LIST_VIEW.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            Venue v = (Venue) adapterView.getItemAtPosition(i);
                            int lIndex = locationList.indexOf(v);
                            v.setLocationDetails(locationList.get(lIndex).locationDetails);
                            Intent intent = new Intent(getApplicationContext(), ConversationActivity.class);
                            intent.putExtra("venueObj", v);
                            intent.putExtra("user", mesijiUser);
                            startActivity(intent);
                        }
                    });
                }
            });
        } else {
            alert.showAlertDialog(getApplicationContext(), "Location Error", "Could not determine Venue...Please try again.");
            Log.e(Constants.LOG, "Could not determine Venue...Please try again.");
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int i = item.getItemId();
        if (i == R.id.signout) {
            logoutUser();
            return true;
        } else if (i == R.id.refresh) {
            ShowLocationListView(latlng);
            return true;
        } else if (i == R.id.search) {
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    private void logoutUser() {
        mesijiCookieStore = new PersistentCookieStore(getApplicationContext());
        mesijiCookieStore.clear();
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }

    private ArrayList<Venue> GetLocations(String response) {
        ArrayList<Venue> locs = new ArrayList<Venue>();
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
        JSONObject jsonObject = null;
        JSONArray venues = null;
        try {
            jsonObject = new JSONObject(sb.toString());
            venues = jsonObject.getJSONObject("response").getJSONArray("venues");
            System.out.println(venues.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        for (int i = 0; i < venues.length(); i++) {
            try {
                JSONObject jlocation = venues.getJSONObject(i).getJSONObject("location");
                String address = null;
//                String lat = null;
//                String lng = null;

                LocationDetails locationDetails = new LocationDetails(jlocation.getDouble("lat"), jlocation.getDouble("lng"));
                if (jlocation.has("address"))
                    address = jlocation.getString("address");
//                locationDetails.setAddress(jlocation.getString("address"));
//                locationDetails.setLat(jlocation.getString("lat"));
//                locationDetails.setLng(jlocation.getString("lng"));
//                locationDetails.setState(jlocation.getString("state"));
//                locationDetails.setPostalCode(jlocation.getString("postalCode"));
//                locationDetails.setCity(jlocation.getString("city"));
//                locationDetails.setCountry(jlocation.getString("country"));
//                locationDetails.setCc(jlocation.getString("cc"));
                locs.add(new Venue(venues.getJSONObject(i).getString("id"), venues.getJSONObject(i).getString("name"), address, locationDetails));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return locs;
    }

    public String getLatLng() {
        locationManager = (LocationManager) this.getSystemService(LOCATION_SERVICE);
        String loc = null;
        List<String> providers = locationManager.getProviders(true);

        Location l = null;

        for (int i=providers.size()-1; i>=0; i--) {
            l = locationManager.getLastKnownLocation(providers.get(i));
            if (l != null) break;
        }

        if (l != null) {
            loc = l.getLatitude() + "/" + l.getLongitude();
        }
        return loc;
    }

}
