package com.example.ahmed.simpdo.presentation.splash;

import android.os.SystemClock;

import com.example.ahmed.simpdo.data.db.TaskDAO;
import com.example.ahmed.simpdo.data.model.AllTasks;
import com.example.ahmed.simpdo.data.model.Task;
import com.example.ahmed.simpdo.utils.RxUtils;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;

/**
 * Created by ahmed on 8/24/17.
 */

public class SplashPresenter {
    private SplashActivity splashActivity;
    private final int Splash_Time_Out = 3000;
    private TaskDAO taskDAO;
    private AllTasks allTasks;

    @Inject
    SplashPresenter(TaskDAO taskDAO){
        this.taskDAO = taskDAO;
    }

    void onAttach(SplashActivity splashActivity){
        this.splashActivity = splashActivity;
    }
    void startTaskList(){
        allTasks = new AllTasks();
        List<Task> taskList = getTasks();
        allTasks.setTaskList(taskList);
        Observable.create(emitter -> {
            SystemClock.sleep(Splash_Time_Out);
            emitter.onNext(5);
            emitter.onComplete();
        }).compose(RxUtils.applySchedulers()).subscribe(observer ->
                splashActivity.gotoTaskList(allTasks));
    }

    private List<Task> getTasks(){
        List<Task> normalTasks = taskDAO.getAllNormalTasks();
        List<Task> weeklyTasks = taskDAO.getAllWeeklyTasks();
        List<Task> monthlyTasks = taskDAO.getAllMonthlyTasks();
        List<Task> yearlyTasks = taskDAO.getAllYearlyTasks();

        List<Task> returnList = new ArrayList<>(normalTasks);

        for (Task task : weeklyTasks) {
            if (!normalTasks.contains(task)){
                //todo override equals method of tasks
                returnList.add(task);
                taskDAO.addNormalTask(task);
            }
        }

        for (Task task : monthlyTasks) {
            if (!normalTasks.contains(task)){
                //todo override equals method of tasks
                returnList.add(task);
                taskDAO.addNormalTask(task);
            }
        }

        for (Task task : yearlyTasks) {
            if (!normalTasks.contains(task)){
                //todo override equals method of tasks
                returnList.add(task);
                taskDAO.addNormalTask(task);
            }
        }

        return returnList;
    }
}
