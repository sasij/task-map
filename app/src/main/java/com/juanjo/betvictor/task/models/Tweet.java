package com.juanjo.betvictor.task.models;

/**
 * Created by juanjo on 4/2/15.
 */
public class Tweet {

    double id;
    double latitude;
    double longitude;

    public Tweet() {
    }

    public Tweet(double id, double latitude, double longitude) {
        this.id = id;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public double getId() {
        return id;
    }

    public void setId(double id) {
        this.id = id;
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
