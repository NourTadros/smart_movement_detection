package com.example.accelerometer;

public class AccelerometerModel {
    private String timestamp,acc_x,acc_y,acc_z, acc_id;//data will be added to table in firebase

    public AccelerometerModel(){

    }

    public AccelerometerModel(String timestamp, String acc_x, String acc_y, String acc_z,  String acc_ID) {
        this.timestamp = timestamp;
        this.acc_x = acc_x;
        this.acc_y = acc_y;
        this.acc_z = acc_z;
        this.acc_id = acc_ID;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getAcc_x() {
        return acc_x;
    }

    public void setAcc_x(String acc_x) {
        this.acc_x = acc_x;
    }

    public String getAcc_y() {
        return acc_y;
    }

    public void setAcc_y(String acc_y) {
        this.acc_y = acc_y;
    }

    public String getAcc_z() {
        return acc_z;
    }

    public void setAcc_z(String acc_z) {
        this.acc_z = acc_z;
    }

    public String getAcc_id() {
        return acc_id;
    }

    public void setAcc_id(String acc_id) {
        this.acc_id = acc_id;
    }





}
