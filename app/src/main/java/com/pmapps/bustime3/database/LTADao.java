package com.pmapps.bustime3.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

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

    // Select matching data
    @Query("SELECT * FROM tbl3_all_bus_stops WHERE name LIKE '%' || :busStopName || '%' ORDER BY CASE WHEN name LIKE :busStopName || '%' THEN 0 ELSE 1 END, name")
    List<LTAModel> getMatchingData(String busStopName);
}
