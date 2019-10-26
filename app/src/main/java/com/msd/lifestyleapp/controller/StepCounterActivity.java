package com.msd.lifestyleapp.controller;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.msd.lifestyleapp.R;

import java.util.Random;

import androidx.appcompat.app.AppCompatActivity;

public class StepCounterActivity extends AppCompatActivity {
    private SensorManager mSensorManager;
    private TextView mTvData;
    private Sensor mStepCounter;
    private final double mThreshold = 10.0;
    long lastUpdate;
    boolean stepCounterOn = false;

    //Previous positions
    private double last_x;
    private double now_x;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (MainActivity.isTablet) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            setContentView(R.layout.activity_step_tablet);

        } else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            setContentView(R.layout.activity_step);
        }

        mTvData = (TextView) findViewById(R.id.tv_data);
        mTvData.setVisibility(View.INVISIBLE);

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mStepCounter = mSensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
    }

    private SensorEventListener mListener = new SensorEventListener() {

        @Override
        public void onSensorChanged(SensorEvent sensorEvent) {
            Sensor mySensor = sensorEvent.sensor;

            if (mySensor.getType() == Sensor.TYPE_ACCELEROMETER) {
                System.out.println("ACCELEROMETER DETECTED");
            } else if (mySensor.getType() == Sensor.TYPE_STEP_COUNTER) {
                System.out.println("STEP COUNTER DETECTED");
            }

            if (mySensor.getType() == Sensor.TYPE_ACCELEROMETER) {
                //Get the acceleration rates along the x axis
                now_x = sensorEvent.values[0];

                long curTime = System.currentTimeMillis();

                double dx = Math.abs(last_x - now_x);

                if ((curTime - lastUpdate) > 1000 && dx > mThreshold) {
                    lastUpdate = curTime;

                    System.out.println("DX: " + dx);

                    if (!stepCounterOn) stepCounterOn = true;
                    else stepCounterOn = false;
                }

                last_x = now_x;
            }

            if (mySensor.getType() == Sensor.TYPE_STEP_COUNTER && stepCounterOn) {
                //start counting steps
                mTvData.setText("" + String.valueOf(sensorEvent.values[0]));
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int i) {

        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        if (mStepCounter != null) {
            mSensorManager.registerListener(mListener, mStepCounter, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mStepCounter != null) {
            mSensorManager.unregisterListener(mListener);
        }
    }


}
