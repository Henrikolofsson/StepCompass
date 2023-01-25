package com.example.stepcompass.Entities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.example.stepcompass.Util.DateConverter;

import java.sql.Date;


@Entity(tableName = "user_step_data")
@TypeConverters(DateConverter.class)
public class UserStepData {
    @NonNull
    @PrimaryKey(autoGenerate = true)
    private int user_step_data_id;

    @ColumnInfo(name = "user_id")
    private int user_id;

    @ColumnInfo(name = "date")
    private String date;

    @ColumnInfo(name = "steps")
    private int steps;

    public UserStepData(int user_id, String date, int steps) {
        this.user_id = user_id;
        this.date = date;
        this.steps = steps;
    }

    public int getUser_step_data_id() {
        return user_step_data_id;
    }

    public void setUser_step_data_id(int user_step_data_id) {
        this.user_step_data_id = user_step_data_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getSteps() {
        return steps;
    }

    public void setSteps(int steps) {
        this.steps = steps;
    }

    @Override
    public String toString() {
        return "UserStepData{" +
                "user_step_data_id=" + user_step_data_id +
                ", user_id=" + user_id +
                ", date=" + date +
                ", steps=" + steps +
                '}';
    }
}
