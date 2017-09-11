package com.example.ahmed.simpdo.presentation.list;

import com.example.ahmed.simpdo.data.db.TaskDAO;
import com.example.ahmed.simpdo.data.model.Task;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by ahmed on 8/24/17.
 */

public class TaskListPresenter{
    private TaskDAO taskDAO;

    @Inject
    TaskListPresenter(TaskDAO taskDAO) {
        this.taskDAO = taskDAO;
    }

    List<Task> getAllTasks(){
        return taskDAO.getAllTasks();
    }

    void deleteTask(Task task) {
        taskDAO.deleteTask(task);
    }

    void addTask(Task task){
        taskDAO.addTask(task);
    }


    void updateTask(Task task) {
        taskDAO.updateTask(task);
    }
}
