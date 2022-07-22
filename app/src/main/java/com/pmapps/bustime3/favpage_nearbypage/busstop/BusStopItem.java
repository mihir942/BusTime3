package com.pmapps.bustime3.favpage_nearbypage.busstop;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.LatLng;

public class BusStopItem implements Parcelable {
    private final String busStopName;
    private final String busStopRoad;
    private final String busStopCode;
    private LatLng busStopLoc;

    public BusStopItem(String busStopName, String busStopRoad, String busStopCode) {
        this.busStopName = busStopName;
        this.busStopRoad = busStopRoad;
        this.busStopCode = busStopCode;
    }

    public BusStopItem(String busStopName, String busStopRoad, String busStopCode, LatLng busStopLoc) {
        this.busStopName = busStopName;
        this.busStopRoad = busStopRoad;
        this.busStopCode = busStopCode;
        this.busStopLoc = busStopLoc;
    }

    protected BusStopItem(Parcel in) {
        busStopName = in.readString();
        busStopRoad = in.readString();
        busStopCode = in.readString();
        busStopLoc = in.readParcelable(LatLng.class.getClassLoader());
    }

    public static final Creator<BusStopItem> CREATOR = new Creator<BusStopItem>() {
        @Override
        public BusStopItem createFromParcel(Parcel in) {
            return new BusStopItem(in);
        }

        @Override
        public BusStopItem[] newArray(int size) {
            return new BusStopItem[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(busStopName);
        parcel.writeString(busStopRoad);
        parcel.writeString(busStopCode);
        parcel.writeParcelable(busStopLoc, i);
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
