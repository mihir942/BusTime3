package com.pmapps.bustimesg.favpage_nearbypage.fragments;

import static com.google.android.gms.location.LocationRequest.PRIORITY_HIGH_ACCURACY;
import static com.pmapps.bustimesg.helper.HelperMethods.*;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.pmapps.bustimesg.favpage_nearbypage.bus.BusTimingsActivity;
import com.pmapps.bustimesg.favpage_nearbypage.busstop.BusStopAdapter;
import com.pmapps.bustimesg.favpage_nearbypage.busstop.BusStopItem;
import com.pmapps.bustimesg.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class NearbyFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnInfoWindowClickListener {

    // map variables
    private final static float DEFAULT_ZOOM_LEVEL = 16.0F;
    private Context mContext;
    private GoogleMap mMap;
    private FusedLocationProviderClient client;

    // recyclerview variables
    private final static String TIH_URL = "https://tih-api.stb.gov.sg/transport/v1/bus_stop";
    private final static short RADIUS = 200;
    private final static byte NUM_OF_BUS_STOPS = 10;
    private final static String ARRAY_NAME = "data";
    private final static String CODE_STRING = "code";
    private final static String ROAD_STRING = "roadName";
    private final static String DESC_STRING = "description";
    private final static String LOC_STRING = "location";
    private final static String LAT_STRING = "latitude";
    private final static String LON_STRING = "longitude";
    RecyclerView recyclerView;
    List<BusStopItem> busStopItemList;
    BusStopAdapter busStopAdapter;

    // initialising the context of fragment, for ease of use. no need to call requireContext() all the time.
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext = context;
    }

    // only when the map is ready (on its own thread), then fetch data.
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnInfoWindowClickListener(this);
        getLocationAndFetchNearby();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_nearby, container, false);
        initialiseStuffs(v);
        return v;
    }

    private void initialiseStuffs(View v) {
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map_fragment);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
        client = LocationServices.getFusedLocationProviderClient(mContext);
        recyclerView = v.findViewById(R.id.nearby_stops_recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        busStopItemList = new ArrayList<>();
        busStopAdapter = new BusStopAdapter(mContext, busStopItemList, this::openBusStop);
        recyclerView.setAdapter(busStopAdapter);
    }

    // figure out current location of the user. based on this, fetch the surrounding bus stops.
    @SuppressLint("MissingPermission")
    private void getLocationAndFetchNearby() {
        client.getCurrentLocation(PRIORITY_HIGH_ACCURACY, null).addOnSuccessListener(location -> {

            LatLng currentLocation = locToLatLng(location);
            mMap.addMarker(new MarkerOptions().position(currentLocation).title("You"));
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLocation,DEFAULT_ZOOM_LEVEL));
            fetchBusStops(currentLocation);
        });
    }

    // fetch data from TIH by making a GET request
    @SuppressLint("NotifyDataSetChanged")
    private void fetchBusStops(LatLng currentLocation) {
        //TODO: Change variable later
        //LatLng loc = new LatLng(1.3270524352831348, 103.90292634985121);
        RequestQueue requestQueue = Volley.newRequestQueue(mContext);
        final String FINAL_URL = TIH_URL
                + "?location=" + currentLocation.latitude + "%2C" + currentLocation.longitude
                + "&radius=" + RADIUS
                + "&apikey=" + TIH_API_KEY(mContext);
        JsonObjectRequest request = new JsonObjectRequest(FINAL_URL, response -> {
            try {
                JSONArray jsonArray = response.getJSONArray(ARRAY_NAME);
                int limit = Math.min(jsonArray.length(), NUM_OF_BUS_STOPS);
                for (int i = 0; i < limit; i++) {

                    //retrieve all the data from a single bus stop
                    JSONObject busStopObject = jsonArray.getJSONObject(i);
                    String bus_stop_name = busStopObject.getString(DESC_STRING);
                    String bus_stop_road = busStopObject.getString(ROAD_STRING);
                    String bus_stop_code = busStopObject.getString(CODE_STRING);
                    JSONObject latLonObject = busStopObject.getJSONObject(LOC_STRING);
                    String bus_stop_lat = latLonObject.getString(LAT_STRING);
                    String bus_stop_lon = latLonObject.getString(LON_STRING);
                    LatLng bus_stop_coord = new LatLng(Double.parseDouble(bus_stop_lat),Double.parseDouble(bus_stop_lon));

                    //create an instance of BusStopItem, and add it to the list
                    BusStopItem busStopItem = new BusStopItem(bus_stop_name,bus_stop_road,bus_stop_code,bus_stop_coord);
                    busStopItemList.add(busStopItem);
                    MarkerOptions markerOptions = new MarkerOptions().title(bus_stop_name).icon(bitmapDescriptorFromVector(mContext,R.drawable.ic_bus_icon)).position(bus_stop_coord);
                    mMap.addMarker(markerOptions).setTag(busStopItem);
                }
                // all bus stop items have been added at this point
                // now pass this big chunk of data to the adapter, let it do its job.
                // but we can't exactly initialise adapter here; it's already initialised.
                // so we notify it that the data set has changed
                busStopAdapter.notifyDataSetChanged();


            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, Throwable::printStackTrace);
        requestQueue.add(request);
    }

    @Override
    public void onInfoWindowClick(@NonNull Marker marker) {
        BusStopItem busStopItem = (BusStopItem) marker.getTag();
        openBusStop(busStopItem);
    }

    private void openBusStop(BusStopItem busStopItem) {
        Intent intent = new Intent(mContext, BusTimingsActivity.class);
        intent.putExtra("BUS_STOP_ITEM",busStopItem);
        startActivity(intent);
    }
}