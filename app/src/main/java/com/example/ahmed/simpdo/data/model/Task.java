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
    private boolean isUrgent;

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

    public boolean isUrgent() {
        return isUrgent;
    }

    public void setUrgent(boolean urgent) {
        isUrgent = urgent;
    }

    @Override
    public String toString(){
        return "* Task Title: " + taskTitle + "\n"
                + "* Task Description: " + taskDesc + "\n"
                + "* Task Date: " + formatDate(taskDate) + "\n"
                + "* Task Category: " + category(isUrgent);
    }

    private String formatDate(Calendar calendar){
        SimpleDateFormat sdf = new SimpleDateFormat("EEE, MMM d, yyyy 'at' h:mm a",
                Locale.US);
        return sdf.format(calendar.getTime());
    }

    private String category(boolean isUrgent){
        return isUrgent ? "Important" : "Normal";
    }
}
