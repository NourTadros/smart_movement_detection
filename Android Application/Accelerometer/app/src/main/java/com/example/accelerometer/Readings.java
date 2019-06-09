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

import java.text.SimpleDateFormat;
import java.util.Date;
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
//        mySensor = SM.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
//        magnetometer = SM.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);




        // Register sensor Listener
//        SM.registerListener( this, mySensor, SensorManager.SENSOR_DELAY_NORMAL);
//
//        SM.registerListener(this, magnetometer, SensorManager.SENSOR_DELAY_NORMAL);


        // Assign TextView
        xText = findViewById(R.id.xText);
        yText = findViewById(R.id.yText);
        zText = findViewById(R.id.zText);
        timestamp= findViewById(R.id.timestamp);

        stopBtn= findViewById(R.id.stpbtn);



        //Initializing Firebase
        FirebaseApp.initializeApp(this);


    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Not in use
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        final float alpha =  0.5f; //alpha = t / (t + dT)
        //τ = 1/(2 * π * fc)
        //dT=0.2s according to SENSOR_DELAY_NORMAL
        //0.5
        float gravityAcc=9.81f; //normal acceleration of gravity due to free fall
//3ashan n7seb el acceleration, el force bt3 gravity lazm yb2a eliminated
        //Low Pass Filter
        gravityAcc=alpha*gravityAcc+(1-alpha)*event.values[2]; //bt pass low frequencies w bt2alel el amplitude b frequencies a3la men l threshold
        //High Pass Filter
        double z=event.values[2]-gravityAcc; // bnpass high frequencies 3shan n reduce l amplitud b frequencies a2al men el threshold
        //bnsheel el gravity men el Z 3asha ndman real acceleration

        //assigning el event values bl corresspoding variable leeha
        double x=event.values[0];
        double y=event.values[1];

//Low and High pass filter
//        gravity[0] = alpha * gravity[0] + (1 - alpha) * event.values[0];
//        gravity[1] = alpha * gravity[1] + (1 - alpha) * event.values[1];
//        gravity[2] = alpha * gravity[2] + (1 - alpha) * event.values[2];




//high pass filter
//        linear_acceleration[0] = event.values[0] - gravity[0];
//        linear_acceleration[1] = event.values[1] - gravity[1];
//        linear_acceleration[2] = event.values[2] - gravity[2];


//        Long tsLong = System.currentTimeMillis()/1000;
//        String ts = tsLong.toString();
        String ts=new SimpleDateFormat("yy-MM-dd HH:mm:ss:SSS").format(new Date()); // el date yb2a day month year time

        //assigning el textviews ll x w el y w el z ely gebnahom b3d el filters
        xText.setText( String.valueOf(event.values[0]));
        yText.setText(String.valueOf( event.values[1]));
        zText.setText(String.valueOf( z));
        timestamp.setText(String.valueOf(ts));
//3ashan n stop l accelerometer sensnor
stopBtn.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        onPause(); // unregister listener
    }
});




//         if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD)
//        {
//            mGeomagnetic = event.values;
//
//
//            if (isTiltDownward())
//            {
//                Log.d("test", "downwards");
//            }
//            else if (isTiltUpward())
//            {
//                Log.d("test", "upwards");
//            }
//        }

        //Windowing based on Thresholding
        //khadna el readings bt3t range wa23at fl x w el y w el z 3ashan y7ot fl db el w23at bs msh kol haga
if(x>-18.94 &&x<19.52 &&y>-18.6 && y<18.94 && z>-12.1 && z<19.4){
    addDataTodb();//function bt7ot fl database
//    addPredTodb();
}
//else if(event.values[0]>-5.4 && event.values[0]<14.7 && event.values[1]>-18.4 && event.values[1]<13.1 && z>-12.1 && z<19.4){
//    UtilitiesHelper.showToast(getApplicationContext(),"el7a2 FKL");
//    Log.i("Bnshof","el72 FKL");
//    addDataTodb();
//}
//else if(event.values[0]>-11.8 && event.values[0]<19.52 && event.values[1]>-18.6 && event.values[1]<17.1 && z>-8.1 && z<19.2){
//    UtilitiesHelper.showToast(getApplicationContext(),"el7a2 FOL");
//    Log.i("Bnshof","el72 FOL");
//    addDataTodb();
//}
//   else if(event.values[0]>-18.94 && event.values[0]<4.39 && event.values[1]>-10.87 && event.values[1]<11.10 && z>-5.45 && z<18.11){
//    UtilitiesHelper.showToast(getApplicationContext(),"el7a2 SDL");
//    Log.i("Bnshof","el72 SDL");
//    addDataTodb();

//}

//        if (stopAcc !=0)
//        {
//            SM.unregisterListener(this);
//
//        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        SM.unregisterListener(this); //ywa2af el accelerometer
        Intent i = new Intent(Readings.this,MainActivity.class);//btwadeena main activity b3d ma dosna stop
        startActivity(i);
        finish();//3ashan b3d ma el activity teroo7 ll main activity, t2fel
    }
    @Override
    protected void onResume() {
        super.onResume();
        SM.registerListener(this,
                SM.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_NORMAL);//ebn3mel register listener w n2ool eno accelerometer w bnkhtar delay normal


    }

