package edu.osu.cse.todolist.to_dolist;

import android.content.ContentValues;

import edu.osu.cse.todolist.to_dolist.database.ToDoDbSchema;
import edu.osu.cse.todolist.to_dolist.database.ToDoDbSchema.GPSCoordinateTable;

/**
 * Created by Sniper on 2015/11/3.
 */
public class GPSCoordinate extends Model {

    private double mLongitude;
    private double mLatitude;
    private double mRange;

    //TODO: should associate Location with GPSCoordinate, either Location.setGPSCoordinate or
    // GPSCoordinate.setLocation. REMEMBER to update GPSCoordinate's Foreign key
    public GPSCoordinate() {
        this(-1);
    }

    public GPSCoordinate(long id) {
        super(id);
        mLongitude = 0;
        mLatitude = 0;
        mRange = 0;
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

    public ContentValues getContentValues() {
        ContentValues values = new ContentValues();

        values.put(GPSCoordinateTable.Cols.LOCATION_ID, getForeignKey());
        values.put(GPSCoordinateTable.Cols.LONGITUDE, mLongitude);
        values.put(GPSCoordinateTable.Cols.LATITUDE, mLatitude);
        values.put(GPSCoordinateTable.Cols.RANGE, mRange);

        return values;
    }
}
