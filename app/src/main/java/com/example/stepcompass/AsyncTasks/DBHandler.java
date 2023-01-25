package com.example.stepcompass.AsyncTasks;

import android.os.Handler;
import android.os.Looper;

import com.example.stepcompass.Controller;
import com.example.stepcompass.DBAccess;
import com.example.stepcompass.Entities.User;
import com.example.stepcompass.Entities.UserStepData;

import java.sql.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/*
 *   @Author    Henrik Olofsson
 *   @Date      2023-01-25
 *
 *   @DBHandler is a util class to make asynchronous calls to the database using the Executors class.
 */
public class DBHandler {
    private Controller controller;
    private DBAccess dbAccess;
    private ExecutorService executor;
    private Handler handler;

    public DBHandler(Controller controller, DBAccess dbAccess) {
        this.controller = controller;
        this.dbAccess = dbAccess;
        executor = Executors.newSingleThreadExecutor();
        handler = new Handler(Looper.getMainLooper());
    }

    public DBHandler(DBAccess dbAccess) {
        this.dbAccess = dbAccess;
        executor = Executors.newSingleThreadExecutor();
        handler = new Handler(Looper.getMainLooper());
    }

    public boolean doUserExists(String username) {
        Future<Integer> result = executor.submit(() -> dbAccess.getUserId(username));
        try {
            return result.get() > 0;
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void createUser(String username, String password) {
        executor.submit(new Runnable() {
            @Override
            public void run() {
                dbAccess.addUser(new User(username, password));
            }
        });
    }

    public int getUserId(String username) {
        Future<Integer> result = executor.submit(() -> dbAccess.getUserId(username));
        try {
            return result.get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public boolean isPasswordCorrect(String username, String password) {
        Future<Boolean> result = executor.submit(() -> dbAccess.isPasswordCorrect(username, password));
        try {
            return result.get();
        } catch (ExecutionException | InterruptedException e) {

            return false;
        }
    }

    public UserStepData getStepEntryForToday(int userId, String date) {
        Future<UserStepData> result = executor.submit(() -> dbAccess.getStepEntryForToday(userId, date));
        try {
            return result.get();
        } catch (ExecutionException | InterruptedException e) {
            return null;
        }
    }

    public List<UserStepData> getAllStepEntries(int userId) {
        Future<List<UserStepData>> result = executor.submit(() -> dbAccess.getStepEntries(userId));
        try {
            return result.get();
        } catch (ExecutionException | InterruptedException e) {
            return null;
        }
    }

    public void addStepEntry(UserStepData userStepData) {
        executor.submit(new Runnable() {
            @Override
            public void run() {
                dbAccess.addStepEntry(userStepData);
            }
        });
    }

    public void updateStepEntry(UserStepData userStepData) {
        executor.submit(new Runnable() {
            @Override
            public void run() {
                dbAccess.updateStepEntry(userStepData);
            }
        });
    }

    public void deleteStepHistory(int userId) {
        executor.submit(new Runnable() {
            @Override
            public void run() {
                dbAccess.deleteStepHistory(userId);
            }
        });
    }



    /*
        public void nukeUserTable() {
        executor.submit(new Runnable() {
            @Override
            public void run() {
                dbAccess.nukeUserTable();
            }
        });
    }
     */


}
