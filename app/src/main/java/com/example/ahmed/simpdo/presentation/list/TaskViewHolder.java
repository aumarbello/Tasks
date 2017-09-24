package com.example.ahmed.simpdo.presentation.list;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.ahmed.simpdo.R;
import com.example.ahmed.simpdo.data.model.Task;
import com.example.ahmed.simpdo.data.pref.TaskPref;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

/**
 * Created by ahmed on 9/23/17.
 */

public class TaskViewHolder extends RecyclerView.ViewHolder{
  private TextView taskTitle;
  private TextView taskDate;
  private TextView isDone;
  private int position;
  private ItemActions actions;
  private Context context;
  private List<Task> taskList;
  private TaskPref pref;

  public TaskViewHolder(View itemView, ItemActions actions,
                        Context context, List<Task> taskList, TaskPref pref) {
    super(itemView);

    taskTitle = itemView.findViewById(R.id.task_title);
    taskDate = itemView.findViewById(R.id.task_time);
    isDone = itemView.findViewById(R.id.task_done);

    itemView.setOnTouchListener(new SwipeTouchListener(context, this));
    this.actions = actions;
    this.context = context;
    this.taskList = taskList;
    this.pref = pref;
  }

  public void bindTask(Task task, int position){
    this.position = position;

    taskTitle.setText(task.getTaskTitle());

    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.US);

    String time = sdf.format(task.getTaskDate().getTime());

    taskDate.setText(time);

    isDone.setVisibility(task.isDone() ? View.VISIBLE: View.GONE);

    int color = pref.getDoneTaskColor();

    if (task.isDone()){
      itemView.setBackgroundColor(color);
    }
  }

  void onClick() {
    Task currentTask = taskList.get(position);
    actions.viewTask(currentTask, position);
  }

  void onDoubleTap(){
    Task currentTask = taskList.get(position);
    currentTask.setDone(true);
    isDone.setVisibility(View.VISIBLE);
    actions.updateTask(currentTask);
    itemView.setBackgroundColor(pref.getDoneTaskColor());
  }

  void onLongClick() {
    Task currentTask = taskList.get(position);
    CardView cardView = (CardView)itemView;
    float initialElevation = cardView.getCardElevation();
    cardView.setCardElevation(cardView.getCardElevation() * 15);
    PopupMenu menu = new PopupMenu(context, itemView);
    menu.inflate(R.menu.pop_up_menu);
    menu.setOnMenuItemClickListener(item -> {
      switch (item.getItemId()){
        case R.id.edit_task:
          actions.editTask(currentTask, position);
          return true;
        case R.id.delete_task:
          actions.deleteTask(currentTask, position);
      }
      return false;
    });

    menu.setOnDismissListener(popupMenu ->
            cardView.setCardElevation(initialElevation));
    menu.show();
  }
}
