package edu.osu.cse.todolist.to_dolist;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import java.util.List;
import java.util.Random;

/**
 * Created by Atwood Wang on 2015/12/7.
 */
public class TimeAlarmService extends IntentService {
    private static final String TAG = "TimeAlarm";
    private String mMessage;

    public TimeAlarmService() {
        super(TAG);
        Log.d(TAG, "Time Alarm Service created");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d(TAG, "Receive time Intent");
        List<Task> inTimeTasks = ToDoLab.get(this).getCurrentTimeTasks();
        Task task = inTimeTasks.get(0);
        for (int i = 0; i < inTimeTasks.size(); i++) {
            Task temp = inTimeTasks.get(i);
            if (temp.getTime() == task.getTime()) {
                sendNotification(temp);
            }
        }
        ToDoLab.get(this).setTimeAlarm();
    }

    public void sendNotification(Task task) {
        NotificationManager notificationManager = (NotificationManager) this.getSystemService(NOTIFICATION_SERVICE);
        Intent resultIntent = TaskDetailActivity.newIntent(getBaseContext(), task.getId());
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(TaskDetailActivity.class);
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent intent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        mMessage = task.getRemindDateString();
        long[] pattern = {1000, 200, 200, 200};
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_notification_icon)
                .setTicker(task.getTitle())
                .setContentTitle(task.getTitle())
                .setContentText(mMessage)
                .setAutoCancel(true)
                .setVibrate(pattern);
        mBuilder.setContentIntent(intent);
        Random random = new Random();
        int i = random.nextInt();
        notificationManager.notify(i, mBuilder.build());
        task.setEnabled(false);
        ToDoLab.get(getApplicationContext()).saveTask(task);
    }
}
