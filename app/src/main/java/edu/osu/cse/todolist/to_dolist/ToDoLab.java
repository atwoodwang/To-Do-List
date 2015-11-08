package edu.osu.cse.todolist.to_dolist;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import static edu.osu.cse.todolist.to_dolist.database.ToDoDbSchema.*;

/**
 * ToDoLab singleton centralized data class
 */
public class ToDoLab {
    private static ToDoLab sToDoLab;

    private Context mContext;

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

    /**
     * Save/update a Model object to database
     *
     * @param model the Model need to be saved/updated
     * @return return <code>true</code> if save successfully, otherwise <code>false</code>
     */
    public <T extends Model> boolean save(T model) {
        boolean result = false;

        if (model.getId() == -1) {
            // TODO: write into database, and update mId with ROW_ID
            // Save object into database
            model.setId(dummyId++);
            result = true;
        } else {
            // Retrieve object from database if necessary
            // and then Update database
            result = true;
        }

        Log.d(TAG, String.format("%s(id=%d) saved", model.getClass().getName(), model.getId()));
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
     * @param <T>
     * @return the table name of the given subclass of Model
     */
    public <T extends Model> String getTableName(T model) {
        String tableName = "";
        String className = model.getClass().getName();
        className = className.substring(className.lastIndexOf(".") + 1);
        switch (className) {
            case "Task":
                tableName = TaskTable.NAME;
                break;
            case "Schedule":
                tableName = ScheduleTable.NAME;
                break;
            case "Location":
                tableName = LocationTable.NAME;
                break;
            case "GPSCoordinate":
                tableName = GPSCoordinateTable.NAME;
                break;
            case "WiFiPosition":
                tableName = WiFiPositionTable.NAME;
                break;
        }
        return tableName;
    }
}
