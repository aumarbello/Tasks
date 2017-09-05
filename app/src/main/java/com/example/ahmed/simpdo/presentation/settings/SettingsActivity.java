package com.example.ahmed.simpdo.presentation.settings;

import android.content.Context;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.MenuItem;

import com.example.ahmed.simpdo.R;

/**
 * Created by ahmed on 9/4/17.
 */

public class SettingsActivity extends PreferenceCompat {
    private static Preference.OnPreferenceChangeListener
            sBindPreferenceSummaryToValueListener = (preference, value) -> {
        String stringValue = value.toString();

        if (preference instanceof ListPreference) {
            ListPreference listPreference = (ListPreference) preference;
            int index = listPreference.findIndexOfValue(stringValue);

            preference.setSummary(
                    index >= 0
                            ? listPreference.getEntries()[index]
                            : null);
        } else {
            preference.setSummary(stringValue);
        }
        return true;
    };

    private static Preference.OnPreferenceChangeListener
            listListener = (preference, value) -> {
        String stringValue = value.toString();

        ListPreference listPreference = (ListPreference) preference;
        int index = listPreference.findIndexOfValue(stringValue);

        preference.setSummary(
                index >= 0
                        ? listPreference.getEntries()[index]
                        : null);

        Log.d("Settings Activity", "Number of days selected changed to " + stringValue);
        return true;
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        PreferenceManager.setDefaultValues(this, R.xml.settings_xml, false);

        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new GeneralPreferenceFragment())
                .commit();
    }

    private static void bindPreferenceSummaryToValue(Preference preference) {
        preference.setOnPreferenceChangeListener(sBindPreferenceSummaryToValueListener);
        sBindPreferenceSummaryToValueListener.onPreferenceChange(preference,
                PreferenceManager
                        .getDefaultSharedPreferences(preference.getContext())
                        .getString(preference.getKey(), ""));
    }

    protected boolean isValidFragment(String fragmentName) {
        return PreferenceFragment.class.getName().equals(fragmentName)
                || GeneralPreferenceFragment.class.getName().equals(fragmentName);
    }

    public boolean onOptionsItemSelected(MenuItem item){
        if (item.getItemId() == android.R.id.home){
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    private static void sendMail(Context context){
        //todo intent to send mail
    }

    private static void bindListPreference(Preference preference) {
        preference.setOnPreferenceChangeListener(listListener);
        listListener.onPreferenceChange(preference,
                PreferenceManager
                        .getDefaultSharedPreferences(preference.getContext())
                        .getString(preference.getKey(), ""));
    }

    public static class GeneralPreferenceFragment extends PreferenceFragment{
        private static ListPreference daysList;
        private static ListPreference numberList;

        @Override
        public void onCreate(Bundle savedInstance) {
            super.onCreate(savedInstance);

            addPreferencesFromResource(R.xml.settings_xml);

            numberList = (ListPreference) findPreference(getString(R.string.number_of_days_key));

            daysList = (ListPreference) findPreference(getString
                    (R.string.select_day_key));

            daysList.setEntries(getResources().getStringArray(
                    R.array.five_days_array_entries));
            daysList.setEntryValues(getResources().getStringArray(
                    R.array.five_days_array_values));

            bindPreferenceSummaryToValue(findPreference(getString
                    (R.string.show_notification_key)));
            bindPreferenceSummaryToValue(daysList);
            bindListPreference(numberList);

            Preference mailPref = findPreference(getString
                    (R.string.contact_developer_key));
            mailPref.setOnPreferenceClickListener(preference -> {
                sendMail(getActivity());
                return true;
            });

//            numberList.setOnPreferenceChangeListener((preference, o) -> {
//                Context context = preference.getContext();
//
//                int intValue = Integer.parseInt(o.toString());
//
//                switch (intValue){
//                    case 3:
//                        daysList.setEntries(getResources().getStringArray(
//                                R.array.three_days_array_entries));
//                        daysList.setEntryValues(getResources().getStringArray(
//                                R.array.three_days_array_values));
//                        break;
//                    case 5:
//                        daysList.setEntries(getResources().getStringArray(
//                                R.array.five_days_array_entries));
//                        daysList.setEntryValues(getResources().getStringArray(
//                                R.array.five_days_array_values));
//                        break;
//                    case 7:
//                        daysList.setEntries(getResources().getStringArray(
//                                R.array.seven_days_array_entries));
//                        daysList.setEntryValues(getResources().getStringArray(
//                                R.array.seven_days_array_values));
//                        break;
//                }
//                return true;
//            });

//            SharedPreferences sharedPreferences = PreferenceManager
//                    .getDefaultSharedPreferences(getActivity());
//            String value = sharedPreferences.getString(getString
//                    (R.string.select_day_key), "3");
//
//            int intValue = Integer.parseInt(value);
//
//            switch (intValue) {
//                case 3:
//                    daysList.setEntries(getResources()
//                            .getStringArray(R.array.three_days_array_entries));
//                    break;
//                case 5:
//                    daysList.setEntries(getResources()
//                            .getStringArray(R.array.five_days_array_entries));
//                    break;
//                case 7:
//                    daysList.setEntries(getResources()
//                            .getStringArray(R.array.seven_days_array_entries));
//                    break;
//            }
        }
    }
}
