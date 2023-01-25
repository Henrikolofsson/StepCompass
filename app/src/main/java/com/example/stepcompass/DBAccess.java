package com.example.stepcompass;

import android.content.Context;
import android.util.Log;

import androidx.room.Room;

import com.example.stepcompass.Database.DAO;
import com.example.stepcompass.Database.Database;
import com.example.stepcompass.Entities.User;
import com.example.stepcompass.Entities.UserStepData;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

/*
 *   @Author    Henrik Olofsson
 *   @Date      2023-01-25
 *
 *   Uses the Database Access Object interface to declare the DBHandling functions.
 */
public class DBAccess {
    private static final String DATABASE_NAME = "StepDB";
    private static Database database;
    private DAO DAO;

    public DBAccess(Context context, Controller controller) {
        database = Room.databaseBuilder(context, Database.class, DATABASE_NAME).fallbackToDestructiveMigration().build();
        DAO = database.databaseAccess();
        Log.d("THISDATABASE", database.toString());
    }

    public DBAccess(Context context) {
        database = Room.databaseBuilder(context, Database.class, DATABASE_NAME).fallbackToDestructiveMigration().build();
        DAO = database.databaseAccess();
        Log.d("THISDATABASE", database.toString());
    }

    public int getUserId(String name) {
        return DAO.getUserId(name);
    }

    public void addUser(User user){
        DAO.addUser(user);
    }

    public void addStepEntry(UserStepData userStepData) {
        DAO.addStepEntry(userStepData);
    }

    public void updateStepEntry(UserStepData userStepData) {
        DAO.updateStepEntry(userStepData);
    }

    public boolean isPasswordCorrect(String username, String password) {
        return DAO.isPasswordCorrect(username, password);
    }

    public List<UserStepData> getStepEntries(int userId) {
        return (ArrayList<UserStepData>) DAO.getStepEntries(userId);
    }

    public void nukeUserTable() {
        DAO.nukeUserTable();
    }


    public UserStepData getStepEntryForToday(int userId, String today) {
        return DAO.getStepEntryFromToday(userId, today);
    }

    public void deleteStepHistory(int userId) {
        DAO.deleteStepHistory(userId);
    }
}
