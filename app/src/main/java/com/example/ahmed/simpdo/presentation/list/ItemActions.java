package com.example.ahmed.simpdo.presentation.list;

import com.example.ahmed.simpdo.data.model.Task;

/**
 * Created by ahmed on 9/23/17.
 */

public interface ItemActions {
    void viewTask(Task task, int position);
    void updateTask(Task task);
    void editTask(Task task, int position);
    void deleteTask(Task task, int position);
}
