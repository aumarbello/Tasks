package com.example.ahmed.simpdo.presentation.list.calender;

import android.app.AlertDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;

import com.applandeo.materialcalendarview.CalendarView;
import com.applandeo.materialcalendarview.EventDay;
import com.example.ahmed.simpdo.App;
import com.example.ahmed.simpdo.R;
import com.example.ahmed.simpdo.data.db.TaskDAO;
import com.example.ahmed.simpdo.data.model.AllTasks;
import com.example.ahmed.simpdo.data.model.Task;
import com.example.ahmed.simpdo.data.pref.TaskPref;
import com.example.ahmed.simpdo.presentation.edit.EditTaskFragment;
import com.example.ahmed.simpdo.presentation.list.ItemActions;
import com.example.ahmed.simpdo.presentation.list.TaskViewHolder;
import com.example.ahmed.simpdo.presentation.splash.SplashActivity;
import com.example.ahmed.simpdo.presentation.view.ViewTaskFragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by ahmed on 9/15/17.
 */

public class CalenderFragment extends Fragment
        implements ItemActions,
        EditTaskFragment.CallBack,
        ViewTaskFragment.CallBack {
    private Unbinder unbinder;
    private List<Task> taskList;
    private static final String TAG = "CalenderFragment";
    private CalenderAdapter adapter;
    private EditTaskFragment editTaskFragment;
    private int position;
    private List<EventDay> eventDayList;
    private long timeInMillSecs;

    @Inject
    TaskPref pref;
    @Inject
    CalenderPresenter presenter;

    @BindView(R.id.task_calender)
    CalendarView calendarView;

    @BindView(R.id.current_task_details)
    RecyclerView currentTask;

    @Override
    public void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);
        ((App)getActivity().getApplication()).getComponent().inject(this);
        AllTasks tasks = (AllTasks) getArguments().getSerializable
                (SplashActivity.taskList);
        if (tasks != null){
            taskList = tasks.getTaskList();
            Log.d(TAG, "Reading taskList from arguments");
        }else {
            TaskDAO dao = new TaskDAO(getActivity());
            dao.open();
            taskList = dao.getAllNormalTasks();
            dao.close();
            Log.d(TAG, "Reading taskList from database directly");
        }
        DatePicker picker = new DatePicker(getActivity());
        timeInMillSecs = picker.getMaxDate();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent,
                             Bundle savedInstance){

        View view = inflater.inflate(R.layout.calender_layout, parent, false);

        unbinder = ButterKnife.bind(this, view);

        currentTask.setLayoutManager(new LinearLayoutManager(getActivity()));

        int today = Calendar.getInstance().get(Calendar.DAY_OF_YEAR);
        update(showTaskDetails(today));

        calendarView.setOnDayClickListener(eventDay -> {
            int day = eventDay.getCalendar().get(Calendar.DAY_OF_YEAR);
            update(showTaskDetails(day));
        });
        selectTaskDays();

        new OtherEvents().execute();
        return view;
    }

    @Override
    public void onDestroy(){
        super.onDestroy();

        unbinder.unbind();
    }

    private void update(List<Task> tasks){
        if (adapter == null){
            adapter = new CalenderAdapter(tasks);
            currentTask.setAdapter(adapter);
        }else {
            adapter.setTasks(tasks);
            currentTask.setAdapter(adapter);
        }
    }

    private List<Task> showTaskDetails(int day){
        List<Task> todayList = new ArrayList<>();
        for (Task task: taskList){
            int taskDay = task.getTaskDate().get(Calendar.DAY_OF_YEAR);
            if (day == taskDay){
                todayList.add(task);
            }
        }
        return todayList;
    }

    private void selectTaskDays(){
        eventDayList = new ArrayList<>();
        for (Task task : taskList) {
            Calendar taskCalender = task.getTaskDate();
            DatePicker picker = new DatePicker(getActivity());
            Calendar repeatCalender = Calendar.getInstance();
            repeatCalender.setTimeInMillis(picker.getMaxDate());
            Calendar todayCalender = Calendar.getInstance();

            int currentYear = taskCalender.get(Calendar.YEAR);
            int maxYear =  repeatCalender.get(Calendar.YEAR);

            eventDayList.add(new EventDay(task.getTaskDate(), R.drawable.todo));
        }
    }

    private String getTaskTime(Calendar calendar){
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.US);
        return sdf.format(calendar.getTime());
    }

    @Override
    public void viewTask(Task task, int position) {
        this.position = position;
        ViewTaskFragment viewTaskFragment = ViewTaskFragment.getInstance(task, position);
        viewTaskFragment.setTargetFragment(this, 11);
        viewTaskFragment.show(getFragmentManager(), "View");
    }

    @Override
    public void updateTask(Task task) {
        presenter.updateTask(task);

        updateView(task);
    }

    @Override
    public void editTask(Task task, int position) {
        this.position =  position;

        editTaskFragment = EditTaskFragment.getInstance(task);
        editTaskFragment.setTargetFragment(this, 22);
        editTaskFragment.show(getFragmentManager(), "Edit");
    }

    @Override
    public void deleteTask(Task task, int position) {
        if (task.getRepeatCategory() == 0){
            presenter.deleteTask(task, false);
            updateAfterDelete(task);
        }else {
            showDeleteAllDialog(task);
        }
    }

    private void showDeleteAllDialog(Task task) {
        AlertDialog dialog = new AlertDialog.Builder(getActivity())
                .setTitle("Delete All")
                .setMessage("Delete All Occurrence of this Task?")
                .setPositiveButton("No", (dialogInterface, i) -> {
                    dialogInterface.dismiss();
                    presenter.deleteTask(task, false);
                    updateAfterDelete(task);
                })
                .setNegativeButton("Yes", (dialogInterface, i) -> {
                    dialogInterface.dismiss();
                    presenter.deleteTask(task, true);
                    updateAfterDelete(task);
                })
                .create();
        dialog.show();
    }

    @Override
    public void updateView(Task task) {
        if (editTaskFragment != null){
            editTaskFragment.dismiss();
        }
        adapter.notifyItemChanged(position);
    }

    @Override
    public void updateAfterDelete(Task task) {
        adapter.removeTask(task);
        adapter.notifyItemRemoved(position);
    }

    private class CalenderAdapter extends RecyclerView.Adapter<TaskViewHolder>{
        private List<Task> tasks;

        private CalenderAdapter(List<Task> tasks){
            this.tasks = tasks;
        }

        @Override
        public TaskViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(CalenderFragment.this.getActivity());

            View view = inflater.inflate(R.layout.task_list_item, parent, false);
            return new TaskViewHolder(view, CalenderFragment.this,
                    CalenderFragment.this.getActivity(), tasks, pref
            );
        }

        @Override
        public void onBindViewHolder(TaskViewHolder holder, int position) {
            holder.bindTask(tasks.get(position), position);
        }

        @Override
        public int getItemCount() {
            return tasks.size();
        }

        private void setTasks(List<Task> tasks){
            this.tasks = tasks;
        }

        private void removeTask(Task task){
            tasks.remove(task);
        }
    }

    private class OtherEvents extends AsyncTask<Void, Void, List<EventDay>>{
        @Override
        protected List<EventDay> doInBackground(Void... voids) {
//            List<Task> tasks = new ArrayList<>(presenter.getWeeklyTasks());
            List<Task> tasks = new ArrayList<>();
            tasks.addAll(presenter.getMonthlyTasks());
            tasks.addAll(presenter.getYearlyTasks());

            taskList.addAll(tasks);

            for (Task task : tasks) {
                Calendar taskCalender = task.getTaskDate();
                Calendar repeatCalender = Calendar.getInstance();
                repeatCalender.setTimeInMillis(timeInMillSecs);
                Calendar todayCalender = Calendar.getInstance();

                int currentYear = taskCalender.get(Calendar.YEAR);
                int maxYear =  repeatCalender.get(Calendar.YEAR);

                eventDayList.add(new EventDay(task.getTaskDate(), R.drawable.todo));
            }
            return eventDayList;
        }
        @Override
        protected void onPostExecute(List<EventDay> eventDays){
            eventDayList.addAll(eventDays);
            calendarView.setEvents(eventDayList);
        }
    }
}
