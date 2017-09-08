package com.example.ahmed.simpdo.presentation.edit;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
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
import android.widget.TextView;

import com.example.ahmed.simpdo.App;
import com.example.ahmed.simpdo.R;
import com.example.ahmed.simpdo.data.model.Task;
import com.example.ahmed.simpdo.utils.AppConstants;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by ahmed on 8/24/17.
 */

public class EditTaskFragment extends DialogFragment {
    public interface CallBack{
        void updateView(Task task);
        void updateAfterDelete(Task task);
    }

    public EditTaskFragment(){

    }

    @Inject
    EditTaskPresenter presenter;

    @BindView(R.id.edit_task_title)
    AutoCompleteTextView taskTitle;

    @BindView(R.id.edit_task_desc)
    AutoCompleteTextView taskDesc;

    @BindView(R.id.edit_task_date)
    TextView dateView;

    @BindView(R.id.edit_task_time)
    TextView timeView;

    @BindView(R.id.select_new_category)
    Spinner spinner;

    private Task currentTask;
    private Unbinder unbinder;
    private Calendar newCalender;
    private Calendar currentDate;
    private CallBack callBack;

    @Override
    public void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);

        ((App)getActivity().getApplication()).getComponent().inject(this);

        callBack = (CallBack) getTargetFragment();

        currentTask = (Task) getArguments().getSerializable
                (AppConstants.EDIT_EXTRA);
        if (currentTask != null) {
            newCalender = currentTask.getTaskDate();
            currentDate = currentTask.getTaskDate();
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstance){
        AlertDialog.Builder dialog;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            dialog  = new AlertDialog.Builder(getActivity(),
                    android.R.style.Theme_Material_Light_Dialog_Alert);
        }else {
            dialog = new AlertDialog.Builder(getActivity());
        }

        LayoutInflater inflater = LayoutInflater.from(getActivity());

        View view = inflater.inflate(R.layout.edit_task_layout, null, false);
        unbinder = ButterKnife.bind(this, view);

        taskTitle.setText(currentTask.getTaskTitle());
        taskDesc.setText(currentTask.getTaskDesc());

        dateView.setText(dateString(currentTask.getTaskDate()));
        timeView.setText(timeString(currentTask.getTaskDate()));

        dateView.setOnClickListener(v -> openDateDialog());
        timeView.setOnClickListener(v -> openTimeDialog());

        setUpSpinner();

         return dialog.setView(view)
                .setTitle("Edit Task")
                .setPositiveButton("Delete", (dialogInterface, i) -> {
                    presenter.deleteTask(currentTask);
                    callBack.updateAfterDelete(currentTask);
                })
                .setNegativeButton("Save", ((dialogInterface, i) -> {
                    saveTask();
                    callBack.updateView(currentTask);
                }))
                .create();
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        unbinder.unbind();
    }

    private String dateString(Calendar calendar){
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy", Locale.US);
        return sdf.format(calendar.getTime());
    }

    private String timeString(Calendar calendar){
        SimpleDateFormat sdf = new SimpleDateFormat("HH: mm", Locale.US);
        return sdf.format(calendar.getTime());
    }

    private void setUpSpinner(){
        ArrayAdapter<CharSequence> categoryAdapter = ArrayAdapter.
                createFromResource(getActivity(), R.array.categories,
                        android.R.layout.simple_spinner_item);
        categoryAdapter.setDropDownViewResource
                (android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(categoryAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i){
                    case 0:
                        //spinner header
                        break;
                    case 1:
                        currentTask.setUrgent(true);
                        break;
                    case 2:
                        currentTask.setUrgent(false);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void openDateDialog(){
        DatePickerDialog dateDialog;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            dateDialog = new DatePickerDialog(getActivity(),
                    android.R.style.Theme_Material_Light_Dialog_Alert,
                    (datePicker, year, month, day) ->
                            newCalender.set(year, month, day),
                    currentDate.get(Calendar.YEAR),
                    currentDate.get(Calendar.MONTH),
                    currentDate.get(Calendar.DAY_OF_MONTH));
        }else {
            dateDialog = new DatePickerDialog(getActivity(),
                    (datePicker, year, month, day) ->
                            newCalender.set(year, month, day),
                    currentDate.get(Calendar.YEAR),
                    currentDate.get(Calendar.MONTH),
                    currentDate.get(Calendar.DAY_OF_MONTH));
        }
        dateDialog.setTitle("Select New Task Date");
        dateDialog.show();
    }

    private void openTimeDialog(){
        TimePickerDialog timeDialog;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            timeDialog = new TimePickerDialog(getActivity(),
                    android.R.style.Theme_Material_Light_Dialog_Alert,
                    (timePicker, hour, minute) -> {
                        newCalender.set(Calendar.HOUR_OF_DAY, hour);
                        newCalender.set(Calendar.MINUTE, minute);
                    },
                    currentDate.get(Calendar.HOUR_OF_DAY),
                    currentDate.get(Calendar.MINUTE), false);
        }else {
            timeDialog = new TimePickerDialog(getActivity(),
                    (timePicker, hour, minute) -> {
                        newCalender.set(Calendar.HOUR_OF_DAY, hour);
                        newCalender.set(Calendar.MINUTE, minute);
                    },
                    currentDate.get(Calendar.HOUR_OF_DAY),
                    currentDate.get(Calendar.MINUTE), false);
        }
        timeDialog.setTitle("Select New Task Time");
        timeDialog.show();
    }

    private void saveTask(){
        currentTask.setTaskTitle(taskTitle.getText().toString());
        currentTask.setTaskDesc(taskDesc.getText().toString());
        currentTask.setTaskDate(newCalender);

        presenter.editTask(currentTask);
    }

    public static EditTaskFragment getInstance(Task task) {
        EditTaskFragment fragment = new EditTaskFragment();
        Bundle args = new Bundle();
        args.putSerializable(AppConstants.EDIT_EXTRA, task);
        fragment.setArguments(args);
        return fragment;
    }
}
