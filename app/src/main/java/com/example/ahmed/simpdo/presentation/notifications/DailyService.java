package com.example.ahmed.simpdo.presentation.notifications;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import com.example.ahmed.simpdo.App;
import com.example.ahmed.simpdo.R;
import com.example.ahmed.simpdo.data.db.TaskDAO;
import com.example.ahmed.simpdo.data.model.Task;
import com.example.ahmed.simpdo.presentation.TaskContainer;

import java.util.Calendar;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by ahmed on 8/31/17.
 */

public class DailyService extends IntentService {
    //todo add to manifest
    private static final String TAG = "DailyService";
    @Inject
    TaskDAO taskDAO;
    private String todayTasks;
    public DailyService() {
        super(TAG);
    }

    public static Intent getIntent(Context context){
        return new Intent(context, DailyService.class);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Log.d(TAG, "DailyService started");
        ((App)getApplicationContext()).getComponent().inject(this);
        List<Task> taskList = taskDAO.getAllTasks();


        todayTasks = taskList.isEmpty() ? "You have no tasks today"
                : "You have " + taskList.size() + " tasks today";

        showAlarmNotification();
    }

    public static void setTimeInterval(Context context){
        Intent selfIntent = getIntent(context);
        PendingIntent pendingIntent = PendingIntent.getService(context, 0,
                selfIntent, 0);

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH) + 1);
        calendar.set(Calendar.HOUR_OF_DAY, 7);
        calendar.set(Calendar.MINUTE, 30);

        AlarmManager manager = (AlarmManager) context.getSystemService
                (Context.ALARM_SERVICE);

        if (isAlarmOn(context)){
            manager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                    SystemClock.elapsedRealtime(), 3000,
                    pendingIntent);
        }else {
            manager.cancel(pendingIntent);
            pendingIntent.cancel();
        }
    }


    public static boolean isAlarmOn(Context context){
        Intent intent = getIntent(context);
        PendingIntent pendingIntent = PendingIntent.getService(
                context, 1, intent, PendingIntent.FLAG_NO_CREATE
        );
        return pendingIntent != null;
    }

    public void showAlarmNotification(){
        int request = (int) System.currentTimeMillis();

        Intent i = new Intent(this, TaskContainer.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pI = PendingIntent.getActivity(this, request, i,
                PendingIntent.FLAG_UPDATE_CURRENT);

        Notification notification = new NotificationCompat.Builder(this)
                .setTicker("Today Tasks")
                .setContentText(todayTasks)
                .setContentTitle("Today's Task")
                .setSmallIcon(R.drawable.task)
                .setContentIntent(pI)
                .setAutoCancel(true)
                .build();

            NotificationManagerCompat managerCompat = NotificationManagerCompat
                    .from(this);
        managerCompat.notify(11011, notification);
    }
}
