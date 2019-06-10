package com.example.accelerometer;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.view.View.OnClickListener;

import com.google.firebase.FirebaseApp;

import java.util.List;

public class MainActivity extends AppCompatActivity implements OnClickListener {

    TextView label;
    Button reading;
    int count = 1;
    private SensorManager mSensorManager;
    boolean record = false;
    Sensor myAcc;
    //MySensorListener listener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        Intent i=new Intent(MainActivity.this,LoginActivity.class);
//        startActivity(i);
//        finish();

        mSensorManager = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
        List<Sensor> sensorList = mSensorManager.getSensorList(Sensor.TYPE_ACCELEROMETER);
        myAcc = sensorList.get(0);

        label = (TextView) findViewById(R.id.number_label);
        reading = (Button) findViewById(R.id.reading_button);
        reading.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) { //3ashan neroo7 ll readings activity
        if (reading.getText().equals("Start")){
            // Register Listener
            //mSensorManager.registerListener(listener = new MySensorListener(count), myAcc , SensorManager.SENSOR_DELAY_UI);
            Intent i = new Intent(MainActivity.this,Readings.class);
            startActivity(i);
            finish();
            reading.setText("Stop");
        }
        else{
            reading.setText("Start");
            count ++;
            label.setText(Integer.toString(count));
           // mSensorManager.unregisterListener(listener, myAcc);
        }

    }
}
