package com.example.android.handyshop;


public class LocationStruct {
    public double minLat;
    public double minLon;
    public double maxLat;
    public double maxLon;

    public double getMaxLat() {
        return maxLat;
    }

    public double getMinLon() {
        return minLon;
    }

    public double getMinLat() {
        return minLat;
    }

    public double getMaxLon() {
        return maxLon;
    }

    public void setMaxLon(double maxLon) {
        this.maxLon = maxLon;
    }

    public void setMaxLat(double maxLat) {
        this.maxLat = maxLat;
    }

    public void setMinLat(double minLat) {
        this.minLat = minLat;
    }

    public void setMinLon(double minLon) {
        this.minLon = minLon;
    }


public LocationStruct(){};

}
