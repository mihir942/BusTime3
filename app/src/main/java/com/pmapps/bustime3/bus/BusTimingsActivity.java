package com.pmapps.bustime3.bus;

import static com.pmapps.bustime3.HelperMethods.*;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.appbar.MaterialToolbar;
import com.pmapps.bustime3.R;
import com.pmapps.bustime3.busstop.BusStopItem;
import com.pmapps.bustime3.database.AppDatabase;
import com.pmapps.bustime3.database.BusStopDao;
import com.pmapps.bustime3.database.BusStopModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class BusTimingsActivity extends AppCompatActivity {

    MaterialToolbar toolbar;

    // recyclerview variables
    private final static String TIH_URL = "https://tih-api.stb.gov.sg/transport/v1/bus_arrival/bus_stop/";
    private final static String ARRAY_NAME_STRING = "data";
    private final static String SERVICE_NO_STRING = "ServiceNo";
    private final static String NEXT_BUS_1_STRING = "NextBus";
    private final static String NEXT_BUS_2_STRING = "NextBus2";
    private final static String NEXT_BUS_3_STRING = "NextBus3";
    private final static String ESTIMATED_ARR_STRING = "EstimatedArrival";
    private final static String LOAD_STRING = "Load";
    private final static String TYPE_STRING = "Type";
    RecyclerView recyclerView;
    List<BusItem> busItemList;
    BusAdapter busAdapter;
    BusStopItem busStopItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bus_timings);

        initialiseStuffs();
        fetchTimings();
    }

    private void initialiseStuffs() {
        busStopItem = getIntent().getParcelableExtra("BUS_STOP_ITEM");
        toolbar = findViewById(R.id.bus_stop_toolbar);
        toolbar.setTitle(busStopItem.getBusStopName());
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(view -> this.finish());
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

        //set favourite icon
        boolean exists = AppDatabase
                .getInstance(getApplicationContext())
                .busStopDao()
                .exists(busStopItem.getBusStopCode());
        if (exists) {
            menu.getItem(1).setIcon(ContextCompat.getDrawable(getApplicationContext(),R.drawable.ic_fav_icon_filled));
        }
        else {
            menu.getItem(1).setIcon(ContextCompat.getDrawable(getApplicationContext(),R.drawable.ic_fav_icon_1));
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.refresh_action_1) {
            fetchTimings();
            Toast.makeText(this, "Refresh", Toast.LENGTH_SHORT).show();
            return true;
        } else if (id == R.id.fav_action_2) {
            Drawable outline = ContextCompat.getDrawable(this, R.drawable.ic_fav_icon_1);
            Drawable filled = ContextCompat.getDrawable(this, R.drawable.ic_fav_icon_filled);

            BusStopDao dao = AppDatabase.getInstance(getApplicationContext()).busStopDao();
            BusStopModel model = new BusStopModel(busStopItem.getBusStopCode(), busStopItem.getBusStopName(), busStopItem.getBusStopRoad());

            boolean exists = AppDatabase
                    .getInstance(getApplicationContext())
                    .busStopDao()
                    .exists(busStopItem.getBusStopCode());

            if (exists) {
                dao.deleteBusStop(model);
                item.setIcon(outline);
            } else {
                dao.insertBusStop(model);
                item.setIcon(filled);
            }
            return true;
        }
        return false;
    }

    private void fetchTimings() {

        // clear existing data
        busItemList.clear();
        busAdapter.notifyDataSetChanged();

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        final String FINAL_URL = TIH_URL
                + busStopItem.getBusStopCode()
                + "?apikey=" + TIH_API_KEY(this);

        JsonObjectRequest request = new JsonObjectRequest(FINAL_URL, (Response.Listener<JSONObject>) response -> {
            try {
                JSONArray jsonArray = response.getJSONArray(ARRAY_NAME_STRING);
                int limit = jsonArray.length();
                for (int i = 0; i < limit; i++) {
                    JSONObject busObject = jsonArray.getJSONObject(i);
                    String busNumber = busObject.getString(SERVICE_NO_STRING);
                    BusItem busItem = new BusItem(busNumber);

                    //bus 1
                    JSONObject nextBus1Object = busObject.getJSONObject(NEXT_BUS_1_STRING);
                    String estimatedArrival = nextBus1Object.getString(ESTIMATED_ARR_STRING);
                    if (!estimatedArrival.isEmpty()) {
                        busItem.setBusTime1(timeForData(estimatedArrival));
                        String loadString = nextBus1Object.getString(LOAD_STRING);
                        busItem.setBusLoad1(loadForData(loadString));
                        String typeString = nextBus1Object.getString(TYPE_STRING);
                        busItem.setBusType1(typeForData(typeString));
                    }

                    //bus2
                    JSONObject nextBus2Object = busObject.getJSONObject(NEXT_BUS_2_STRING);
                    String estimatedArrival2 = nextBus2Object.getString(ESTIMATED_ARR_STRING);
                    if (!estimatedArrival2.isEmpty()) {
                        busItem.setBusTime2(timeForData(estimatedArrival2));
                        String loadString2 = nextBus2Object.getString(LOAD_STRING);
                        busItem.setBusLoad2(loadForData(loadString2));
                        String typeString2 = nextBus2Object.getString(TYPE_STRING);
                        busItem.setBusType2(typeForData(typeString2));
                    }

                    //bus3
                    JSONObject nextBus3Object = busObject.getJSONObject(NEXT_BUS_3_STRING);
                    String estimatedArrival3 = nextBus3Object.getString(ESTIMATED_ARR_STRING);
                    if (!estimatedArrival3.isEmpty()) {
                        busItem.setBusTime3(timeForData(estimatedArrival3));
                        String loadString3 = nextBus3Object.getString(LOAD_STRING);
                        busItem.setBusLoad3(loadForData(loadString3));
                        String typeString3 = nextBus3Object.getString(TYPE_STRING);
                        busItem.setBusType3(typeForData(typeString3));
                    }

                    busItemList.add(busItem);
                }

                busAdapter.notifyDataSetChanged();

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, Throwable::printStackTrace);

        requestQueue.add(request);
    }
}