package com.pmapps.bustime3.fragments;

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

import com.pmapps.bustime3.R;
import com.pmapps.bustime3.bus.BusTimingsActivity;
import com.pmapps.bustime3.busstop.BusStopAdapter;
import com.pmapps.bustime3.busstop.BusStopItem;
import com.pmapps.bustime3.database.BusStopDao;
import com.pmapps.bustime3.database.BusStopDatabase;
import com.pmapps.bustime3.database.BusStopModel;

import java.util.ArrayList;
import java.util.List;

public class FavFragment extends Fragment {

    private Context mContext;

    //recyclerview variables
    RecyclerView recyclerView;
    List<BusStopItem> busStopItemList;
    BusStopAdapter busStopAdapter;

    // initialising the context of fragment, for ease of use. no need to call requireContext() all the time.
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_fav, container, false);
        initialiseStuffs(v);
        fetchBusStops();
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        fetchBusStops();
    }

    private void initialiseStuffs(View v) {
        recyclerView = v.findViewById(R.id.fav_stops_recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        busStopItemList = new ArrayList<>();
        busStopAdapter = new BusStopAdapter(mContext, busStopItemList, busStopItem -> {
            openBusStop(busStopItem);
        });
        recyclerView.setAdapter(busStopAdapter);
    }

    private void openBusStop(BusStopItem busStopItem) {
        Intent intent = new Intent(mContext, BusTimingsActivity.class);
        intent.putExtra("BUS_STOP_ITEM",busStopItem);
        startActivity(intent);
    }

    private void fetchBusStops() {
        busStopItemList.clear();
        BusStopDao dao = BusStopDatabase.getInstance(mContext.getApplicationContext()).busStopDao();

        for (BusStopModel model: dao.getAllData()) {
            BusStopItem item = new BusStopItem(model.getBusStopName(), model.getBusStopRoad(), model.getBusStopCode());
            busStopItemList.add(item);
        }

        //after fetching
        busStopAdapter.notifyDataSetChanged();
    }
}