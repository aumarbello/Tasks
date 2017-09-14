package com.example.ahmed.simpdo.presentation.list;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Spinner;

import com.example.ahmed.simpdo.R;

import butterknife.BindArray;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by ahmed on 8/28/17.
 */

public class DetailsDialog extends DialogFragment {
    interface DetailsCallBack{
        void showCalenderDialog(String title, String description,
                                String category, int repeatTask,
                                int alarmTime);
    }

    public DetailsDialog(){

    }

    @BindView(R.id.add_task_title)
    AutoCompleteTextView titleView;

    @BindView(R.id.add_task_description)
    AutoCompleteTextView descView;

    @BindView(R.id.category_spinner)
    Spinner categorySpinner;

    @BindView(R.id.alarm_time)
    Spinner alarmSpinner;

    @BindView(R.id.repeat_task)
    Spinner repeatSpinner;

    @BindArray(R.array.categories)
    String[] categories;

    private DetailsCallBack callBack;
    private Unbinder unbinder;
    private String category;
    private static final String taskTitle = "title";
    private static final String taskDesc = "desc";
    private int alarmTimeSelected;
    private int repeatCategory;

    static DetailsDialog getInstance(){
        return new DetailsDialog();
    }

    @Override
    public void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);
        setRetainInstance(true);

        callBack = (DetailsCallBack) getTargetFragment();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle inState){
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View view = inflater.inflate(R.layout.task_details_layout, null, false);
        unbinder = ButterKnife.bind(this, view);

        if (inState != null){
            String title = inState.getString(taskTitle);
            String desc = inState.getString(taskDesc);

            titleView.setText(title);
            descView.setText(desc);
        }

        AlertDialog.Builder dialog;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            dialog  = new AlertDialog.Builder(getActivity(),
                    android.R.style.Theme_Material_Light_Dialog_Alert);
        }else {
            dialog = new AlertDialog.Builder(getActivity());
        }

        setUpCategories();
        setUpRepeatTasks();
        setUpAlarmTime();

        return dialog.setTitle("Input Task Details")
                .setView(view)
                .setPositiveButton("Submit", ((dialogInterface, i) -> {
                    String taskTitle = titleView.getText().toString();
                    String taskDescription = descView.getText().toString();

                    callBack.showCalenderDialog(taskTitle, taskDescription,
                            category, repeatCategory, alarmTimeSelected);
                }))
                .create();
    }

    private void setUpCategories(){
        ArrayAdapter<CharSequence> categoryAdapter = ArrayAdapter.
                createFromResource(getActivity(), R.array.categories,
                        android.R.layout.simple_spinner_item);
        categoryAdapter.setDropDownViewResource
                (android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(categoryAdapter);
        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i){
                    case 0:
                        //header
                        break;
                    case 1:
                        category = categories[1];
                        break;
                    case 2:
                        category = categories[2];
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                category = categories[2];
            }
        });
    }

    private void setUpAlarmTime() {
        ArrayAdapter<CharSequence> alarmTimeAdapter = ArrayAdapter
                .createFromResource(getContext(), R.array.alarm_time,
                        android.R.layout.simple_spinner_item);
        alarmTimeAdapter.setDropDownViewResource
                (android.R.layout.simple_spinner_dropdown_item);
        alarmSpinner.setAdapter(alarmTimeAdapter);

        alarmSpinner.setOnItemSelectedListener(new AdapterView
                .OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view,
                                       int position, long id) {
                switch (position){
                    case 0:
                        //header
                        break;
                    case 1:
                        alarmTimeSelected = 0;
                        //at task's time
                        break;
                    case 2:
                        alarmTimeSelected = 1;
                        //15 before task
                        break;
                    case 3:
                        alarmTimeSelected = 2;
                        //30 before task
                        break;
                    case 4:
                        alarmTimeSelected = 3;
                        //45 before task
                        break;
                    case 5:
                        alarmTimeSelected = 4;
                        //1 hour before task
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                alarmTimeSelected = 0;
            }
        });
    }

    private void setUpRepeatTasks() {
        ArrayAdapter<CharSequence> repeatAdapter = ArrayAdapter
                .createFromResource(getContext(), R.array.repeat_tasks,
                        android.R.layout.simple_spinner_item);
        repeatAdapter.setDropDownViewResource
                (android.R.layout.simple_spinner_dropdown_item);

        repeatSpinner.setAdapter(repeatAdapter);

        repeatSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view,
                                       int position, long id) {
                switch (position){
                    case 0:
                        //header
                        break;
                    case 1:
                        repeatCategory = 0;
                        //do not repeat
                        break;
                    case 2:
                        repeatCategory = 1;
                        //weekly
                        break;
                    case 3:
                        repeatCategory = 2;
                        //monthly
                        break;
                    case 4:
                        repeatCategory = 3;
                        //yearly
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                repeatCategory = 0;
            }
        });
    }

    @Override
    public void onSaveInstanceState(Bundle outState){
        outState.putString(taskTitle, titleView.getText().toString());
        outState.putString(taskDesc, descView.getText().toString());
    }

    @Override
    public void onDestroyView(){
        unbinder.unbind();
        Dialog dialog = getDialog();

        if (dialog != null && getRetainInstance()){
            dialog.setDismissMessage(null);
        }
        super.onDestroyView();
    }
}
