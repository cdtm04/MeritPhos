package com.merit.myapplication.models;

import java.io.Serializable;

/**
 * Created by merit on 7/13/2015.
 */
public class Location implements Serializable {
    String id, name;
    double latitude, longitude;

    public Location(String id, String name, double latitude, double longitude) {
        this.id = id;
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Location() {
        this.id = "";
        this.name = "";
        this.latitude = 0;
        this.longitude = 0;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
