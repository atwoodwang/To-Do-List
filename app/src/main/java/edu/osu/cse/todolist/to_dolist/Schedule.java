package edu.osu.cse.todolist.to_dolist;

import android.content.ContentValues;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Sniper on 2015/11/3.
 */
public class Schedule extends Model {

    private ConfigType mConfig;
    private Date mDate;
    private Task mTask;

    private final String DATE_FORMAT = "MM/dd/yy HH:mm E";

    public enum ConfigType {
        NONE
    }

    public Schedule(Task task) {
        this(-1, task);
    }

    public Schedule(long id, Task task) {
        super(id);
        mTask = task;
        mConfig = ConfigType.NONE;
        mDate = null;
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

    //TODO: May need refactor to deal with i18n, use resource string as date format
    @Override
    public String toString() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATE_FORMAT, Locale.US);
        return simpleDateFormat.format(mDate);
    }

    public ContentValues getContentValues() {
        return null;
    }
}
