package com.sellersync.jantz.sellersync;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.sql.Array;
import java.util.ArrayList;

import static com.sellersync.jantz.sellersync.RemindersFragment.getAlarmId;
import static java.security.AccessController.getContext;

/**
 * Created by jantz on 6/17/2017.
 */

public class RestartAlarmService extends IntentService{
    DbHelper dbHelper;
    AlarmManager alarm_manager;
    PendingIntent mAlarmIntent;



    public RestartAlarmService() {
        super("RestartAlarmsService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        dbHelper = new DbHelper(getApplicationContext());



        for (int i = 0; i < dbHelper.getReminderIdList().size(); i++){
            int setAlarmId = getAlarmId(getApplication());

            alarm_manager = (AlarmManager) getApplication().getSystemService(Context.ALARM_SERVICE);

            mAlarmIntent = PendingIntent.getBroadcast(getApplication(), setAlarmId, intent, 0);


            alarm_manager.set(AlarmManager.RTC_WAKEUP, dbHelper.getReminderIdList().get(i), mAlarmIntent);

        }



    }

    public static int getAlarmId(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        int alarmId = preferences.getInt("ALARM", 1);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("ALARM", alarmId + 1).apply();
        return alarmId;
    }
}