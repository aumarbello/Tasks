package com.example.ahmed.simpdo.presentation.list;

import android.util.Log;

import com.example.ahmed.simpdo.data.model.Task;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by ahmed on 9/6/17.
 */

public class GetTaskList {
    private static final String TAG = "GetTaskList";
    private int section;
    private List<Task> taskList;

    public GetTaskList(int section, List<Task> taskList){
        this.section = section;
        this.taskList = taskList;
    }

    List<Task> getTasks(String segment, List<String> dayString){
        switch (section){
            case 0:
                return threeGetTasks(segment, dayString);
            case 1:
                return fiveGetTasks(segment, dayString);
            case 2:
                return sevenGetTasks(segment, dayString);
        }
        Log.d(TAG, "Unable to resolve section parameter");
        return new ArrayList<>();
    }

    private List<Task> threeGetTasks(String segment, List<String> dayString){
        Log.d(TAG, "Entered get three tasks");
        List<Task> returnTasks = new ArrayList<>();
        Calendar today = Calendar.getInstance();
        int currentDay = today.get(Calendar.DAY_OF_YEAR);

        if (segment.equals(dayString.get(0))) {
            for (Task task : taskList) {
                int currentTaskDay = task.getTaskDate().get(Calendar.DAY_OF_YEAR);
                if (currentTaskDay == currentDay){
                    returnTasks.add(task);
                }
            }
        }else if (segment.equals(dayString.get(1))){
            for (Task task : taskList) {
                int currentTaskDay = task.getTaskDate().get(Calendar.DAY_OF_YEAR);
                if (currentTaskDay == currentDay + 1){
                    returnTasks.add(task);
                }
            }
        }else if (segment.equals(dayString.get(2))){
            for (Task task : taskList) {
                int currentTaskDay = task.getTaskDate().get(Calendar.DAY_OF_YEAR);
                if (currentTaskDay == currentDay + 2){
                    returnTasks.add(task);
                }
            }
        }else if (segment.equals("Others")){
            for (Task task : taskList) {
                int currentTaskDay = task.getTaskDate().get(Calendar.DAY_OF_YEAR);
                if (currentTaskDay > currentDay + 2){
                    returnTasks.add(task);
                }
            }
        }else if (segment.equals("Previous")){
            for (Task task : taskList) {
                int currentTaskDay = task.getTaskDate().get(Calendar.DAY_OF_YEAR);
                if (currentTaskDay < currentDay){
                    returnTasks.add(task);
                }
            }
        }
        return returnTasks;
    }

    private List<Task> fiveGetTasks(String segment, List<String> dayString){
        Log.d(TAG, "Entered get five tasks");
        List<Task> returnTasks = new ArrayList<>();
        Calendar today = Calendar.getInstance();
        int currentDay = today.get(Calendar.DAY_OF_YEAR);
        if (segment.equals(dayString.get(0))) {
            for (Task task : taskList) {
                int currentTaskDay = task.getTaskDate().get(Calendar.DAY_OF_YEAR);
                if (currentTaskDay == currentDay){
                    returnTasks.add(task);
                }
            }
        }else if (segment.equals(dayString.get(1))){
            for (Task task : taskList) {
                int currentTaskDay = task.getTaskDate().get(Calendar.DAY_OF_YEAR);
                if (currentTaskDay == currentDay + 1){
                    returnTasks.add(task);
                }
            }
        }else if (segment.equals(dayString.get(2))){
            for (Task task : taskList) {
                int currentTaskDay = task.getTaskDate().get(Calendar.DAY_OF_YEAR);
                if (currentTaskDay == currentDay + 2){
                    returnTasks.add(task);
                }
            }
        }else if (segment.equals(dayString.get(3))){
            for (Task task : taskList) {
                int currentTaskDay = task.getTaskDate().get(Calendar.DAY_OF_YEAR);
                if (currentTaskDay == currentDay + 3){
                    returnTasks.add(task);
                }
            }
        }else if (segment.equals(dayString.get(4))){
            for (Task task : taskList) {
                int currentTaskDay = task.getTaskDate().get(Calendar.DAY_OF_YEAR);
                if (currentTaskDay == currentDay + 4){
                    returnTasks.add(task);
                }
            }
        }else if (segment.equals("Others")){
            for (Task task : taskList) {
                int currentTaskDay = task.getTaskDate().get(Calendar.DAY_OF_YEAR);
                if (currentTaskDay > currentDay + 4){
                    returnTasks.add(task);
                }
            }
        }else if (segment.equals("Previous")){
            for (Task task : taskList) {
                int currentTaskDay = task.getTaskDate().get(Calendar.DAY_OF_YEAR);
                if (currentTaskDay < currentDay){
                    returnTasks.add(task);
                }
            }
        }
        return returnTasks;
    }

    private List<Task> sevenGetTasks(String segment, List<String> dayString){
        Log.d(TAG, "Entered get seven tasks");
        List<Task> returnTasks = new ArrayList<>();
        Calendar today = Calendar.getInstance();
        int currentDay = today.get(Calendar.DAY_OF_YEAR);
        if (segment.equals(dayString.get(0))) {
            for (Task task : taskList) {
                int currentTaskDay = task.getTaskDate().get(Calendar.DAY_OF_YEAR);
                if (currentTaskDay == currentDay){
                    returnTasks.add(task);
                }
            }
        }else if (segment.equals(dayString.get(1))){
            for (Task task : taskList) {
                int currentTaskDay = task.getTaskDate().get(Calendar.DAY_OF_YEAR);
                if (currentTaskDay == currentDay + 1){
                    returnTasks.add(task);
                }
            }
        }else if (segment.equals(dayString.get(2))){
            for (Task task : taskList) {
                int currentTaskDay = task.getTaskDate().get(Calendar.DAY_OF_YEAR);
                if (currentTaskDay == currentDay + 2){
                    returnTasks.add(task);
                }
            }
        }else if (segment.equals(dayString.get(3))){
            for (Task task : taskList) {
                int currentTaskDay = task.getTaskDate().get(Calendar.DAY_OF_YEAR);
                if (currentTaskDay == currentDay + 3){
                    returnTasks.add(task);
                }
            }
        }else if (segment.equals(dayString.get(4))){
            for (Task task : taskList) {
                int currentTaskDay = task.getTaskDate().get(Calendar.DAY_OF_YEAR);
                if (currentTaskDay == currentDay + 4){
                    returnTasks.add(task);
                }
            }
        }else if (segment.equals(dayString.get(5))){
            for (Task task : taskList) {
                int currentTaskDay = task.getTaskDate().get(Calendar.DAY_OF_YEAR);
                if (currentTaskDay == currentDay + 5){
                    returnTasks.add(task);
                }
            }
        }else if (segment.equals(dayString.get(6))){
            for (Task task : taskList) {
                int currentTaskDay = task.getTaskDate().get(Calendar.DAY_OF_YEAR);
                if (currentTaskDay == currentDay + 6){
                    returnTasks.add(task);
                }
            }
        }else if (segment.equals("Others")){
            for (Task task : taskList) {
                int currentTaskDay = task.getTaskDate().get(Calendar.DAY_OF_YEAR);
                if (currentTaskDay > currentDay + 6){
                    returnTasks.add(task);
                }
            }
        }else if (segment.equals("Previous")){
            for (Task task : taskList) {
                int currentTaskDay = task.getTaskDate().get(Calendar.DAY_OF_YEAR);
                if (currentTaskDay < currentDay){
                    returnTasks.add(task);
                }
            }
        }
        return returnTasks;
    }
}
