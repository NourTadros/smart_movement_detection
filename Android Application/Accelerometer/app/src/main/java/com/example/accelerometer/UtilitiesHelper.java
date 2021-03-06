package com.example.accelerometer;


import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.text.format.DateFormat;
import android.util.Log;
import android.widget.Toast;




import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import static android.content.Context.MODE_PRIVATE;


public class UtilitiesHelper {
//3ashan y2alel repetition fl code, asami l tables bs
    public static String ACCELEROMETER_TABLE = "accelerometerReadings";
    public static String PREDICTIONS_TABLE="predictions";




//function l toast 3ashan anadeeha 3alatool
    public static void showToast(Context c, String message){
        Toast.makeText(c, message, Toast.LENGTH_SHORT).show();
    }

    public static String getStatus(Context context) {
        SharedPreferences prefs = context.getSharedPreferences("status", MODE_PRIVATE);
        String restoredText = prefs.getString("status", null);

        if (restoredText != null) {
            String rider = prefs.getString("Rider", "No type defined");
            String driver = prefs.getString("Driver", "No type defined");
        }
        return restoredText;
    }


    public static void setStatus(Context context, String status){
        SharedPreferences.Editor editor = context.getSharedPreferences("status", MODE_PRIVATE).edit();
        editor.putString("status", status);
        editor.apply();
    }








}
