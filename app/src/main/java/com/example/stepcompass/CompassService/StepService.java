package com.example.stepcompass.CompassService;

import android.app.Service;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.example.stepcompass.AsyncTasks.DBHandler;
import com.example.stepcompass.DBAccess;
import com.example.stepcompass.Entities.UserStepData;

import java.sql.Date;
import java.util.Calendar;

/*
 *   @Author    Henrik Olofsson
 *   @Date      2023-01-25
 *
 *   @StepService   A service started by the StepActivity, its a local bound service. It will destroy when the Activity destroys.
 */
public class StepService extends Service implements SensorEventListener {
    public StepServiceBinder serviceBinder;
    private SensorManager sensorManager;
    private Sensor stepSensor;
    int stepCount;
    private DBAccess dbAccess;
    int userId;
    private Intent intent;
    private int count = 0;
    private int prevStepCount;
    private int actualStepCount;
    private DBHandler dbHandler;



    public StepService() {
    }

    /*
        @OnBind     Will be called when an intent to start the StepService is called from StepActivity.
                    It registers/subscribing the stepSensor for SensorEvent calls.
     */
    @Override
    public IBinder onBind(Intent intent) {
        this.intent = intent;
        if(intent != null) {
            Bundle extras = intent.getExtras();
            this.userId = extras.getInt("Id");
            Log.d("USERID", String.valueOf(userId));
        } else {
            Log.d("USERID", "INTENT NULL");
        }
        serviceBinder = new StepService.StepServiceBinder();
        sensorManager.registerListener(this, stepSensor, SensorManager.SENSOR_DELAY_NORMAL);
        return serviceBinder;
    }


    /*
       @onUBind     Will be called when the Activity is either destroyed or paused.
                    If there is an existing StepEntry for this user at the same day it will update the step count values.
                    Else, it will create a new StepEntry in the users step history.
                    Unregisters the Sensor.TYPE_STEP_COUNTER
    */
    @Override
    public boolean onUnbind(Intent intent) {
        Calendar c = Calendar.getInstance();
        long day = c.getTimeInMillis();
        Date date = new Date(day);
        Log.d("SAVED", date.toString());
        UserStepData userStepData = dbHandler.getStepEntryForToday(userId, date.toString());
        if(userStepData == null) {
            dbHandler.addStepEntry(new UserStepData(userId, date.toString(), actualStepCount));
        } else {
            int newStepCount = userStepData.getSteps() + actualStepCount;
            userStepData.setSteps(newStepCount);
            dbHandler.updateStepEntry(userStepData);
        }
        sensorManager.unregisterListener(this);
        return super.onUnbind(intent);
    }

    /*
        @OnCreate   Sets up database access objects. Registers the sensor.
     */
    @Override
    public void onCreate() {
        super.onCreate();
        dbAccess = new DBAccess(getApplicationContext());
        dbHandler = new DBHandler(dbAccess);
        sensorManager = (SensorManager) this.getSystemService(SENSOR_SERVICE);
        if(sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER) != null) {
            stepSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        } else {
            Toast.makeText(this, "SENSOR: STEP COUNTER DOESN'T EXIST", Toast.LENGTH_LONG).show();
        }
    }

    public class StepServiceBinder extends Binder {
        public StepService getBoundService() {
            return StepService.this;
        }
    }

    /*
        Since the Sensor.TYPE_STEP_COUNTER starts with a value that is immutable by me, I had to make a solution
        to remember what step count I started on, so that I can subtract it from the value when I send it to
        the database. Recommended solution is to use Sensor.TYPE_STEP_DETECTOR (less accurate, but starts at 0).
        But I don't have this sensor on my Huawei P30 lite.
     */
    @Override
    public void onSensorChanged(SensorEvent event) {
        if(event.sensor.getType() == Sensor.TYPE_STEP_COUNTER) {
            stepCount = (int) event.values[0];
            if(count < 1) {
                prevStepCount = stepCount;
                count++;
            }
            Log.d("STEPS", "DERIVED FROM SENSOR: "+String.valueOf(stepCount));
            Log.d("STEPS","PREVSTEPCOUNT(FIRST DERIVED):" + String.valueOf(prevStepCount));
            stepCount -= prevStepCount;
            Log.d("STEPS", "ACTUAL STEPCOUNT: "+String.valueOf(stepCount));
            actualStepCount = stepCount;
            Intent intent = new Intent("com.example.broadcast.stepupdates");
            intent.putExtra("steps", stepCount);
            sendBroadcast(intent);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }


}