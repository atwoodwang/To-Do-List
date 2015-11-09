package edu.osu.cse.todolist.to_dolist.database;

import android.database.Cursor;
import android.database.CursorWrapper;

import edu.osu.cse.todolist.to_dolist.WiFiPosition;

import static edu.osu.cse.todolist.to_dolist.database.ToDoDbSchema.WiFiPositionTable;

/**
 * Created by Sniper on 2015/11/9.
 */
public class WiFiPositionCursorWrapper extends CursorWrapper {
    /**
     * Creates a cursor wrapper.
     *
     * @param cursor The underlying cursor to wrap.
     */
    public WiFiPositionCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public WiFiPosition get() {
        long id = getLong((getColumnIndex(WiFiPositionTable.Cols.ID)));
        long locationId = getLong((getColumnIndex(WiFiPositionTable.Cols.LOCATION_ID)));
        String ssid = getString((getColumnIndex(WiFiPositionTable.Cols.SSID)));
        String bssid = getString((getColumnIndex(WiFiPositionTable.Cols.BSSID)));

        WiFiPosition wifiPos = new WiFiPosition(id);
        wifiPos.setForeignKey(locationId);
        wifiPos.setSSID(ssid);
        wifiPos.setBSSID(bssid);

        return wifiPos;
    }
}
