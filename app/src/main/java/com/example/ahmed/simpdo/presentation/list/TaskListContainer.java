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

public class TaskListContainer extends AppCompatActivity
        implements TaskListFragment.Callback, CalenderFragment.CallBack {
    @Inject
    TaskListFragment listFragment;

    @Inject
    CalenderFragment calenderFragment;

    private FragmentManager manager;

    @Override
    protected void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);
        setContentView(R.layout.container);

        manager = getSupportFragmentManager();

        AllTasks allTasks = (AllTasks) getIntent().getSerializableExtra
                (SplashActivity.taskList);

        ((App)getApplication()).getComponent().inject(this);

        overridePendingTransition(0, 0);
        Fragment existingFragment = getSupportFragmentManager()
                .findFragmentById(R.id.list_container);
        if (existingFragment == null) {
            Bundle args = new Bundle();
            args.putSerializable(SplashActivity.taskList, allTasks);
            listFragment.setArguments(args);

            int count = manager.getBackStackEntryCount();
            manager.beginTransaction()
                    .add(R.id.list_container, listFragment)
                    .commit();
        }
    }

    @Override
    public void openCalender() {
        manager.beginTransaction()
                .replace(R.id.list_container, calenderFragment)
                .commit();
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }


    @Override
    public void returnToList() {
        manager.beginTransaction()
                .replace(R.id.list_container, listFragment)
                .commit();
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        }
    }

    @Override
    public void onBackPressed(){
        Fragment currentFrag = manager.findFragmentById(R.id.list_container);

        if (currentFrag.getClass().getName().equals(CalenderFragment.class.getName())){
            returnToList();
        }
    }
}
