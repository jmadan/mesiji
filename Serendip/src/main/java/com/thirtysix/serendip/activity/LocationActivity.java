package com.thirtysix.serendip.activity;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import android.app.ActionBar;
import android.app.Activity;
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

import com.loopj.android.http.AsyncHttpClient;
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
    AsyncHttpClient mesijiClient = new AsyncHttpClient();
    PersistentCookieStore mesijiCookieStore;
    User mesijiUser;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.default_menu, menu); // Inflate the menu; this adds items to the action bar if it is present.
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.location_list_view);
        ActionBar bar = getActionBar();
        bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#006868")));
        Intent intent = getIntent();
        Bundle b = intent.getExtras();
        mesijiUser = b.getParcelable("user");
        Log.e(Constants.LOG, mesijiUser.getEmail().toString()+" I am in LocationActivity");
        mesijiCookieStore = new PersistentCookieStore(getApplicationContext());
        locationManager = (LocationManager) this.getSystemService(LOCATION_SERVICE);
        final ListView LOCATION_LIST_VIEW = (ListView) findViewById(R.id.location_list);
        String latlng = getLatLng();
        final ArrayList<Venue> LOCATION_LIST = new ArrayList<Venue>();
        if (latlng != null) {
            MesijiClient.get("/location/coordinates/" + latlng, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(String response) {
                    System.out.println(response);
                    GetLocations(response, LOCATION_LIST);
                    LocationAdaptor adapter = new LocationAdaptor(LOCATION_LIST, getApplicationContext());
                    LOCATION_LIST_VIEW.setAdapter(adapter);
                    LOCATION_LIST_VIEW.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            Venue v = (Venue) adapterView.getItemAtPosition(i);
                            Intent intent = new Intent(getApplicationContext(), ConversationActivity.class);
                            Bundle b = new Bundle();
                            int lIndex = LOCATION_LIST.indexOf(v);
                            v.setLocationDetails(LOCATION_LIST.get(lIndex).locationDetails);
                            b.putParcelable("venueObj", v);
                            b.putParcelable("user", mesijiUser);
                            intent.putExtras(b);
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
        switch (item.getItemId()) {
            case R.id.signout:
                logoutUser();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void logoutUser() {

        System.out.println("LOGGING OUT.......OR TRYING......");
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }

    private void GetLocations(String response, ArrayList<Venue> locs) {
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
        JSONArray venues = null;
        try {
            json = new JSONObject(output);
            venues = json.getJSONObject("response").getJSONArray("venues");
            System.out.println(venues.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        int i;
        for (i = 0; i < venues.length(); i++) {
            try {
                final JSONObject jsonLocation = venues.getJSONObject(i);
                JSONObject jlocation = jsonLocation.getJSONObject("location");
                String address = null;
                String lat = null;
                String lng = null;

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
                locs.add(new Venue(jsonLocation.getString("id"), jsonLocation.getString("name"), address, locationDetails));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void Get_Conversations(String item) {
        if (!item.isEmpty())

            MesijiClient.get("/api/conversations/all/" + item, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(String response) {
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
                        System.out.println(conversations);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            });
    }

    public String getLatLng() {
        String loc = null;
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            locationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
                    1000,
                    1,
                    new MyLocationListener());
            Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (location != null) {
                loc = location.getLatitude() + "/" + location.getLongitude();
                Log.e(Constants.LOG, loc);
            } else {
                // This has to be removed once basic version is deployable
                loc = "53.484601/-2.237296";
                Log.e(Constants.LOG, "No Venue");
            }
        }
        return loc;
    }

    private class MyLocationListener implements LocationListener {

        public void onLocationChanged(Location location) {
            String message = String.format(
                    "New Venue \n Longitude: %1$s \n Latitude: %2$s",
                    location.getLongitude(), location.getLatitude()
            );
            Toast.makeText(LocationActivity.this, message, Toast.LENGTH_LONG).show();
        }

        public void onStatusChanged(String s, int i, Bundle b) {
            Toast.makeText(LocationActivity.this, "Provider status changed",
                    Toast.LENGTH_LONG).show();
        }

        public void onProviderDisabled(String s) {
            Toast.makeText(LocationActivity.this,
                    "Provider disabled by the user. GPS turned off",
                    Toast.LENGTH_LONG).show();
        }

        public void onProviderEnabled(String s) {
            Toast.makeText(LocationActivity.this,
                    "Provider enabled by the user. GPS turned on",
                    Toast.LENGTH_LONG).show();
        }
    }
}