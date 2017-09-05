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
        void createTask(String title, String description, String category);
    }

    public DetailsDialog(){

    }

    @BindView(R.id.add_task_title)
    AutoCompleteTextView titleView;

    @BindView(R.id.add_task_description)
    AutoCompleteTextView descView;

    @BindView(R.id.category_spinner)
    Spinner categorySpinner;

    @BindArray(R.array.categories)
    String[] categories;

    private DetailsCallBack callBack;
    private Unbinder unbinder;
    private String category;

    static DetailsDialog getInstance(){
        return new DetailsDialog();
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
        View view = inflater.inflate(R.layout.add_task_layout, null, false);

        callBack = (DetailsCallBack) getTargetFragment();

        unbinder = ButterKnife.bind(this, view);

        setUpSpinner();

        return dialog.setTitle("Input Task Details")
                .setView(view)
                .setPositiveButton("Submit", ((dialogInterface, i) -> {
                    String taskTitle = titleView.getText().toString();
                    String taskDescription = descView.getText().toString();

                    callBack.createTask(taskTitle, taskDescription, category);
                }))
                .create();
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        unbinder.unbind();
    }

    private void setUpSpinner(){
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

            }
        });
    }
}
