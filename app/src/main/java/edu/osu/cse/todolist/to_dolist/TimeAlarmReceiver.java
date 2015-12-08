package edu.osu.cse.todolist.to_dolist;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;

/**
 * Created by Atwood Wang on 2015/12/7.
 */
public class TimeAlarmReceiver extends WakefulBroadcastReceiver {
    private static AlarmManager mAlarmManager;
    private PendingIntent mTimeAlarmIntent;
    private String TAG = "TimeReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent service = new Intent(context, TimeAlarmService.class);
        startWakefulService(context, service);
    }


    public void setAlarm(Context context, Task task) {

        mAlarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, TimeAlarmReceiver.class);
        mTimeAlarmIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            mAlarmManager.setExact(AlarmManager.RTC_WAKEUP, task.getTime(), mTimeAlarmIntent);
        }
        ComponentName receiver = new ComponentName(context, TimeAlarmReceiver.class);
        PackageManager packageManager = context.getPackageManager();

        packageManager.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP);
        Log.d(TAG, "setAlarm called" + task.getRemindDateString());
    }

    public void cancelAlarm(Context context) {
        Log.d(TAG, "cancelAlarm called");
        if (mAlarmManager != null) {
            mAlarmManager.cancel(mTimeAlarmIntent);
            Log.d(TAG, "Alarm cancelled");
        }

        ComponentName receiver = new ComponentName(context, TimeAlarmReceiver.class);
        PackageManager packageManager = context.getPackageManager();

        packageManager.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                PackageManager.DONT_KILL_APP);

    }
}
