package com.example.ahmed.simpdo.dagger;

import android.app.Application;
import android.content.Context;

import com.example.ahmed.simpdo.data.db.TaskDAO;
import com.example.ahmed.simpdo.data.model.Task;
import com.example.ahmed.simpdo.data.pref.TaskPref;
import com.example.ahmed.simpdo.presentation.list.GetTaskList;
import com.example.ahmed.simpdo.presentation.list.TaskListFragment;
import com.example.ahmed.simpdo.presentation.splash.SplashFragment;

import java.util.List;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by ahmed on 8/24/17.
 */
@Module
public class AppModule {
    private Application application;

    public AppModule(Application application){
        this.application = application;
    }

    @Provides
    @Singleton
    Context providesContext(){
        return application;
    }

    @Provides
    @Singleton
    TaskDAO taskDAO(Context context){
        return new TaskDAO(context);
    }

    @Provides
    @Singleton
    SplashFragment providesSplashFragment(){
        return new SplashFragment();
    }

    @Provides
    @Singleton
    TaskListFragment providesListFragment(){
        return new TaskListFragment();
    }

    @Provides
    @Singleton
    GetTaskList providesGetTaskList(TaskDAO taskDAO, TaskPref pref){
        List<Task> taskList =taskDAO.getAllTasks();
        int section = pref.getDaysSection();
        return new GetTaskList(section, taskList);
    }
}
