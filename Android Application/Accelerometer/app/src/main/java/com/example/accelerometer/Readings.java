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
    private Sensor magnetometer;
    private SensorManager SM;
    public float[] mGravity;
    public float[] mGeomagnetic;
    public float orientation[] = new float[3];
    public float pitch;
    public float roll;
    public static int stopAcc =0;
    public float[] gravity=new float[3];
    public DatabaseReference mDatabase;
    float force;

    public float[] linear_acceleration = new float[3];



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_readings);



        // Create our Sensor Manager
        SM = (SensorManager)getSystemService(SENSOR_SERVICE);




        // Accelerometer Sensor
        mySensor = SM.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
        magnetometer = SM.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);




        // Register sensor Listener
        SM.registerListener( this, mySensor, SensorManager.SENSOR_DELAY_UI);

        SM.registerListener(this, magnetometer, SensorManager.SENSOR_DELAY_NORMAL);


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

        final float alpha =  0.8f; //alpha = t / (t + dT)
        //τ = 1/(2 * π * fc)
        //dT=0.2s according to SENSOR_DELAY_NORMAL
        //0.5
//Low and High pass filter
        gravity[0] = alpha * gravity[0] + (1 - alpha) * event.values[0];
        gravity[1] = alpha * gravity[1] + (1 - alpha) * event.values[1];
        gravity[2] = alpha * gravity[2] + (1 - alpha) * event.values[2];

//        Log.i("gravity[0]",String.valueOf(gravity[0]));
//        Log.i("gravity[1]",String.valueOf(gravity[1]));
//
//        Log.i("gravity[2]",String.valueOf(gravity[2]));

        if(gravity[2]>9.81){
            Log.i("Keberna ya gravity", String.valueOf(gravity[2]));
        }

//high pass filter
        linear_acceleration[0] = event.values[0] - gravity[0];
        linear_acceleration[1] = event.values[1] - gravity[1];
        linear_acceleration[2] = event.values[2] - gravity[2];


        Long tsLong = System.currentTimeMillis()/1000;
        String ts = tsLong.toString();

        xText.setText( String.valueOf(linear_acceleration[0]));
        yText.setText(String.valueOf( linear_acceleration[1]));
        zText.setText(String.valueOf( linear_acceleration[2]));
        timestamp.setText(String.valueOf(ts));


//        Log.i("zText", String.valueOf(linear_acceleration[2]));
        if(linear_acceleration[2]>9.81){
            Log.i("Keberna ya teta", String.valueOf(linear_acceleration[2]));
        }


        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER)
        {
            mGravity = event.values;

        }
        else if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD)
        {
            mGeomagnetic = event.values;


            if (isTiltDownward())
            {
                Log.d("test", "downwards");
            }
            else if (isTiltUpward())
            {
                Log.d("test", "upwards");
            }
        }

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

    public boolean isTiltUpward()
    {

        if (mGravity != null && mGeomagnetic != null)
        {
            float R[] = new float[9];
            float I[] = new float[9];

            boolean success = SensorManager.getRotationMatrix(R, I, mGravity, mGeomagnetic);

            if (success)
            {
                float orientation[] = new float[3];
                SensorManager.getOrientation(R, orientation);


                pitch = orientation[1];
                roll = orientation[2];

                gravity = mGravity.clone();

                double norm_Of_g = Math.sqrt(gravity[0] * gravity[0] + gravity[1] * gravity[1] + gravity[2] * gravity[2]);

                // Normalize the accelerometer vector
                gravity[0] = (float) (gravity[0] / norm_Of_g);
                gravity[1] = (float) (gravity[1] / norm_Of_g);
                gravity[2] = (float) (gravity[2] / norm_Of_g);

                //Checks if device is flat on ground or not
                int inclination = (int) Math.round(Math.toDegrees(Math.acos(gravity[2])));


                Float objPitch = new Float(pitch);
                Float objZero = new Float(0.0);
                Float objZeroPointTwo = new Float(0.2);
                Float objZeroPointTwoNegative = new Float(-0.2);

                int objPitchZeroResult = objPitch.compareTo(objZero);
                int objPitchZeroPointTwoResult = objZeroPointTwo.compareTo(objPitch);
                int objPitchZeroPointTwoNegativeResult = objPitch.compareTo(objZeroPointTwoNegative);

                if (roll < 0 && ((objPitchZeroResult > 0 && objPitchZeroPointTwoResult > 0) || (objPitchZeroResult < 0 && objPitchZeroPointTwoNegativeResult > 0)) && (inclination > 30 && inclination < 40))
                {
                    Log.d("test","UPWARDS YA WALAAAA");
                    return true;
                }
                else
                {
                    return false;
                }
            }
        }

        return false;
    }

    public boolean isTiltDownward()
    {

        if (mGravity != null && mGeomagnetic != null)
        {
            float R[] = new float[9];
            float I[] = new float[9];

            boolean success = SensorManager.getRotationMatrix(R, I, mGravity, mGeomagnetic);

            if (success)
            {
                float orientation[] = new float[3];
                SensorManager.getOrientation(R, orientation);

                pitch = orientation[1];
                roll = orientation[2];

                gravity = mGravity.clone();

                double norm_Of_g = Math.sqrt(gravity[0] * gravity[0] + gravity[1] * gravity[1] + gravity[2] * gravity[2]);

                // Normalize the accelerometer vector
                gravity[0] = (float) (gravity[0] / norm_Of_g);
                gravity[1] = (float) (gravity[1] / norm_Of_g);
                gravity[2] = (float) (gravity[2] / norm_Of_g);

                //Checks if device is flat on groud or not
                int inclination = (int) Math.round(Math.toDegrees(Math.acos(gravity[2])));

                Float objPitch = new Float(pitch);
                Float objZero = new Float(0.0);
                Float objZeroPointTwo = new Float(0.2);
                Float objZeroPointTwoNegative = new Float(-0.2);

                int objPitchZeroResult = objPitch.compareTo(objZero);
                int objPitchZeroPointTwoResult = objZeroPointTwo.compareTo(objPitch);
                int objPitchZeroPointTwoNegativeResult = objPitch.compareTo(objZeroPointTwoNegative);
//                if (inclination < 25 || inclination > 155)
//                {
////                    Log.d("test","flaaaaaaaaat");
//                }
//                else
//                {
                    if (roll < 0 && ((objPitchZeroResult > 0 && objPitchZeroPointTwoResult > 0) || (objPitchZeroResult < 0 && objPitchZeroPointTwoNegativeResult > 0)) && (inclination > 140 && inclination < 170))
                    {
                        Log.d("test","DOWNWARDS YA WALAAAA");
                        return true;
                    }
                    else
                    {
                        return false;
                    }
                }



            }
       // }

        return false;
    }

    public void addDataTodb() {
        FirebaseApp.initializeApp(getApplicationContext());


         mDatabase = FirebaseDatabase.getInstance().getReference(UtilitiesHelper.ACCELEROMETER_TABLE);

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


