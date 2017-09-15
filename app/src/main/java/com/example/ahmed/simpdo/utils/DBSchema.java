package com.example.ahmed.simpdo.utils;

/**
 * Created by ahmed on 8/24/17.
 */

public final class DBSchema {
    public static final String TASK_TABLE = "TASK";

    public static final String TASK_ID = "ID";
    public static final String TASK_TITLE = "TITLE";
    public static final String TASK_DESC = "DESCRIPTION";
    public static final String TASK_DATE = "DATE";
    public static final String TASK_IMPORTANT = "IMPORTANT";
    public static final String TASK_DONE = "DONE";
    public static final String TASK_ALARM_TIME = "ALARM_TIME";
    public static final String TASK_REPEAT = "REPEAT_TASK";

    public static final String CREATE_TABLE = "create table if not exists " + TASK_TABLE + "(" +
            " _id integer primary key autoincrement, " +

            TASK_ID + " text, " +
            TASK_TITLE + " text, " +
            TASK_DESC + " text, " +
            TASK_IMPORTANT + " integer, " +
            TASK_DONE + " integer, " +
            TASK_DATE + " long, " +
            TASK_ALARM_TIME + " integer, " +
            TASK_REPEAT + " integer" +
            ")";

    public static final String DROP_TABLE = "DROP TABLE IF EXISTS " + TASK_TABLE;
}
