package com.mobileapp.sab.localchat;

import android.location.Location;

/**
 * Created by sab on 2/15/2016.
 */
public class LocationData {

    private static LocationData instance = null;

    private LocationData(){}

    private Location location;


    public Location getLocation(){
        return location;
    }

    public void setLocation(Location _location){
        location = _location;
    }

    public static LocationData getLocationData(){
        if(instance == null){
            instance = new LocationData();
        }
        return instance;
    }

}
