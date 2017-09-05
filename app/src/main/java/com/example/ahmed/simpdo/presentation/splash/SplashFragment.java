package com.example.ahmed.simpdo.presentation.splash;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ahmed.simpdo.App;
import com.example.ahmed.simpdo.R;
import com.example.ahmed.simpdo.data.db.TaskDAO;

import javax.inject.Inject;

/**
 * Created by ahmed on 8/24/17.
 */

public class SplashFragment extends Fragment {
    public interface CallBack{
        void gotoList();
    }
    @Inject
    TaskDAO taskDAO;

    @Inject
    SplashPresenter presenter;
    private CallBack callBack;

    @Override
    public void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);

        ((App)getActivity().getApplication()).getComponent().inject(this);
        taskDAO.open();

        presenter.startTaskList();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent,
                             Bundle savedInstance){
        return inflater.inflate(R.layout.splash_layout, parent, false);
    }

    @Override
    public void onAttach(Context context){
        super.onAttach(context);

        if (context instanceof CallBack){
            callBack = (CallBack) context;
        }else
            throw new RuntimeException("Container activity must implement CallBack interface");
    }

    public void startTaskList() {
        callBack.gotoList();
    }
}
