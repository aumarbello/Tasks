package com.example.ahmed.simpdo.data.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.example.ahmed.simpdo.data.model.Task;
import com.example.ahmed.simpdo.data.model.TaskWrapper;

import java.util.ArrayList;
import java.util.List;

import static com.example.ahmed.simpdo.utils.DBSchema.*;

/**
 * Created by ahmed on 8/24/17.
 */

public class TaskDAO {
    private SQLiteDatabase database;
    private Helper dbHelper;

    public TaskDAO(Context context){
        dbHelper = new Helper(context);
    }

    public void open(){
        database = dbHelper.getWritableDatabase();
    }

    public void addNormalTask(Task task){
        database.insert(NORMAL_TASKS_TABLE, null, getValues(task));
    }

    public void addWeeklyTask(Task task){
        database.insert(WEEKLY_TASKS_TABLE, null, getValues(task));
    }

    public void addMonthlyTask(Task task){
        database.insert(MONTHLY_TASKS_TABLE, null, getValues(task));
    }

    public void addYearlyTask(Task task){
        database.insert(YEARLY_TASKS_TABLE, null, getValues(task));
    }

    public void updateNormalTask(Task task){
        database.update(
                NORMAL_TASKS_TABLE,
                getValues(task),
                TASK_ID +  " = ?",
                new String[]{task.getTaskID().toString()}
        );
    }

    public void updateWeeklyTask(Task task){
        database.update(
                WEEKLY_TASKS_TABLE,
                getValues(task),
                TASK_ID +  " = ?",
                new String[]{task.getTaskID().toString()}
        );
    }

    public void updateMonthlyTask(Task task){
        database.update(
                MONTHLY_TASKS_TABLE,
                getValues(task),
                TASK_ID +  " = ?",
                new String[]{task.getTaskID().toString()}
        );
    }

    public void updateYearlyTask(Task task){
        database.update(
                YEARLY_TASKS_TABLE,
                getValues(task),
                TASK_ID +  " = ?",
                new String[]{task.getTaskID().toString()}
        );
    }

    public void deleteNormalTask(Task task){
        database.delete(
                NORMAL_TASKS_TABLE,
                TASK_ID + " = ?",
                new String[]{task.getTaskID().toString()}
        );
    }

    public void deleteWeeklyTask(Task task){
        database.delete(
                WEEKLY_TASKS_TABLE,
                TASK_ID + " = ?",
                new String[]{task.getTaskID().toString()}
        );
    }

    public void deleteMonthlyTask(Task task){
        database.delete(
                MONTHLY_TASKS_TABLE,
                TASK_ID + " = ?",
                new String[]{task.getTaskID().toString()}
        );
    }

    public void deleteYearlyTask(Task task){
        database.delete(
                YEARLY_TASKS_TABLE,
                TASK_ID + " = ?",
                new String[]{task.getTaskID().toString()}
        );
    }

    public List<Task> getAllNormalTasks(){
        TaskWrapper wrapper = query(null, null, NORMAL_TASKS_TABLE);
        List<Task> taskList = new ArrayList<>();
        wrapper.moveToFirst();
        while (!wrapper.isAfterLast()){
            Task task = wrapper.getTask();
            taskList.add(task);
            wrapper.moveToNext();
        }
        return taskList;
    }

    public List<Task> getAllWeeklyTasks(){
        TaskWrapper wrapper = query(null, null, WEEKLY_TASKS_TABLE);
        List<Task> taskList = new ArrayList<>();
        wrapper.moveToFirst();
        while (!wrapper.isAfterLast()){
            Task task = wrapper.getTask();
            taskList.add(task);
            wrapper.moveToNext();
        }
        return taskList;
    }

    public List<Task> getAllMonthlyTasks(){
        TaskWrapper wrapper = query(null, null, MONTHLY_TASKS_TABLE);
        List<Task> taskList = new ArrayList<>();
        wrapper.moveToFirst();
        while (!wrapper.isAfterLast()){
            Task task = wrapper.getTask();
            taskList.add(task);
            wrapper.moveToNext();
        }
        return taskList;
    }

    public List<Task> getAllYearlyTasks(){
        TaskWrapper wrapper = query(null, null, YEARLY_TASKS_TABLE);
        List<Task> taskList = new ArrayList<>();
        wrapper.moveToFirst();
        while (!wrapper.isAfterLast()){
            Task task = wrapper.getTask();
            taskList.add(task);
            wrapper.moveToNext();
        }
        return taskList;
    }

    private TaskWrapper query(String whereClause, String[] args, String table){
        return new TaskWrapper(database.query(
                        table,
                        null,
                        whereClause,
                        args, null, null, null
        ));
    }

    private ContentValues getValues(Task task){
        ContentValues values = new ContentValues();
        values.put(TASK_ID, task.getTaskID().toString());
        values.put(TASK_TITLE, task.getTaskTitle());
        values.put(TASK_DESC, task.getTaskDesc());
        values.put(TASK_DATE, task.getTaskDate().getTimeInMillis());
        values.put(TASK_DONE, task.isDone() ? 0 : 1);
        values.put(TASK_ALARM_TIME, task.getAlarmTime());
        values.put(TASK_REPEAT, task.getRepeatCategory());
        values.put(TASK_IS_REPEATING, task.isAlreadyRepeating() ? 0 : 1);
        return values;
    }

    public void close(){
        database.close();
    }
}
