package com.example.elementaryapp2.services;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.ImageView;

import com.example.elementaryapp2.R;

public class Services {

    public static String ipAddress = "http://192.168.113.125:5000/";
    public static String userEmail = "";

    public static void createIpKey(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        if (sharedPreferences.contains("key")) {
            ipAddress = sharedPreferences.getString("key", "");
        } else {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("key", ipAddress);
            editor.apply();
        }
    }

    public static void updateIpKey(Context context, String key) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("key", key);
        editor.apply();
        ipAddress = key;
    }

    //back button functionality
    public static void onPressBack (Activity activity) {
        ImageView btn = activity.findViewById(R.id.back_btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.finish();
            }
        });
    }

}
