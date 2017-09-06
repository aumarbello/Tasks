package com.example.ahmed.simpdo.presentation.list;


import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.ahmed.simpdo.App;
import com.example.ahmed.simpdo.R;
import com.example.ahmed.simpdo.data.model.Task;
import com.example.ahmed.simpdo.data.pref.TaskPref;
import com.example.ahmed.simpdo.presentation.edit.EditTaskFragment;
import com.example.ahmed.simpdo.presentation.settings.SettingsActivity;
import com.example.ahmed.simpdo.presentation.view.ViewTaskFragment;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter;

/**
 * Created by ahmed on 8/24/17.
 */

public class TaskListFragment extends Fragment implements
        DetailsDialog.DetailsCallBack,
        ViewTaskFragment.CallBack,
        EditTaskFragment.CallBack{

    @Inject
    TaskListPresenter presenter;

    @Inject
    TaskPref preferences;

    @Inject
    GetTaskList getTaskList;

    private Unbinder unbinder;
    private List<String> taskSegments;
    private SectionedRecyclerViewAdapter adapter;
    private Calendar todayCalender;
    private Calendar taskCalender;
    private DatePickerDialog dateDialog;
    private TimePickerDialog timeDialog;
    private DetailsDialog detailsDialog;
    private ViewTaskFragment viewDialog;
    private EditTaskFragment editDialog;
    private int position;
    private final static String TAG = "TaskList";
    private int daysSectionValue;

    @BindView(R.id.task_list)
    RecyclerView taskListView;

    @BindView(R.id.fab)
    FloatingActionButton addTask;

    @Override
    public void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);
        setHasOptionsMenu(true);

        ((App)getActivity().getApplication()).getComponent().inject(this);
        int numberOfDaysPref = preferences.getDaysSection();
        taskSegments = new ArrayList<>();

        if (preferences.isPreviousTaskShown()){
            taskSegments.add("Previous");
        }

        todayCalender = Calendar.getInstance();
        taskCalender = Calendar.getInstance();

        int currentDay = todayCalender.get(Calendar.DAY_OF_WEEK);
        switch (numberOfDaysPref){
            case 0:
                for (int a = 1; a <= 3; a++){
                    if (currentDay != 7){
                        taskSegments.add(getDayString(currentDay++ % 7));
                    }else {
                        taskSegments.add(getDayString(currentDay++));
                    }
                }
                daysSectionValue = 3;
                break;
            case 1:
                for (int a = 1; a <= 5; a++){
                    if (currentDay != 7){
                        taskSegments.add(getDayString(currentDay++ % 7));
                    }else {
                        taskSegments.add(getDayString(currentDay++));
                    }
                }
                daysSectionValue = 5;
                break;
            case 2:
                for (int a = 1; a <= 7; a++){
                    if (currentDay != 7){
                        taskSegments.add(getDayString(currentDay++ % 7));
                    }else {
                        taskSegments.add(getDayString(currentDay++));
                    }
                }
                daysSectionValue = 7;
                break;
        }
        taskSegments.add("Others");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent,
                             Bundle savedInstance){
        View view = inflater.inflate(R.layout.task_list, parent, false);

        unbinder = ButterKnife.bind(this, view);

        addTask.setOnClickListener(fab -> showCalender());

        taskListView.setLayoutManager(new LinearLayoutManager(getActivity()));
        update();

        return view;
    }

    @Override
    public void onDestroyView(){
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){
        inflater.inflate(R.menu.list_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.settings:
                startActivity(new Intent(getActivity(), SettingsActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void viewTask(Task task) {
        viewDialog = ViewTaskFragment.getInstance(task);
        viewDialog.setTargetFragment(this, 101111);
        viewDialog.show(getFragmentManager(), "View Fragment");
    }

    public List<Task> getDayTask(String segment){
        int day = todayCalender.get(Calendar.DAY_OF_YEAR);
        int dayInWeek = todayCalender.get(Calendar.DAY_OF_WEEK);

        List<String> dayString = new ArrayList<>();
        for (int a = 0; a <= daysSectionValue - 1; a++){
            if (dayInWeek != 7){
                dayString.add(getDayString(dayInWeek++ % 7));
            }else {
                dayString.add(getDayString(dayInWeek++));
            }
        }

        if (preferences.isPreviousTaskShown()){
            dayString.add("Previous");
        }
        dayString.add("Others");

        return getTaskList.getTasks(segment, dayString);
    }

    //view task fragment call back
    //also called from list menu
    @Override
    public void editTask(Task task){
        if (viewDialog != null) {
            viewDialog.dismiss();
        }

        editDialog = EditTaskFragment.getInstance(task);
        editDialog.setTargetFragment(this, 1000111);
        editDialog.show(getFragmentManager(), "Edit Task");
    }

    public void deleteTask(Task task, int position){
        this.position = position;
        presenter.deleteTask(task);
    }

    private void showCalender(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            dateDialog = new DatePickerDialog(getActivity(),
                    android.R.style.Theme_Material_Light_Dialog_Alert,
                    (datePicker, year, month, day) -> {
                        taskCalender.set(year, month, day);
                        showTimePicker();
                    },
                    todayCalender.get(Calendar.YEAR),
                    todayCalender.get(Calendar.MONTH),
                    todayCalender.get(Calendar.DAY_OF_MONTH));
        }else {
            dateDialog = new DatePickerDialog(getActivity(),
                    (datePicker, year, month, day) -> {
                        taskCalender.set(year, month, day);
                        showTimePicker();
                    },
                    todayCalender.get(Calendar.YEAR),
                    todayCalender.get(Calendar.MONTH),
                    todayCalender.get(Calendar.DAY_OF_MONTH));
        }
        dateDialog.setTitle("Select Task Date");
        dateDialog.show();
    }

    private void showTimePicker() {
        dateDialog.dismiss();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            timeDialog = new TimePickerDialog(getActivity(),
                    android.R.style.Theme_Material_Light_Dialog_Alert,
                    (timePicker, hour, minute) -> {
                        taskCalender.set(Calendar.HOUR_OF_DAY, hour);
                        taskCalender.set(Calendar.MINUTE, minute);
                        showDetailsDialog();
                    },
                    todayCalender.get(Calendar.HOUR_OF_DAY),
                    todayCalender.get(Calendar.MINUTE), false);
        }else {
            timeDialog = new TimePickerDialog(getActivity(),
                    (timePicker, hour, minute) -> {
                        taskCalender.set(Calendar.HOUR_OF_DAY, hour);
                        taskCalender.set(Calendar.MINUTE, minute);
                        showDetailsDialog();
                    },
                    todayCalender.get(Calendar.HOUR_OF_DAY),
                    todayCalender.get(Calendar.MINUTE), false);
        }
        timeDialog.setTitle("Select Task Time");
        timeDialog.show();
    }

    private void showDetailsDialog(){
        timeDialog.dismiss();
        detailsDialog = DetailsDialog.getInstance();
        detailsDialog.setTargetFragment(this, 101011);
        detailsDialog.show(getFragmentManager(), "Task Details");
    }

    //details dialog callBack method
    @Override
    public void createTask(String title, String description, String category) {
        detailsDialog.dismiss();
        Task task = new Task();
        task.setTaskDate(taskCalender);
        task.setTaskTitle(title);
        task.setTaskDesc(description);

        if (!(category == null)) {
            task.setUrgent(category.equals("Important"));
        }

        presenter.addTask(task);
    }

    //update list view
    public void update(){
        if (adapter == null){
            adapter = new SectionedRecyclerViewAdapter();
            createSections();
            taskListView.setAdapter(adapter);
        }else {
            createSections();
            adapter.notifyDataSetChanged();
        }
    }

    private void createSections(){
        adapter.removeAllSections();
        for (String segment : taskSegments) {
            List<Task> taskList = getDayTask(segment);
            if (taskList.size() != 0){
                adapter.addSection(new TaskSection(segment, taskList, this, preferences));
            }
            //todo else show empty view
        }
    }

    //edit task call back
    @Override
    public void updateView() {
        editDialog.dismiss();
        update();
    }

    @Override
    public void updateAfterDelete() {
        if (editDialog != null){
            editDialog.dismiss();
        }
        createSections();
        adapter.notifyItemRemoved(position);
    }

    public String getDayString(int day){
        String[] daysOfTheWeek = new String[]{"Sunday", "Monday", "Tuesday", "Wednesday",
                "Thursday", "Friday", "Saturday"};
        return daysOfTheWeek[day - 1];
    }

    public void updateTask(Task task) {
        presenter.updateTask(task);
    }
}
