package com.example.ahmed.simpdo.presentation.view;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.util.Log;

import com.example.ahmed.simpdo.R;
import com.example.ahmed.simpdo.data.model.Task;
import com.example.ahmed.simpdo.utils.AppConstants;

/**
 * Created by ahmed on 8/24/17.
 */

public class ViewTaskFragment extends DialogFragment {
    public ViewTaskFragment(){
    }

    public interface CallBack{
        void editTask(Task task, int position);
    }

    private CallBack callBack;
    private int position;
    private static final String POSITION = "position";

    public static ViewTaskFragment getInstance(Task task, int position){
        ViewTaskFragment fragment = new ViewTaskFragment();
        Bundle args = new Bundle();
        args.putSerializable(AppConstants.ADD_VIEW_EXTRA, task);
        args.putInt(POSITION, position);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);

        callBack = (CallBack) getTargetFragment();

        setRetainInstance(true);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstance){
        Task task = (Task) getArguments().getSerializable
                (AppConstants.ADD_VIEW_EXTRA);

        position = getArguments().getInt(POSITION);
        AlertDialog.Builder dialog;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            dialog  = new AlertDialog.Builder(getActivity(),
                    android.R.style.Theme_Material_Light_Dialog_Alert);
        }else {
            dialog = new AlertDialog.Builder(getActivity());
        }

        String taskString = task == null ? "" : task.toString();

        if (task == null){
            Log.d("ViewTaskFragment", "Task Fragment");
        }

        AlertDialog alertDialog = dialog.setTitle("Task Details")
                .setMessage(taskString)
                //dismiss dialog
                .setPositiveButton("Cancel", (dialogInterface, i) -> dismiss())
                //edit callBack
                .setNegativeButton("Edit", (dialogInterface, i) -> editTask(task))
                .create();

        alertDialog.setOnShowListener(dialogInterface -> {
            alertDialog.getButton(DialogInterface.BUTTON_POSITIVE)
                    .setTextColor(getResources().getColor(R.color.colorAccent));
            alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE)
                    .setTextColor(getResources().getColor(R.color.colorAccent));
        });

        return alertDialog;
    }


    public void editTask(Task task) {
        callBack.editTask(task, position);
    }

    @Override
    public void onDestroyView(){
        Dialog dialog = getDialog();

        if (dialog != null && getRetainInstance()){
            dialog.setDismissMessage(null);
        }
        super.onDestroyView();
    }
}
