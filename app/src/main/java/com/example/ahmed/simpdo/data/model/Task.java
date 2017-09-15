package com.example.ahmed.simpdo.data.model;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.UUID;

/**
 * Created by ahmed on 8/24/17.
 */

public class Task implements Serializable{
    private UUID taskID;
    private String taskTitle;
    private String taskDesc;
    private Calendar taskDate;
    private int repeatCategory;
    private int alarmTime;
    private boolean isDone;

    public Task(){
        taskID = UUID.randomUUID();
    }

    Task(UUID taskID){
        this.taskID = taskID;
    }


    public UUID getTaskID() {
        return taskID;
    }

    public String getTaskTitle() {
        return taskTitle;
    }

    public void setTaskTitle(String taskTitle) {
        this.taskTitle = taskTitle;
    }

    public String getTaskDesc() {
        return taskDesc;
    }

    public void setTaskDesc(String taskDesc) {
        this.taskDesc = taskDesc;
    }

    public Calendar getTaskDate() {
        return taskDate;
    }

    public void setTaskDate(Calendar taskDate) {
        this.taskDate = taskDate;
    }

    @Override
    public String toString(){
        return "Task Title: " + taskTitle + "\n"
                + "Task Description: " + taskDesc + "\n"
                + "Task Date: " + formatDate(taskDate);
    }

    private String formatDate(Calendar calendar){
        SimpleDateFormat sdf = new SimpleDateFormat("EEE, MMM d, yyyy 'at' h:mm a",
                Locale.US);
        return sdf.format(calendar.getTime());
    }

    public boolean isDone() {
        return isDone;
    }

    public void setDone(boolean done) {
        isDone = done;
    }

    public int getRepeatCategory() {
        return repeatCategory;
    }

    public void setRepeatCategory(int repeatCategory) {
        this.repeatCategory = repeatCategory;
    }

    public int getAlarmTime() {
        return alarmTime;
    }

    public void setAlarmTime(int alarmTime) {
        this.alarmTime = alarmTime;
    }
}
