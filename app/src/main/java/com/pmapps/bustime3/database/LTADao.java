package com.pmapps.bustime3.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface LTADao {

    // Select all data
    @Query("SELECT * FROM tbl3_all_bus_stops")
    List<LTAModel> getAllData();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertBusStop(LTAModel model);

    @Delete
    void deleteBusStop(LTAModel model);

    @Query("SELECT COUNT(code) FROM tbl3_all_bus_stops")
    int getNumRows();

    @Query("SELECT name FROM tbl3_all_bus_stops WHERE code = :code")
    String getNameForCode(String code);

    @Query("SELECT road FROM tbl3_all_bus_stops WHERE code = :code")
    String getRoadForCode(String code);

    @Query("DELETE FROM tbl3_all_bus_stops")
    void clearAllStops();
}
