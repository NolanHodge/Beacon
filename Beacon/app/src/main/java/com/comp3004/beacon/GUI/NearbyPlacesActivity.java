package com.comp3004.beacon.GUI;

import android.content.DialogInterface;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.comp3004.beacon.LocationManagement.LocationService;
import com.comp3004.beacon.LocationManagement.Place_JSON;
import com.comp3004.beacon.LocationManagement.APlace;
import com.comp3004.beacon.Networking.MessageSenderHandler;
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

    private Location myLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nearby_places);

        final LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        final LocationService locationService = new LocationService() {
            @Override
            public void onLocationChanged(Location location) {
                myLocation = location;
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

    private class ParserTask extends AsyncTask<String, Integer, ArrayList<APlace>> {

        JSONObject jObject;

        // Invoked by execute() method of this object
        @Override
        protected ArrayList<APlace> doInBackground(String... jsonData) {
            ArrayList<APlace> places = null;
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
        protected void onPostExecute(ArrayList<APlace> list) {
            try {
                Log.w("Map", "list size: " + list.size());
                // Clears all the existing markers;

                ListView listView = (ListView) findViewById(R.id.nearby_place_listview);

                listView.setAdapter(new NearbyAdapter(list));

            } catch (NullPointerException e) {

            }

        }
    }

    private class NearbyAdapter extends ArrayAdapter<APlace> {

        public NearbyAdapter(List<APlace> objects) {
            super(NearbyPlacesActivity.this, R.layout.nearby_list_item, objects);
        }

        @NonNull
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            final APlace place = getItem(position);
            if (convertView == null) {

                viewHolder = new ViewHolder();
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.nearby_list_item, parent, false);
                viewHolder.layout = convertView.findViewById(R.id.layout);
                viewHolder.nameText = (TextView) convertView.findViewById(R.id.txt_place_name);
                viewHolder.distanceText = (TextView) convertView.findViewById(R.id.txt_distance);
                viewHolder.addressText = (TextView) convertView.findViewById(R.id.txt_address);

                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            viewHolder.nameText.setText(place.getName());
            viewHolder.addressText.setText(place.getAddress());

            float[] dist = new float[1];
            Location.distanceBetween(myLocation.getLatitude(), myLocation.getLongitude(),
                    place.getLocation().latitude, place.getLocation().longitude, dist);

            viewHolder.distanceText.setText((Math.round(dist[0])) + " m");

            viewHolder.layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog dialog = new AlertDialog.Builder(NearbyPlacesActivity.this, R.style.MyDialogTheme)
                            .setTitle("Create a Beacon")
                            .setMessage("Would you like to create a beacon here?")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    MessageSenderHandler.getInstance().sendPublicBeacon(place.getLocation());
                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            })
                            .show();
                }
            });

            return convertView;
        }

    }

    private static class ViewHolder {
        public View layout;
        public TextView nameText;
        public TextView distanceText;
        public TextView addressText;
    }

}
