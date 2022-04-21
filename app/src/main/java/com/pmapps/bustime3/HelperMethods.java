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
}
