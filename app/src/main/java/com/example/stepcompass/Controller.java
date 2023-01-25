package com.example.stepcompass;

import android.content.IntentFilter;
import android.widget.Toast;


import com.example.stepcompass.AsyncTasks.DBHandler;
import com.example.stepcompass.Fragments.LoginFragment;
import com.example.stepcompass.Fragments.MenuFragment;
import com.example.stepcompass.Util.CompassBroadcastReceiver;


public class Controller {
    private MainActivity activity;
    private DBAccess dbAccess;
    private DBHandler dbHandler;

    private LoginFragment loginFragment;
    private MenuFragment menuFragment;

    private String userName;
    private int userId;

    private CompassBroadcastReceiver br;

    public Controller(MainActivity mainActivity) {
        this.activity = mainActivity;
        dbAccess = new DBAccess(activity, this);
        dbHandler = new DBHandler(this, dbAccess);
        loginFragment = new LoginFragment(this);
        menuFragment = new MenuFragment();
        menuFragment.setController(this);
        activity.setFragment(loginFragment, "LoginFragment");
        initBroadcastReceiver();
    }

    public void signCreateBtnPressed(String username, String password) {
        if(dbHandler.doUserExists(username)) {
            if(dbHandler.isPasswordCorrect(username, password)) {
                setUserName(username);
                setUserId(dbHandler.getUserId(username));
                activity.setFragment(menuFragment, "MenuFragment");
            } else {
                Toast.makeText(activity, "Password not correct.", Toast.LENGTH_LONG).show();
            }
        } else {
            createUser(username, password);
            setUserName(username);
            setUserId(dbHandler.getUserId(username));
            activity.setFragment(menuFragment, "MenuFragment");
        }

    }

    private void createUser(String username, String password) {
        dbHandler.createUser(username, password);
    }

    public void ActivityChosen(String chosenActivity) {
        if(chosenActivity.equals("compass")) {
            activity.startCompassActivity();
        } else if(chosenActivity.equals("steps")) {
            activity.startStepActivity();
        }
    }

    private void initBroadcastReceiver() {
        br = new CompassBroadcastReceiver(this);
        IntentFilter intentfilter = new IntentFilter("com.example.broadcast.stepupdates");
        activity.registerReceiver(br, intentfilter);
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void updateSteps(int steps) {

    }
}
