package com.komsi.lab.kjur.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Afyad Kafa on 1/28/2019.
 */

public class LocationListResponse {
    @SerializedName("location")
    @Expose
    private ArrayList<Location> location = null;

    public LocationListResponse(ArrayList<Location> location) {
        this.location = location;
    }

    public ArrayList<Location> getLocation() {
        return location;
    }

    public void setLocation(ArrayList<Location> location) {
        this.location = location;
    }
}
