package com.example.accelerometer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.support.annotation.NonNull;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MySensorListener implements SensorEventListener{
    String comma = new String(",");
    private PrintWriter mCurrentFile;

    public MySensorListener(int count){
        //Creating a file to print the data into

        String nameStr = new String("/sdcard/data" + count + ".csv");
        File outputFile = new File(nameStr);
        mCurrentFile = null;
        try {
            mCurrentFile = new PrintWriter(new FileOutputStream(outputFile));
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // TODO Auto-generated method stub

    }


    @Override
    public void onSensorChanged(SensorEvent event) {

        StringBuffer buff = new StringBuffer();
        buff.append(String.valueOf(event.timestamp));
        buff.append(comma);
        buff.append(String.valueOf(event.values[0]));
        buff.append(comma);
        buff.append(String.valueOf(event.values[1]));
        buff.append(comma);
        buff.append(String.valueOf(event.values[2]));

        if(mCurrentFile !=null){
            mCurrentFile.println(buff.toString());
            mCurrentFile.close();
        }

    }



    }
