package com.example.stepcompass.Util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.example.stepcompass.Activities.StepActivity;
import com.example.stepcompass.Controller;

public class CompassBroadcastReceiver extends BroadcastReceiver {
    private Controller controller;
    private StepActivity stepActivity;

    public CompassBroadcastReceiver(StepActivity stepActivity) {
        this.stepActivity = stepActivity;
    }

    public CompassBroadcastReceiver(Controller controller) {
        this.controller = controller;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        int steps = intent.getIntExtra("steps", -1);
        Log.d("STEPS", "Steps: " + String.valueOf(steps));
        if(steps > 0) {
            if(controller != null) {
                controller.updateSteps(steps);
            }
            if(stepActivity != null) {
                stepActivity.updateSteps(steps);
            }
        }
    }
}