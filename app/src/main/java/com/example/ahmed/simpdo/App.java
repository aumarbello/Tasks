package com.example.ahmed.simpdo;

import android.app.Application;

import com.example.ahmed.simpdo.dagger.AppComponent;
import com.example.ahmed.simpdo.dagger.AppModule;
import com.example.ahmed.simpdo.dagger.DaggerAppComponent;

/**
 * Created by ahmed on 8/24/17.
 */

public class App extends Application {
    private AppComponent component;

    public AppComponent getComponent(){
        return component;
    }

    @Override
    public void onCreate(){
        super.onCreate();

        component = init(this);
    }

    protected AppComponent init(App app){
        return DaggerAppComponent.builder()
                .appModule(new AppModule(app))
                .build();
    }
}
