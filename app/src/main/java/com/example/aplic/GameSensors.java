package com.example.aplic;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public class GameSensors implements SensorEventListener {

    private final SensorManager sensorManager;
    private float xRotation;

    public GameSensors(Context context) {
        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
        xRotation = 0;
    }
    // gives the x rotation to the position censor object
    public float getXRotation() {
        return xRotation;
    }
    // stops the censor
    public void stopSensors() {
        sensorManager.unregisterListener(this);
    }
    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        Sensor sensor = sensorEvent.sensor;
        // get the phone's x rotation and save it in a variable
        if (sensor.getType()==Sensor.TYPE_ACCELEROMETER) {
            xRotation = sensorEvent.values[0];
        }
    }
    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}
