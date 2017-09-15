package com.example.ahmed.simpdo.dagger;

import android.app.Application;
import android.content.Context;

import com.example.ahmed.simpdo.data.db.TaskDAO;
import com.example.ahmed.simpdo.presentation.list.CalenderFragment;
import com.example.ahmed.simpdo.presentation.list.TaskListFragment;

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
    TaskDAO providesTaskDAO(Context context){
        return new TaskDAO(context);
    }

    @Provides
    TaskListFragment providesListFragment(){
        return new TaskListFragment();
    }

    @Provides
    CalenderFragment providesCalenderView(){
        return new CalenderFragment();
    }
}
