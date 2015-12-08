package edu.osu.cse.todolist.to_dolist;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.util.Pair;

import java.util.List;
import java.util.Random;

/**
 * Created by Sniper on 2015/12/7.
 */
public class WiFiReceiver extends BroadcastReceiver {
    private String TAG = "WiFiReceiver";

    private final static String PREFERENCE_NAME = "LastConnectedWiFi";
    private final static String ENABLE_WIFI_REMIND = "EnableWiFiRemind";
    private final static String LAST_CONNECTED_SSID = "LastConnectedSSID";
    private final static String LAST_CONNECTED_BSSID = "LastConnectedBSSID";

    @Override
    public void onReceive(Context context, Intent intent) {
        // bypass Intent if there is no task need to be remind by WiFi
        if (!isWiFiRemindEnabled(context)) {
            return;
        }

        String ssid, bssid;
        final String action = intent.getAction();

        if (WifiManager.NETWORK_STATE_CHANGED_ACTION.equals(action)) {
            NetworkInfo networkInfo = intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
            NetworkInfo.DetailedState state = networkInfo.getDetailedState();
            String extraBSSID = intent.getStringExtra(WifiManager.EXTRA_BSSID);
            Log.d(TAG, "onReceive called, [Action] " + action + ", [State] " + state.toString()
                    + ", [Extra BSSID] " + extraBSSID);

            // Connected to a WiFi AP (CONNECTED state with BSSID)
            if (NetworkInfo.DetailedState.CONNECTED.equals(state) && extraBSSID != null) {
                final WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
                final WifiInfo connectionInfo = wifiManager.getConnectionInfo();
                if (connectionInfo != null && connectionInfo.getBSSID() != null) {
                    ssid = connectionInfo.getSSID();
                    bssid = connectionInfo.getBSSID().toUpperCase();
                    // Save the current connected WiFi info to LastConnectedSSID and
                    // LastConnectedBSSID
                    setLastConnectedWiFi(context, ssid, bssid);
                    Log.d(TAG, String.format("[+] WiFi Connected to %s -> %s", ssid, bssid));
                    // TODO: task remind
                    List<Task> tasks = ToDoLab.get(context).getLocationArrivingTasks();
                    for (Task task : tasks) {
                        WiFiPosition wiFiPosition = task.getLocation().getWiFiPosition();
                        if (wiFiPosition.getSSID().equals(ssid) &&
                                wiFiPosition.getBSSID().equals(bssid)) {
                            sendNotification(context, task);
                            Log.d(TAG, task.getTitle() + "  Arriving: " + task.getLocation().getTitle
                                    () + "(WIFI)");
                        }
                    }
                }
                // Disconnected from a WiFi AP (DISCONNECTED state with BSSID)
            } else if (NetworkInfo.DetailedState.DISCONNECTED.equals(state) && extraBSSID != null) {
                Pair<String, String> APInfo = getLastConnectedWiFi(context);
                ssid = APInfo.first;
                bssid = APInfo.second;
                Log.d(TAG, String.format("[-] WiFi Disconnected from %s -> %s", ssid, bssid));
                // TODO: task remind
                List<Task> tasks = ToDoLab.get(context).getLocationLeavingTasks();
                for (Task task : tasks) {
                    WiFiPosition wiFiPosition = task.getLocation().getWiFiPosition();
                    if (wiFiPosition.getSSID().equals(ssid) &&
                            wiFiPosition.getBSSID().equals(bssid)) {
                        sendNotification(context, task);
                        Log.d(TAG, task.getTitle() + "  Leaving: " + task.getLocation().getTitle
                                () + "(WIFI)");
                    }
                }
            }
        }
    }

    public static void setLastConnectedWiFi(Context context, String SSID, String BSSID) {
        SharedPreferences sharedPref = context.getSharedPreferences(PREFERENCE_NAME,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(LAST_CONNECTED_SSID, SSID);
        editor.putString(LAST_CONNECTED_BSSID, BSSID);
        editor.commit();
    }

    /**
     * Return the SSID and BSSID of last connected WiFi AccessPoint
     *
     * @param context
     * @return SSID and BSSID as Pair<SSID, BSSID>
     */
    public static Pair<String, String> getLastConnectedWiFi(Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences(PREFERENCE_NAME,
                Context.MODE_PRIVATE);
        String ssid = sharedPref.getString(LAST_CONNECTED_SSID, null);
        String bssid = sharedPref.getString(LAST_CONNECTED_BSSID, null);
        return new Pair<>(ssid, bssid);
    }

    public static void setWiFiRemindEnabled(Context context, boolean enabled) {
        SharedPreferences sharedPref = context.getSharedPreferences(PREFERENCE_NAME,
                Context.MODE_PRIVATE);
        sharedPref.edit().putBoolean(ENABLE_WIFI_REMIND, enabled).commit();
    }

    public static boolean isWiFiRemindEnabled(Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences(PREFERENCE_NAME,
                Context.MODE_PRIVATE);
        return sharedPref.getBoolean(ENABLE_WIFI_REMIND, false);
    }

    public void sendNotification(Context context, Task task) {
        String message = "";
        NotificationManager notificationManager = (NotificationManager) context.getSystemService
                (Context.NOTIFICATION_SERVICE);
        Intent resultIntent = TaskDetailActivity.newIntent(context, task.getId());
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(TaskDetailActivity.class);
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent intent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        if (task.getConfig() == Task.ConfigType.LOCATION_ARRIVING) {
            message = context.getString(R.string.location_arriving) + " " + task.getLocation()
                    .getTitle();
        } else if (task.getConfig() == Task.ConfigType.LOCATION_LEAVING) {
            message = context.getString(R.string.location_leaving) + " " + task.getLocation()
                    .getTitle();
        }
        long[] pattern = {1000, 200, 200, 200};
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.ic_notification_icon)
                .setTicker(task.getTitle())
                .setContentTitle(task.getTitle())
                .setContentText(message)
                .setAutoCancel(true)
                .setVibrate(pattern);
        mBuilder.setContentIntent(intent);
        Random random = new Random();
        int i = random.nextInt();
        notificationManager.notify(i, mBuilder.build());
        task.setEnabled(false);
        ToDoLab.get(context).saveTask(task);
    }
}
