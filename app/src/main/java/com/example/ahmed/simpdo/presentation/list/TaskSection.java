package com.example.ahmed.simpdo.presentation.list;

import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ahmed.simpdo.R;
import com.example.ahmed.simpdo.data.model.Task;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import io.github.luizgrp.sectionedrecyclerviewadapter.SectionParameters;
import io.github.luizgrp.sectionedrecyclerviewadapter.StatelessSection;

/**
 * Created by ahmed on 8/25/17.
 */

class TaskSection extends StatelessSection{
    private String title;
    private List<Task> taskList;
    private TaskList taskListFragment;
    TaskSection(String title, List<Task> taskList, TaskList taskListFragment) {
        super(new SectionParameters.Builder(R.layout.task_list_item)
                    .headerResourceId(R.layout.task_item_header)
                     .build());

        this.title = title;
        this.taskList = taskList;
        this.taskListFragment = taskListFragment;
    }

    @Override
    public int getContentItemsTotal() {
        return taskList.size();
    }

    @Override
    public RecyclerView.ViewHolder getItemViewHolder(View view) {
        return new TaskViewHolder(view);
    }

    @Override
    public void onBindItemViewHolder(RecyclerView.ViewHolder holder, int position) {
        TaskViewHolder taskHolder = (TaskViewHolder) holder;
        taskHolder.bindTask(position);
    }

    @Override
    public RecyclerView.ViewHolder getHeaderViewHolder(View view){
        return new TaskSectionHolder(view);
    }

    @Override
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder) {
        TaskSectionHolder sectionHolder = (TaskSectionHolder) holder;

        sectionHolder.bindHeader(title);
    }

    class TaskViewHolder extends RecyclerView.ViewHolder{
        private TextView taskTitle;
        private TextView taskDate;
        private ImageView isImportant;
        private Task task;
        private int position;

        TaskViewHolder(View itemView) {
            super(itemView);

            taskTitle = itemView.findViewById(R.id.task_title);
            taskDate = itemView.findViewById(R.id.task_time);
            isImportant = itemView.findViewById(R.id.task_important);

            itemView.setOnTouchListener(new SwipeTouchListener(
                    taskListFragment.getActivity(), this));
        }

        void bindTask(int position){
            this.task = taskList.get(position);
            this.position = position;

            taskTitle.setText(task.getTaskTitle());

            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.US);

            String time = sdf.format(task.getTaskDate().getTime());

            taskDate.setText(time);

            isImportant.setVisibility(task.isUrgent() ? View.VISIBLE : View.GONE);
        }

        void onClick() {
            taskListFragment.viewTask(task);
        }

        void onSwipe(){
            Animation leave = AnimationUtils.loadAnimation(taskListFragment.getActivity(), R.anim.leave_left);
            itemView.startAnimation(leave);
            leave.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    leave();
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
        }

        void leave(){
            Animation enter = AnimationUtils.loadAnimation(taskListFragment.getActivity(), R.anim.enter_right);
            itemView.setBackgroundColor(Color.BLUE);
            itemView.startAnimation(enter);
        }

        void onLongClick() {
            CardView cardView = (CardView)itemView;
            float initialElevation = cardView.getCardElevation();
            cardView.setCardElevation(cardView.getCardElevation() * 15);
            PopupMenu menu = new PopupMenu(taskListFragment.getActivity(),
                    itemView);
            menu.inflate(R.menu.pop_up_menu);
            menu.setOnMenuItemClickListener(item -> {
                switch (item.getItemId()){
                    case R.id.edit_task:
                        taskListFragment.editTask(task);
                        return true;
                    case R.id.delete_task:
                        taskListFragment.deleteTask(task, position);
                }
                return false;
            });

            menu.setOnDismissListener(popupMenu ->
                    cardView.setCardElevation(initialElevation));
            menu.show();
        }
    }

    private class TaskSectionHolder extends RecyclerView.ViewHolder{
        private TextView headerView;
        TaskSectionHolder(View itemView) {
            super(itemView);
            headerView = itemView.findViewById(R.id.task_header);
        }

        @SuppressWarnings("deprecation")
        void bindHeader(String headerTitle){
            switch (headerTitle) {
                case "Today": {
                    headerView.setText(headerTitle);
                    int color = headerView.getResources().getColor(R.color.today);
                    headerView.setBackgroundColor(color);
                    break;
                }
                case "Tomorrow": {
                    headerView.setText(headerTitle);
                    int color = headerView.getResources().getColor(R.color.tomorrow);
                    headerView.setBackgroundColor(color);
                    break;
                }
                default: {
                    headerView.setText(headerTitle);
                    int color = headerView.getResources().getColor(R.color.rest);
                    headerView.setBackgroundColor(color);
                    break;
                }
            }
        }
    }
}
