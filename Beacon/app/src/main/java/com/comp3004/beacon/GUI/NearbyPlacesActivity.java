package com.comp3004.beacon.GUI;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.comp3004.beacon.FirebaseServices.DatabaseManager;
import com.comp3004.beacon.LocationManagement.LocationService;
import com.comp3004.beacon.LocationManagement.Place_JSON;
import com.comp3004.beacon.LocationManagement.APlace;
import com.comp3004.beacon.Networking.MessageSenderHandler;
import com.comp3004.beacon.R;
import com.comp3004.beacon.User.CurrentBeaconUser;
import com.google.android.gms.maps.model.LatLng;

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
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generic_list);

        setSupportActionBar((Toolbar) findViewById(R.id.generic_toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle(getTitle());
        findViewById(R.id.generic_progress_bar).setVisibility(View.VISIBLE);
        context = this;
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

                ListView listView = (ListView) findViewById(R.id.generic_listview);

                listView.setAdapter(new NearbyAdapter(list));

                findViewById(R.id.generic_progress_bar).setVisibility(View.INVISIBLE);

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
                            .setTitle("Public Location")
                            .setItems(new String[]{"Create Beacon Here", "Track", "Cancel"}, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    switch (which) {
                                        case 0:
                                            MessageSenderHandler.getInstance().sendPublicBeacon(place.getLocation());
                                            Intent publicBeaconIntent = new Intent(context, MapsActivity.class);
                                            publicBeaconIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                            publicBeaconIntent.putExtra(MapsActivity.FRIEND_REQUEST, true);
                                            break;
                                        case 1:
                                            LatLng latlng = place.getLocation();
                                            String lat = "" + latlng.latitude;
                                            String lon = "" + latlng.longitude;
                                            Intent intent2 = new Intent(NearbyPlacesActivity.this, ArrowActivity2.class);
                                            intent2.putExtra(ArrowActivity2.FROM_MAP_TRACK_LAT, lat);
                                            intent2.putExtra(ArrowActivity2.FROM_MAP_TRACK_LON, lon);
                                            startActivity(intent2);
                                            break;
                                        case 2:
                                            break;
                                    }
                                }
                            }).show();
                }
            });

            TextView text1 = (TextView) convertView.findViewById(R.id.txt_distance);
            TextView text2 = (TextView) convertView.findViewById(R.id.txt_place_name);
            TextView text3 = (TextView) convertView.findViewById(R.id.txt_address);

            if ((position % 2) == 1) {
                convertView.setBackgroundColor(getContext().getResources().getColor(R.color.colorPrimary));
                text1.setTextColor(getContext().getResources().getColor(android.R.color.white));
                text2.setTextColor(getContext().getResources().getColor(android.R.color.white));
                text3.setTextColor(getContext().getResources().getColor(android.R.color.white));
            } else {
                convertView.setBackgroundColor(getContext().getResources().getColor(R.color.colorPrimaryDark));
                text1.setTextColor(getContext().getResources().getColor(android.R.color.white));
                text2.setTextColor(getContext().getResources().getColor(android.R.color.white));
                text3.setTextColor(getContext().getResources().getColor(android.R.color.white));
            }

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
