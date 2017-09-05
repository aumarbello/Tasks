package com.example.ahmed.simpdo.presentation.list;

import android.content.Context;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by ahmed on 8/31/17.
 */

class SwipeTouchListener implements View.OnTouchListener {
    private final GestureDetector detector;
    private TaskSection.TaskViewHolder holder;

    SwipeTouchListener(Context context, TaskSection.TaskViewHolder holder){
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
        public boolean onFling(MotionEvent event1, MotionEvent event2, float velocityX, float velocityY){
            boolean result = false;

                float diffY = event2.getY() - event1.getY();
                float diffX = event2.getX() - event1.getX();

                if (Math.abs(diffX) > Math.abs(diffY)){
                    if (Math.abs(diffX) > SWIPE_THRESHOLD
                            && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD){
                        if (diffX > 0){
                            Log.d("SwipeTouch", "Swiped to the right");
                        }else{
                            holder.onSwipe();
                        }
                        result = true;
                    }else if (Math.abs(diffY) > SWIPE_THRESHOLD
                            && Math.abs(velocityY) > SWIPE_VELOCITY_THRESHOLD){
                        if (diffX > 0){
                            Log.d("SwipeTouch", "Swiped to the bottom");
                        }else {
                            Log.d("SwipeTouch", "Swiped to the top");
                        }
                        result = true;
                    }
                }
            return result;
        }

        @Override
        public void onLongPress(MotionEvent event) {
            holder.onLongClick();
        }

        public boolean onSingleTapUp(MotionEvent event) {
            holder.onClick();
            return true;
        }

    }
}
