package edu.osu.cse.todolist.to_dolist;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import edu.osu.cse.todolist.to_dolist.database.ToDoBaseHelper;

import static edu.osu.cse.todolist.to_dolist.database.ToDoDbSchema.*;

/**
 * ToDoLab singleton centralized data class
 */
public class ToDoLab {
    private static ToDoLab sToDoLab;

    private Context mContext;
    private SQLiteDatabase mDatabase;

    private List<Task> mTasks;
    //    private List<Folder> mFolders;
    private List<Location> mLocations;
    private List<GPSCoordinate> mGPSCoordinates;
    private List<WiFiPosition> mWiFiPositions;
    //    private List<AccessPoint> mAccessPoints;
    private List<Schedule> mSchedules;

    /**
     * Use static class variable to simulate SQLite ROW_ID. This is only used for developing and
     * test
     */
    private static long dummyId = 1;

    /**
     * Tag used for debug
     */
    private static final String TAG = "ToDoLab";

    public static ToDoLab get(Context context) {
        if (sToDoLab == null) {
            sToDoLab = new ToDoLab(context);
        }
        return sToDoLab;
    }

    private ToDoLab(Context context) {
        mContext = context.getApplicationContext();
        mDatabase = new ToDoBaseHelper(mContext).getWritableDatabase();

        mTasks = new ArrayList<>();
//        mFolders = new ArrayList<>();
        mLocations = new ArrayList<>();
        mGPSCoordinates = new ArrayList<>();
        mWiFiPositions = new ArrayList<>();
//        mAccessPoints = new ArrayList<>();
        mSchedules = new ArrayList<>();
    }

    private void GenerateTestData() {
        //Generate random data for coding and test
    }

    public Context getContext() {
        return mContext;
    }

    public List<Task> getTasks() {
        return mTasks;
    }

//    public List<Folder> getFolders() {
//        return mFolders;
//    }

    public List<Location> getLocations() {
        return mLocations;
    }

    public List<GPSCoordinate> getGPSCoordinates() {
        return mGPSCoordinates;
    }

    public List<WiFiPosition> getWiFiPositions() {
        return mWiFiPositions;
    }

    public List<Schedule> getSchedules() {
        return mSchedules;
    }

    public void addTask(Task task) {
        mTasks.add(task);
    }

    public void addLocation(Location location) {
        mLocations.add(location);
    }

    public Location removeLocation(Location location) {
        if (mLocations.contains(location)) {
            mLocations.remove(location);
            return location;
        } else {
            return null;
        }
    }

    public Task removeTask(Task task) {
        if (mTasks.contains(task)) {
            mTasks.remove(task);
            return task;
        } else {
            return null;
        }
    }

    public Task getTask(long id) {
        // TODO: need read task from database
        for (Task task : mTasks) {
            if (task.getId() == id) {
                return task;
            }
        }
        return null;
    }

    public Location getLocation(long id) {
        // TODO: need read location from database
        for (Location location : mLocations) {
            if (location.getId() == id) {
                return location;
            }
        }
        return null;
    }

    /**
     * Save/update a Model object to database
     *
     * @param model the Model need to be saved/updated
     * @return return <code>true</code> if save successfully, otherwise <code>false</code>
     */
    public <T extends Model> boolean save(T model) {
        boolean result = false;
        ContentValues values = model.getContentValues();
        String tableName = getTableName(model);

        // if the Model object hasn't saved into database, use insert
        if (model.getId() == -1) {
            long id = mDatabase.insert(tableName, null, values);
            // if successfully insert data
            if (id != -1) {
                // update mId with ROW_ID of this record
                model.setId(id);
                result = true;
            }
        } else { // if the model object is already saved in database, then update it
            //update database
            if (mDatabase.update(tableName, values, "ID = ?",
                    new String[]{Long.toString(model.getId())}) > 0) {
                result = true;
            }
        }

        // output debug info
        if (result) {
            Log.d(TAG, String.format("%s(id=%d) saved", model.getClass().getName(), model.getId()));
        } else {
            Log.d(TAG, String.format("%s(id=%d) failed to save", model.getClass().getName(), model
                    .getId()));
        }

        return result;
    }

    /**
     * Delete a Model object record from database
     *
     * @return <code>true</code> if successfully delete, otherwise <code>false</code>.
     */
    public <T extends Model> boolean delete(T model) {
        boolean result = false;
        if (model.getId() != -1) {
            result = true;
            Log.d(TAG, String.format("%s(id=%d) deleted", model.getClass().getName(), model.getId()));
            model.setId(-1);
        } else {
            Log.d(TAG, String.format("Cannot delete %s(id=%d)", model.getClass().getName(),
                    model.getId()));
        }
        return result;
    }

    /**
     * Find an object from database with its id
     *
     * @param type class type of the object
     * @param id   primary key id
     * @param <T>  class type
     * @return the object if found, otherwise return <code>null</code>
     */
    public <T extends Model> T findById(Class<T> type, Long id) {
        return null;
    }

    /**
     * Get the table name of the given object
     *
     * @param model given object which is a subclass of Model
     * @return the table name of the given subclass of Model
     */
    public <T extends Model> String getTableName(T model) {
        String className = model.getClass().getSimpleName();
        return getTableName(className);
    }

    /**
     * Get the table name of the given model class
     *
     * @param type the class type of Model class
     * @return the table name associated with the class type
     */
    public <T extends Model> String getTableName(Class<T> type) {
        String className = type.getSimpleName();
        return getTableName(className);
    }

    /**
     * Get the table name of the given class name
     *
     * @param className the given class name, the class must be a subclass of Model
     * @return the associated table name of given class name if exists, othewise an empty string
     */
    public String getTableName(String className) {
        String tableName = "";

        if ("Task".equals(className)) { // Task class
            tableName = TaskTable.NAME;
        } else if ("Schedule".equals(className)) { // Schedule class
            tableName = ScheduleTable.NAME;
        } else if ("Location".equals(className)) { // Location class
            tableName = LocationTable.NAME;
        } else if ("GPSCoordinate".equals(className)) { // GPSCoordinate class
            tableName = GPSCoordinateTable.NAME;
        } else if ("WiFIPosition".equals(className)) { // WiFIPosition
            tableName = WiFiPositionTable.NAME;
        }

        return tableName;
    }
}
