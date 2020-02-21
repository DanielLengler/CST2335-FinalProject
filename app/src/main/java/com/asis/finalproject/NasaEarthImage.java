package com.asis.finalproject;

import android.graphics.Bitmap;

public class NasaEarthImage {

    private Bitmap image;
    private double latitude, longitude;

    public NasaEarthImage(){

    }

    public NasaEarthImage(double latitude, double longitude, Bitmap image) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.image = image;
    }

    public Bitmap getImage() {
        return image;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
