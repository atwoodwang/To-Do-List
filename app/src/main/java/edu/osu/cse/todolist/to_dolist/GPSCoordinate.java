package edu.osu.cse.todolist.to_dolist;

/**
 * Created by Sniper on 2015/11/3.
 */
public class GPSCoordinate {
    private long mId;
    private double mLongitude;
    private double mLatitude;
    private double mRange;

    public GPSCoordinate() {
        mId = -1;
    }

    public GPSCoordinate(int id) {
        mId = id;
    }

    public long getId() {
        return mId;
    }

    public double getLongitude() {
        return mLongitude;
    }

    public void setLongitude(double longitude) {
        mLongitude = longitude;
    }

    public double getLatitude() {
        return mLatitude;
    }

    public void setLatitude(double latitude) {
        mLatitude = latitude;
    }

    public double getRange() {
        return mRange;
    }

    public void setRange(double range) {
        mRange = range;
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
