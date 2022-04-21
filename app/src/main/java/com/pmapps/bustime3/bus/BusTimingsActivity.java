package com.pmapps.bustime3.bus;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.appbar.MaterialToolbar;
import com.pmapps.bustime3.R;
import com.pmapps.bustime3.busstop.BusStopItem;

public class BusTimingsActivity extends AppCompatActivity {

    MaterialToolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bus_timings);
        toolbar = findViewById(R.id.bus_stop_toolbar);
        setSupportActionBar(toolbar);

        BusStopItem busStopItem = getIntent().getParcelableExtra("BUS_STOP_ITEM");
        Toast.makeText(this,"BUSSTOP: " + busStopItem.getBusStopName() + busStopItem.getBusStopCode(),Toast.LENGTH_SHORT).show();
        Log.d("CORD:",": " + busStopItem.getBusStopLoc().latitude);
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
            Toast.makeText(this, "Refresh!", Toast.LENGTH_SHORT).show();
            return true;
        } else if (id == R.id.fav_action_2) {
            favouriteStop();
            Toast.makeText(this, "Favourite!", Toast.LENGTH_SHORT).show();
            return true;
        }
        return false;
    }

    private void refreshTimings() {

    }

    private void favouriteStop() {

    }
}