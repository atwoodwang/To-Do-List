package edu.osu.cse.todolist.to_dolist;

/**
 * Created by Sniper on 2015/11/3.
 */
public class AccessPoint {
    private long mId;
    private String mSSID;
    private String mMAC;
    private double mMaxSignal;
    private double mMinSignal;

    public AccessPoint() {
        mId = -1;
    }

    public AccessPoint(long id) {
        mId = id;
    }

    public long getId() {
        return mId;
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

    public boolean isValid() {
        return false;
    }

    public boolean save() {
        boolean result = false;
        if (mId == -1) {
            // TODO: write into database, and update mId with ROW_ID
            // Save object into database
            result = true;
        } else {
            // Update database
            result = true;
        }
        return result;
    }
}
