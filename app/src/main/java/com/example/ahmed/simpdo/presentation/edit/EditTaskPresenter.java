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

    void deleteTask(Task task){
        taskDAO.deleteNormalTask(task);
    }
}
