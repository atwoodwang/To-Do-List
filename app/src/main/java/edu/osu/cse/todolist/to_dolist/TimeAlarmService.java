package edu.osu.cse.todolist.to_dolist;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Intent;
import android.content.Context;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import java.util.Date;
import java.util.List;


public class TimeAlarmService extends IntentService {
    private static final String TAG = "TimeAlarmService";
    public TimeAlarmService(){
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent){
        List<Task> tasks = ToDoLab.get(this).getTasks();
        for (Task task:tasks){
            if(task.getSchedule()!=null&task.getConfig()==Task.ConfigType.TIME){
                long setTime = task.getRemindDate().getTime();
                long currentTime = new Date().getTime();
                if(setTime<currentTime&currentTime<setTime+60*1000){
                    sendNotification(task.getTitle());
                    Log.d(TAG,task.getTitle());
                }
            }
        }
    }

    public void sendNotification(String msg){
        NotificationManager notificationManager = (NotificationManager)this.getSystemService(NOTIFICATION_SERVICE);
        Intent resultIntent = new Intent(this,TaskListActivity.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(TaskListActivity.class);
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent intent = stackBuilder.getPendingIntent(0,PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(android.R.drawable.stat_sys_warning)
                .setContentTitle(msg)
                .setContentText("aaa");

        mBuilder.setContentIntent(intent);
        notificationManager.notify(1,mBuilder.build());

    }

}
