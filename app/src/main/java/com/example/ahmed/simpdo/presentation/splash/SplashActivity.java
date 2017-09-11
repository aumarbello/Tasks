package com.example.ahmed.simpdo.presentation.splash;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.ahmed.simpdo.App;
import com.example.ahmed.simpdo.R;
import com.example.ahmed.simpdo.data.db.TaskDAO;
import com.example.ahmed.simpdo.data.model.AllTasks;
import com.example.ahmed.simpdo.presentation.list.TaskListContainer;
import com.example.ahmed.simpdo.presentation.notifications.DailyService;
import com.example.ahmed.simpdo.presentation.notifications.IndividualService;

import javax.inject.Inject;

/**
 * Created by ahmed on 8/24/17.
 */

public class SplashActivity extends AppCompatActivity {
    @Inject
    TaskDAO taskDAO;

    @Inject
    SplashPresenter presenter;

    public static final String taskList = "list";

    @Override
    public void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);

        ((App)getApplication()).getComponent().inject(this);
        taskDAO.open();
        presenter.onAttach(this);

        setContentView(R.layout.splash_layout);
        overridePendingTransition(0, 0);

        presenter.startTaskList();
    }

    void gotoTaskList(AllTasks tasks) {
        Intent intent = new Intent(this, TaskListContainer.class);
        intent.putExtra(taskList, tasks);
        startActivity(intent);
        finish();
    }
}
