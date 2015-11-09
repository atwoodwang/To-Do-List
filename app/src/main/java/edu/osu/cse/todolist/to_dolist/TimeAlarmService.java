package edu.osu.cse.todolist.to_dolist;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Intent;
import android.content.Context;
import android.os.Vibrator;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import java.util.Date;
import java.util.List;


public class TimeAlarmService extends IntentService {
    private static final String TAG = "TimeAlarmService";
    private String mMessage;
    private WiFiPosition mWiFiPosition;

    public TimeAlarmService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        //TODO CHECK IF TASK IS ALREADY COMPLETED
        List<Task> tasks = ToDoLab.get(this).getTasks();
        for (Task task : tasks) {
            if (task.getSchedule() != null & task.getConfig() == Task.ConfigType.TIME) {
                long setTime = task.getRemindDate().getTime();
                long currentTime = new Date().getTime();
                if (setTime < currentTime & currentTime < setTime + 60 * 1000) {
                    sendNotification(task);
//                    Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
//                    long[] pattern = {1000, 200, 200, 200};
//                    vibrator.vibrate(pattern, 0);
                    Log.d(TAG, task.getTitle());
                }
            } else if (task.getLocation() != null & task.getConfig() == Task.ConfigType
                    .LOCATION_ARRIVING) {
                if (task.getLocation().getConfig() == Location.ConfigType.WiFi & task.getLocation()
                        .getWiFiPosition() != null) {
                    mWiFiPosition = task.getLocation().getWiFiPosition();
                    String[] wifiInfo = WiFiPosition.getCurrentWifiInfo(getBaseContext());
                    String ssid = wifiInfo[0];
                    String mac = wifiInfo[1];
                    if (ssid.equals(mWiFiPosition.getSSID()) & mac.equals(mWiFiPosition.getMAC())) {
                        sendNotification(task);
                        Log.d(TAG, task.getTitle());
                    }
                }
            } else if (task.getLocation() != null & task.getConfig() == Task.ConfigType
                    .LOCATION_LEAVING) {
                if (task.getLocation().getConfig() == Location.ConfigType.WiFi & task.getLocation()
                        .getWiFiPosition() != null) {
                    mWiFiPosition = task.getLocation().getWiFiPosition();
                    String[] wifiInfo = WiFiPosition.getCurrentWifiInfo(getBaseContext());
                    String ssid = wifiInfo[0];
                    String mac = wifiInfo[1];
                    if (!ssid.equals(mWiFiPosition.getSSID()) & !mac.equals(mWiFiPosition.getMAC()
                    )) {
                        sendNotification(task);
                        Log.d(TAG, task.getTitle());
                    }
                }
            }
        }
    }

    public void sendNotification(Task task) {
        NotificationManager notificationManager = (NotificationManager) this.getSystemService(NOTIFICATION_SERVICE);
        Intent resultIntent = new Intent(this, TaskListActivity.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(TaskListActivity.class);
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent intent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        if (task.getConfig() == Task.ConfigType.TIME) {
            mMessage = task.getRemindDateString();
        } else if (task.getLocation().getConfig() == Location.ConfigType.GPS) {
            mMessage = task.getLocation().getTitle();
        }

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(android.R.drawable.stat_sys_warning)
                .setContentTitle(task.getTitle())
                .setContentText(mMessage);
        mBuilder.setContentIntent(intent);
        notificationManager.notify(1, mBuilder.build());
    }
}
