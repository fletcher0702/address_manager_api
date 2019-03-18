package com.fantech.addressmanager.api.entity.common;

public class Coordinates {

    private double lat;
    private double lng;

    public Coordinates(){

    }
    public Coordinates(double lat, double lng) {
        this.lat = lat;
        this.lng = lng;
    }

    public double getLat() {
        return lat;
    }

    public double getLng() {
        return lng;
    }
}
