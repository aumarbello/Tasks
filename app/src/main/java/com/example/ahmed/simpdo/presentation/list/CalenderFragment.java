package com.example.ahmed.simpdo.presentation.list;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.applandeo.materialcalendarview.CalendarView;
import com.applandeo.materialcalendarview.EventDay;
import com.example.ahmed.simpdo.R;
import com.example.ahmed.simpdo.data.db.TaskDAO;
import com.example.ahmed.simpdo.data.model.Task;

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

        TaskDAO dao = new TaskDAO(getActivity());
        dao.open();
        taskList = dao.getAllTasks();
        dao.close();
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
            eventDays.add(new EventDay(task.getTaskDate(), R.drawable.todo));
        }
        calendarView.setEvents(eventDays);
    }

    private String getTaskTime(Calendar calendar){
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.US);
        return sdf.format(calendar.getTime());
    }
}
