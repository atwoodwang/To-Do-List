package edu.osu.cse.todolist.to_dolist;

import android.content.ContentValues;

import java.util.List;

/**
 * Created by Sniper on 2015/11/3.
 */
public class WiFiPosition extends Model {

    private String mSSID;
    private String mMAC;

    // The MaxSignal and MinSignal defines a range of signal strength
    private double mMaxSignal;
    private double mMinSignal;

    public WiFiPosition() {
        this(-1);
    }

    public WiFiPosition(long id) {
        super(id);
        mSSID = "";
        mMAC = "";
        mMaxSignal = 100;
        mMinSignal = 1;
    }

    public String getSSID() {
        return mSSID;
    }

    public void setSSID(String SSID) {
        mSSID = SSID;
    }

    public String getMAC() {
        return mMAC;
    }

    public void setMAC(String MAC) {
        mMAC = MAC;
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
        return null;
    }
}
