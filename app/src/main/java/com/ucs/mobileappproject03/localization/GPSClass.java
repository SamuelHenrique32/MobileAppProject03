package com.ucs.mobileappproject03.localization;

import java.io.Serializable;

public class GPSClass implements Serializable
{
    private int id;
    private String latitude;
    private String longitude;
    private String data;

    public GPSClass(){}

    public GPSClass(String latitude, String longitude, String data)
    {
        this.latitude = latitude;
        this.longitude = longitude;
        this.data = data;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }
}
