package com.sellersync.jantz.sellersync;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.icu.text.DateFormat;
import android.icu.text.DateIntervalFormat;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Handler;
import android.os.Vibrator;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import static android.content.Context.NOTIFICATION_SERVICE;
import static android.support.v4.app.NotificationCompat.PRIORITY_MAX;

public class AlarmReceiver extends BroadcastReceiver {
    DbHelper dbHelper;

    //private static final String TAG = AlarmReceiver.class.getName();


    @Override
    public void onReceive(Context context, Intent intent) {
        final Long currentTime = System.currentTimeMillis();
        dbHelper = new DbHelper(context);
        Long setTime = dbHelper.getAlarmId(currentTime);
        String reminderName = dbHelper.getReminderName(setTime);
        int IntentId = dbHelper.getIntentId(reminderName);

        SharedPreferences pref = context.getApplicationContext().getSharedPreferences("login.conf",Context.MODE_PRIVATE);
        final int remNotPref = pref.getInt("remNotPref",0);


        if ("android.intent.action.BOOT_COMPLETED".equals(intent.getAction())) {
            Intent i = new Intent(context, RestartAlarmService.class);
            ComponentName service = context.startService(i);
        }

        if(remNotPref == 1) {
            //Log.i(TAG, "onReceive");
            Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);

            NotificationCompat.Builder mBuilder =
                    new NotificationCompat.Builder(context)
                            .setAutoCancel(true)
                            .setVibrate(new long[]{5000, 5000, 5000, 5000, 5000})
                            .setLights(Color.GREEN, 12500, 12500)
                            .setSound(uri)
                            .setColor(Color.argb(1, 196, 238, 152))
                            .setSmallIcon(R.drawable.reminders_two)
                            .setContentTitle("SellerSync Reminder")
                            .setContentText(reminderName);

// Sets an ID for the notification
            int mNotificationId = IntentId;
// Gets an instance of the NotificationManager service
            NotificationManager mNotifyMgr =
                    (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
// Builds the notification and issues it.
            mNotifyMgr.notify(mNotificationId, mBuilder.setPriority(PRIORITY_MAX).build());

            // For our recurring task, we'll just display a message
            Toast.makeText(context, reminderName, Toast.LENGTH_SHORT).show();

        }

            //mNotifyMgr.cancel(mNotificationId);                                                                                  // FIXME: 6/18/2017
            //ringtone.stop();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                try {
                    if (dbHelper.getAlarmId(currentTime) != null) {
                        dbHelper.deleteReminder(dbHelper.getReminderName(dbHelper.getAlarmId(currentTime)));
                    }
                } catch (Exception e) {}
            }
        }, 1000);



    }}
