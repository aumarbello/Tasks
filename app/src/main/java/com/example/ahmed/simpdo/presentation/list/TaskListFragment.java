package com.example.ahmed.simpdo.presentation.list;


import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TimePicker;

import com.example.ahmed.simpdo.App;
import com.example.ahmed.simpdo.R;
import com.example.ahmed.simpdo.data.model.AllTasks;
import com.example.ahmed.simpdo.data.model.Task;
import com.example.ahmed.simpdo.data.pref.TaskPref;
import com.example.ahmed.simpdo.presentation.edit.EditTaskFragment;
import com.example.ahmed.simpdo.presentation.settings.SettingsActivity;
import com.example.ahmed.simpdo.presentation.splash.SplashActivity;
import com.example.ahmed.simpdo.presentation.view.ViewTaskFragment;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.github.luizgrp.sectionedrecyclerviewadapter.Section;
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter;

/**
 * Created by ahmed on 8/24/17.
 */

public class TaskListFragment extends BackgroundFragment implements
        DetailsDialog.DetailsCallBack,
        ViewTaskFragment.CallBack,
        EditTaskFragment.CallBack, TimePickerDialog.OnTimeSetListener {
    interface Callback{
        void openCalender();
    }

    @Inject
    TaskListPresenter presenter;

    @Inject
    TaskPref preferences;

    private GetTaskList getTaskList;
    private Unbinder unbinder;
    private SectionedRecyclerViewAdapter adapter;
    private Calendar todayCalender;
    private Calendar taskCalender;
    private DatePickerDialog dateDialog;
    private DetailsDialog detailsDialog;
    private ViewTaskFragment viewDialog;
    private EditTaskFragment editDialog;
    private int position;
    private final static String TAG = "TaskList";
    private int daysSectionValue;
    private List<String> dayString;
    private AllTasks allTasks;
    private Task task;
    private Callback callback;

    @BindView(R.id.task_list)
    RecyclerView taskListView;

    @BindView(R.id.fab)
    FloatingActionButton addTask;

    @Override
    public void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);
        setHasOptionsMenu(true);
        setRetainInstance(true);
        ((App)getActivity().getApplication()).getComponent().inject(this);
        adapter = new SectionedRecyclerViewAdapter();
        task = new Task();

        Bundle args = getArguments();
        allTasks = new AllTasks();

        if (args != null){
            allTasks = (AllTasks) getArguments().getSerializable
                    (SplashActivity.taskList);
            if (allTasks == null){
                allTasks = new AllTasks();
                List<Task> taskList = presenter.getAllTasks();
                allTasks.setTaskList(taskList);
            }
        }else if (savedInstance != null){
            allTasks = (AllTasks) savedInstance.getSerializable(TAG);
        }else{
            List<Task> taskList = presenter.getAllTasks();
            allTasks.setTaskList(taskList);
        }



        int numberOfDaysPref = preferences.getDaysSection();

        todayCalender = Calendar.getInstance();
        taskCalender = Calendar.getInstance();

        int currentDay = todayCalender.get(Calendar.DAY_OF_WEEK);
        switch (numberOfDaysPref){
            case 0:
                daysSectionValue = 3;
                break;
            case 1:
                daysSectionValue = 5;
                break;
            case 2:
                daysSectionValue = 7;
                break;
        }
        getTaskList = new GetTaskList(numberOfDaysPref, allTasks.getTaskList());
        populateDayStrings();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent,
                             Bundle savedInstance){
        View view = inflater.inflate(R.layout.task_list, parent, false);

        unbinder = ButterKnife.bind(this, view);

        addTask.setOnClickListener(fab -> showDetailsDialog());

        taskListView.setLayoutManager(new LinearLayoutManager(getActivity()));
        createSections();
        taskListView.setAdapter(adapter);

        return view;
    }

    @Override
    public void onDestroyView(){
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onAttach(Context context){
        super.onAttach(context);

        if (context instanceof Callback){
            callback = (Callback) context;
        }else
            throw new RuntimeException("Container Activity must implement " +
                    "Callback interface");
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
            case R.id.calender_view:
                callback.openCalender();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void viewTask(Task task, int position) {
        this.position = position;
        viewDialog = ViewTaskFragment.getInstance(task, position);
        viewDialog.setTargetFragment(this, 101111);
        viewDialog.show(getFragmentManager(), "View Fragment");
    }

    public List<Task> getDayTask(String segment){
        return getTaskList.getTasks(segment, dayString);
    }

    //view task listFragment call back
    //also called from list menu
    @Override
    public void editTask(Task task, int position){
        this.position = position;
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
        updateAfterDelete(task);
    }


    //dialogs
    private void showCalender(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            dateDialog = new DatePickerDialog(getActivity(),
                    android.R.style.Theme_Material_Light_Dialog_Alert,
                    (datePicker, year, month, day) -> {
                        taskCalender.set(year, month, day);
                        dateDialog.dismiss();
                        showTimePicker();
                    },
                    todayCalender.get(Calendar.YEAR),
                    todayCalender.get(Calendar.MONTH),
                    todayCalender.get(Calendar.DAY_OF_MONTH));
        }else {
            dateDialog = new DatePickerDialog(getActivity(),
                    null,
                    todayCalender.get(Calendar.YEAR),
                    todayCalender.get(Calendar.MONTH),
                    todayCalender.get(Calendar.DAY_OF_MONTH));

            dateDialog.setButton(DialogInterface.BUTTON_POSITIVE,
                    "Submit", (dialogInterface, i) -> {
                        int year = dateDialog.getDatePicker().getYear();
                        int month = dateDialog.getDatePicker().getMonth();
                        int day = dateDialog.getDatePicker().getDayOfMonth();
                        taskCalender.set(year, month, day);
                        dialogInterface.dismiss();
                        showTimePicker();

            });
        }
        dateDialog.setTitle("Select Task Date");
        dateDialog.show();
    }

    private void showTimePicker() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            TimePickerDialog timeDialog = new TimePickerDialog(getActivity(),
                    android.R.style.Theme_Material_Light_Dialog_Alert,
                    (timePicker, hour, minute) -> {
                        taskCalender.set(Calendar.HOUR_OF_DAY, hour);
                        taskCalender.set(Calendar.MINUTE, minute);
                        task.setTaskDate(taskCalender);
                        presenter.addTask(task);
                        updateAfterAdding(task);
                    },
                    todayCalender.get(Calendar.HOUR_OF_DAY),
                    todayCalender.get(Calendar.MINUTE), false);
            timeDialog.setTitle("Select Task Time");
            timeDialog.show();
        }else {
            TimePicker picker = new TimePicker(getActivity());
            picker.setOnTimeChangedListener((timePicker, hour, minute) -> {
                taskCalender.set(Calendar.HOUR_OF_DAY, hour);
                taskCalender.set(Calendar.MINUTE, minute);
            });

            AlertDialog dialog = new AlertDialog.Builder(getActivity())
                    .setTitle("Select Task Time")
                    .setView(picker)
                    .setPositiveButton("Submit", (dialogInterface, i) -> {
                        dialogInterface.dismiss();
                        task.setTaskDate(taskCalender);
                        presenter.addTask(task);
                        updateAfterAdding(task);
                    })
                    .create();

            dialog.show();
        }
    }

    private void showDetailsDialog(){
        detailsDialog = DetailsDialog.getInstance();
        detailsDialog.setTargetFragment(this, 101011);
        detailsDialog.show(getFragmentManager(), "Task Details");
    }

    //details dialog callBack method
    @Override
    public void showCalenderDialog(String title, String description,
                                   int repeatTask, int alarmTime) {
        task.setTaskTitle(title);
        task.setTaskDesc(description);
        task.setAlarmTime(alarmTime);
        task.setRepeatCategory(repeatTask);
        detailsDialog.dismiss();
        showCalender();
    }

    private void createSections(){
        for (String segment : dayString) {
            List<Task> taskList = getDayTask(segment);
            if (taskList.size() != 0){
                adapter.addSection(segment, new TaskSection(segment, taskList, this,
                        preferences));
            }else {
                TaskSection section = new TaskSection(segment, taskList, this,
                        preferences);
                section.setState(Section.State.EMPTY);
                adapter.addSection(segment, section);
            }
        }
    }

    //edit task call back
    @Override
    public void updateView(Task task) {
        editDialog.dismiss();
        updateAfterEditing(task);
        if (!task.isAlreadyRepeating() && task.getRepeatCategory() != 0){
            addRepeatTask(task);
        }
    }

    public void updateAfterAdding(Task task){
        String sectionTag = getTaskSectionTag(task);

        TaskSection currentSection = (TaskSection) adapter.getSection(sectionTag);
        int posInList = currentSection.addTaskToList(task);

        int items = currentSection.getContentItemsTotal();

        if (currentSection.getState().equals(Section.State.EMPTY)){
            currentSection.setState(Section.State.LOADED);
            adapter.notifyDataSetChanged();
        }else {
            adapter.notifyItemInsertedInSection(currentSection,
                    items);
        }

        if (task.getRepeatCategory() != 0){
            new RepeatOp().execute(task);
        }
    }

    public void updateAfterEditing(Task task){
        String sectionTag = getTaskSectionTag(task);
        int taskPositionInSection = adapter.getPositionInSection(position);

        TaskSection currentSection = (TaskSection) adapter.getSection(sectionTag);
        adapter.notifyItemChangedInSection(currentSection, taskPositionInSection);
    }

    @Override
    public void updateAfterDelete(Task task) {
        if (editDialog != null){
            editDialog.dismiss();
        }

        int taskPositionInSection = adapter.getPositionInSection(position);
        String sectionTag = getTaskSectionTag(task);

        TaskSection currentSection = (TaskSection) adapter.getSection(sectionTag);
        currentSection.removeFromList(task);

        adapter.notifyItemRemovedFromSection(currentSection,
                taskPositionInSection);

        if (currentSection.isSectionEmpty()){
            currentSection.setState(Section.State.EMPTY);
        }
    }

    public String getDayString(int day){
        String[] daysOfTheWeek = new String[]{"Sunday", "Monday", "Tuesday", "Wednesday",
                "Thursday", "Friday", "Saturday"};
        return daysOfTheWeek[day - 1];
    }

    public void updateTask(Task task) {
        presenter.updateTask(task);
    }

    public String getTaskSectionTag(Task task){
        GetTaskTag taskTag = new GetTaskTag(preferences, dayString);
        return taskTag.getTaskSegment(task);
    }

    private void populateDayStrings(){
        int day = todayCalender.get(Calendar.DAY_OF_YEAR);
        int dayInWeek = todayCalender.get(Calendar.DAY_OF_WEEK);

        dayString = new ArrayList<>();
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
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstance){
        savedInstance.putSerializable(TAG, allTasks);
    }

    private class RepeatOp extends AsyncTask<Task, Void, Void>{

        @Override
        protected Void doInBackground(Task... tasks) {
            Task currentTask = tasks[0];
            addRepeatTask(currentTask);
            return null;
        }
    }
    private void addRepeatTask(Task task){
        task.setAlreadyRepeating(true);

        Calendar repeatCalender = Calendar.getInstance();
        repeatCalender.setTimeInMillis(dateDialog.getDatePicker().getMaxDate());

        Calendar taskCalender = task.getTaskDate();

        int currentYear = taskCalender.get(Calendar.YEAR);
        int maxYear = repeatCalender.get(Calendar.YEAR);

        switch (task.getRepeatCategory()){
            case 1:
                int currentWeek = taskCalender.get(Calendar.WEEK_OF_YEAR);
                int maxWeek = repeatCalender.getMaximum(Calendar.WEEK_OF_YEAR);

                while (currentYear < maxYear){
                    if (currentYear > todayCalender.get(Calendar.YEAR)){
                        currentWeek = repeatCalender.getMinimum(Calendar.WEEK_OF_YEAR)- 1;
                    }
                    taskCalender.set(Calendar.YEAR, currentYear);
                    while (currentWeek < maxWeek){
                        taskCalender.set(Calendar.WEEK_OF_YEAR, ++currentWeek);
                        task.setTaskDate(taskCalender);
                        presenter.addTask(task);
//                        updateAfterAdding(task);
                    }
                    currentYear++;
                }
                break;
            case 2:
                int currentMonth = taskCalender.get(Calendar.MONTH);
                int maxMonth = repeatCalender.getMaximum(Calendar.MONTH);

                while (currentYear < maxYear){
                    if (currentYear > todayCalender.get(Calendar.YEAR)){
                        currentMonth = repeatCalender.getMinimum(Calendar.MONTH) - 1;
                    }
                    taskCalender.set(Calendar.YEAR, currentYear);
                    while (currentMonth < maxMonth){
                        taskCalender.set(Calendar.MONTH,
                                ++currentMonth);
                        task.setTaskDate(taskCalender);
                        presenter.addTask(task);
//                        updateAfterAdding(task);
                    }
                    currentYear++;
                    Log.d(TAG, "Current Year - " + currentYear);
                }
                break;
            case 3:
                while (currentYear < maxYear){
                    taskCalender.set(Calendar.YEAR,
                            ++currentYear);
                    task.setTaskDate(taskCalender);
                    presenter.addTask(task);
//                    updateAfterAdding(task);
                }
                break;
        }
    }

    @Override
    public void onTimeSet(TimePicker timePicker, int hour, int minute) {

    }
}
