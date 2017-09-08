package com.example.ahmed.simpdo.presentation;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.example.ahmed.simpdo.App;
import com.example.ahmed.simpdo.R;
import com.example.ahmed.simpdo.presentation.list.TaskListFragment;
import com.example.ahmed.simpdo.presentation.notifications.ImportantService;
import com.example.ahmed.simpdo.presentation.splash.SplashFragment;

import javax.inject.Inject;

/**
 * Created by ahmed on 8/24/17.
 */

public class TaskContainer extends AppCompatActivity implements
        SplashFragment.CallBack{
    @Inject
    SplashFragment fragment;

    @Inject
    TaskListFragment taskListFragment;

    private FragmentManager manager;

    @Override
    protected void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);
        setContentView(R.layout.task_container);
        manager = getSupportFragmentManager();

        ((App)getApplication()).getComponent().inject(this);

        manager.beginTransaction()
                .add(R.id.task_container, fragment)
                .commit();

        ImportantService.setTimeInterval(this);
    }

    //return to taskListFragment
    //general callBack
    @Override
    public void gotoList() {
        manager.beginTransaction()
                .replace(R.id.task_container, taskListFragment)
                .commit();
    }
}
