package com.example.ahmed.simpdo.presentation.list.calender;

import com.example.ahmed.simpdo.data.db.TaskDAO;
import com.example.ahmed.simpdo.data.model.Task;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by ahmed on 9/24/17.
 */

public class CalenderPresenter {
    private TaskDAO dao;

    @Inject
    CalenderPresenter(TaskDAO dao){
        this.dao = dao;
    }

    void updateTask(Task task){
        switch (task.getRepeatCategory()){
            case 0:
                dao.updateNormalTask(task);
                break;
            case 1:
                dao.updateWeeklyTask(task);
                break;
            case 2:
                dao.updateMonthlyTask(task);
                break;
            case 3:
                dao.updateYearlyTask(task);
                break;
        }
    }

    void deleteTask(Task task, boolean deleteAll){
        dao.deleteNormalTask(task);

        if (deleteAll){
            switch (task.getRepeatCategory()){
                case 1:
                    dao.deleteWeeklyTask(task);
                    break;
                case 2:
                    dao.deleteMonthlyTask(task);
                    break;
                case 3:
                    dao.deleteYearlyTask(task);
                    break;
            }
        }
    }

    public List<Task> getWeeklyTasks(){
        return dao.getAllWeeklyTasks();
    }

    public List<Task> getMonthlyTasks(){
        return dao.getAllMonthlyTasks();
    }

    public List<Task> getYearlyTasks(){
        return dao.getAllYearlyTasks();
    }


    public List<Task> getNormalTasks() {
        return dao.getAllNormalTasks();
    }
}
