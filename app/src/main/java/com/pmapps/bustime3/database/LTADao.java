package com.pmapps.bustime3.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

@Dao
public interface LTADao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertBusStop(LTAModel model);

    @Query("SELECT COUNT(code) FROM tbl3_all_bus_stops")
    int getNumRows();

    @Query("SELECT name FROM tbl3_all_bus_stops WHERE code = :code")
    String getNameForCode(String code);

    @Query("SELECT road FROM tbl3_all_bus_stops WHERE code = :code")
    String getRoadForCode(String code);
}
