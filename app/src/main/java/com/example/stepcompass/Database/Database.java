package com.example.stepcompass.Database;


import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.stepcompass.Entities.User;
import com.example.stepcompass.Entities.UserStepData;
import com.example.stepcompass.Util.DateConverter;
/*
 *   @Author    Henrik Olofsson
 *   @Date      2023-01-25
 *
 *   @Database creates the actual room database, read from local files.
 */
@androidx.room.Database(entities = {User.class, UserStepData.class}, version = 17, exportSchema = false)
@TypeConverters({DateConverter.class})
public abstract class Database extends RoomDatabase {
    public abstract DAO databaseAccess();
}
