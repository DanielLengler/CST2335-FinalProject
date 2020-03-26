package com.asis.finalproject.nasaearthimage;

import java.io.Serializable;
import java.util.Calendar;

/**
 * A DTO class used for storing variables related to Nasa Images.
 */
class NasaEarthImage implements Serializable {

    private long id;
    private String path;
    private Calendar date;
    private double latitude, longitude;

    /**
     * Default no args constructor
     */
    NasaEarthImage(){

    }

    /**
     * 5 args constructor
     * @param id - the id of the nasaEarthImage
     * @param latitude - the latitude of the nasaEarthImage
     * @param longitude - the longitude of the nasaEarthImage
     * @param path - the path of the image on the phone
     * @param date - the date of the nasaEarthImage
     */
    NasaEarthImage(long id, double latitude, double longitude, String path, Calendar date) {
        setLatitude(latitude);
        setLongitude(longitude);
        setPath(path);
        setId(id);
        setDate(date);
    }

    /**
     * Helper method that returns a Calendar instance with the date from the argument
     * @param timeInMilliseconds - the time in milliseconds since epoch
     * @return calender instance
     */
    static Calendar getCalenderFromLong(long timeInMilliseconds) {
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

    String getPath() {
        return path;
    }

    void setPath(String path) {
        this.path = path;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    Calendar getDate() {
        return date;
    }

    void setDate(Calendar date) {
        this.date = date;
    }
}
