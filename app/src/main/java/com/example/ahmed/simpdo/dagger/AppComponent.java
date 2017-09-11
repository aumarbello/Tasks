package com.example.ahmed.simpdo.dagger;

import com.example.ahmed.simpdo.presentation.edit.EditTaskFragment;
import com.example.ahmed.simpdo.presentation.list.TaskListContainer;
import com.example.ahmed.simpdo.presentation.list.TaskListFragment;
import com.example.ahmed.simpdo.presentation.notifications.IndividualService;
import com.example.ahmed.simpdo.presentation.splash.SplashActivity;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by ahmed on 8/24/17.
 */
@Singleton
@Component(modules = AppModule.class)
public interface AppComponent {
    void inject(SplashActivity splashActivity);
    void inject(TaskListFragment taskListFragment);
    void inject(EditTaskFragment editTaskFragment);
    void inject(TaskListContainer container);
    void inject(IndividualService individualService);
}
