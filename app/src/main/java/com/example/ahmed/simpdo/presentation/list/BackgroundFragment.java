package com.example.ahmed.simpdo.presentation.list;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.example.ahmed.simpdo.presentation.notifications.DailyService;
import com.example.ahmed.simpdo.presentation.notifications.IndividualService;

/**
 * Created by ahmed on 9/8/17.
 */

public abstract class BackgroundFragment extends Fragment {
    private static final String TAG = "BackGroundFragment";

    private BroadcastReceiver importantReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Receiving this intent means the fragment is visible
            // hence it's cancelled
            Log.d(TAG, "canceling individual notification");
            setResultCode(Activity.RESULT_CANCELED);
        }
    };

    private BroadcastReceiver dailyReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Receiving this intent means the fragment is visible
            // hence it's cancelled
            Log.d(TAG, "canceling daily notification");
            setResultCode(Activity.RESULT_CANCELED);
        }
    };

    @Override
    public void onStart(){
        super.onStart();

        //individual filter
        IntentFilter filter = new IntentFilter(IndividualService.IMPORTANT_SERVICE);
        getActivity().registerReceiver(importantReceiver, filter,
                IndividualService.PRIVATE, null);

        //daily filter
        IntentFilter dailyFilter = new IntentFilter(DailyService.DAILY_SERVICE);
        getActivity().registerReceiver(dailyReceiver, dailyFilter,
                DailyService.DAILY_PRIVATE, null);
    }

    @Override
    public void onStop(){
        super.onStop();

        getActivity().unregisterReceiver(importantReceiver);
        getActivity().unregisterReceiver(dailyReceiver);
    }
}
