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
    private boolean isAlreadyRepeating;

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
                + "Task Date: " + formatDate(taskDate) + "\n"
                + "Alarm Time: " + getAlarmString(alarmTime) + "\n"
                + getRepeatString(repeatCategory);
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


    public boolean isAlreadyRepeating() {
        return isAlreadyRepeating;
    }

    public void setAlreadyRepeating(boolean alreadyRepeating) {
        isAlreadyRepeating = alreadyRepeating;
    }

    private String getAlarmString(int category){
        switch (category){
            case 1:
                return "Fifteen Minutes before Task";
            case 2:
                return "Thirty Minutes before Task";
            case 3:
                return "Forty Five Minutes before Task";
            case 4:
                return "One hour before Task";
            default:
                return "On Task\'s time";
        }
    }

    private String getRepeatString(int category){
        switch (category){
            case 1:
                return "Repeats: Weekly";
            case 2:
                return "Repeats: Monthly";
            case 3:
                return "Repeats: Yearly";
            default:
                return "Task does not Repeat";
        }

    }

    @Override
    public boolean equals(Object object){
        if ((object == null) || (object.getClass() != Task.class))
            return false;
        Task task = (Task) object;
        return taskID.equals(task.getTaskID())
                && taskTitle.equals(task.getTaskTitle())
                && taskDate.equals(task.getTaskDate());
    }
}
