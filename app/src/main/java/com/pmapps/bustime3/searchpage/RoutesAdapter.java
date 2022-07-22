package com.pmapps.bustime3.searchpage;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.pmapps.bustime3.R;
import com.pmapps.bustime3.database.RouteModel;

import java.util.ArrayList;

public class RoutesAdapter extends ArrayAdapter<RouteModel> {

    private OnBusRouteClickListener mListener;

    public RoutesAdapter(@NonNull Context context, ArrayList<RouteModel> routeModelArrayList, OnBusRouteClickListener listener) {
        super(context, 0, routeModelArrayList);
        this.mListener = listener;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        RouteModel routeModel = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.bus_route_item, parent, false);
        }

        TextView bus_route_textview = (TextView) convertView.findViewById(R.id.bus_route_text_view_cardview);
        bus_route_textview.setText(routeModel.getBusNumber());

        convertView.setOnClickListener(view -> mListener.onBusRouteClicked(routeModel));

        return convertView;
    }
}
