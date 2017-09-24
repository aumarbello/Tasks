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
        return taskDAO.getAllNormalTasks();
    }

    void deleteTask(Task task, boolean deleteAll) {
        taskDAO.deleteNormalTask(task);

        if (deleteAll){
            switch (task.getRepeatCategory()){
                case 1:
                    taskDAO.deleteWeeklyTask(task);
                    break;
                case 2:
                    taskDAO.deleteMonthlyTask(task);
                    break;
                case 3:
                    taskDAO.deleteYearlyTask(task);
                    break;
            }
        }
    }

    void addTask(Task task, boolean isRepeating){
        taskDAO.addNormalTask(task);

        if (isRepeating){
            switch (task.getRepeatCategory()){
                case 1:
                    taskDAO.addWeeklyTask(task);
                    break;
                case 2:
                    taskDAO.addMonthlyTask(task);
                    break;
                case 3:
                    taskDAO.addYearlyTask(task);
                    break;
            }
        }
    }

    void updateTask(Task task) {
        taskDAO.updateNormalTask(task);
    }
}
