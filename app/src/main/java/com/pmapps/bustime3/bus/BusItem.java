package com.pmapps.bustime3.bus;

import com.pmapps.bustime3.enums.*;

public class BusItem {
    private final String busNumber;
    private String busTime1;
    private Load busLoad1;
    private Type busType1;

    private String busTime2;
    private Load busLoad2;
    private Type busType2;

    private String busTime3;
    private Load busLoad3;
    private Type busType3;

    public BusItem(String busNumber) {
        this.busNumber = busNumber;
    }

    public void setBusTime1(String busTime1) {
        this.busTime1 = busTime1;
    }

    public void setBusLoad1(Load busLoad1) {
        this.busLoad1 = busLoad1;
    }

    public void setBusType1(Type busType1) {
        this.busType1 = busType1;
    }

    public void setBusTime2(String busTime2) {
        this.busTime2 = busTime2;
    }

    public void setBusLoad2(Load busLoad2) {
        this.busLoad2 = busLoad2;
    }

    public void setBusType2(Type busType2) {
        this.busType2 = busType2;
    }

    public void setBusTime3(String busTime3) {
        this.busTime3 = busTime3;
    }

    public void setBusLoad3(Load busLoad3) {
        this.busLoad3 = busLoad3;
    }

    public void setBusType3(Type busType3) {
        this.busType3 = busType3;
    }

    public String getBusNumber() {
        return busNumber;
    }

    public String getBusTime1() {
        return busTime1;
    }

    public Load getBusLoad1() {
        return busLoad1;
    }

    public Type getBusType1() {
        return busType1;
    }

    public String getBusTime2() {
        return busTime2;
    }

    public Load getBusLoad2() {
        return busLoad2;
    }

    public Type getBusType2() {
        return busType2;
    }

    public String getBusTime3() {
        return busTime3;
    }

    public Load getBusLoad3() {
        return busLoad3;
    }

    public Type getBusType3() {
        return busType3;
    }
}
