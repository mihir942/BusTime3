package com.pmapps.bustime3;

import com.google.android.gms.maps.model.LatLng;

public class BusStopItem {
    private String busStopName;
    private String busStopRoad;
    private String busStopCode;
    private LatLng busStopLoc;

    public BusStopItem(String busStopName, String busStopRoad, String busStopCode, LatLng busStopLoc) {
        this.busStopName = busStopName;
        this.busStopRoad = busStopRoad;
        this.busStopCode = busStopCode;
        this.busStopLoc = busStopLoc;
    }

    public String getBusStopName() {
        return busStopName;
    }

    public String getBusStopRoad() {
        return busStopRoad;
    }

    public String getBusStopCode() {
        return busStopCode;
    }
}
