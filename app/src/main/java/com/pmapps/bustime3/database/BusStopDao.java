package com.pmapps.bustime3.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface BusStopDao {

    // Select all data
    @Query("SELECT * FROM fav_bus_stops")
    List<BusStopModel> getAllData();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertBusStop(BusStopModel model);

    @Delete
    void deleteBusStop(BusStopModel model);

    @Query("DELETE FROM fav_bus_stops WHERE code=:code")
    void deleteBusStopWithCode(String code);

    @Query("SELECT EXISTS (SELECT 1 FROM fav_bus_stops WHERE code = :code)")
    boolean exists(String code);

}
