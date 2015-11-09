package edu.osu.cse.todolist.to_dolist;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;


/**
 * Created by Zicong on 2015/11/8.
 */
public class BootReceiver extends BroadcastReceiver {
    TimeAlarmReceiver mTimeAlarmReceiver = new TimeAlarmReceiver();

    @Override
    public void onReceive(Context context, Intent intent){
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")){
            mTimeAlarmReceiver.setAlarm(context);
        }
    }
}