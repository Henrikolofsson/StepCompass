package com.example.stepcompass.Database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.stepcompass.Entities.User;
import com.example.stepcompass.Entities.UserStepData;

import java.sql.Date;
import java.util.List;


@Dao
public interface DAO {
    @Insert
    void addUser(User... user);

    @Insert
    void addStepEntry(UserStepData... userStepData);

    @Query("SELECT user_name FROM user_table WHERE user_name= :name")
    String getName(String name);

    @Query("SELECT user_id FROM user_table WHERE user_name= :name")
    int getUserId(String name);

    @Query("SELECT user_id FROM user_table WHERE user_name= :name AND user_password= :password")
    boolean isPasswordCorrect(String name, String password);

    @Update
    void updateStepEntry(UserStepData... userStepData);

    @Query("SELECT * FROM user_step_data WHERE user_id= :userId")
    List<UserStepData> getStepEntries(int userId);

    @Query("SELECT * FROM user_step_data WHERE user_id= :userId AND date= :today")
    UserStepData getStepEntryFromToday(int userId, String today);

    @Query("SELECT * FROM user_step_data WHERE user_id= :userId")
    List<UserStepData> getStepEntryFromToday(int userId);

    @Query("DELETE FROM user_table")
    void nukeUserTable();

    @Query("DELETE FROM user_step_data WHERE user_id= :userId")
    void deleteStepHistory(int userId);
}
