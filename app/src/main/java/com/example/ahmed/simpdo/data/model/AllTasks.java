package com.example.ahmed.simpdo.data.model;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by ahmed on 9/11/17.
 */

public class AllTasks implements Serializable {
    private List<Task> taskList;

    public List<Task> getTaskList() {
        return taskList;
    }

    public void setTaskList(List<Task> taskList) {
        this.taskList = taskList;
    }
}
