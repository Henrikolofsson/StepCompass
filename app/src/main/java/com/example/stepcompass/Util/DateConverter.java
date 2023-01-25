package com.example.stepcompass.Util;

import androidx.room.TypeConverter;

import java.sql.Date;
/*
 *   @Author    Henrik Olofsson
 *   @Date      2023-01-25
 *
 *   NOT USED!!!
 *   Initially tried to use this class to store date as Long variable, but didn't manage to get it to work :(
 */
public class DateConverter {

    @TypeConverter
    public static Date toDate(Long dateLong) {
        return dateLong == null ? null: new Date(dateLong);
    }

    @TypeConverter
    public static long fromDate(Date date) {
        return date == null ? null: date.getTime();
    }
}
