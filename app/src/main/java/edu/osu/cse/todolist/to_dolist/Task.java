package edu.osu.cse.todolist.to_dolist;

import android.content.ContentValues;

import java.util.Date;

import static edu.osu.cse.todolist.to_dolist.database.ToDoDbSchema.TaskTable;

/**
 * Task Model class
 */

/*
Conventions:
    * Task belongs to one Folder
    * Task has one schedule (optional)
    * Task has one Location (optional)
*/
public class Task extends Model {

    private String mTitle;
    private String mNote;

    /**
     * Indicates this Task is starred or not
     */
    private boolean mStarred;

    /**
     * Config for how to trigger remind
     */
    private ConfigType mConfig;
    private Schedule mSchedule;
    private Location mLocation;
    private boolean mComplete;

    /**
     * Enable/Disable remind
     */
    private boolean mEnabled;

    /**
     * Config Type for setting up reminder manner
     */
    public enum ConfigType {
        /**
         * default, no reminder
         */
        NONE,

        /**
         * Reminder based on date and time
         */
        TIME,

        /**
         * Remind based on arriving a location
         */
        LOCATION_ARRIVING,

        /**
         * Remind based on leaving a location
         */
        LOCATION_LEAVING
    }

    public Task() {
        this(-1);
    }

    public Task(long id) {
        super(id);
        mTitle = "";
        mNote = "";
        mStarred = false;
        mConfig = ConfigType.NONE;
        mComplete = false;

        // default: disable remind, enable remind when setting config
        mEnabled = false;

        mSchedule = null;
        mLocation = null;
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

    /**
     * Check if the Task is starred
     *
     * @return <code>true</code> if it is starred, otherwise <code>false</code>
     */
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

    /**
     * Get the remind time in Date format
     * <p/>
     * This method returns a Date object indicates when to remind this Task
     *
     * @return the <code>Date</code> of mSchedule, or <code>null</code> if there is no associated
     * schedule.
     */
    public Date getRemindDate() {
        Date result = null;
        if (mSchedule != null) {
            result = mSchedule.getDate();
        }
        return result;
    }

    /**
     * Get the remind time in String format
     *
     * @return the <code>Date</code> of mSchedule in String, or "" if there is if there is no
     * associated schedule
     */
    public String getRemindDateString() {
        String result = "";
        if (mSchedule != null) {
            result = mSchedule.toString();
        }
        return result;
    }

    /**
     * Set when to remind in Date format
     * <p/>
     * This method is responsible for creating or update associated schedule.
     *
     * @param date of when to remind
     */
    public Schedule setRemindDate(Date date) {
        //TODO: implement setRemindDate()

        // if there is no associated schedule, create a new one
        if (mSchedule == null) {
            mSchedule = new Schedule(this);
        }
        mSchedule.setDate(date);
        return mSchedule;

        // TODO: update Task.mSchedule and Schedule.mTask
    }

    public boolean isEnabled() {
        return mEnabled;
    }

    public void setEnabled(boolean enabled) {
        mEnabled = enabled;
    }

    @Override
    public ContentValues getContentValues() {
        ContentValues values = new ContentValues();

//        if (getId() != -1) {
//            values.put(TaskTable.Cols.ID, getId());
//        }
        values.put(TaskTable.Cols.TITLE, mTitle);
        values.put(TaskTable.Cols.NOTE, mNote);
        values.put(TaskTable.Cols.STARRED, mStarred);
        values.put(TaskTable.Cols.CONFIG, mConfig.ordinal());
        values.put(TaskTable.Cols.COMPLETE, mComplete);
        values.put(TaskTable.Cols.ENABLED, mEnabled);

        return values;
    }
}
