package com.example.ahmed.simpdo.presentation.notifications;

import android.app.Activity;
import android.app.Notification;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

/**
 * Created by ahmed on 9/8/17.
 */

public class DailyReceiver extends BroadcastReceiver {
    private static final String TAG = "DailyReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "Receive result with code - " + getResultCode());
        if (getResultCode() != Activity.RESULT_OK){
            return;
        }

        int reqCode = intent.getIntExtra(DailyService.DAILY_REQ_CODE, 4);
        Notification notification = intent.getParcelableExtra
                (DailyService.DAILY_NOTIFICATION);

        NotificationManagerCompat compat = NotificationManagerCompat.from(context);
        compat.notify(reqCode, notification);
    }
}
