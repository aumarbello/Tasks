package com.example.ahmed.simpdo.dagger;

import com.example.ahmed.simpdo.presentation.TaskContainer;
import com.example.ahmed.simpdo.presentation.edit.EditTaskFragment;
import com.example.ahmed.simpdo.presentation.list.TaskList;
import com.example.ahmed.simpdo.presentation.notifications.DailyService;
import com.example.ahmed.simpdo.presentation.notifications.ImportantService;
import com.example.ahmed.simpdo.presentation.splash.SplashFragment;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by ahmed on 8/24/17.
 */
@Singleton
@Component(modules = AppModule.class)
public interface AppComponent {
    void inject(SplashFragment splashFragment);
    void inject(TaskList taskList);
    void inject(EditTaskFragment editTaskFragment);
    void inject(TaskContainer container);
    void inject(ImportantService importantService);
    void inject(DailyService dailyService);
}
