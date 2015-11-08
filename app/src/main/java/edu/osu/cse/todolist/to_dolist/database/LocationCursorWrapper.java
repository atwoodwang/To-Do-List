package edu.osu.cse.todolist.to_dolist.database;

import android.database.Cursor;
import android.database.CursorWrapper;

import edu.osu.cse.todolist.to_dolist.Location;
import edu.osu.cse.todolist.to_dolist.Task;
import edu.osu.cse.todolist.to_dolist.database.ToDoDbSchema.LocationTable;

/**
 * Created by Sniper on 2015/11/8.
 */
public class LocationCursorWrapper extends CursorWrapper {
    /**
     * Creates a cursor wrapper.
     *
     * @param cursor The underlying cursor to wrap.
     */
    public LocationCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public Location get() {
        // get field values
        long id = getLong((getColumnIndex(LocationTable.Cols.ID)));
        String title = getString((getColumnIndex(LocationTable.Cols.TITLE)));
        String note = getString((getColumnIndex(LocationTable.Cols.NOTE)));
        int configInt = getInt(getColumnIndex(LocationTable.Cols.CONFIG));
        Location.ConfigType config = Location.ConfigType.values()[configInt];

        // create Task object and set values
        Location loc = new Location(id);
        loc.setTitle(title);
        loc.setNote(note);
        loc.setConfig(config);

        // TODO: set GPSCoordinate and WiFiPosition
        switch (config) {
            case GPS:
                break;
            case WiFi:
                break;
        }

        return loc;
    }
}
