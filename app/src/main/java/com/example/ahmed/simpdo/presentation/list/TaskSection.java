package com.example.ahmed.simpdo.presentation.list;

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

import io.github.luizgrp.sectionedrecyclerviewadapter.Section;
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionParameters;

/**
 * Created by ahmed on 8/25/17.
 */

class TaskSection extends Section{
    private String title;
    private List<Task> taskList;
    private TaskListFragment taskListFragment;
    private TaskPref pref;
    TaskSection(String title, List<Task> taskList, TaskListFragment taskListFragment,
                TaskPref pref) {
        super(new SectionParameters.Builder(R.layout.task_list_item)
                    .headerResourceId(R.layout.task_item_header)
                    .emptyResourceId(R.layout.empty_list)
                     .build());

        this.title = title;
        this.taskList = taskList;
        this.taskListFragment = taskListFragment;
        this.pref = pref;
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
        taskHolder.bindTask(taskList.get(position), taskHolder.getAdapterPosition());
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

    @Override
    public RecyclerView.ViewHolder getEmptyViewHolder(View view){
        return new TaskEmptyHolder(view);
    }

    @Override
    public void onBindEmptyViewHolder(RecyclerView.ViewHolder holder){
        TaskEmptyHolder emptyHolder = (TaskEmptyHolder) holder;
        emptyHolder.bindEmptyView(title);
    }

    int addTaskToList(Task task){
        taskList.add(task);
        return taskList.indexOf(task) + 1;
    }

    void removeFromList(Task removedTask){
        taskList.remove(removedTask);
    }

    String getSectionTitle(){
        return title;
    }

    boolean isSectionEmpty(){
        return taskList.isEmpty();
    }

    class TaskViewHolder extends RecyclerView.ViewHolder{
        private TextView taskTitle;
        private TextView taskDate;
        private TextView isDone;
        private Task task;
        private int position;

        TaskViewHolder(View itemView) {
            super(itemView);

            taskTitle = itemView.findViewById(R.id.task_title);
            taskDate = itemView.findViewById(R.id.task_time);
            isDone = itemView.findViewById(R.id.task_done);

            itemView.setOnTouchListener(new SwipeTouchListener(
                    taskListFragment.getActivity(), this));
        }

        void bindTask(Task task, int position){
            this.task = task;
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
            taskListFragment.viewTask(task, position);
        }

        void onDoubleTap(){
            task.setDone(true);
            isDone.setVisibility(View.VISIBLE);
            taskListFragment.updateTask(task);
            itemView.setBackgroundColor(pref.getDoneTaskColor());
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
                        taskListFragment.editTask(task, position);
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
                case "Previous": {
                    headerView.setText(headerTitle);
                    int color = headerView.getResources().getColor(R.color.previous);
                    headerView.setBackgroundColor(color);
                    break;
                }
                case "Others": {
                    headerView.setText(headerTitle);
                    int color = headerView.getResources().getColor(R.color.others);
                    headerView.setBackgroundColor(color);
                    break;
                }
                default: {
                    headerView.setText(headerTitle);
                    int color = headerView.getResources().getColor(R.color.daily);
                    headerView.setBackgroundColor(color);
                    break;
                }
            }
        }
    }

    private class TaskEmptyHolder extends RecyclerView.ViewHolder{
        private TextView emptyView;
        TaskEmptyHolder(View itemView) {
            super(itemView);

            emptyView = itemView.findViewById(R.id.empty_list_view);
        }

        void bindEmptyView(String segment){
            emptyView.setText(taskListFragment.getString((R.string.no_tasks), segment));
        }
    }
}
