package com.asis.finalproject.nasaearthimage;

import java.io.Serializable;
import java.util.Calendar;

class NasaEarthImage implements Serializable {

    private long id;
    private String path;
    private Calendar date;
    private double latitude, longitude;

    NasaEarthImage(){

    }

    NasaEarthImage(long id, double latitude, double longitude, String path, Calendar date) {
        setLatitude(latitude);
        setLongitude(longitude);
        setPath(path);
        setId(id);
        setDate(date);
    }

    public static Calendar getCalenderFromLong(long timeInMilliseconds) {
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.setTimeInMillis(timeInMilliseconds);
        return calendar;
    }

    double getLatitude() {
        return latitude;
    }

    double getLongitude() {
        return longitude;
    }

    void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Calendar getDate() {
        return date;
    }

    public void setDate(Calendar date) {
        this.date = date;
    }
}
