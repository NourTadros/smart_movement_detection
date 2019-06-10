package com.example.accelerometer;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class DoctorProfile extends AppCompatActivity {
private TextView Hello;
private TextView patient;



    private TextView statusLbl;

String status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_profile);
        Hello=findViewById(R.id.hellodr);
        patient=findViewById(R.id.patient);
        statusLbl=findViewById(R.id.status);

        Intent iin= getIntent();
        Bundle b = iin.getExtras();


        status = UtilitiesHelper.getStatus(getBaseContext());

        if (status != null){
            statusLbl.setText(status);
        }

    }
}
