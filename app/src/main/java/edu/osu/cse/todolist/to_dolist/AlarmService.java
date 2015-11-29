package edu.osu.cse.todolist.to_dolist;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Intent;
import android.location.LocationManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.android.gms.common.api.GoogleApiClient;

import java.util.Date;
import java.util.List;
import java.util.Random;


public class AlarmService extends IntentService {
    private static final String TAG = "AlarmService";
    private String mMessage;
    private WiFiPosition mWiFiPosition;
    private GPSCoordinate mGPSCoordinate;
    private GoogleApiClient mGoogleApiClient;
    private LocationManager mLocationManager;
    private String mProvider;

    public AlarmService() {
        super(TAG);
        Log.d(TAG, "Alarm Service created");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d(TAG, "Receive 1 minute Intent");
        List<Task> tasks = ToDoLab.get(this).getTasks();
        for (Task task : tasks) {
            if (task.isComplete() | (!task.isEnabled())) {
                continue;
            }
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
                    if (wifiInfo != null) {
                        String ssid = wifiInfo[0];
                        String mac = wifiInfo[1];
                        if (ssid.equals(mWiFiPosition.getSSID()) & mac.equals(mWiFiPosition.getBSSID())) {
                            sendNotification(task);
                            Log.d(TAG, task.getTitle() + "  Arriving: " + task.getLocation().getTitle
                                    () + "(WIFI)");
                        }
                    }
                } else if (task.getLocation().getConfig() == Location.ConfigType.GPS & task
                        .getLocation().getGPSCoordinate() != null) {
                    mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

                    List<String> providerList = mLocationManager.getProviders(true);
                    if (providerList.contains(LocationManager.GPS_PROVIDER)) {
                        mProvider = LocationManager.GPS_PROVIDER;
                    } else if (providerList.contains(LocationManager.NETWORK_PROVIDER)) {
                        mProvider = LocationManager.NETWORK_PROVIDER;
                    }
                    try {
                        android.location.Location location = mLocationManager
                                .getLastKnownLocation(mProvider);
                        if (location == null) {
                            location = mLocationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
                        }
                        if (location != null) {
                            Double lng = location.getLongitude();
                            Double lat = location.getLatitude();

                            mGPSCoordinate = task.getLocation().getGPSCoordinate();
                            Double setLat = mGPSCoordinate.getLatitude();
                            Double setLng = mGPSCoordinate.getLongitude();
                            if (Math.abs(setLat - lat) < 0.001 & Math.abs(setLng - lng) < 0.001) {
                                sendNotification(task);
                                Log.d(TAG, task.getTitle() + "  Arriving: " + task.getLocation()
                                        .getTitle() + "(GPS)");
                            }
                        }
                    } catch (SecurityException ex) {
                        Log.d(TAG, "Permission denied.....");
                        continue;
                    } catch (IllegalArgumentException ex) {
                        Log.d(TAG, "No provider");
                        continue;
                    }
                }
            } else if (task.getLocation() != null & task.getConfig() == Task.ConfigType
                    .LOCATION_LEAVING) {
                if (task.getLocation().getConfig() == Location.ConfigType.WiFi & task.getLocation()
                        .getWiFiPosition() != null) {
                    mWiFiPosition = task.getLocation().getWiFiPosition();
                    String[] wifiInfo = WiFiPosition.getCurrentWifiInfo(getBaseContext());
                    if (wifiInfo != null) {
                        String ssid = wifiInfo[0];
                        String mac = wifiInfo[1];
                        if (!ssid.equals(mWiFiPosition.getSSID()) & !mac.equals(mWiFiPosition.getBSSID()
                        )) {
                            sendNotification(task);
                            Log.d(TAG, task.getTitle() + "  leaving: " + task.getLocation().getTitle
                                    () + "(WIFI)");
                        }
                    } else {
                        sendNotification(task);
                        Log.d(TAG, task.getTitle() + "  leaving: " + task.getLocation().getTitle
                                () + "(WIFI)");
                    }
                } else if (task.getLocation().getConfig() == Location.ConfigType.GPS & task
                        .getLocation().getGPSCoordinate() != null) {
                    mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
                    List<String> providerList = mLocationManager.getProviders(true);
                    if (providerList.contains(LocationManager.GPS_PROVIDER)) {
                        mProvider = LocationManager.GPS_PROVIDER;
                    } else if (providerList.contains(LocationManager.NETWORK_PROVIDER)) {
                        mProvider = LocationManager.NETWORK_PROVIDER;
                    }

                    try {
                        android.location.Location location = mLocationManager.getLastKnownLocation(mProvider);
                        if (location == null) {
                            location = mLocationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
                        }
                        if (location != null) {
                            Double lng = location.getLongitude();
                            Double lat = location.getLatitude();

                            mGPSCoordinate = task.getLocation().getGPSCoordinate();
                            Double setLat = mGPSCoordinate.getLatitude();
                            Double setLng = mGPSCoordinate.getLongitude();
                            if (Math.abs(setLat - lat) > 0.001 & Math.abs(setLng - lng) > 0.001) {
                                sendNotification(task);
                                Log.d(TAG, task.getTitle() + "  Leaving: " + task.getLocation()
                                        .getTitle() + "(GPS)");
                            }
                        }
                    } catch (SecurityException ex) {
                        Log.d(TAG, "permission denied");
                        continue;
                    } catch (IllegalArgumentException ex) {
                        Log.d(TAG, "No provider");
                        continue;
                    }
                }
            }
        }
        ToDoLab.get(getApplicationContext()).setupRemindService();
    }

    public void sendNotification(Task task) {
        NotificationManager notificationManager = (NotificationManager) this.getSystemService(NOTIFICATION_SERVICE);
        Intent resultIntent = TaskDetailActivity.newIntent(getBaseContext(), task.getId());
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(TaskDetailActivity.class);
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent intent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        if (task.getConfig() == Task.ConfigType.TIME) {
            mMessage = task.getRemindDateString();
        } else if (task.getConfig() == Task.ConfigType.LOCATION_ARRIVING) {
            mMessage = getString(R.string.location_arriving) + " " + task.getLocation().getTitle();
        } else if (task.getConfig() == Task.ConfigType.LOCATION_LEAVING) {
            mMessage = getString(R.string.location_leaving) + " " + task.getLocation().getTitle();
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
        task.setEnabled(false);
        ToDoLab.get(getApplicationContext()).saveTask(task);
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "Alarm Service Destroyed");
    }
}
