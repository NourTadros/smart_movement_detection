package com.example.accelerometer;

import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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
    public float[] gravity=new float[3];

    public float[] linear_acceleration = new float[3];



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

        final float alpha = (float) 0.5; //alpha = t / (t + dT)
        //τ = 1/(2 * π * fc)
        //dT=0.2s according to SENSOR_DELAY_NORMAL
        //0.5

        gravity[0] = alpha * gravity[0] + (1 - alpha) * event.values[0];
        gravity[1] = alpha * gravity[1] + (1 - alpha) * event.values[1];
        gravity[2] = alpha * gravity[2] + (1 - alpha) * event.values[2];

        linear_acceleration[0] = event.values[0] - gravity[0];
        linear_acceleration[1] = event.values[1] - gravity[1];
        linear_acceleration[2] = event.values[2] - gravity[2];

        Long tsLong = System.currentTimeMillis()/1000;
        String ts = tsLong.toString();

        xText.setText( String.valueOf(linear_acceleration[0]));
        yText.setText(String.valueOf( linear_acceleration[1]));
        zText.setText(String.valueOf( linear_acceleration[2]));
        timestamp.setText(String.valueOf(ts));

        Log.i("zText", String.valueOf(linear_acceleration[2]));

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


