package com.example.accelerometer;

import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Timer;


public class Readings extends AppCompatActivity implements SensorEventListener{
    private TextView xText, yText, zText,timestamp;
    private Button stopBtn;
    private Sensor mySensor;
    private SensorManager SM;
    public static int stopAcc =0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_readings);
        // Create our Sensor Manager
        SM = (SensorManager)getSystemService(SENSOR_SERVICE);




        // Accelerometer Sensor
        mySensor = SM.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        // Register sensor Listener
        SM.registerListener( this, mySensor, SensorManager.SENSOR_DELAY_NORMAL);



        // Assign TextView
        xText = findViewById(R.id.xText);
        yText = findViewById(R.id.yText);
        zText = findViewById(R.id.zText);
        timestamp= findViewById(R.id.timestamp);

        stopBtn= findViewById(R.id.stpbtn);




        FirebaseApp.initializeApp(this);


    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Not in use
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        Long tsLong = System.currentTimeMillis()/1000;
        String ts = tsLong.toString();

        xText.setText( String.valueOf(event.values[0]));
        yText.setText(String.valueOf( event.values[1]));
        zText.setText(String.valueOf( event.values[2]));
        timestamp.setText(String.valueOf(ts));

        addDataTodb();
        if (stopAcc !=0)
        {
            SM.unregisterListener(this);

        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        SM.unregisterListener(this);
    }
    @Override
    protected void onResume() {
        super.onResume();


    }


    public void addDataTodb() {
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference(UtilitiesHelper.ACCELEROMETER_TABLE);

        String AccID = mDatabase.push().getKey();


        AccelerometerModel accelerometerModel = new AccelerometerModel(
                timestamp.getText().toString(),
                xText.getText().toString(),
                yText.getText().toString(),
                zText.getText().toString(),
                AccID
        );

        if (AccID != null) {
            mDatabase.child(AccID).setValue(accelerometerModel).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        UtilitiesHelper.showToast(getBaseContext(), "Successfully Added to the Firebase" );

                    } else {
                        UtilitiesHelper.showToast(getApplicationContext(), "Error occured, " + task.getException());
                    }
                }
            });
        }


    }

    public void stop(View v)
    {
        stopAcc =1;
        Intent i = new Intent(Readings.this,MainActivity.class);
        startActivity(i);
        finish();



    }


}


