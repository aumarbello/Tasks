package com.example.ahmed.simpdo.presentation.list;

import android.content.Context;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by ahmed on 8/31/17.
 */

class SwipeTouchListener implements View.OnTouchListener {
    private final GestureDetector detector;
    private TaskViewHolder holder;

    SwipeTouchListener(Context context, TaskViewHolder holder){
        detector = new GestureDetector(context, new ListeningGesture());
        this.holder = holder;
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        return detector.onTouchEvent(motionEvent);
    }

    private class ListeningGesture extends
            GestureDetector.SimpleOnGestureListener{
        private static final int SWIPE_THRESHOLD = 100;
        private static final int SWIPE_VELOCITY_THRESHOLD = 100;

        @Override
        public boolean onDown(MotionEvent event){
            return true;
        }

        @Override
        public void onLongPress(MotionEvent event) {
            holder.onLongClick();
        }

        @Override
        public boolean onSingleTapConfirmed(MotionEvent event) {
            holder.onClick();
            return true;
        }

        @Override
        public boolean onDoubleTap(MotionEvent event){
            holder.onDoubleTap();
            return true;
        }
    }
}
