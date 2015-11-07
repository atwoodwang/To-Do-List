package edu.osu.cse.todolist.to_dolist.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import static edu.osu.cse.todolist.to_dolist.database.ToDoDbSchema.GPSCoordinateTable;
import static edu.osu.cse.todolist.to_dolist.database.ToDoDbSchema.LocationTable;
import static edu.osu.cse.todolist.to_dolist.database.ToDoDbSchema.ScheduleTable;
import static edu.osu.cse.todolist.to_dolist.database.ToDoDbSchema.TaskLocationTable;
import static edu.osu.cse.todolist.to_dolist.database.ToDoDbSchema.TaskTable;
import static edu.osu.cse.todolist.to_dolist.database.ToDoDbSchema.WiFiPositionTable;

/**
 * SQLiteOpenHelper class for To-Do List
 */
public class ToDoBaseHelper extends SQLiteOpenHelper {
    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "ToDoBase.db";

    private static final String TAG = "ToDoBaseHelper";

    public ToDoBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        createTaskTable(db);
        createScheduleTable(db);
        createLocationTable(db);
        // Task and Location joint table
        createTaskLocationTable(db);
        createGPSCoordinateTable(db);
        createWiFiPositionTable(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    private void createTaskTable(SQLiteDatabase db) {
        String sql = "CREATE TABLE %s(\n" +   //NAME
                "    %s INTEGER PRIMARY KEY AUTOINCREMENT,\n" +   //ID
                "    %s TEXT NOT NULL,\n" +   //TITLE
                "    %s TEXT,\n" +    //NOTE
                "    %s BOOLEAN NOT NULL,\n" +    //STARRED
                "    %s INTEGER NOT NULL,\n" +    //CONFIG
                "    %s BOOLEAN NOT NULL\n" + //COMPLETE
                ")";
        sql = String.format(sql, TaskTable.NAME,
                TaskTable.Cols.ID,
                TaskTable.Cols.TITLE,
                TaskTable.Cols.NOTE,
                TaskTable.Cols.STARRED,
                TaskTable.Cols.CONFIG,
                TaskTable.Cols.COMPLETE);

        Log.d(TAG, sql);
        db.execSQL(sql);
    }

    private void createScheduleTable(SQLiteDatabase db) {
        String sql = "CREATE TABLE %s(\n" +   //NAME
                "    %s INTEGER PRIMARY KEY AUTOINCREMENT,\n" +   //ID
                "    %s INTEGER NOT NULL,\n" +    //TASK_ID
                "    %s INTEGER NOT NULL,\n" +    //CONFIG
                "    %s DATE NOT NULL,\n" +   //DATE
                "    FOREIGN KEY(%s) REFERENCES %s(%s)\n" + //TASK_ID & Task(ID)
                ")";
        sql = String.format(sql, ScheduleTable.NAME,
                ScheduleTable.Cols.ID,
                ScheduleTable.Cols.TASK_ID,
                ScheduleTable.Cols.CONFIG,
                ScheduleTable.Cols.DATE,
                ScheduleTable.Cols.TASK_ID, TaskTable.NAME, TaskTable.Cols.ID);

        Log.d(TAG, sql);
        db.execSQL(sql);
    }

    private void createLocationTable(SQLiteDatabase db) {
        String sql = "CREATE TABLE %s(\n" +   //TABLE
                "    %s INTEGER PRIMARY KEY AUTOINCREMENT,\n" + //ID
                "    %s TEXT NOT NULL,\n" +  //TITLE
                "    %s TEXT,\n" +    //NOTE
                "    %s INTEGER NOT NULL\n" +   //CONFIG
                ")";
        sql = String.format(sql, LocationTable.NAME,
                LocationTable.Cols.ID,
                LocationTable.Cols.TITLE,
                LocationTable.Cols.NOTE,
                LocationTable.Cols.CONFIG);

        Log.d(TAG, sql);
        db.execSQL(sql);
    }

    private void createTaskLocationTable(SQLiteDatabase db) {
        String sql = "CREATE TABLE %s(\n" +  //NAME
                "    %s INTEGER PRIMARY KEY AUTOINCREMENT,\n" + //ID
                "    %s INTEGER NOT NULL,\n" + //TASK_ID
                "    %s INTEGER NOT NULL,\n" + //LOCATION_ID
                "    FOREIGN KEY(%s) REFERENCES %s(%s),\n" + //TASK_ID & Task(ID)
                "    FOREIGN KEY(%s) REFERENCES %s(%s)\n" +  //LOCATION_ID & Location(ID)
                ")";
        sql = String.format(sql, TaskLocationTable.NAME,
                TaskLocationTable.Cols.ID,
                TaskLocationTable.Cols.TASK_ID,
                TaskLocationTable.Cols.LOCATION_ID,
                TaskLocationTable.Cols.TASK_ID, TaskTable.NAME, TaskTable.Cols.ID,
                TaskLocationTable.Cols.LOCATION_ID, LocationTable.NAME, LocationTable.Cols.ID);

        Log.d(TAG, sql);
        db.execSQL(sql);
    }

    private void createGPSCoordinateTable(SQLiteDatabase db) {
        String sql = "CREATE TABLE %s(\n" +    //NAME
                "    %s INTEGER PRIMARY KEY AUTOINCREMENT,\n" + //ID
                "    %s INTEGER NOT NULL,\n" + //LOCATION_ID
                "    %s REAL NOT NULL,\n" +  //LONGITUDE
                "    %s REAL NOT NULL,\n" +   //LATITUDE
                "    %s REAL NOT NULL,\n" +  //RANGE
                "    FOREIGN KEY(%s) REFERENCES %s(%s)\n" +  //LOCATION_ID & Location(ID)
                ")";

        sql = String.format(sql, GPSCoordinateTable.NAME,
                GPSCoordinateTable.Cols.ID,
                GPSCoordinateTable.Cols.LOCATION_ID,
                GPSCoordinateTable.Cols.LONGITUDE,
                GPSCoordinateTable.Cols.LATITUDE,
                GPSCoordinateTable.Cols.RANGE,
                GPSCoordinateTable.Cols.LOCATION_ID, LocationTable.NAME, LocationTable.Cols.ID);

        Log.d(TAG, sql);
        db.execSQL(sql);
    }

    private void createWiFiPositionTable(SQLiteDatabase db) {
        String sql = "CREATE TABLE %s(\n" + //NAME
                "    %s INTEGER PRIMARY KEY AUTOINCREMENT,\n" + //ID
                "    %s INTEGER NOT NULL,\n" + //LOCATION_ID
                "    %s TEXT NOT NULL,\n" +   //SSID
                "    %s TEXT,\n" + //MAC
                "    %s INTEGER NOT NULL,\n" +   //MAXSIGNAL
                "    %s INTEGER NOT NULL,\n" + //MINSIGNAL
                "    FOREIGN KEY(%s) REFERENCES %s(%s)\n" +  //LOCATION_ID & Location(ID)
                ")";

        sql = String.format(sql, WiFiPositionTable.NAME,
                WiFiPositionTable.Cols.ID,
                WiFiPositionTable.Cols.LOCATION_ID,
                WiFiPositionTable.Cols.SSID,
                WiFiPositionTable.Cols.MAC,
                WiFiPositionTable.Cols.MAXSIGNAL,
                WiFiPositionTable.Cols.MINSIGNAL,
                WiFiPositionTable.Cols.LOCATION_ID, LocationTable.NAME, LocationTable.Cols.ID);

        Log.d(TAG, sql);
        db.execSQL(sql);
    }
}
