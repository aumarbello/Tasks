package com.example.ahmed.simpdo.presentation.list;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.example.ahmed.simpdo.App;
import com.example.ahmed.simpdo.R;
import com.example.ahmed.simpdo.data.model.AllTasks;
import com.example.ahmed.simpdo.data.pref.TaskPref;
import com.example.ahmed.simpdo.presentation.list.calender.CalenderFragment;
import com.example.ahmed.simpdo.presentation.splash.SplashActivity;

import javax.inject.Inject;

/**
 * Created by ahmed on 9/11/17.
 */

public class TaskListContainer extends AppCompatActivity
        implements TaskListFragment.Callback{
    @Inject
    TaskListFragment listFragment;

    @Inject
    CalenderFragment calenderFragment;

    @Inject
    TaskPref pref;

    @Inject
    TaskListPresenter presenter;

    private FragmentManager manager;
    private AllTasks allTasks;

    @Override
    protected void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);
        setContentView(R.layout.container);
        ((App)getApplication()).getComponent().inject(this);

        manager = getSupportFragmentManager();

        allTasks = (AllTasks) getIntent().getSerializableExtra
                (SplashActivity.taskList);
        if (allTasks == null){
            allTasks = new AllTasks();
            allTasks.setTaskList(presenter.getAllTasks());
        }

        overridePendingTransition(0, 0);
        Fragment existingFragment = manager.findFragmentById(R.id.list_container);

        if (existingFragment == null) {
            Bundle args = new Bundle();
            args.putSerializable(SplashActivity.taskList, allTasks);
            listFragment.setArguments(args);

            manager.beginTransaction()
                    .add(R.id.list_container, listFragment)
                    .commit();
        }
    }

    @Override
    public void openCalender() {
        Bundle bundle = new Bundle();
        bundle.putSerializable(SplashActivity.taskList, allTasks);
        calenderFragment.setArguments(bundle);
        manager.beginTransaction()
                .replace(R.id.list_container, calenderFragment)
                .commit();
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        if (item.getItemId() == android.R.id.home){
            onBackPressed();
            return true;
        }
        return false;
    }

    public void returnToList() {
        Bundle bundle = new Bundle();
        bundle.putSerializable(SplashActivity.taskList, allTasks);
        TaskListFragment fragment = new TaskListFragment();
        fragment.setArguments(bundle);
        manager.beginTransaction()
                .replace(R.id.list_container, fragment)
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
        }else {
            super.onBackPressed();
        }
    }
}
