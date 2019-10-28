package com.msd.lifestyleapp.controller;

import android.app.Activity;
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
import com.msd.lifestyleapp.model.AWSHandler;
import com.msd.lifestyleapp.model.SharedPreferencesHandler;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

import androidx.appcompat.app.AppCompatActivity;

public class StepCounterActivity extends AppCompatActivity {
    private SensorManager mSensorManager;
    private TextView mTvData;
    private Sensor mStepCounter;
    private Sensor mLinearAccelerometer;
    private SharedPreferencesHandler prefs;
    private AWSHandler awsHandler;
    private Activity activity;

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

        prefs = new SharedPreferencesHandler(this);
        awsHandler = new AWSHandler();
        activity = this;

        mTvData = (TextView) findViewById(R.id.tv_data);
        mTvData.setText("STEP COUNTER OFF");

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mLinearAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
        mStepCounter = mSensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);

    }

    private SensorEventListener mListener = new SensorEventListener() {

        @Override
        public void onSensorChanged(SensorEvent sensorEvent) {
            Sensor mySensor = sensorEvent.sensor;

            if (mySensor.getType() == Sensor.TYPE_LINEAR_ACCELERATION) {
                //Get the acceleration rates along the x axis
                now_x = sensorEvent.values[0];

                long curTime = System.currentTimeMillis();

                double dx = Math.abs(last_x - now_x);

                if ((curTime - lastUpdate) > 1000 && dx > mThreshold) {
                    lastUpdate = curTime;

//                    System.out.println("DX: " + dx);

                    if (!stepCounterOn) {
                        stepCounterOn = true;
                        System.out.println("STEP COUNTER ON");
                        mTvData.setText("STEP COUNTER ON");
                    }
                    else {
                        stepCounterOn = false;
                        System.out.println("STEP COUNTER OFF");
                        mTvData.setText("STEP COUNTER OFF");
                    }
                }

                last_x = now_x;
            }

            if (mySensor.getType() == Sensor.TYPE_STEP_COUNTER && stepCounterOn) {
                System.out.println("counting steps");
                System.out.println(sensorEvent.values[0]);
                //start counting steps
                mTvData.setText("" + String.valueOf((int)sensorEvent.values[0]));

                boolean backup = prefs.addTime();

                if(backup){
                    float val = sensorEvent.values[0];

                    File file = new File(activity.getBaseContext().getFilesDir(), "steps");

                    // Write to a file.
                    BufferedWriter writer = null;
                    try {
                        writer = new BufferedWriter(new FileWriter(file));
                        writer.write(Float.toString(val));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    awsHandler.uploadWithTransferUtility(file.getAbsolutePath(), activity);
                }
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
        if (mLinearAccelerometer != null) {
            mSensorManager.registerListener(mListener, mLinearAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mStepCounter != null || mLinearAccelerometer != null) {
            mSensorManager.unregisterListener(mListener);
        }
    }


}
