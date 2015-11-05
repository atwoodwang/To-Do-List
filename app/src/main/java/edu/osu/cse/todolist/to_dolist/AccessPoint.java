package edu.osu.cse.todolist.to_dolist;

/**
 * Created by Sniper on 2015/11/3.
 */
public class AccessPoint extends Model {
    private String mSSID;
    private String mMAC;

    // The MaxSignal and MinSignal defines a range of signal strength
    private double mMaxSignal;
    private double mMinSignal;

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

}
