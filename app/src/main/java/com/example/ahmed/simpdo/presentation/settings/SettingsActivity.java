package com.example.ahmed.simpdo.presentation.settings;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.view.MenuItem;

import com.example.ahmed.simpdo.R;
import com.example.ahmed.simpdo.presentation.TaskContainer;

/**
 * Created by ahmed on 9/4/17.
 */

public class SettingsActivity extends PreferenceCompat {
    private static ListPreference daysList;
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
        Resources resources = preference.getContext().getResources();

        ListPreference listPreference = (ListPreference) preference;
        int index = listPreference.findIndexOfValue(stringValue);

        preference.setSummary(
                index >= 0
                        ? listPreference.getEntries()[index]
                        : null);
        int intValue = Integer.parseInt(stringValue);

        switch (intValue){
            case 3:
                daysList.setEntries(resources.getStringArray(
                        R.array.three_days_array_entries));
                daysList.setEntryValues(resources.getStringArray(
                        R.array.three_days_array_values));
                break;
            case 5:
                daysList.setEntries(resources.getStringArray(
                        R.array.five_days_array_entries));
                daysList.setEntryValues(resources.getStringArray(
                        R.array.five_days_array_values));
                break;
            case 7:
                daysList.setEntries(resources.getStringArray(
                        R.array.seven_days_array_entries));
                daysList.setEntryValues(resources.getStringArray(
                        R.array.seven_days_array_values));
                break;
        }
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
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("message/rfc822");
        intent.putExtra(Intent.EXTRA_EMAIL, "john@doe.com");
        intent.putExtra(Intent.EXTRA_SUBJECT, "FeedBack on Tasks App");
        context.startActivity(Intent.createChooser(intent, "Send FeedBack Mail"));
    }

    private static void bindListPreference(Preference preference) {
        preference.setOnPreferenceChangeListener(listListener);
        listListener.onPreferenceChange(preference,
                PreferenceManager
                        .getDefaultSharedPreferences(preference.getContext())
                        .getString(preference.getKey(), ""));
    }

//    public void onBackPressed(){
        //todo create new container for taskList and start the activity here
//        Intent intent = new Intent(this, TaskContainer.class);
//        startActivity(intent);
//    }

    public static class GeneralPreferenceFragment extends PreferenceFragment{
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
        }
    }
}
