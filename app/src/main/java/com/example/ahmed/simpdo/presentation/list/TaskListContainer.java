package com.example.ahmed.simpdo.presentation.list;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.example.ahmed.simpdo.App;
import com.example.ahmed.simpdo.R;
import com.example.ahmed.simpdo.data.model.AllTasks;
import com.example.ahmed.simpdo.presentation.splash.SplashActivity;

import javax.inject.Inject;

/**
 * Created by ahmed on 9/11/17.
 */

public class TaskListContainer extends AppCompatActivity {
    @Inject
    TaskListFragment fragment;

    @Override
    protected void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);
        setContentView(R.layout.container);

        FragmentManager manager = getSupportFragmentManager();

        AllTasks allTasks = (AllTasks) getIntent().getSerializableExtra
                (SplashActivity.taskList);

        ((App)getApplication()).getComponent().inject(this);

        overridePendingTransition(0, 0);
        Fragment existingFragment = getSupportFragmentManager()
                .findFragmentById(R.id.list_container);
        if (existingFragment == null) {
            Bundle args = new Bundle();
            args.putSerializable(SplashActivity.taskList, allTasks);
            fragment.setArguments(args);

            int count = manager.getBackStackEntryCount();
            manager.beginTransaction()
                    .add(R.id.list_container, fragment)
                    .commit();
        }
    }
}
