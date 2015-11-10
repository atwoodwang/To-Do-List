package edu.osu.cse.todolist.to_dolist;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import java.util.Date;
import java.util.List;
import java.util.Random;


public class AlarmService extends IntentService {
    private static final String TAG = "AlarmService";
    private String mMessage;
    private WiFiPosition mWiFiPosition;
    private GPSCoordinate mGPSCoordinate;
    private GoogleApiClient mGoogleApiClient;

    public AlarmService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d(TAG,"RECEIVE INTENT");
        //TODO CHECK IF TASK IS ALREADY COMPLETED
        List<Task> tasks = ToDoLab.get(this).getTasks();
        for (Task task : tasks) {
            if (task.getSchedule() != null & task.getConfig() == Task.ConfigType.TIME) {
                long setTime = task.getRemindDate().getTime();
                long currentTime = new Date().getTime();
                if (setTime < currentTime & currentTime < setTime + 60 * 1000) {
                    sendNotification(task);
                    Log.d(TAG, task.getTitle());
                }
            } else if (task.getLocation() != null & task.getConfig() == Task.ConfigType
                    .LOCATION_ARRIVING) {
                if (task.getLocation().getConfig() == Location.ConfigType.WiFi & task.getLocation()
                        .getWiFiPosition() != null) {
                    mWiFiPosition = task.getLocation().getWiFiPosition();
                    String[] wifiInfo = WiFiPosition.getCurrentWifiInfo(getBaseContext());
                    if (wifiInfo != null){
                        String ssid = wifiInfo[0];
                        String mac = wifiInfo[1];
                        if (ssid.equals(mWiFiPosition.getSSID()) & mac.equals(mWiFiPosition.getBSSID())) {
                            sendNotification(task);
                            Log.d(TAG, task.getTitle());
                        }
                    }
                }
//                else if(task.getLocation().getConfig()== Location.ConfigType.GPS&task
//                        .getLocation().getGPSCoordinate()!=null){
//                    buildGoogleApiClient();
//                    mGoogleApiClient.connect();
//                    android.location.Location mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
//                            mGoogleApiClient);
//                    if (mLastLocation != null) {
//                        mGPSCoordinate = task.getLocation().getGPSCoordinate();
//                        if(mLastLocation.getLongitude()==mGPSCoordinate.getLongitude()
//                                &mLastLocation.getLatitude()==mGPSCoordinate.getLatitude()){
//                            sendNotification(task);
//                            Log.d(TAG,"arrving at"+task.getLocation().getTitle());
//                            mGoogleApiClient.disconnect();
//                        }
//                    }
//                }
            } else if (task.getLocation() != null & task.getConfig() == Task.ConfigType
                    .LOCATION_LEAVING) {
                if (task.getLocation().getConfig() == Location.ConfigType.WiFi & task.getLocation()
                        .getWiFiPosition() != null) {
                    mWiFiPosition = task.getLocation().getWiFiPosition();
                    String[] wifiInfo = WiFiPosition.getCurrentWifiInfo(getBaseContext());
                    if(wifiInfo != null){
                        String ssid = wifiInfo[0];
                        String mac = wifiInfo[1];
                        if (!ssid.equals(mWiFiPosition.getSSID()) & !mac.equals(mWiFiPosition.getBSSID()
                        )) {
                            sendNotification(task);
                            Log.d(TAG, task.getTitle());
                        }
                    }
                }
            }
        }
    }

    public void sendNotification(Task task) {
        NotificationManager notificationManager = (NotificationManager) this.getSystemService(NOTIFICATION_SERVICE);
        Intent resultIntent = TaskDetailActivity.newIntent(getBaseContext(),task.getId());
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(TaskDetailActivity.class);
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent intent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        if (task.getConfig() == Task.ConfigType.TIME) {
            mMessage = task.getRemindDateString();
        } else if (task.getLocation().getConfig() == Location.ConfigType.GPS) {
            mMessage = task.getLocation().getTitle();
        }
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
    }

//    protected synchronized void buildGoogleApiClient() {
//        mGoogleApiClient = new GoogleApiClient.Builder(this)
//                .addConnectionCallbacks((GoogleApiClient.ConnectionCallbacks) getBaseContext())
//                .addOnConnectionFailedListener((GoogleApiClient.OnConnectionFailedListener) this)
//                .addApi(LocationServices.API)
//                .build();
//    }
}
