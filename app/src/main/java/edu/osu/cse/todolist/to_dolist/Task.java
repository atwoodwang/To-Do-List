package edu.osu.cse.todolist.to_dolist;

import java.util.Date;

/**
 * Created by NoAnyLove on 2015/11/2.
 */
public class Task {
    private long mId;
    private String mTitle;
    private String mNote;
    private boolean mStarred;
    private ConfigType mConfig;
    private Schedule mSchedule;
    private Location mLocation;
    private boolean mComplete;

    public enum ConfigType {
        NONE, TIME, LOCATION
    }

    public Task() {
        //TODO: generate a random id for current coding
        // Id=-1 means this Object hasn't saved in the database yet
        mId = -1;
        mStarred = false;
        mConfig = ConfigType.NONE;
        mSchedule = null;
        mLocation = null;
    }

    public Task(long id) {
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

    public boolean isStarred() {
        return mStarred;
    }

    public void setStarred(boolean starred) {
        mStarred = starred;
    }

    public ConfigType getConfig() {
        return mConfig;
    }

    public void setConfig(ConfigType config) {
        mConfig = config;
    }

    public Schedule getSchedule() {
        return mSchedule;
    }

    public void setSchedule(Schedule schedule) {
        mSchedule = schedule;
    }

    public Location getLocation() {
        return mLocation;
    }

    public void setLocation(Location location) {
        mLocation = location;
    }

    public boolean isComplete() {
        return mComplete;
    }

    public void setComplete(boolean complete) {
        mComplete = complete;
    }

    public Date getRemindDate() {
        Date result = null;
        if (mSchedule != null) {
            result = mSchedule.getDate();
        }
        return result;
    }

    public String getRemindDateString() {
        String result = "";
        if (mSchedule != null) {
            result = mSchedule.toString();
        }
        return result;
    }

    public void setRemindDate(Date date) {
        //TODO: implement setRemindDate()
        // if no associated schedule, create one and call Schedule.setDate()
        // if has associated schedule, directly set its date and save to Database
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
