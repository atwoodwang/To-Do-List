package edu.osu.cse.todolist.to_dolist;

import android.content.ContentValues;

import edu.osu.cse.todolist.to_dolist.database.ToDoDbSchema;

import static edu.osu.cse.todolist.to_dolist.database.ToDoDbSchema.*;

/**
 * Created by Sniper on 2015/11/3.
 */
public class Location extends Model {

    private String mTitle;
    private String mNote;
    private ConfigType mConfig;
    private GPSCoordinate mGPSCoordinate;
    private WiFiPosition mWiFiPosition;

    /**
     * Config Type for setting up Location definition
     */
    public enum ConfigType {
        /**
         * Define a Location based on GPS Coordinates
         */
        GPS,

        /**
         * Define a Location based on WiFi and Access Point
         */
        WiFi
    }

    public Location() {
        this(-1);
    }

    public Location(long id) {
        super(id);
        mTitle = "";
        mNote = "";
        mConfig = ConfigType.GPS;
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

    public WiFiPosition getWiFiPosition() {
        return mWiFiPosition;
    }

    public void setWiFiPosition(WiFiPosition wiFiPosition) {
        mWiFiPosition = wiFiPosition;
    }

    public ContentValues getContentValues() {
        ContentValues values = new ContentValues();

        values.put(LocationTable.Cols.TITLE, mTitle);
        values.put(LocationTable.Cols.NOTE, mNote);
        values.put(LocationTable.Cols.CONFIG, mConfig.ordinal());

        return values;
    }

    @Override
    public String toString() {
        return mTitle;
    }
}
