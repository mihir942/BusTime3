package com.pmapps.bustimesg.searchpage;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pmapps.bustimesg.R;
import com.pmapps.bustimesg.database.RouteModel;

import java.util.ArrayList;

public class RoutesAdapter extends RecyclerView.Adapter<RoutesAdapter.RoutesViewHolder> {

    private LayoutInflater inflater;
    private ArrayList<RouteModel> routeModelList;
    private OnBusRouteClickListener mListener;

    // constructor for adapter
    public RoutesAdapter(Context context, ArrayList<RouteModel> routeModelList, OnBusRouteClickListener listener) {
        this.inflater = LayoutInflater.from(context);
        this.routeModelList = routeModelList;
        this.mListener = listener;
    }

    // viewholder class
    public static class RoutesViewHolder extends RecyclerView.ViewHolder {
        TextView bus_route_textview;

        // constructor for viewholder

        public RoutesViewHolder(@NonNull View itemView) {
            super(itemView);

            bus_route_textview = itemView.findViewById(R.id.bus_route_text_view_cardview);
        }
    }

    @NonNull
    @Override
    public RoutesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = inflater.inflate(R.layout.bus_route_item, parent, false);
        return new RoutesViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RoutesViewHolder holder, int position) {
        RouteModel routeModel = routeModelList.get(position);

        holder.bus_route_textview.setText(routeModel.getBusNumber());
        holder.itemView.setOnClickListener(view -> mListener.onBusRouteClicked(routeModel));
    }

    @Override
    public int getItemCount() {
        return routeModelList.size();
    }
}

//    private OnBusRouteClickListener mListener;
//
//    public RoutesAdapter(@NonNull Context context, ArrayList<RouteModel> routeModelArrayList, OnBusRouteClickListener listener) {
//        super(context, 0, routeModelArrayList);
//        this.mListener = listener;
//    }
//
//    @NonNull
//    @Override
//    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
//        RouteModel routeModel = getItem(position);
//
//        if (convertView == null) {
//            convertView = LayoutInflater.from(getContext()).inflate(R.layout.bus_route_item, parent, false);
//        }
//
//        TextView bus_route_textview = convertView.findViewById(R.id.bus_route_text_view_cardview);
//        bus_route_textview.setText(routeModel.getBusNumber());
//
//        convertView.setOnClickListener(view -> mListener.onBusRouteClicked(routeModel));
//
//        return convertView;
//    }