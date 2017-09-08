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
import com.example.ahmed.simpdo.data.pref.TaskPref;
import com.example.ahmed.simpdo.presentation.TaskContainer;

import java.util.Calendar;

import javax.inject.Inject;

/**
 * Created by ahmed on 8/31/17.
 */

public class ImportantService extends IntentService {
    private static final String TAG = "Important Service";

    @Inject
    TaskPref pref;
    private StringBuilder dueTask;
    private int taskCount;

    public ImportantService() {
        super(TAG);
    }

    public static Intent getIntent(Context context){
        return new Intent(context, ImportantService.class);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        TaskDAO taskDAO = new TaskDAO(this);
        taskDAO.open();
        Log.d(TAG, "ImportantService started");
        ((App)getApplicationContext()).getComponent().inject(this);

        Calendar today = Calendar.getInstance();
        int hour = today.get(Calendar.HOUR_OF_DAY);
        boolean showNotification = false;

        String category = pref.getNotificationCategory();

        for (Task task : taskDAO.getAllTasks()) {
            int taskHour = task.getTaskDate().get(Calendar.HOUR_OF_DAY);

            switch (category){
                case "Important":
                    if (hour == taskHour && task.isUrgent()){
                        dueTask.append("Task ")
                                .append(task.getTaskTitle())
                                .append(" is Due.\n");
                        showNotification = true;
                        taskCount++;
                    }
                    break;
                case "Normal":
                    if (hour == taskHour && !task.isUrgent()){
                        dueTask.append("Task ")
                                .append(task.getTaskTitle())
                                .append(" is Due.\n");
                        showNotification = true;
                        taskCount++;
                    }
                    break;
                case "All":
                    if (hour == taskHour){
                        dueTask.append("Task ")
                                .append(task.getTaskTitle())
                                .append(" is Due.\n");
                        showNotification = true;
                        taskCount++;
                    }
                    break;
            }
        }
        taskDAO.close();

        if (showNotification){
            showAlarmNotification();
        }
    }

    public static void setTimeInterval(Context context){
        Intent selfIntent = ImportantService.getIntent(context);
        PendingIntent pendingIntent = PendingIntent.getService(context, 0,
                selfIntent, 0);

        AlarmManager manager = (AlarmManager) context.getSystemService
                (Context.ALARM_SERVICE);

        if (isAlarmOn(context)){
            manager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                    SystemClock.elapsedRealtime(), 60000,
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

    public void showAlarmNotification(){
        int request = (int) System.currentTimeMillis();

        Intent i = new Intent(this, TaskContainer.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pI = PendingIntent.getActivity(this, request, i,
                PendingIntent.FLAG_UPDATE_CURRENT);

        Notification notification = new NotificationCompat.Builder(this)
                .setTicker("Task is Due")
                .setContentText(dueTask.toString())
                .setContentTitle("A Task is Due")
                .setSmallIcon(R.drawable.task)
                .setContentIntent(pI)
                .setAutoCancel(true)
                .setShowWhen(true)
                .setNumber(taskCount)
                .build();

        notification.when = Calendar.getInstance().getTimeInMillis();

        NotificationManagerCompat managerCompat = NotificationManagerCompat
                .from(this);
        managerCompat.notify(11011, notification);
    }
}
