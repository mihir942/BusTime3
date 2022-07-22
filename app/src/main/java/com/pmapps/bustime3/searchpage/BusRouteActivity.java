package com.pmapps.bustime3.searchpage;

import static com.pmapps.bustime3.helper.HelperMethods.*;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.appbar.MaterialToolbar;
import com.pmapps.bustime3.R;
import com.pmapps.bustime3.database.AppDatabase;
import com.pmapps.bustime3.database.LTADao;
import com.pmapps.bustime3.favpage_nearbypage.bus.BusTimingsActivity;
import com.pmapps.bustime3.favpage_nearbypage.busstop.BusStopAdapter;
import com.pmapps.bustime3.favpage_nearbypage.busstop.BusStopItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class BusRouteActivity extends AppCompatActivity {

    MaterialToolbar toolbar;
    RecyclerView recyclerView;
    List<BusStopItem> busStopItemList1;
    List<BusStopItem> busStopItemList2;
    List<BusStopItem> finalBusStopItemList;
    BusStopAdapter busStopAdapter;

    String busNumber;
    boolean hasBothDirections = true;
    int currentDirection = 1;

    private final static String TIH_URL = "https://tih-api.stb.gov.sg/transport/v1/bus_route/service/";
    private final static String ARRAY_NAME = "data";
    private final static String BUS_STOP_STRING = "busStop";
    private final static String DIRECTION_STRING = "direction";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bus_route);

        initialiseStuffs();
        fetchBusRoute();
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
        busStopItemList2 = new ArrayList<>();
        finalBusStopItemList = new ArrayList<>();

        busStopAdapter = new BusStopAdapter(this,finalBusStopItemList, this::openBusStop);
        recyclerView.setAdapter(busStopAdapter);
    }

    private void fetchBusRoute() {
        LTADao ltaDao = AppDatabase.getInstance(getApplicationContext()).ltaDao();

        String FINAL_URL = TIH_URL + busNumber + "?apikey=" + TIH_API_KEY(this);
        JsonObjectRequest request = new JsonObjectRequest(FINAL_URL, response -> {
            try {
                JSONArray jsonArray = response.getJSONArray(ARRAY_NAME);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject busStopHit = jsonArray.getJSONObject(i);
                    int direction = busStopHit.getInt(DIRECTION_STRING);

                    String busStopCode = busStopHit.getString(BUS_STOP_STRING);
                    String busStopName = ltaDao.getNameForCode(busStopCode);
                    String busStopRoad = ltaDao.getRoadForCode(busStopCode);

                    BusStopItem busStopItem = new BusStopItem(busStopName, busStopRoad, busStopCode);
                    if (direction == 1) {
                        busStopItemList1.add(busStopItem);
                    } else if (direction == 2) {
                        busStopItemList2.add(busStopItem);
                    }
                }

                if (busStopItemList2.isEmpty()) {
                    hasBothDirections = false;
                }

                showBusStops();

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, Throwable::printStackTrace);
        Volley.newRequestQueue(this).add(request);
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

    @SuppressLint("NotifyDataSetChanged")
    private void showBusStops() {
        finalBusStopItemList.clear();
        if (currentDirection == 1) {
            //show 1's bus stops
            finalBusStopItemList.addAll(busStopItemList1);

        } else if (currentDirection == 2) {
            //show 2's bus stops
            finalBusStopItemList.addAll(busStopItemList2);
        }

        busStopAdapter.notifyDataSetChanged();
    }

    private void swapDirection() {
        if (hasBothDirections) {
            if (currentDirection == 1) {
                currentDirection = 2;
                showBusStops();
            } else if (currentDirection == 2) {
                currentDirection = 1;
                showBusStops();
            }
        }
    }
}