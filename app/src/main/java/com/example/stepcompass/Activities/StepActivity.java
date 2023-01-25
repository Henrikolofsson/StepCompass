package com.example.stepcompass.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.stepcompass.Adapters.StepEntriesAdapter;
import com.example.stepcompass.AsyncTasks.DBHandler;
import com.example.stepcompass.CompassService.StepService;
import com.example.stepcompass.Controller;
import com.example.stepcompass.DBAccess;
import com.example.stepcompass.Entities.UserStepData;
import com.example.stepcompass.R;
import com.example.stepcompass.Util.CompassBroadcastReceiver;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class StepActivity extends AppCompatActivity {
    private StepService stepService;
    private boolean isConnected;
    private Controller controller;
    private View view;
    private RecyclerView rvStepEntries;
    private StepEntriesAdapter entriesAdapter;
    private TextView tvShowSecondsTotal;
    private TextView tvTotalSteps;
    private TextView tvStepsPerSecond;
    private Button btnReset;
    private List<UserStepData> listOfEntries;
    private Timer timer;
    private int secondsPassed;
    private CompassBroadcastReceiver br;
    private int steps;
    int userId;
    private DBAccess dbAccess;
    private DBHandler dbHandler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step);
        dbAccess = new DBAccess(this);
        dbHandler = new DBHandler(dbAccess);
        userId = getIntent().getIntExtra("Id", -1);
        Intent intent = new Intent(this, StepService.class);
        intent.putExtra("Id", userId);
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
        startCounter();

    }

    @Override
    protected void onStart() {
        super.onStart();
        initializeComponents();
        registerListeners();
        initBroadcastReceiver();
    }

    private void initBroadcastReceiver() {
        br = new CompassBroadcastReceiver(this);
        IntentFilter intentfilter = new IntentFilter("com.example.broadcast.stepupdates");
        registerReceiver(br, intentfilter);
    }

    private void startCounter() {
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                secondsPassed++;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        updateSeconds(secondsPassed);
                    }
                });
            }
        }, 1000, 1000);
    }


    private void initializeComponents() {
        listOfEntries = dbHandler.getAllStepEntries(userId);
        if(listOfEntries == null) {
            listOfEntries = new ArrayList<>();
        }
        entriesAdapter = new StepEntriesAdapter(this, listOfEntries);
        rvStepEntries = (RecyclerView) findViewById(R.id.rv_entries);
        rvStepEntries.setLayoutManager(new LinearLayoutManager(this));
        rvStepEntries.setAdapter(entriesAdapter);

        tvTotalSteps = (TextView) findViewById(R.id.tv_total_steps);
        tvStepsPerSecond = (TextView) findViewById(R.id.tv_steps_per_second);
        tvShowSecondsTotal = (TextView) findViewById(R.id.tv_seconds_passed);
        btnReset = (Button) findViewById(R.id.btn_reset);
    }



    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            StepService.StepServiceBinder binder = (StepService.StepServiceBinder) service;
            stepService = binder.getBoundService();
            isConnected = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            isConnected = false;
        }
    };

    public void updateSeconds(int seconds) {
        tvShowSecondsTotal.setText(String.valueOf(seconds));
        updateStepsPerSecond(seconds);
    }

    private void updateStepsPerSecond(int secondsPassed) {
        double stepsPerSecond = ((double)(steps) / (double)(secondsPassed));
        tvStepsPerSecond.setText(String.format(Locale.getDefault(),"%.2g%n", stepsPerSecond));
    }

    public void updateSteps(int steps) {
        this.steps = steps;
        tvTotalSteps.setText(String.valueOf(steps));
    }

    private void registerListeners() {
        btnReset.setOnClickListener(new ResetStepHistoryListener());
    }

    private class ResetStepHistoryListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            dbHandler.deleteStepHistory(userId);
            listOfEntries.clear();
            entriesAdapter = new StepEntriesAdapter(getApplicationContext(), listOfEntries);
            rvStepEntries.setAdapter(entriesAdapter);
        }
    }
}