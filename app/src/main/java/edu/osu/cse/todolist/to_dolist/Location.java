package edu.osu.cse.todolist.to_dolist;

/**
 * Created by Sniper on 2015/11/3.
 */
public class Location {
    private long mId;
    private String mTitle;
    private String mNote;
    private ConfigType mConfig;
    private GPSCoordinate mGPSCoordinate;
    private WiFiPosition mWiFiPos;

    public enum ConfigType {
        GPS, WiFi
    }

    public Location() {
        mId = -1;
    }

    public Location(long id) {
        mId = id;
    }

    public long getId() {
        return mId;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getNote() {
        return mNote;
    }

    public void setNote(String note) {
        mNote = note;
    }

    public ConfigType getConfig() {
        return mConfig;
    }

    public void setConfig(ConfigType config) {
        mConfig = config;
    }

    public GPSCoordinate getGPSCoordinate() {
        return mGPSCoordinate;
    }

    public void setGPSCoordinate(GPSCoordinate GPSCoordinate) {
        mGPSCoordinate = GPSCoordinate;
    }

    public WiFiPosition getWiFiPos() {
        return mWiFiPos;
    }

    public void setWiFiPos(WiFiPosition wiFiPos) {
        mWiFiPos = wiFiPos;
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
