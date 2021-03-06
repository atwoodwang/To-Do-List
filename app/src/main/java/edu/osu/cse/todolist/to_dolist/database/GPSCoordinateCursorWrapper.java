package edu.osu.cse.todolist.to_dolist.database;

import android.database.Cursor;
import android.database.CursorWrapper;

import edu.osu.cse.todolist.to_dolist.GPSCoordinate;

import static edu.osu.cse.todolist.to_dolist.database.ToDoDbSchema.GPSCoordinateTable;

/**
 * Created by Sniper on 2015/11/9.
 */
public class GPSCoordinateCursorWrapper extends CursorWrapper {
    /**
     * Creates a cursor wrapper.
     *
     * @param cursor The underlying cursor to wrap.
     */
    public GPSCoordinateCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public GPSCoordinate get() {
        long id = getLong((getColumnIndex(GPSCoordinateTable.Cols.ID)));
        long locationID = getLong((getColumnIndex(GPSCoordinateTable.Cols.LOCATION_ID)));
        double longitude = getDouble((getColumnIndex(GPSCoordinateTable.Cols.LONGITUDE)));
        double latitude = getDouble((getColumnIndex(GPSCoordinateTable.Cols.LATITUDE)));
        double range = getDouble((getColumnIndex(GPSCoordinateTable.Cols.RANGE)));

        String address = getString((getColumnIndex(GPSCoordinateTable.Cols.ADDRESS)));
        String placeId = getString((getColumnIndex(GPSCoordinateTable.Cols.PLACE_ID)));

        GPSCoordinate gps = new GPSCoordinate(id);
        gps.setForeignKey(locationID);
        gps.setLongitude(longitude);
        gps.setLatitude(latitude);
        gps.setRange(range);
        gps.setAddress(address);
        gps.setPlaceId(placeId);

        return gps;
    }
}
