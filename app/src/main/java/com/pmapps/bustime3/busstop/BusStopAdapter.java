package com.pmapps.bustime3.busstop;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pmapps.bustime3.R;

import java.util.List;

public class BusStopAdapter extends RecyclerView.Adapter<BusStopAdapter.BusStopViewHolder> {

    private LayoutInflater inflater;
    private List<BusStopItem> busStopItemList;
    private OnBusStopClickListener mListener;

    // constructor for adapter
    public BusStopAdapter(Context context, List<BusStopItem> busStopItemList, OnBusStopClickListener listener) {
        this.inflater = LayoutInflater.from(context);
        this.busStopItemList = busStopItemList;
        this.mListener = listener;
    }

    // viewholder class
    public class BusStopViewHolder extends RecyclerView.ViewHolder {

        TextView bus_stop_name_textview;
        TextView bus_stop_road_textview;
        TextView bus_stop_code_textview;

        // constructor for viewholder
        public BusStopViewHolder(@NonNull View itemView) {
            super(itemView);

            bus_stop_name_textview = itemView.findViewById(R.id.bus_stop_name_textview);
            bus_stop_road_textview = itemView.findViewById(R.id.bus_stop_road_textview);
            bus_stop_code_textview = itemView.findViewById(R.id.bus_stop_code_textview);
        }
    }

    @NonNull
    @Override
    public BusStopViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = inflater.inflate(R.layout.bus_stop_item, parent, false);
        return new BusStopViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull BusStopViewHolder holder, int position) {

        BusStopItem busStopItem = busStopItemList.get(position);

        //TODO: might need to change to holder.getAdapterPosition()
        holder.bus_stop_name_textview.setText(busStopItem.getBusStopName());
        holder.bus_stop_road_textview.setText(busStopItem.getBusStopRoad());
        holder.bus_stop_code_textview.setText(busStopItem.getBusStopCode());
        holder.itemView.setOnClickListener(view -> mListener.onBusStopClicked(busStopItem));
    }

    @Override
    public int getItemCount() {
        return busStopItemList.size();
    }
}
