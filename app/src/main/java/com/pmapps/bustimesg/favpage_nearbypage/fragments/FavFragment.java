package com.pmapps.bustimesg.favpage_nearbypage.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pmapps.bustimesg.R;
import com.pmapps.bustimesg.favpage_nearbypage.bus.BusTimingsActivity;
import com.pmapps.bustimesg.favpage_nearbypage.busstop.BusStopAdapter;
import com.pmapps.bustimesg.favpage_nearbypage.busstop.BusStopItem;
import com.pmapps.bustimesg.database.BusStopDao;
import com.pmapps.bustimesg.database.AppDatabase;
import com.pmapps.bustimesg.database.BusStopModel;

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
        busStopAdapter = new BusStopAdapter(mContext, busStopItemList, this::openBusStop);
        recyclerView.setAdapter(busStopAdapter);
        ItemTouchHelper helper = new ItemTouchHelper(itemTouchHelperCallback);
        helper.attachToRecyclerView(recyclerView);
    }

    private void openBusStop(BusStopItem busStopItem) {
        Intent intent = new Intent(mContext, BusTimingsActivity.class);
        intent.putExtra("BUS_STOP_ITEM",busStopItem);
        startActivity(intent);
    }

    @SuppressLint("NotifyDataSetChanged")
    private void fetchBusStops() {
        busStopItemList.clear();
        BusStopDao dao = AppDatabase.getInstance(mContext.getApplicationContext()).busStopDao();

        for (BusStopModel model: dao.getAllData()) {
            BusStopItem item = new BusStopItem(model.getBusStopName(),model.getBusStopRoad(), model.getBusStopCode());
            busStopItemList.add(item);
        }

        //after fetching
        busStopAdapter.notifyDataSetChanged();
    }

    ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT) {

        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

            BusStopDao dao = AppDatabase.getInstance(mContext.getApplicationContext()).busStopDao();
            dao.deleteBusStopWithCode(busStopItemList.get(viewHolder.getAdapterPosition()).getBusStopCode());
            busStopAdapter.notifyItemRemoved(viewHolder.getAdapterPosition());
            fetchBusStops();
        }

        @Override
        public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);

            final Drawable icon = ContextCompat.getDrawable(mContext.getApplicationContext(),R.drawable.ic_delete_icon);
            final ColorDrawable background = new ColorDrawable(Color.RED);

            View itemView = viewHolder.itemView;
            int backgroundCornerOffset = 24;

            int iconMargin = (itemView.getHeight() - icon.getIntrinsicHeight()) / 2;
            int iconTop = itemView.getTop() + (itemView.getHeight() - icon.getIntrinsicHeight()) / 2;
            int iconBottom = iconTop + icon.getIntrinsicHeight();

            if (dX < 0) {
                int iconLeft = itemView.getRight() - iconMargin - icon.getIntrinsicWidth();
                int iconRight = itemView.getRight() - iconMargin;
                icon.setBounds(iconLeft, iconTop, iconRight, iconBottom);

                background.setBounds(itemView.getRight() + ((int) dX) - backgroundCornerOffset,
                        itemView.getTop(),
                        itemView.getRight(),
                        itemView.getBottom());
            } else {
                background.setBounds(0, 0, 0, 0);
            }
            background.draw(c);
            icon.draw(c);
        }
    };
}