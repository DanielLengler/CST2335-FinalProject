package com.asis.finalproject.nasaearthimage;

import android.graphics.Bitmap;

import java.io.Serializable;

class NasaEarthImage implements Serializable {

    private Bitmap image;
    private String path;
    private double latitude, longitude;

    NasaEarthImage(){

    }

    NasaEarthImage(double latitude, double longitude, String path) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.path = path;
    }

    NasaEarthImage(double latitude, double longitude, Bitmap image) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.image = image;
    }

    Bitmap getImage() {
        return image;
    }

    double getLatitude() {
        return latitude;
    }

    double getLongitude() {
        return longitude;
    }

    void setImage(Bitmap image) {
        this.image = image;
    }

    void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
