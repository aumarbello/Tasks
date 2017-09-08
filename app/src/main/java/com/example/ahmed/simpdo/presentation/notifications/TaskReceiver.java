package com.example.ahmed.simpdo.presentation.notifications;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by ahmed on 9/8/17.
 */

public class TaskReceiver extends BroadcastReceiver {
    private static final String TAG = "TaskReceiver";
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "Received intent - " + intent);

        DailyService.setTimeInterval(context);
        ImportantService.setTimeInterval(context);
    }
}
