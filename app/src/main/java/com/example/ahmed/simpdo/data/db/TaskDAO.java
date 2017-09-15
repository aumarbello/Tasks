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

    public void addTask(Task task){
        database.insert(TASK_TABLE, null, getValues(task));
    }

    public void updateTask(Task task){
        database.update(
                TASK_TABLE,
                getValues(task),
                TASK_ID +  " = ?",
                new String[]{task.getTaskID().toString()}
        );
    }

    public void deleteTask(Task task){
        database.delete(
                TASK_TABLE,
                TASK_ID + " = ?",
                new String[]{task.getTaskID().toString()}
        );
    }

    public List<Task> getAllTasks(){
        TaskWrapper wrapper = query(null, null);
        List<Task> taskList = new ArrayList<>();
        wrapper.moveToFirst();
        while (!wrapper.isAfterLast()){
            Task task = wrapper.getTask();
            taskList.add(task);
            wrapper.moveToNext();
        }
        return taskList;
    }

    private TaskWrapper query(String whereClause, String[] args){
        return new TaskWrapper(database.query(
                        TASK_TABLE,
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
        values.put(TASK_IMPORTANT, task.isUrgent() ? 0 : 1);
        values.put(TASK_DONE, task.isDone() ? 0 : 1);
        values.put(TASK_ALARM_TIME, task.getAlarmTime());
        values.put(TASK_REPEAT, task.getRepeatCategory());
        return values;
    }

    public void close(){
        database.close();
    }
}
