package com.example.ahmed.simpdo.data.pref;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.preference.PreferenceManager;

import com.example.ahmed.simpdo.R;

import javax.inject.Inject;

/**
 * Created by ahmed on 9/5/17.
 */

public class TaskPref {
    private SharedPreferences sharedPreferences;
    private Resources resources;

    @Inject
    TaskPref(Context context){
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        resources = context.getResources();
    }

    public boolean isPreviousTaskShown(){
        return sharedPreferences.getBoolean(resources.getString
                (R.string.key_show_previous_tasks), true);
    }

    public int getDaysSection(){
        String dayString = sharedPreferences.getString(resources.getString
                (R.string.number_of_days_key), "3");
        return Integer.parseInt(dayString);
    }

    public String getNotificationCategory(){
        return sharedPreferences.getString(resources.getString
                (R.string.show_notification_key), "Important");
    }

    public boolean isDaysColorRandom(){
        return sharedPreferences.getBoolean(resources.getString
                (R.string.days_color_key), true);
    }

    //todo color for each day header

    public int getDoneTaskColor(){
        return sharedPreferences.getInt(resources.getString
                (R.string.done_select_color_key), Color.GRAY);
    }
}
