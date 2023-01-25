package com.example.stepcompass.Util;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.stepcompass.Activities.StepActivity;
import com.example.stepcompass.Controller;

/*
 *   @Author    Henrik Olofsson
 *   @Date      2023-01-25
 *
 *   @StepBroadcastReceiver     Subscribing on a broadcast as a client.
 *                              What it is subscribed on is defined by the IntentFilter that it gets when it is instantiated by another class. (The @param Action)
 *                              Updates the controller and StepActivity.
 */
public class StepBroadcastReceiver extends android.content.BroadcastReceiver {
    private Controller controller;
    private StepActivity stepActivity;

    public StepBroadcastReceiver(StepActivity stepActivity) {
        this.stepActivity = stepActivity;
    }

    public StepBroadcastReceiver(Controller controller) {
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