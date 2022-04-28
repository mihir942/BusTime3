package com.pmapps.bustime3;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Location;

import androidx.core.content.ContextCompat;

import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.pmapps.bustime3.enums.*;


import java.time.Duration;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class HelperMethods {
    // Helper method #1
    public static LatLng locToLatLng(Location location) {
        return new LatLng(location.getLatitude(), location.getLongitude());
    }

    // Helper method #2
    public static String TIH_API_KEY(Context context) {
        return context.getResources().getString(R.string.TIH_API_KEY);
    }

    // Helper method #3
    public static BitmapDescriptor bitmapDescriptorFromVector(Context context, int vectorResId) {
        Drawable vectorDrawable = ContextCompat.getDrawable(context,vectorResId);

        int width, height;
        if (vectorDrawable != null) {
            width = vectorDrawable.getIntrinsicWidth();
            height = vectorDrawable.getIntrinsicHeight();
            vectorDrawable.setBounds(0,0,width,height);
            Bitmap bitmap = Bitmap.createBitmap(width,height, Bitmap.Config.ARGB_8888);
            vectorDrawable.draw(new Canvas(bitmap));
            return BitmapDescriptorFactory.fromBitmap(bitmap);
        }
        return null;
    }

    // Helper method #4
    public static int colorForLoad(Context context, Load load) {
        if (load == Load.LIGHT) {
            return ContextCompat.getColor(context, R.color.light_load);
        } else if (load == Load.MEDIUM) {
            return ContextCompat.getColor(context, R.color.medium_load);
        } else if (load == Load.HEAVY) {
            return ContextCompat.getColor(context, R.color.heavy_load);
        }
        return 0;
    }

    // Helper method #5
    public static String stringForType(Type type) {
        if (type == Type.SINGLE) return "single";
        else if (type == Type.DOUBLE) return "double";
        else if (type == Type.BENDY) return "bendy";
        else return "";
    }

    // Helper method #6
    public static String timeForData(String data) {

        String time_only = data.split("T")[1].split("[+]")[0];

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss");
        LocalTime time1 = LocalTime.parse(time_only, dtf);
        LocalTime time2 = LocalTime.now();

        Duration duration = Duration.between(time2, time1);
        long minutes = duration.toMinutes();
        String minutes_str = String.valueOf(minutes);
        int minutes_int = Integer.parseInt(minutes_str);

        if (minutes_int <= 0) {
            return "Arr";
        } else {
            return minutes_str;
        }
    }

    // Helper method #7
    public static Load loadForData(String data) {
        switch (data) {
            case "SEA":
                return Load.LIGHT;
            case "SDA":
                return Load.MEDIUM;
            case "LSD":
                return Load.HEAVY;
            default:
                return Load.LIGHT;
        }
    }

    // Helper method #8
    public static Type typeForData(String data) {
        switch (data) {
            case "SD":
                return Type.SINGLE;
            case "DD":
                return Type.DOUBLE;
            case "BD":
                return Type.BENDY;
            default:
                return Type.SINGLE;
        }
    }

}
