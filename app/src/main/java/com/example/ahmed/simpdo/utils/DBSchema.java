package com.example.ahmed.simpdo.utils;

/**
 * Created by ahmed on 8/24/17.
 */

public final class DBSchema {
    public static final String NORMAL_TASKS_TABLE = "TASK";
    public static final String WEEKLY_TASKS_TABLE = "WEEKLY";
    public static final String MONTHLY_TASKS_TABLE = "MONTHLY";
    public static final String YEARLY_TASKS_TABLE = "YEARLY";

    public static final String TASK_ID = "ID";
    public static final String TASK_TITLE = "TITLE";
    public static final String TASK_DESC = "DESCRIPTION";
    public static final String TASK_DATE = "DATE";
    public static final String TASK_DONE = "DONE";
    public static final String TASK_ALARM_TIME = "ALARM_TIME";
    public static final String TASK_REPEAT = "REPEAT_TASK";
    public static final String TASK_IS_REPEATING = "REPEATING";

    public static final String CREATE_NORMAL_TABLE = "create table if not exists " + NORMAL_TASKS_TABLE + "(" +
            " _id integer primary key autoincrement, " +
            TASK_ID + " text, " +
            TASK_TITLE + " text, " +
            TASK_DESC + " text, " +
            TASK_DONE + " integer, " +
            TASK_DATE + " long, " +
            TASK_ALARM_TIME + " integer, " +
            TASK_IS_REPEATING + " integer, " +
            TASK_REPEAT + " integer" +
            ")";


    public static final String CREATE_WEEKLY_TABLE = "create table if not exists " + WEEKLY_TASKS_TABLE + "(" +
            " _id integer primary key autoincrement, " +
            TASK_ID + " text, " +
            TASK_TITLE + " text, " +
            TASK_DESC + " text, " +
            TASK_DONE + " integer, " +
            TASK_DATE + " long, " +
            TASK_ALARM_TIME + " integer, " +
            TASK_IS_REPEATING + " integer, " +
            TASK_REPEAT + " integer" +
            ")";

    public static final String CREATE_MONTHLY_TABLE = "create table if not exists " + MONTHLY_TASKS_TABLE + "(" +
            " _id integer primary key autoincrement, " +
            TASK_ID + " text, " +
            TASK_TITLE + " text, " +
            TASK_DESC + " text, " +
            TASK_DONE + " integer, " +
            TASK_DATE + " long, " +
            TASK_ALARM_TIME + " integer, " +
            TASK_IS_REPEATING + " integer, " +
            TASK_REPEAT + " integer" +
            ")";

    public static final String CREATE_YEARLY_TABLE = "create table if not exists " + YEARLY_TASKS_TABLE + "(" +
            " _id integer primary key autoincrement, " +
            TASK_ID + " text, " +
            TASK_TITLE + " text, " +
            TASK_DESC + " text, " +
            TASK_DONE + " integer, " +
            TASK_DATE + " long, " +
            TASK_ALARM_TIME + " integer, " +
            TASK_IS_REPEATING + " integer, " +
            TASK_REPEAT + " integer" +
            ")";

    public static final String DROP_NORMAL_TABLE = "DROP TABLE IF EXISTS " + NORMAL_TASKS_TABLE;
    public static final String DROP_WEEKLY_TABLE = "DROP TABLE IF EXISTS " + WEEKLY_TASKS_TABLE;
    public static final String DROP_MONTHLY_TABLE = "DROP TABLE IF EXISTS " + MONTHLY_TASKS_TABLE;
    public static final String DROP_YEARLY_TABLE = "DROP TABLE IF EXISTS " + YEARLY_TASKS_TABLE;
}
