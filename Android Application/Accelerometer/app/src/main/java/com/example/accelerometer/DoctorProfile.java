package com.example.accelerometer;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class DoctorProfile extends AppCompatActivity {
private TextView Hello;
private TextView patient;
private TextView status;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_profile);
        Hello=findViewById(R.id.hellodr);
        patient=findViewById(R.id.patient);
        status=findViewById(R.id.status);


    }
}
