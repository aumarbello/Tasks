package com.example.ahmed.simpdo.presentation.splash;

import android.os.SystemClock;

import com.example.ahmed.simpdo.utils.RxUtils;

import javax.inject.Inject;

import io.reactivex.Observable;

/**
 * Created by ahmed on 8/24/17.
 */

public class SplashPresenter {
    private SplashFragment fragment;
    private final int Splash_Time_Out = 3000;

    @Inject
    public SplashPresenter(SplashFragment fragment){
        this.fragment = fragment;
    }


    void startTaskList(){
        Observable.create(emitter -> {
            SystemClock.sleep(Splash_Time_Out);
            emitter.onNext(5);
            emitter.onComplete();
        }).compose(RxUtils.applySchedulers()).subscribe(observer ->
                fragment.startTaskList());
    }
}
