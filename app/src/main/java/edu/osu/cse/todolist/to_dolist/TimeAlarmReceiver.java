package edu.osu.cse.todolist.to_dolist;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;

/**
 * Created by Zicong on 2015/11/8.
 */
public class TimeAlarmReceiver extends WakefulBroadcastReceiver{
    private AlarmManager mTimemAlarmManager;
    private PendingIntent mTimeAlarmIntent;
    private static final String TAG = "RECEIVER";
    private static final int INTERVAL = 1000*10;

    @Override
    public void onReceive(Context context,Intent intent){
        Intent service = new Intent(context,TimeAlarmService.class);
        startWakefulService(context,service);
    }


    public void setAlarm(Context context){
        mTimemAlarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context,TimeAlarmReceiver.class);
        mTimeAlarmIntent = PendingIntent.getBroadcast(context,0,intent,0);
        mTimemAlarmManager.setRepeating(AlarmManager.RTC_WAKEUP,System.currentTimeMillis(),INTERVAL,mTimeAlarmIntent);

        ComponentName receiver = new ComponentName(context, TimeAlarmReceiver.class);
        PackageManager packageManager = context.getPackageManager();

        packageManager.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP);
    }

    public void cancelAlarm(Context context){
        if(mTimemAlarmManager!=null){
            mTimemAlarmManager.cancel(mTimeAlarmIntent);
        }

        ComponentName receiver = new ComponentName(context, TimeAlarmReceiver.class);
        PackageManager packageManager = context.getPackageManager();

        packageManager.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                PackageManager.DONT_KILL_APP);

    }

}
