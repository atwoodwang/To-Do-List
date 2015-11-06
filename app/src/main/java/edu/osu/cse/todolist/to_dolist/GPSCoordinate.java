package edu.osu.cse.todolist.to_dolist;

/**
 * Created by Sniper on 2015/11/3.
 */
public class GPSCoordinate extends Model {

    private double mLongitude;
    private double mLatitude;
    private double mRange;

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

}
