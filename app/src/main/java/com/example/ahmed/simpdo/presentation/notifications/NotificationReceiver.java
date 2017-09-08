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

public class NotificationReceiver extends BroadcastReceiver {
    private static final String TAG = "NotificationReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "Receive result with code - " + getResultCode());
        if (getResultCode() != Activity.RESULT_OK){
            return;
        }

        int reqCode = intent.getIntExtra(ImportantService.REQ_CODE, 0);
        Notification notification = intent.getParcelableExtra(ImportantService.NOTIFICATION);

        NotificationManagerCompat compat = NotificationManagerCompat.from(context);
        compat.notify(reqCode, notification);
    }
}
