package com.example.ahmed.simpdo.presentation.list;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.TextView;

import com.example.ahmed.simpdo.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by ahmed on 9/15/17.
 */

public class CalenderFragment extends Fragment {
    interface CallBack{
        void returnToList();
    }

    private CallBack callBack;
    private Unbinder unbinder;

    @BindView(R.id.task_calender)
    CalendarView calendarView;

    @BindView(R.id.current_task_details)
    TextView currentTask;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent,
                             Bundle savedInstance){

        View view = inflater.inflate(R.layout.calender_layout, parent, false);

        unbinder = ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onAttach(Context context){
        super.onAttach(context);

        if (context instanceof CallBack){
            callBack = (CallBack) context;
        }else
            throw new RuntimeException("Container Activity must " +
                    "implement Calender Callback interface");
    }

    @Override
    public void onDestroy(){
        super.onDestroy();

        unbinder.unbind();
    }
}
