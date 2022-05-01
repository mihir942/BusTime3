package com.pmapps.bustime3.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {BusStopModel.class}, version = 1, exportSchema = false)
public abstract class BusStopDatabase extends RoomDatabase {

    // database name
    private static final String DB_NAME = "bus_stops_db";

    // create database instance
    private static BusStopDatabase instance;

    // instance creation
    public static synchronized BusStopDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room
                    .databaseBuilder(
                        context.getApplicationContext(),
                        BusStopDatabase.class,
                        DB_NAME)
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }

    public abstract BusStopDao busStopDao();

}
