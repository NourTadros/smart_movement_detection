package com.example.accelerometer;

public class PredictionsModel {
    private String predictions, id;

    public PredictionsModel(){

    }

    public PredictionsModel(String predictions, String id) {
        this.predictions = predictions;
        this.id = id;
    }

    public String getPredictions() {
        return predictions;
    }

    public void setPredictions(String predictions) {
        this.predictions = predictions;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }





}


