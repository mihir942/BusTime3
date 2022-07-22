package com.pmapps.bustime3.searchpage;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.appbar.MaterialToolbar;
import com.pmapps.bustime3.R;
import com.pmapps.bustime3.favpage_nearbypage.bus.BusTimingsActivity;
import com.pmapps.bustime3.favpage_nearbypage.busstop.BusStopAdapter;
import com.pmapps.bustime3.favpage_nearbypage.busstop.BusStopItem;

import java.util.ArrayList;
import java.util.List;

public class BusRouteActivity extends AppCompatActivity {

    MaterialToolbar toolbar;
    RecyclerView recyclerView;
    List<BusStopItem> busStopItemList1;
    List<BusStopItem> busStopItemList2;
    BusStopAdapter busStopAdapter;

    String busNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bus_route);

        initialiseStuffs();
    }

    private void initialiseStuffs() {
        busNumber = getIntent().getStringExtra("BUS_ROUTE_NUM");
        toolbar = findViewById(R.id.bus_route_toolbar);
        toolbar.setTitle(busNumber);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(view -> this.finish());
        recyclerView = findViewById(R.id.bus_route_recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        busStopItemList1 = new ArrayList<>();
        busStopAdapter = new BusStopAdapter(this,busStopItemList1,busStopItem -> {
            openBusStop(busStopItem);
        });
        recyclerView.setAdapter(busStopAdapter);
    }

    private void openBusStop(BusStopItem busStopItem) {
        Intent intent = new Intent(this, BusTimingsActivity.class);
        intent.putExtra("BUS_STOP_ITEM",busStopItem);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.bus_route_menu, menu);

        //set swap direction icon
        menu.getItem(0).setIcon(ContextCompat.getDrawable(getApplicationContext(),R.drawable.ic_swap_dir));
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.direction_action_1) {
            swapDirection();
            return true;
        }
        return false;
    }

    private void swapDirection() {
        Toast.makeText(this,"direction swap",Toast.LENGTH_SHORT).show();
    }
}