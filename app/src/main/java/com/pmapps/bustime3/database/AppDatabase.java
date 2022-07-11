package com.pmapps.bustime3.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {BusStopModel.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    // database name
    private static final String DB_NAME = "app_db";

    // create database instance
    private static AppDatabase instance;

    // instance creation
    public static synchronized AppDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room
                    .databaseBuilder(
                        context.getApplicationContext(),
                        AppDatabase.class,
                        DB_NAME)
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }

    public abstract BusStopDao busStopDao();

}
