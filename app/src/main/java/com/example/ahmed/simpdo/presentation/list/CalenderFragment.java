package com.example.ahmed.simpdo.presentation.list;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TextView;

import com.applandeo.materialcalendarview.CalendarView;
import com.applandeo.materialcalendarview.EventDay;
import com.example.ahmed.simpdo.R;
import com.example.ahmed.simpdo.data.db.TaskDAO;
import com.example.ahmed.simpdo.data.model.AllTasks;
import com.example.ahmed.simpdo.data.model.Task;
import com.example.ahmed.simpdo.presentation.splash.SplashActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by ahmed on 9/15/17.
 */

public class CalenderFragment extends Fragment {
    private Unbinder unbinder;
    private List<Task> taskList;
    private static final String TAG = "CalenderFragment";

    @BindView(R.id.task_calender)
    CalendarView calendarView;

    @BindView(R.id.current_task_details)
    TextView currentTask;

    @Override
    public void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);
        AllTasks tasks = (AllTasks) getArguments().getSerializable
                (SplashActivity.taskList);
        if (tasks != null){
            taskList = tasks.getTaskList();
            Log.d(TAG, "Reading taskList from arguments");
        }else {
            TaskDAO dao = new TaskDAO(getActivity());
            dao.open();
            taskList = dao.getAllTasks();
            dao.close();
            Log.d(TAG, "Reading taskList from database directly");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent,
                             Bundle savedInstance){

        View view = inflater.inflate(R.layout.calender_layout, parent, false);

        unbinder = ButterKnife.bind(this, view);

        int today = Calendar.getInstance().get(Calendar.DAY_OF_YEAR);
        showTaskDetails(today);

        calendarView.setOnDayClickListener(eventDay -> {
            int day = eventDay.getCalendar().get(Calendar.DAY_OF_YEAR);
            showTaskDetails(day);
        });
        selectTaskDays();
        return view;
    }

    @Override
    public void onDestroy(){
        super.onDestroy();

        unbinder.unbind();
    }

    private void showTaskDetails(int day){
        StringBuilder taskString = new StringBuilder();
        for (Task task: taskList){
            int taskDay = task.getTaskDate().get(Calendar.DAY_OF_YEAR);
            if (day == taskDay){
                taskString.append(task.getTaskTitle());
                taskString.append("\n");
                taskString.append(getTaskTime(task.getTaskDate()));
                taskString.append("\n");
                taskString.append("\n");
            }
        }
        currentTask.setText(taskString.toString());
    }

    private void selectTaskDays(){
        List<EventDay> eventDays = new ArrayList<>();

        for (Task task : taskList) {
            Calendar taskCalender = task.getTaskDate();
            DatePicker picker = new DatePicker(getActivity());
            Calendar repeatCalender = Calendar.getInstance();
            repeatCalender.setTimeInMillis(picker.getMaxDate());
            Calendar todayCalender = Calendar.getInstance();

            int currentYear = taskCalender.get(Calendar.YEAR);
            int maxYear =  repeatCalender.get(Calendar.YEAR);

//            if (task.getRepeatCategory() == 0){
                eventDays.add(new EventDay(task.getTaskDate(), R.drawable.todo));
//            }
//            else {
//                switch (task.getRepeatCategory()){
//                    case 1:
//                        int currentWeek = taskCalender.get(Calendar.WEEK_OF_YEAR);
//                        int maxWeek = repeatCalender.getMaximum(Calendar.WEEK_OF_YEAR);
//
//                        while (currentYear < maxYear) {
//                            if (currentYear > todayCalender.get(Calendar.YEAR)) {
//                                currentWeek = repeatCalender.getMinimum(Calendar.WEEK_OF_YEAR) - 1;
//                            }
//                            taskCalender.set(Calendar.YEAR, currentYear);
//                            while (currentWeek < maxWeek) {
//                                taskCalender.set(Calendar.WEEK_OF_YEAR, ++currentWeek);
//                                task.setTaskDate(taskCalender);
////                                presenter.addTask(task);
////                                eventDays.add(new EventDay(task.getTaskDate(), R.drawable.todo));
//                                Log.d(TAG, "Adding for week - " + currentWeek);
//                            }
//                            currentYear++;
//                            Log.d(TAG, "Adding for year - " + currentYear);
//
//                        }  //weekly add makers for every week
//                        break;
//                    case 2:
//                        //monthly add markers for every month
//                        break;
//                    case 3:
//                        //yearly add markers every year
//                        break;
//                }
//            }
//            for (int a = 0; a < 2000; a++){
//                int some = taskCalender.get(Calendar.DAY_OF_YEAR);
//                taskCalender.set(Calendar.DAY_OF_YEAR, ++some);
//                eventDays.add(new EventDay(taskCalender, R.drawable.todo));
//                Log.d(TAG, "Adding new event day - " + some);
//            }
        }
        calendarView.setEvents(eventDays);
    }

    private String getTaskTime(Calendar calendar){
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.US);
        return sdf.format(calendar.getTime());
    }
}
