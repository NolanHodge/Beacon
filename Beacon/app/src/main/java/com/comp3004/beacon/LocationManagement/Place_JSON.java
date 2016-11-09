package com.comp3004.beacon.LocationManagement;


import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Jason on 18/09/2015.
 */
public class Place_JSON {

    /**
     * Receives a JSONObject and returns a list
     */
    public ArrayList<APlace> parse(JSONObject jObject) {

        JSONArray jPlaces = null;
        try {

            jPlaces = jObject.getJSONArray("results");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        /** Invoking getPlaces with the array of json object
         * where each json object represent a place
         */
        return getPlaces(jPlaces);
    }

    private ArrayList<APlace> getPlaces(JSONArray jPlaces) {
        int placesCount = jPlaces.length();
        ArrayList<APlace> placesList = new ArrayList<>();

        /** Taking each place, parses and adds to list object */
        for (int i = 0; i < placesCount; i++) {
            try {
                /** Call getPlace with place JSON object to parse the place */
                placesList.add(getPlace((JSONObject) jPlaces.get(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return placesList;
    }

    /**
     * Parsing the Place JSON object
     */
    private APlace getPlace(JSONObject jPlace) {
        final APlace place = new APlace();

        try {
            // Extracting Place name, if available
            if (!jPlace.isNull("name")) {
                place.setName(jPlace.getString("name"));
            }

            // Extracting Place Vicinity, if available
            if (!jPlace.isNull("vicinity")) {
                place.setAddress(jPlace.getString("vicinity"));
            }

            place.setLocation(new LatLng(jPlace.getJSONObject("geometry").getJSONObject("location").getDouble("lat"),
                    jPlace.getJSONObject("geometry").getJSONObject("location").getDouble("lng")));

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return place;
    }
}