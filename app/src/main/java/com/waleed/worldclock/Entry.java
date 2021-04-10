package com.waleed.worldclock;

public class Entry {
    private String zone_name;
    private String time;
    private String city_name;

    public Entry(String city_name, String zone_name,String time) {
        setZone_name(zone_name);
        setCity_name(city_name);
        setTime(time);
    }

    public String getZone_name() {
        return zone_name;
    }

    public void setZone_name(String city_name) {
        this.zone_name = city_name;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }


    public String getCity_name() {
        return city_name;
    }

    public void setCity_name(String city_name) {
        this.city_name = city_name;
    }
}
