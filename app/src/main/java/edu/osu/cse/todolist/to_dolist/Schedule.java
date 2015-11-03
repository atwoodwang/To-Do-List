package edu.osu.cse.todolist.to_dolist;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Sniper on 2015/11/3.
 */
public class Schedule {
    private long mId;
    private ConfigType mConfig;
    private Date mDate;
    private Task mTask;

    private final static String sFormats = "MM/dd/yy HH:mm E";

    public enum ConfigType {
        NONE
    }

    public Schedule(Task task) {
        mId = -1;
        mTask = task;
    }

    public Schedule(long id, Task task) {
        mId = id;
        mTask = task;
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

    public Date getDate() {
        return mDate;
    }

    public void setDate(Date date) {
        mDate = date;
    }

    public Task getTask() {
        return mTask;
    }

    // TODO: need refactor
    public static String formatDate(Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(sFormats, Locale.US);
        return simpleDateFormat.format(date);
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
