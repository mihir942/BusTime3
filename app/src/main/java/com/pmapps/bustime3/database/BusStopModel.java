package com.pmapps.bustime3.database;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "tbl1_fav_bus_stops")
public class BusStopModel {

    public BusStopModel(@NonNull String busStopCode, String busStopName, String busStopRoad) {
        this.busStopCode = busStopCode;
        this.busStopName = busStopName;
        this.busStopRoad = busStopRoad;
    }

    @ColumnInfo(name = "code")
    @PrimaryKey()
    @NonNull
    private String busStopCode;

    @ColumnInfo(name = "name")
    private String busStopName;

    @ColumnInfo(name = "road")
    private String busStopRoad;

    @NonNull
    public String getBusStopCode() {
        return busStopCode;
    }

    public void setBusStopCode(@NonNull String busStopCode) {
        this.busStopCode = busStopCode;
    }

    public String getBusStopName() {
        return busStopName;
    }

    public String getBusStopRoad() {
        return busStopRoad;
    }
}
