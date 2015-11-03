package edu.osu.cse.todolist.to_dolist;

import java.util.List;

/**
 * Created by Sniper on 2015/11/3.
 */
public class WiFiPosition {
    private long mId;
    private List<AccessPoint> APs;
    private ConfigType mConfig;

    public enum ConfigType {
        BASIC
    }

    public WiFiPosition() {
        mId = -1;
    }

    public WiFiPosition(long id) {
        mId = id;
    }

    public long getId() {
        return mId;
    }

    public ConfigType getConfig() {
        return mConfig;
    }

    public void setConfig(ConfigType config) {
        mConfig = config;
    }

    public void addAP(AccessPoint AP) {

    }

    // TODO: modify the way of remove AP, maybe by SSID or MAC
    public AccessPoint removeAP(AccessPoint AP) {
        return null;
    }

    public List<AccessPoint> removeAllAP() {
        return null;
    }

    public boolean isInRange() {
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
