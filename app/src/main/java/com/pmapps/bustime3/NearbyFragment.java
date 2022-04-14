package com.pmapps.bustime3;

import static com.google.android.gms.location.LocationRequest.PRIORITY_HIGH_ACCURACY;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class NearbyFragment extends Fragment implements OnMapReadyCallback {

    private Context mContext;
    private GoogleMap mMap;
    private FusedLocationProviderClient client;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        getLocation();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_nearby, container, false);

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map_fragment);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
        client = LocationServices.getFusedLocationProviderClient(mContext);
        return v;
    }


    @SuppressLint("MissingPermission")
    private void getLocation() {
        client.getCurrentLocation(PRIORITY_HIGH_ACCURACY, null).addOnSuccessListener(location -> {

            double latitude = location.getLatitude();
            double longitude = location.getLongitude();
            Log.d("LOCATION","LAT: " + latitude);
            Log.d("LOCATION","LON: " + longitude);

            LatLng home = new LatLng(latitude,longitude);
            mMap.addMarker(new MarkerOptions().position(home).title("Home"));
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(home,17.0F));
        });
    }
}