package edu.osu.cse.todolist.to_dolist;

import java.util.Date;

/**
 * Created by NoAnyLove on 2015/11/2.
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
         * Remind based on location
         */
        LOCATION
    }

    public Task() {
        super();
        mStarred = false;
        mConfig = ConfigType.NONE;
        mSchedule = null;
        mLocation = null;
    }

    public Task(long id) {
        super(id);
        // Load data from database
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
    public void setRemindDate(Date date) {
        //TODO: implement setRemindDate()
        // if no associated schedule, create one and call Schedule.setDate()
        // if has associated schedule, directly set its date and save to Database
    }
}
