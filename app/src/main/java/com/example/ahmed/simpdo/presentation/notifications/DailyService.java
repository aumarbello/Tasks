package com.example.ahmed.simpdo.presentation.notifications;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.IntentService;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import com.example.ahmed.simpdo.R;
import com.example.ahmed.simpdo.data.db.TaskDAO;
import com.example.ahmed.simpdo.data.model.Task;
import com.example.ahmed.simpdo.presentation.TaskContainer;

import java.util.Calendar;
import java.util.List;

/**
 * Created by ahmed on 8/31/17.
 */

public class DailyService extends IntentService {
    private static final String TAG = "DailyService";
    private String todayTasks;
    public static final String DAILY_SERVICE =
            "com.example.ahmed.simpdo.presentation.notifications.dailyService";
    public static final String DAILY_PRIVATE =
            "com.example.ahmed.simpdo.presentation.notifications.dailyService.DAILY_PRIVATE";
    public static final String DAILY_REQ_CODE = "DAILY_REQUEST_CODE";
    public static final String DAILY_NOTIFICATION = "DAILY_NOTIFICATION";

    public DailyService() {
        super(TAG);
    }

    public static Intent getIntent(Context context){
        return new Intent(context, DailyService.class);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        TaskDAO taskDAO = new TaskDAO(this);
        taskDAO.open();

        Log.d(TAG, "DailyService started");

        List<Task> taskList = taskDAO.getAllTasks();

        int today = Calendar.getInstance().get(Calendar.DAY_OF_YEAR);
        int taskCount = 0;

        for(Task task: taskList){
            int taskDay = task.getTaskDate().get(Calendar.DAY_OF_YEAR);
            if (today == taskDay){
                taskCount++;
            }
        }
        taskDAO.close();

        todayTasks = taskCount == 0 ? "You have no tasks today"
                : "You have " + taskCount + " tasks today";

        showAlarmNotification(taskCount);
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
                    SystemClock.elapsedRealtime(), calendar.getTimeInMillis(),
                    pendingIntent);
        }else {
            manager.cancel(pendingIntent);
            pendingIntent.cancel();
        }
    }


    public static boolean isAlarmOn(Context context){
        Intent intent = getIntent(context);
        PendingIntent pendingIntent = PendingIntent.getService(
                context, 0, intent, PendingIntent.FLAG_NO_CREATE
        );
        return pendingIntent != null;
    }

    public void showAlarmNotification(int taskCount){
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
                .setNumber(taskCount)
                .setShowWhen(true)
                .build();
        notification.when = Calendar.getInstance().getTimeInMillis();

        showNotificationInBackground(4, notification);
    }

    private void showNotificationInBackground(int requestCode, Notification notification) {
        Intent i = new Intent(DAILY_SERVICE);
        i.putExtra(DAILY_REQ_CODE, requestCode);
        i.putExtra(DAILY_NOTIFICATION, notification);
        sendOrderedBroadcast(i, DAILY_PRIVATE, null, null,
                Activity.RESULT_OK, null, null);
    }
}
