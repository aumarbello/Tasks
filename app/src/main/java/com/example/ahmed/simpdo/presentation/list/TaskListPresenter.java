package com.example.ahmed.simpdo.presentation.list;

import com.example.ahmed.simpdo.data.db.TaskDAO;
import com.example.ahmed.simpdo.data.model.Task;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by ahmed on 8/24/17.
 */

public class TaskListPresenter{
    private TaskList fragment;
    private TaskDAO taskDAO;

    @Inject
    TaskListPresenter(TaskDAO taskDAO, TaskList fragment) {
        this.taskDAO = taskDAO;
        this.fragment = fragment;
    }

    List<Task> getAllTasks(){
        return taskDAO.getAllTasks();
    }

    void deleteTask(Task task) {
        taskDAO.deleteTask(task);
        fragment.updateAfterDelete();
    }

    void addTask(Task task){
        taskDAO.addTask(task);
        fragment.update();
    }


}
