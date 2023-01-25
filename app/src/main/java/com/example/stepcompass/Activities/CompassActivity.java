package com.example.stepcompass.Activities;

import androidx.appcompat.app.AppCompatActivity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.Toast;
import com.example.stepcompass.R;

import java.util.Random;

/*
 *   @Author    Henrik Olofsson
 *   @Date      2023-01-25
 */
public class CompassActivity extends AppCompatActivity implements SensorEventListener {
    private SensorManager sensorManager;
    private Sensor mAccelerometer;
    private Sensor mMagnetometer;

    private ImageView ivCompass;
    private boolean isAnimation = false;
    private boolean isShakeDetected = false;
    private float acceleration = 10f;
    private float currentAcceleration = SensorManager.GRAVITY_EARTH;
    private float lastAcceleration = SensorManager.GRAVITY_EARTH;
    private float[] floatGravity = new float[3];
    private float[] floatGeoMagnetic = new float[3];
    private float[] floatOrientation = new float[3];
    private float[] floatRotationMatrix = new float[9];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compass);
        sensorManager = (SensorManager) this.getSystemService(SENSOR_SERVICE);
        ivCompass = (ImageView) findViewById(R.id.iv_compass);

        if(sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) != null && sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD) != null) {
            mAccelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            mMagnetometer = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        } else {
            Toast.makeText(this, "Do not have all the sensors for compass to work..", Toast.LENGTH_LONG).show();
        }
        registerListeners();
    }

    private void registerListeners() {
        sensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_UI);
        sensorManager.registerListener(this, mMagnetometer, SensorManager.SENSOR_DELAY_UI);
        Toast.makeText(this, "Register listeners on Accelerometer and Magnetometer", Toast.LENGTH_SHORT).show();
    }


    private void rotateCompass(float v) {
        ivCompass.setRotation(v);
    }

    /*
        @OnSensorChanged is called when a sensor event is triggered.
     */
    @Override
    public void onSensorChanged(SensorEvent event) {
        if(event.sensor == mAccelerometer) {
            // Getting the x,y,z values
            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];
            lastAcceleration = currentAcceleration;

            // Getting current accelerations
            currentAcceleration = (float) Math.sqrt(x * x + y * y + z * z);
            float delta = (currentAcceleration - lastAcceleration);
            acceleration = acceleration * 0.9f + delta;

            // Show a Toast message if acceleration value is over the threshold
            if (acceleration > 30) {
                Toast.makeText(this, "Shake event detected", Toast.LENGTH_SHORT).show();
                isShakeDetected = true;
                Log.d("ACCELERATION", String.valueOf(acceleration));
            }
        }

        //If the phone is shook and an animation is not already in progress.
        if(isShakeDetected && !isAnimation) {
            Random rand = new Random();
            int nbrOfRotations = rand.nextInt(3);
            float rotationDegrees = nbrOfRotations * 360;
            float calculatedDegrees = rotationDegrees + (float) (-floatOrientation[0]*180/3.14159);
            RotateAnimation rotateAnimation = new RotateAnimation((float) (-floatOrientation[0]*180/3.14159),
                    calculatedDegrees, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
            rotateAnimation.setDuration(5000);
            rotateAnimation.setInterpolator(new LinearInterpolator());
            ivCompass.startAnimation(rotateAnimation);
            isAnimation = true;

            final Handler handler = new Handler(Looper.getMainLooper());
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    isAnimation = false;
                    isShakeDetected = false;
                }
            }, 5000);

            //If a phone is not shaken and the animation is not in progress, rotate the compass to North.
        } else if (!isShakeDetected && !isAnimation){
            if(event.sensor == mAccelerometer) {
                floatGravity = event.values;
                SensorManager.getRotationMatrix(floatRotationMatrix, null, floatGravity, floatGeoMagnetic);
                SensorManager.getOrientation(floatRotationMatrix, floatOrientation);
                rotateCompass((float) (-floatOrientation[0]*180/3.14159));

            } else if(event.sensor == mMagnetometer) {
                floatGeoMagnetic = event.values;
                SensorManager.getRotationMatrix(floatRotationMatrix, null, floatGravity, floatGeoMagnetic);
                SensorManager.getOrientation(floatRotationMatrix, floatOrientation);
                rotateCompass((float) (-floatOrientation[0]*180/3.14159));
            }
        }

    }


    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterListeners();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterListeners();
    }

    private void unregisterListeners() {
        sensorManager.unregisterListener(this, mAccelerometer);
        sensorManager.unregisterListener(this, mMagnetometer);
        Toast.makeText(this, "Unregister listeners on Accelerometer and Magnetometer", Toast.LENGTH_SHORT).show();
    }
}