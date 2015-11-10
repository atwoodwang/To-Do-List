package edu.osu.cse.todolist.to_dolist;

import android.content.ContentValues;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.text.TextUtils;

import edu.osu.cse.todolist.to_dolist.database.ToDoDbSchema.WiFiPositionTable;

/**
 * Created by Sniper on 2015/11/3.
 */
public class WiFiPosition extends Model {

    private String mSSID;
    private String mBSSID;

    // The MaxSignal and MinSignal defines a range of signal strength
    private double mMaxSignal;
    private double mMinSignal;

    public WiFiPosition() {
        this(-1);
    }

    public WiFiPosition(long id) {
        super(id);
        mSSID = "";
        mBSSID = "";
        mMaxSignal = 100;
        mMinSignal = 1;
    }

    public String getSSID() {
        return mSSID;
    }

    public void setSSID(String SSID) {
        mSSID = SSID;
    }

    public String getBSSID() {
        return mBSSID;
    }

    public void setBSSID(String BSSID) {
        mBSSID = BSSID;
    }

    public double getMaxSignal() {
        return mMaxSignal;
    }

    public void setMaxSignal(double maxSignal) {
        mMaxSignal = maxSignal;
    }

    public double getMinSignal() {
        return mMinSignal;
    }

    public void setMinSignal(double minSignal) {
        mMinSignal = minSignal;
    }

    /**
     * Check if it is in the specific location defined by WiFi
     *
     * @return <code>true</code> if it's in the position it defines, otherwise <code>false</code>
     */
    public boolean isInRange() {
        return false;
    }

    public ContentValues getContentValues() {
        ContentValues values = new ContentValues();

        values.put(WiFiPositionTable.Cols.LOCATION_ID, getForeignKey());
        values.put(WiFiPositionTable.Cols.SSID, mSSID);
        values.put(WiFiPositionTable.Cols.BSSID, mBSSID);
        values.put(WiFiPositionTable.Cols.MAXSIGNAL, mMaxSignal);
        values.put(WiFiPositionTable.Cols.MINSIGNAL, mMinSignal);

        return values;
    }

    public static String[] getCurrentWifiInfo(Context context) {
        String ssid = null;
        String bssid = null;
        ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connManager.getActiveNetworkInfo();
        if (networkInfo != null) {
            if (networkInfo.isConnected()) {
                final WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
                final WifiInfo connectionInfo = wifiManager.getConnectionInfo();
                if (connectionInfo != null && !TextUtils.isEmpty(connectionInfo.getSSID())) {
                    ssid = connectionInfo.getSSID();
                bssid = connectionInfo.getBSSID();
                }
            }
        return new String[]{ssid, bssid};
        }
        return null;
    }
}
