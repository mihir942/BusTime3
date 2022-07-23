package com.pmapps.bustimesg.database;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "tbl2_routes")
public class RouteModel {

    public RouteModel(@NonNull String busNumber, String operator) {
        this.busNumber = busNumber;
        this.operator = operator;
    }

    @ColumnInfo(name = "bus_number")
    @PrimaryKey
    @NonNull
    private String busNumber;

    @ColumnInfo(name = "operator")
    private String operator;

    @NonNull
    public String getBusNumber() {
        return busNumber;
    }

    public String getOperator() {
        return operator;
    }

}
