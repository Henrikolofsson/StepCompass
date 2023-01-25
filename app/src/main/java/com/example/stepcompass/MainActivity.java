package com.example.stepcompass;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import com.example.stepcompass.Activities.CompassActivity;
import com.example.stepcompass.Activities.StepActivity;

/*
 *   @Author    Henrik Olofsson
 *   @Date      2023-01-25
 *
 *   APP STARTS HERE.
 *   @MainActivity Where the application is started. It creates a controller, and asks for ACTIVITY_RECOGNITION permission. (Needed for the step counter)
 */
public class MainActivity extends AppCompatActivity {
    private FragmentManager fragmentManager;
    private Controller controller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fragmentManager = getSupportFragmentManager();
        controller = new Controller(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            requestPermissione();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    private void requestPermissione() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACTIVITY_RECOGNITION) != PackageManager.PERMISSION_GRANTED)
        {
            requestPermissions(new String[] {Manifest.permission.ACTIVITY_RECOGNITION}, 0);
        }
    }

    public void setFragment(Fragment fragment, String tag) {
        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.replace(R.id.main_container, fragment, tag);
        ft.commit();
    }

    public void startStepActivity() {
        Intent stepIntent = new Intent(this, StepActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("Username", controller.getUserName());
        //bundle.putString("Password", controller.getUserPassword());
        bundle.putInt("Id", controller.getUserId());
        onSaveInstanceState(bundle);
        stepIntent.putExtras(bundle);
        startActivity(stepIntent);
    }

    public void startCompassActivity() {
        Intent stepIntent = new Intent(this, CompassActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("Username", controller.getUserName());
        //bundle.putString("Password", controller.getUserPassword());
        bundle.putInt("Id", controller.getUserId());
        onSaveInstanceState(bundle);
        stepIntent.putExtras(bundle);
        startActivity(stepIntent);
    }
}