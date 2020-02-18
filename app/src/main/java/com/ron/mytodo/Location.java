package com.ron.mytodo;

import com.google.firebase.database.IgnoreExtraProperties;



@IgnoreExtraProperties
public class Location {


    public double latitude;
    public double longitude;

    // Default constructor required for calls to
    // DataSnapshot.getValue(User.class)
    public Location() {
    }

    public Location(double latitude, double longitude) {

        this.latitude = latitude;
        this.longitude = longitude;
    }
}