package com.comp3004.beacon.GUI;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SeekBar;

import com.comp3004.beacon.LocationManagement.LocationService;
import com.comp3004.beacon.R;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class NearbyPlacesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nearby_places);

        final LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        final LocationService locationService = new LocationService() {
            @Override
            public void onLocationChanged(Location location) {

                String search = String.format
                        ("https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=%f,%f&rankby=distance&key=%s&sensor=true",
                                location.getLatitude(), location.getLongitude(), getString(R.string.api_key));
                Log.w("SEARCH", search);
                new PlacesTask().execute(search);
                try {
                    locationManager.removeUpdates(this);
                } catch (SecurityException e) {
                }
            }
        };
        try {
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 5, locationService);
        } catch (SecurityException e) {
        }
    }


    private class PlacesTask extends AsyncTask<String, Integer, String> {
        String data = null;

        // Invoked by execute() method of this object
        @Override
        protected String doInBackground(String... url) {
            try {
                data = downloadUrl(url[0]);
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        // Executed after the complete execution of doInBackground() method
        @Override
        protected void onPostExecute(String result) {
            new ParserTask().execute(result);
        }
    }

    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);

            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();

            // Connecting to url
            urlConnection.connect();

            // Reading data from url
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuilder sb = new StringBuilder();

            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            data = sb.toString();

            br.close();

        } catch (Exception e) {
            Log.w("Error downloading url", e.toString());
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }

    private class ParserTask extends AsyncTask<String, Integer, ArrayList<aPlace>> {

        JSONObject jObject;

        // Invoked by execute() method of this object
        @Override
        protected ArrayList<aPlace> doInBackground(String... jsonData) {
            ArrayList<aPlace> places = null;
            Place_JSON placeJson = new Place_JSON();

            try {
                jObject = new JSONObject(jsonData[0]);
                places = placeJson.parse(jObject);

            } catch (Exception e) {
                Log.d("Exception", e.toString());
            }
            return places;
        }

        // Executed after the complete execution of doInBackground() method
        @Override
        protected void onPostExecute(ArrayList<aPlace> list) {
            try {
                Log.w("Map", "list size: " + list.size());
                // Clears all the existing markers;

                ListView listView = (ListView) findViewById(R.id.nearby_place_listview);

                ArrayList<String> placeList = new ArrayList<>();
                for (aPlace place : list) {
                    placeList.add(place.getName());
                }

                // specify an adapter (see also next example)

                listView.setAdapter(new ArrayAdapter<String>(NearbyPlacesActivity.this, android.R.layout.simple_list_item_1, placeList));

            } catch (NullPointerException e) {

            }

        }
    }

}
