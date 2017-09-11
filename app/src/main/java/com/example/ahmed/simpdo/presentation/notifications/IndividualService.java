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

import com.example.ahmed.simpdo.App;
import com.example.ahmed.simpdo.R;
import com.example.ahmed.simpdo.data.db.TaskDAO;
import com.example.ahmed.simpdo.data.model.Task;
import com.example.ahmed.simpdo.data.pref.TaskPref;
import com.example.ahmed.simpdo.presentation.list.TaskListContainer;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by ahmed on 8/31/17.
 */

public class IndividualService extends IntentService {
    private static final String TAG = "Important Service";
    public static final String IMPORTANT_SERVICE =
            "com.example.ahmed.simpdo.presentation.notifications";
    public static final String PRIVATE =
            "com.example.ahmed.simpdo.presentation.notifications.PRIVATE";
    public static final String REQ_CODE = "REQUEST_CODE";
    public static final String NOTIFICATION = "NOTIFICATION";

    @Inject
    TaskPref pref;
    private StringBuilder dueTask;
    private int taskCount;

    public IndividualService() {
        super(TAG);
    }

    public static Intent getIntent(Context context){
        return new Intent(context, IndividualService.class);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        TaskDAO taskDAO = new TaskDAO(this);
        taskDAO.open();

        Log.d(TAG, "IndividualService started");
        ((App)getApplicationContext()).getComponent().inject(this);

        dueTask = new StringBuilder();

        Calendar today = Calendar.getInstance();
        int hour = today.get(Calendar.HOUR_OF_DAY);
        boolean showNotification = false;

        int category = pref.getNotificationCategory();
        int dayOfYear = today.get(Calendar.DAY_OF_YEAR);

        List<Task> taskList = taskDAO.getAllTasks();
        List<Task> todayTaskList = new ArrayList<>();

        for (Task task : taskList) {
            int taskDay = task.getTaskDate().get(Calendar.DAY_OF_YEAR);
            if (taskDay == dayOfYear){
                todayTaskList.add(task);
            }
        }

        for (Task task : todayTaskList) {
            int taskHour = task.getTaskDate().get(Calendar.HOUR_OF_DAY);

            switch (category){
                case 0:
                    if (hour == taskHour){
                        dueTask.append("Task ")
                                .append(task.getTaskTitle())
                                .append(" is Due.\n");
                        showNotification = true;
                        taskCount++;
                    }
                    break;
                case 1:
                    if (hour == taskHour && !task.isUrgent()){
                        dueTask.append("Task ")
                                .append(task.getTaskTitle())
                                .append(" is Due.\n");
                        showNotification = true;
                        taskCount++;
                    }
                    break;
                case 2:
                    if (hour == taskHour && task.isUrgent()){
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

        if (showNotification) {
            showAlarmNotification();
        }
    }

    public static void setTimeInterval(Context context, boolean isOn){
        Intent selfIntent = IndividualService.getIntent(context);
        PendingIntent pendingIntent = PendingIntent.getService(context, 0,
                selfIntent, 0);

        AlarmManager manager = (AlarmManager) context.getSystemService
                (Context.ALARM_SERVICE);

        if (isOn){
            manager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                    SystemClock.elapsedRealtime(), AlarmManager.INTERVAL_HOUR,
                    pendingIntent);
        }else {
            manager.cancel(pendingIntent);
            pendingIntent.cancel();
        }
    }

    public void showAlarmNotification(){
        int request = (int) System.currentTimeMillis();

        Intent i = new Intent(this, TaskListContainer.class);
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

        showNotificationInBackground(0, notification);
    }

    private void showNotificationInBackground(int requestCode, Notification notification) {
        Intent i = new Intent(IMPORTANT_SERVICE);
        i.putExtra(REQ_CODE, requestCode);
        i.putExtra(NOTIFICATION, notification);
        sendOrderedBroadcast(i, PRIVATE, null, null,
                Activity.RESULT_OK, null, null);
    }
}
