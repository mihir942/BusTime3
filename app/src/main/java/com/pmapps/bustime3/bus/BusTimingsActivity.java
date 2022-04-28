package com.pmapps.bustime3.bus;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.material.appbar.MaterialToolbar;
import com.pmapps.bustime3.R;
import com.pmapps.bustime3.busstop.BusStopItem;
import com.pmapps.bustime3.enums.Load;
import com.pmapps.bustime3.enums.Type;

import java.util.ArrayList;
import java.util.List;

public class BusTimingsActivity extends AppCompatActivity {

    MaterialToolbar toolbar;

    RecyclerView recyclerView;
    List<BusItem> busItemList;
    BusAdapter busAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bus_timings);

        initialiseStuffs();
    }

    private void initialiseStuffs() {
        BusStopItem busStopItem = getIntent().getParcelableExtra("BUS_STOP_ITEM");
        toolbar = findViewById(R.id.bus_stop_toolbar);
        toolbar.setTitle(busStopItem.getBusStopName());
        setSupportActionBar(toolbar);

        recyclerView = findViewById(R.id.bus_timings_recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        busItemList = new ArrayList<>();
        busAdapter = new BusAdapter(this, busItemList);
        recyclerView.setAdapter(busAdapter);
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