//    public boolean isTiltUpward()
//    {
//
//        if (mGravity != null && mGeomagnetic != null)
//        {
//            float R[] = new float[9];
//            float I[] = new float[9];
//
//            boolean success = SensorManager.getRotationMatrix(R, I, mGravity, mGeomagnetic);
//
//            if (success)
//            {
//                float orientation[] = new float[3];
//                SensorManager.getOrientation(R, orientation);
//
//
//                pitch = orientation[1];
//                roll = orientation[2];
//
//                gravity = mGravity.clone();
//
//                double norm_Of_g = Math.sqrt(gravity[0] * gravity[0] + gravity[1] * gravity[1] + gravity[2] * gravity[2]);
//
//                // Normalize the accelerometer vector
//                gravity[0] = (float) (gravity[0] / norm_Of_g);
//                gravity[1] = (float) (gravity[1] / norm_Of_g);
//                gravity[2] = (float) (gravity[2] / norm_Of_g);
//
//                //Checks if device is flat on ground or not
//                int inclination = (int) Math.round(Math.toDegrees(Math.acos(gravity[2])));
//
//
//                Float objPitch = new Float(pitch);
//                Float objZero = new Float(0.0);
//                Float objZeroPointTwo = new Float(0.2);
//                Float objZeroPointTwoNegative = new Float(-0.2);
//
//                int objPitchZeroResult = objPitch.compareTo(objZero);
//                int objPitchZeroPointTwoResult = objZeroPointTwo.compareTo(objPitch);
//                int objPitchZeroPointTwoNegativeResult = objPitch.compareTo(objZeroPointTwoNegative);
//
//                if (roll < 0 && ((objPitchZeroResult > 0 && objPitchZeroPointTwoResult > 0) || (objPitchZeroResult < 0 && objPitchZeroPointTwoNegativeResult > 0)) && (inclination > 30 && inclination < 40))
//                {
//                    Log.d("test","UPWARDS YA WALAAAA");
//                    return true;
//                }
//                else
//                {
//                    return false;
//                }
//            }
//        }
//
//        return false;
//    }
//
//    public boolean isTiltDownward()
//    {
//
//        if (mGravity != null && mGeomagnetic != null)
//        {
//            float R[] = new float[9];
//            float I[] = new float[9];
//
//            boolean success = SensorManager.getRotationMatrix(R, I, mGravity, mGeomagnetic);
//
//            if (success)
//            {
//                float orientation[] = new float[3];
//                SensorManager.getOrientation(R, orientation);
//
//                pitch = orientation[1];
//                roll = orientation[2];
//
//                gravity = mGravity.clone();
//
//                double norm_Of_g = Math.sqrt(gravity[0] * gravity[0] + gravity[1] * gravity[1] + gravity[2] * gravity[2]);
//
//                // Normalize the accelerometer vector
//                gravity[0] = (float) (gravity[0] / norm_Of_g);
//                gravity[1] = (float) (gravity[1] / norm_Of_g);
//                gravity[2] = (float) (gravity[2] / norm_Of_g);
//
//                //Checks if device is flat on groud or not
//                int inclination = (int) Math.round(Math.toDegrees(Math.acos(gravity[2])));
//
//                Float objPitch = new Float(pitch);
//                Float objZero = new Float(0.0);
//                Float objZeroPointTwo = new Float(0.2);
//                Float objZeroPointTwoNegative = new Float(-0.2);
//
//                int objPitchZeroResult = objPitch.compareTo(objZero);
//                int objPitchZeroPointTwoResult = objZeroPointTwo.compareTo(objPitch);
//                int objPitchZeroPointTwoNegativeResult = objPitch.compareTo(objZeroPointTwoNegative);
////                if (inclination < 25 || inclination > 155)
////                {
//////                    Log.d("test","flaaaaaaaaat");
////                }
////                else
////                {
//                    if (roll < 0 && ((objPitchZeroResult > 0 && objPitchZeroPointTwoResult > 0) || (objPitchZeroResult < 0 && objPitchZeroPointTwoNegativeResult > 0)) && (inclination > 140 && inclination < 170))
//                    {
//                        Log.d("test","DOWNWARDS YA WALAAAA");
//                        return true;
//                    }
//                    else
//                    {
//                        return false;
//                    }
//                }
//
//
//
//            }
//       // }
//
//        return false;
//    }

    public void addDataTodb() {

        //firebase
        FirebaseApp.initializeApp(getApplicationContext());

        //bngeeb instance men el table ely 3mleeno fl utilities helper
         mDatabase = FirebaseDatabase.getInstance().getReference(UtilitiesHelper.ACCELEROMETER_TABLE);
        //bngeeb unique key ely hay7oto fl db
        String AccID = mDatabase.push().getKey();

        //bn3mel object ml class ely esmo accelerometer model w n7ot gowah el data ely gbnaha mn el textviews
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
                    if (task.isSuccessful()) { //3mlen listener 3ashan lw el data et7atet sah fl db
                        UtilitiesHelper.showToast(getBaseContext(), "Successfully Added to the Firebase" );

                    } else {
                        UtilitiesHelper.showToast(getApplicationContext(), "Error occured, " + task.getException());
                    }
                }
            });
        }


    }

    public void addPredTodb() { //3ashan n7ot el predections fl database
        FirebaseApp.initializeApp(getApplicationContext());


        mDatabase = FirebaseDatabase.getInstance().getReference(UtilitiesHelper.PREDICTIONS_TABLE);

        String PredID = mDatabase.push().getKey();


        PredictionsModel predictionsModel = new PredictionsModel(
                "FKL",
                PredID
        );

        if (PredID != null) {
            mDatabase.child(PredID).setValue(predictionsModel).addOnCompleteListener(new OnCompleteListener<Void>() {
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


}


