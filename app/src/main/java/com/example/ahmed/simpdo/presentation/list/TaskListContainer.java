package com.example.ahmed.simpdo.presentation.list;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.example.ahmed.simpdo.App;
import com.example.ahmed.simpdo.R;
import com.example.ahmed.simpdo.data.pref.TaskPref;
import com.example.ahmed.simpdo.presentation.list.calender.CalenderFragment;

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

    @Override
    protected void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);
        setContentView(R.layout.container);
        ((App)getApplication()).getComponent().inject(this);
        overridePendingTransition(0, 0);
        manager = getSupportFragmentManager();

        overridePendingTransition(0, 0);
        Fragment existingFragment = manager.findFragmentById(R.id.list_container);

        if (existingFragment == null) {
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
    public boolean onOptionsItemSelected(MenuItem item){
        if (item.getItemId() == android.R.id.home){
            onBackPressed();
            return true;
        }
        return false;
    }

    public void returnToList() {
        TaskListFragment fragment = new TaskListFragment();
        manager.beginTransaction()
                .replace(R.id.list_container, fragment)
                .commit();
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        }
    }

    @Override
    public void onBackPressed(){
        if (calenderFragment.isVisible()){
            returnToList();
        }else {
            super.onBackPressed();
        }
    }
}
