package com.pmapps.bustime3.bus;

import static com.pmapps.bustime3.HelperMethods.colorForLoad;
import static com.pmapps.bustime3.HelperMethods.stringForType;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pmapps.bustime3.R;
import com.pmapps.bustime3.enums.*;

import java.util.List;

public class BusAdapter extends RecyclerView.Adapter<BusAdapter.BusViewHolder>{

    private LayoutInflater inflater;
    private List<BusItem> busItemList;
    private final static String DASHES = "--";
    private final static String EMPTY = "";

    // constructor for adapter
    public BusAdapter(Context context, List<BusItem> busItemList) {
        this.inflater = LayoutInflater.from(context);
        this.busItemList = busItemList;
    }

    // viewholder class
    public static class BusViewHolder extends RecyclerView.ViewHolder {

        TextView bus_number_textview;

        TextView bus_number_1_time_textview;
        View bus_number_1_load_view;
        TextView bus_number_1_type_textview;

        TextView bus_number_2_time_textview;
        View bus_number_2_load_view;
        TextView bus_number_2_type_textview;

        TextView bus_number_3_time_textview;
        View bus_number_3_load_view;
        TextView bus_number_3_type_textview;

        // constructor for viewholder
        public BusViewHolder(@NonNull View itemView) {
            super(itemView);

            bus_number_textview = itemView.findViewById(R.id.bus_number_textview);

            bus_number_1_time_textview = itemView.findViewById(R.id.bus_number_1_time_textview);
            bus_number_1_load_view = itemView.findViewById(R.id.bus_number_1_load_view);
            bus_number_1_type_textview = itemView.findViewById(R.id.bus_number_1_type_textview);

            bus_number_2_time_textview = itemView.findViewById(R.id.bus_number_2_time_textview);
            bus_number_2_load_view = itemView.findViewById(R.id.bus_number_2_load_view);
            bus_number_2_type_textview = itemView.findViewById(R.id.bus_number_2_type_textview);

            bus_number_3_time_textview = itemView.findViewById(R.id.bus_number_3_time_textview);
            bus_number_3_load_view = itemView.findViewById(R.id.bus_number_3_load_view);
            bus_number_3_type_textview = itemView.findViewById(R.id.bus_number_3_type_textview);
        }
    }

    @NonNull
    @Override
    public BusViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = inflater.inflate(R.layout.bus_item, parent, false);
        return new BusViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull BusViewHolder holder, int position) {
        BusItem busItem = busItemList.get(position);
        holder.bus_number_textview.setText(busItem.getBusNumber());

        //Full logic. IF BUS TIME EXISTS.

        String time1, time2, time3;
        Load load1, load2, load3;
        int color1, color2, color3;
        Type type1, type2, type3;
        String type1_str, type2_str, type3_str;

        if (busItem.getBusTime1() != null) {
            time1 = busItem.getBusTime1();
            load1 = busItem.getBusLoad1();
            color1 = colorForLoad(load1);
            type1 = busItem.getBusType1();
            type1_str = stringForType(type1);

            if (busItem.getBusTime2() != null) {
                time2 = busItem.getBusTime2();
                load2 = busItem.getBusLoad2();
                color2 = colorForLoad(load2);
                type2 = busItem.getBusType2();
                type2_str = stringForType(type2);

                if (busItem.getBusTime3() != null) {
                    time3 = busItem.getBusTime3();
                    load3 = busItem.getBusLoad3();
                    color3 = colorForLoad(load3);
                    type3 = busItem.getBusType3();
                    type3_str = stringForType(type3);

                } else {
                    // 3 is null
                    time3 = DASHES;
                    color3 = Color.TRANSPARENT;
                    type3_str = EMPTY;
                }
            } else {
                // 2 and 3 are null
                time2 = time3 = DASHES;
                color2 = color3 = Color.TRANSPARENT;
                type2_str = type3_str = EMPTY;
            }
        } else {
            // 1, 2 and 3 are null
            time1 = time2 = time3 = DASHES;
            color1 = color2 = color3 = Color.TRANSPARENT;
            type1_str = type2_str = type3_str = EMPTY;
        }

        // at the end, set all views
        holder.bus_number_1_time_textview.setText(time1);
        holder.bus_number_2_time_textview.setText(time2);
        holder.bus_number_3_time_textview.setText(time3);

        holder.bus_number_1_load_view.setBackgroundColor(color1);
        holder.bus_number_2_load_view.setBackgroundColor(color2);
        holder.bus_number_3_load_view.setBackgroundColor(color3);

        holder.bus_number_1_type_textview.setText(type1_str);
        holder.bus_number_2_type_textview.setText(type2_str);
        holder.bus_number_3_type_textview.setText(type3_str);
    }

    @Override
    public int getItemCount() {
        return busItemList.size();
    }
}
