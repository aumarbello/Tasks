package com.example.ahmed.simpdo.presentation.edit;

import com.example.ahmed.simpdo.data.db.TaskDAO;
import com.example.ahmed.simpdo.data.model.Task;

import javax.inject.Inject;

/**
 * Created by ahmed on 8/24/17.
 */

public class EditTaskPresenter {
    private TaskDAO taskDAO;

    @Inject
    EditTaskPresenter(TaskDAO taskDAO){
        this.taskDAO = taskDAO;
    }

    void editTask(Task task){
        taskDAO.updateNormalTask(task);
    }

    void deleteTask(Task task, boolean deleteAll){
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
}
