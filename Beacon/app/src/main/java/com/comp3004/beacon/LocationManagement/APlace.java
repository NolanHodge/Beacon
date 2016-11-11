package com.comp3004.beacon.LocationManagement;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Jason on 06/11/2016.
 */
public class APlace {
    private String name, address;
    LatLng location;

    public String getAddress() {
        return address;
    }

    public String getName() {
        return name;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LatLng getLocation() {
        return location;
    }

    public void setLocation(LatLng location) {
        this.location = location;
    }
}
