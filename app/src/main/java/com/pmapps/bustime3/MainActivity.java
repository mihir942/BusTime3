package com.pmapps.bustime3;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.pmapps.bustime3.fragments.FavFragment;
import com.pmapps.bustime3.fragments.NearbyFragment;
import com.pmapps.bustime3.fragments.SearchFragment;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;

    final String[] ALL_PERMISSIONS = {
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.INTERNET };
    private ActivityResultContracts.RequestMultiplePermissions contract;
    private ActivityResultLauncher<String[]> launcher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnItemSelectedListener(listener);

        // at the start, goes to the Fav Page automatically
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, new FavFragment()).commit();

        // permissions related stuff
        contract = new ActivityResultContracts.RequestMultiplePermissions();
        launcher = registerForActivityResult(contract, result -> {
            if (result.containsValue(false)) {
                Log.d("PERMISSIONS", "At least one of the permissions was not granted, launching again...");
                launcher.launch(ALL_PERMISSIONS);
            }
        });
        askPermissions(launcher);
    }

    private void askPermissions(ActivityResultLauncher<String[]> mLauncher) {
        if (!permissionsGranted(this)) {
            Log.d("PERMISSIONS", "Launching multiple contract permission launcher for ALL required permissions");
            mLauncher.launch(ALL_PERMISSIONS);
        } else {
            Log.d("PERMISSIONS", "All permissions are already granted");
        }
    }

    private final NavigationBarView.OnItemSelectedListener listener = item -> {

        Fragment selectedFragment = null;

        int id = item.getItemId();

        if (id == R.id.fav_page_1) selectedFragment = new FavFragment();
        else if (id == R.id.search_page_2) selectedFragment = new SearchFragment();
        else if (id == R.id.nearby_page_3) selectedFragment = new NearbyFragment();

        if (selectedFragment != null) getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, selectedFragment).commit();
        return true;
    };

    private boolean permissionsGranted(Context context) {
        for (String perm: ALL_PERMISSIONS) {
            if (ContextCompat.checkSelfPermission(context, perm) != PackageManager.PERMISSION_GRANTED) {
                Log.d("PERMISSIONS", "Permission is not granted: " + perm);
                return false;
            }
            Log.d("PERMISSIONS", "Permission already granted: " + perm);
        }
        return true;
    }

}