package com.example.ahmed.simpdo.presentation.list;

import android.util.Log;

import com.example.ahmed.simpdo.data.model.Task;
import com.example.ahmed.simpdo.data.pref.TaskPref;

import java.util.Calendar;
import java.util.List;

/**
 * Created by ahmed on 9/6/17.
 */

class GetTaskTag {
    private TaskPref pref;
    private List<String> segmentList;

    GetTaskTag(TaskPref pref, List<String> segmentList){
        this.pref = pref;
        this.segmentList = segmentList;
    }

    String getTaskSegment(Task task){
        int daysSegment = pref.getDaysSection();
        switch (daysSegment){
            case 0:
                return threeDaySegment(task);
            case 1:
                return fiveDaySegment(task);
            case 2:
                return sevenDaySegment(task);
            default:
                Log.d(getClass().getSimpleName(), "Could not resolve task - "
                        + task + " - for days segment - " + daysSegment);
                return "Previous";
        }
    }

    private String threeDaySegment(Task task){
        int today = Calendar.getInstance().get(Calendar.DAY_OF_YEAR);
        int taskDay = task.getTaskDate().get(Calendar.DAY_OF_YEAR);

        if (taskDay == today){
            return segmentList.get(0);
        }else if (taskDay == today + 1){
            return segmentList.get(1);
        }else if (taskDay == today + 2){
            return segmentList.get(2);
        }else if (taskDay > today + 2){
            return "Others";
        }else if (taskDay < today){
            return "Previous";
        }
        return "Others";
    }

    private String fiveDaySegment(Task task){
        int today = Calendar.getInstance().get(Calendar.DAY_OF_YEAR);
        int taskDay = task.getTaskDate().get(Calendar.DAY_OF_YEAR);

        if (taskDay == today){
            return segmentList.get(0);
        }else if (taskDay == today + 1){
            return segmentList.get(1);
        }else if (taskDay == today + 2){
            return segmentList.get(2);
        }else if (taskDay == today + 3){
            return segmentList.get(3);
        }else if (taskDay == today + 4){
            return segmentList.get(4);
        }else if (taskDay > today + 4){
            return "Others";
        }else if (taskDay < today){
            return "Previous";
        }
        return "Others";
    }

    private String sevenDaySegment(Task task){
        int today = Calendar.getInstance().get(Calendar.DAY_OF_YEAR);
        int taskDay = task.getTaskDate().get(Calendar.DAY_OF_YEAR);

        if (taskDay == today){
            return segmentList.get(0);
        }else if (taskDay == today + 1){
            return segmentList.get(1);
        }else if (taskDay == today + 2){
            return segmentList.get(2);
        }else if (taskDay == today + 3){
            return segmentList.get(3);
        }else if (taskDay == today + 4){
            return segmentList.get(4);
        }else if (taskDay == today + 5){
            return segmentList.get(5);
        }else if (taskDay == today + 6){
            return segmentList.get(6);
        }else if (taskDay > today + 6){
            return "Others";
        }else if (taskDay < today){
            return "Previous";
        }
        return "Others";
    }
}
