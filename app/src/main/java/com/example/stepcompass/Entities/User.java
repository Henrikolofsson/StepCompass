package com.example.stepcompass.Entities;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

/*
 *   @Author    Henrik Olofsson
 *   @Date      2023-01-25
 *
 *   @User  Entity class that also annotates the database table as a reference.
 */
@Entity(tableName = "user_table")
public class User {
    @PrimaryKey(autoGenerate = true)
    private int user_id;

    @ColumnInfo(name = "user_name")
    private String user_name;

    @ColumnInfo(name = "user_password")
    private String user_password;

    public User(String user_name, String user_password) {
        this.user_name = user_name;
        this.user_password = user_password;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getUser_password() {
        return user_password;
    }

    public void setUser_password(String user_password) {
        this.user_password = user_password;
    }

    @Override
    public String toString() {
        return "User{" +
                "user_id=" + user_id +
                ", user_name='" + user_name + '\'' +
                ", user_password='" + user_password + '\'' +
                '}';
    }
}
