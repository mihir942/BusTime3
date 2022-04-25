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
    public static int colorForLoad(Load load) {
        if (load == Load.LIGHT) {
            return R.color.light_load;
        } else if (load == Load.MEDIUM) {
            return R.color.medium_load;
        } else if (load == Load.HEAVY) {
            return R.color.heavy_load;
        }
        return 0;
    }

    // Helper method #5
    public static String stringForType(Type type) {
        if (type == Type.SINGLE) return "single";
        else if (type == Type.DOUBLE) return "double";
        else return "";
    }
}
