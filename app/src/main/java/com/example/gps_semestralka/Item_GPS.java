package com.example.gps_semestralka;

public class Item_GPS {
    private String lat;
    private String lon;
    private String alt;
    private String desc;

    public Item_GPS(String lat, String lon, String alt, String desc) {
        this.lat = lat;
        this.lon = lon;
        this.alt = alt;
        this.desc = desc;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLon() {
        return lon;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }

    public String getAlt() {
        return alt;
    }

    public void setAlt(String alt) {
        this.alt = alt;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    @Override
    public String toString() {
        return lat + lon + alt + desc;
    }

}
