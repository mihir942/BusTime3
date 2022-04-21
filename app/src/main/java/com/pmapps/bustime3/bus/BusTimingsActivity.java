package com.pmapps.bustime3.bus;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.material.appbar.MaterialToolbar;
import com.pmapps.bustime3.R;
import com.pmapps.bustime3.busstop.BusStopItem;

public class BusTimingsActivity extends AppCompatActivity {

    MaterialToolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bus_timings);

        BusStopItem busStopItem = getIntent().getParcelableExtra("BUS_STOP_ITEM");
        toolbar = findViewById(R.id.bus_stop_toolbar);
        toolbar.setTitle(busStopItem.getBusStopName());
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.bus_timings_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.refresh_action_1) {
            refreshTimings();
            return true;
        } else if (id == R.id.fav_action_2) {
            favouriteStop();
            return true;
        }
        return false;
    }

    private void refreshTimings() {

    }

    private void favouriteStop() {

    }
}