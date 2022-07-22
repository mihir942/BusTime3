package com.pmapps.bustime3.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface RouteDao {

    // Select matching data
    @Query("SELECT * FROM TBL2_ROUTES WHERE bus_number LIKE '%' || :busNumber || '%' ORDER BY CASE WHEN bus_number LIKE :busNumber || '%' THEN 0 ELSE 1 END, bus_number LIMIT 7")
    List<RouteModel> getMatchingData(String busNumber);

    // Insert Route
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertRoute(RouteModel model);

    // Clear Table
    @Query("DELETE FROM tbl2_routes")
    void clearAllRoutes();
}
