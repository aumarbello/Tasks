package com.example.ahmed.simpdo.presentation.edit;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

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
@SuppressWarnings("deprecation")
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

    @BindView(R.id.select_alarm_time)
    Spinner alarmSpinner;

    @BindView(R.id.select_repeat_time)
    Spinner repeatSpinner;

    private Task currentTask;
    private Unbinder unbinder;
    private Calendar newCalender;
    private Calendar currentDate;
    private CallBack callBack;

    private static final String TASK_TITLE = "title";
    private static final String TASK_DESC = "desc";
    private static final String TASK_DATE = "date";

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

        setRetainInstance(true);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstance){
        AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());

        View view = View.inflate(getActivity(), R.layout.edit_task_layout, null);
        unbinder = ButterKnife.bind(this, view);
        if (savedInstance != null){
            String title = savedInstance.getString(TASK_TITLE);
            String desc = savedInstance.getString(TASK_DESC);
            Calendar newCal = Calendar.getInstance();
            long dateInMillSecs = savedInstance.getLong(TASK_DATE);
            newCal.setTimeInMillis(dateInMillSecs);

            taskTitle.setText(title);
            taskDesc.setText(desc);

            dateView.setText(dateString(newCal));
            timeView.setText(timeString(newCal));
        }else {
            taskTitle.setText(currentTask.getTaskTitle());
            taskDesc.setText(currentTask.getTaskDesc());

            dateView.setText(dateString(currentTask.getTaskDate()));
            timeView.setText(timeString(currentTask.getTaskDate()));
        }


        dateView.setOnClickListener(v -> openDateDialog());
        timeView.setOnClickListener(v -> openTimeDialog());

        setUpRepeatTaskSpinner();
        setUpAlarmTimeSpinner();

        AlertDialog alertDialog = dialog.setView(view)
                .setTitle("Edit Task")
                .setPositiveButton("Delete", (dialogInterface, i) -> {
                    if (currentTask.getRepeatCategory() == 0){
                        presenter.deleteTask(currentTask, false);
                        callBack.updateAfterDelete(currentTask);
                    }else {
                        showDeleteAllDialog();
                    }
                })
                .setNegativeButton("Save", ((dialogInterface, i) -> {
                    saveTask();
                    callBack.updateView(currentTask);
                }))
                .create();

        alertDialog.setOnShowListener(dialogInterface -> {
            alertDialog.getButton(DialogInterface.BUTTON_POSITIVE)
                    .setTextColor(getResources().getColor(R.color.colorAccent));
            alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE)
                    .setTextColor(getResources().getColor(R.color.colorAccent));
        });

        return alertDialog;
    }

    private void showDeleteAllDialog() {
        AlertDialog dialog = new AlertDialog.Builder(getActivity())
                .setTitle("Delete All")
                .setMessage("Delete All Occurrence of this Task?")
                .setPositiveButton("No", (dialogInterface, i) -> {
                    dialogInterface.dismiss();
                    presenter.deleteTask(currentTask, false);
                    callBack.updateAfterDelete(currentTask);
                })
                .setNegativeButton("Yes", (dialogInterface, i) -> {
                    dialogInterface.dismiss();
                    presenter.deleteTask(currentTask, true);
                    callBack.updateAfterDelete(currentTask);
                })
                .create();
        dialog.show();
    }

    private void setUpAlarmTimeSpinner() {
        ArrayAdapter<CharSequence> alarmAdapter = ArrayAdapter
                .createFromResource(getContext(), R.array.alarm_time,
                        android.R.layout.simple_spinner_item);
        alarmAdapter.setDropDownViewResource
                (android.R.layout.simple_spinner_dropdown_item);
        alarmSpinner.setAdapter(alarmAdapter);
        alarmSpinner.setSelection(currentTask.getAlarmTime() + 1);
        alarmSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view,
                                       int position, long id) {
                switch (position){
                    case 0:
                        //header
                        break;
                    case 1:
                        currentTask.setAlarmTime(0);
                        //at task's time
                        break;
                    case 2:
                        currentTask.setAlarmTime(1);
                        //15 before task
                        break;
                    case 3:
                        currentTask.setAlarmTime(2);
                        //30 before task
                        break;
                    case 4:
                        currentTask.setAlarmTime(3);
                        //45 before task
                        break;
                    case 5:
                        currentTask.setAlarmTime(4);
                        //1 hour before task
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void setUpRepeatTaskSpinner() {
        ArrayAdapter<CharSequence> repeatAdapter = ArrayAdapter
                .createFromResource(getContext(), R.array.repeat_tasks,
                        android.R.layout.simple_spinner_item);
        repeatAdapter.setDropDownViewResource
                (android.R.layout.simple_spinner_dropdown_item);

        repeatSpinner.setAdapter(repeatAdapter);
        repeatSpinner.setSelection(currentTask.getRepeatCategory() + 1);
        repeatSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view,
                                       int position, long id) {
                switch (position){
                    case 0:
                        //header
                        break;
                    case 1:
                        currentTask.setRepeatCategory(0);
                        //do not repeat
                        break;
                    case 2:
                        currentTask.setRepeatCategory(1);
                        //weekly
                        break;
                    case 3:
                        currentTask.setRepeatCategory(2);
                        //monthly
                        break;
                    case 4:
                        currentTask.setRepeatCategory(3);
                        //yearly
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private String dateString(Calendar calendar){
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy", Locale.US);
        return sdf.format(calendar.getTime());
    }

    private String timeString(Calendar calendar){
        SimpleDateFormat sdf = new SimpleDateFormat("HH: mm", Locale.US);
        return sdf.format(calendar.getTime());
    }

    private void openDateDialog(){
        DatePickerDialog dateDialog;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            dateDialog = new DatePickerDialog(getActivity(),
                    (datePicker, year, month, day) -> {
                        newCalender.set(year, month, day);
                        dateView.setText(dateString(newCalender));
                    },
                    currentDate.get(Calendar.YEAR),
                    currentDate.get(Calendar.MONTH),
                    currentDate.get(Calendar.DAY_OF_MONTH));
        }else {
            dateDialog = new DatePickerDialog(getActivity(),
                    null,
                    currentDate.get(Calendar.YEAR),
                    currentDate.get(Calendar.MONTH),
                    currentDate.get(Calendar.DAY_OF_MONTH));

            dateDialog.setButton(DialogInterface.BUTTON_POSITIVE,
                    "Submit", (dialogInterface, i) -> {
                        int year = dateDialog.getDatePicker().getYear();
                        int month = dateDialog.getDatePicker().getMonth();
                        int day = dateDialog.getDatePicker().getDayOfMonth();
                        newCalender.set(year, month, day);
                        dialogInterface.dismiss();
                    });
        }
        dateDialog.setTitle("Select New Task Date");
        dateDialog.show();
    }

    private void openTimeDialog(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            TimePickerDialog timeDialog = new TimePickerDialog(getActivity(),
                    (timePicker, hour, minute) -> {
                        newCalender.set(Calendar.HOUR_OF_DAY, hour);
                        newCalender.set(Calendar.MINUTE, minute);
                        timeView.setText(timeString(newCalender));
                    },
                    currentDate.get(Calendar.HOUR_OF_DAY),
                    currentDate.get(Calendar.MINUTE), false);
            timeDialog.setTitle("Select New Task Time");
            timeDialog.show();
        }else {
            TimePicker picker = new TimePicker(getActivity());
            picker.setOnTimeChangedListener((timePicker, hour, minute) -> {
                newCalender.set(Calendar.HOUR_OF_DAY, hour);
                newCalender.set(Calendar.MINUTE, minute);

            });
            AlertDialog timeDialog = new AlertDialog.Builder(getActivity())
                    .setView(picker)
                    .setPositiveButton("Submit", (dialogInterface, i) -> {
                        dialogInterface.dismiss();
                        timeView.setText(timeString(newCalender));
                    })
                    .setTitle("Select New Task Time")
                    .create();

            timeDialog.show();
        }
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

    @Override
    public void onSaveInstanceState(Bundle bundle){
        bundle.putString(TASK_TITLE, taskTitle.getText().toString());
        bundle.putString(TASK_DESC, taskDesc.getText().toString());
        bundle.putLong(TASK_DATE, newCalender.getTimeInMillis());
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
