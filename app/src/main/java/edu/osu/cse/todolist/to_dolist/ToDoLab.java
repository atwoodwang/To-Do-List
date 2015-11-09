package edu.osu.cse.todolist.to_dolist;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorWrapper;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import edu.osu.cse.todolist.to_dolist.database.LocationCursorWrapper;
import edu.osu.cse.todolist.to_dolist.database.ScheduleCursorWrapper;
import edu.osu.cse.todolist.to_dolist.database.TaskCursorWrapper;
import edu.osu.cse.todolist.to_dolist.database.TaskLocationCursorWrapper;
import edu.osu.cse.todolist.to_dolist.database.ToDoBaseHelper;
import edu.osu.cse.todolist.to_dolist.database.ToDoDbSchema;

import static edu.osu.cse.todolist.to_dolist.database.ToDoDbSchema.*;

/**
 * ToDoLab singleton centralized data class
 */
public class ToDoLab {
    private static ToDoLab sToDoLab;

    private Context mContext;
    private SQLiteDatabase mDatabase;

    /**
     * Currently manipulate Task
     */
    private Task mTask;
    private List<Task> mTasks;

    /**
     * Current in-memory Location
     */
    private Location mLocation;
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
        return findAll(Task.class);
    }

//    public List<Folder> getFolders() {
//        return mFolders;
//    }

    public List<Location> getLocations() {
        return findAll(Location.class);
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
        if (task.getId() == -1) {
            mTask = task;
        }
    }

    public void addLocation(Location location) {
        if (location.getId() == -1) {
            mLocation = location;
        }
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
        Task task = null;
        if (id == -1) {
            task = mTask;
        } else {
            task = findById(Task.class, id);
        }
        return task;
    }

    public Location getLocation(long id) {
        Location loc = null;
        if (id == -1) {
            loc = mLocation;
        } else {
            loc = findById(Location.class, id);
        }
        return loc;
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

    public boolean saveTask(Task task) {
        if (task == null) {
            return false;
        }

        // save task first
        if (!save(task)) {
            return false;
        }

        // save associated Schedule or Location according to ConfigType
        boolean result = false;
        switch (task.getConfig()) {
            case NONE:
                cleanTaskScheduleRelation(task);
                cleanTaskLocationRelation(task);
                break;
            case TIME:
                cleanTaskLocationRelation(task);
                if (save(task.getSchedule())) {
                    result = true;
                }
                break;
            case LOCATION_ARRIVING:
            case LOCATION_LEAVING:
                cleanTaskScheduleRelation(task);
                Location loc = findLocationByTask(task);
                if (loc == null) { // no exists Task_Location relationship
                    if (createTaskLocation(task, task.getLocation())) {
                        result = true;
                    }
                } else if (loc.getId() != task.getLocation().getId()) {
                    // if exists different previous Task_Location relationship, update it
                    if (updateTaskLocation(task, task.getLocation())) {
                        result = true;
                    }
                } else { // no need to update Task_Location relation
                    result = true;
                }
                break;
        }
        return result;
    }

    private void cleanTaskLocationRelation(Task task) {
        if (task.getLocation() != null) {
            deleteTaskLocation(task);
            task.setLocation(null);
        }
    }

    private void cleanTaskScheduleRelation(Task task) {
        Schedule s = task.getSchedule();
        if (s != null && s.getId() != -1) {
            delete(s);
            task.setSchedule(null);
        }
    }

    public boolean createTaskLocation(Task task, Location loc) {
        boolean result = false;
        ContentValues values = new ContentValues();
        values.put(TaskLocationTable.Cols.TASK_ID, task.getId());
        values.put(TaskLocationTable.Cols.LOCATION_ID, loc.getId());

        long id = mDatabase.insert(TaskLocationTable.NAME, null, values);

        if (id != -1) {
            result = true;
            Log.d(TAG, String.format("Create Task_Location relation(id=%d) Task(id=%d)" +
                    "<==>Location(id=%d)", id, task.getId(), loc.getId()));
        }

        return result;
    }

    public boolean updateTaskLocation(Task task, Location loc) {
        boolean result = false;
        ContentValues values = new ContentValues();
        values.put(TaskLocationTable.Cols.TASK_ID, task.getId());
        values.put(TaskLocationTable.Cols.LOCATION_ID, loc.getId());

        long id = mDatabase.update(TaskLocationTable.NAME,
                values,
                TaskLocationTable.Cols.TASK_ID + " = ?",
                new String[]{Long.toString(task.getId())}
        );

        if (id != -1) {
            result = true;
            Log.d(TAG, String.format("Update Task_Location relation(id=%d) Task(id=%d)" +
                    "<==>Location(id=%d)", id, task.getId(), loc.getId()));
        }

        return result;
    }

    public boolean deleteTaskLocation(Task task) {
        boolean result = false;

        int numOfLine = mDatabase.delete(TaskLocationTable.NAME,
                TaskLocationTable.Cols.TASK_ID + " = ?",
                new String[]{Long.toString(task.getId())}
        );

        if (numOfLine > 0) {
            result = true;
            Log.d(TAG, String.format("Delete %d Task_Location relation with Task(id=%d)",
                    numOfLine, task.getId()));
        }
        return result;
    }

    /**
     * Delete a Model object record from database
     *
     * @return <code>true</code> if successfully delete, otherwise <code>false</code>.
     */
    public <T extends Model> boolean delete(T model) {
        // handle in-memory Task object and error id parameter
        long id = model.getId();
        if (id == -1) {
            if (mTask != null) {
                mTask = null;
                return true;
            } else {
                return false;
            }
        }

        boolean result = false;
        int numOfLine = mDatabase.delete(getTableName(model),
                "ID = ?",
                new String[]{Long.toString(id)}
        );
        if (numOfLine > 0) {
            result = true;
        }

        if (result) {
            Log.d(TAG, String.format("%s(id=%d) deleted", model.getClass().getSimpleName(), id));
        } else {
            Log.d(TAG, String.format("%s(id=%d) failed to delet", model.getClass().getSimpleName(), id));
        }
        return result;
    }

    public Cursor query(String table, String whereClause, String[] whereArgs) {
        Cursor cursor = mDatabase.query(
                table,
                null, // Columns - null selects all columns
                whereClause,
                whereArgs,
                null, // groupBy
                null, // having
                null  // orderBy
        );
        return cursor;
    }

    public <T extends Model> T findById(Class<T> type, long id) {
        T model = null;

        Cursor cursor = query(
                getTableName(type),     //Table Name
                "ID = ?",       //select by primary key ID
                new String[]{Long.toString(id)}
        );

        try {
            model = loadFromCursor(type, cursor);
        } finally {
            cursor.close();
        }

        return model;
    }

    public Schedule findScheduleByTask(Task task) {
        if (task == null) {
            return null;
        }
        if (task.getId() == -1) {
            return null;
        }
        Schedule schedule = null;
        Cursor cursor = query(
                ScheduleTable.NAME,     //Table Name
                ScheduleTable.Cols.TASK_ID + " = ?",       //select by primary key ID
                new String[]{Long.toString(task.getId())}
        );
        ScheduleCursorWrapper cw = new ScheduleCursorWrapper(cursor);
        try {
            if (cw.getCount() == 0) {
                return null;
            }
            cw.moveToFirst();
            schedule = cw.get(task);
        } finally {
            cw.close();
        }

        return schedule;
    }

    public Location findLocationByTask(Task task) {
        if (task == null) {
            return null;
        }
        if (task.getId() == -1) {
            return null;
        }

        Cursor cursor = query(
                TaskLocationTable.NAME,     //Table and Location joint table
                TaskLocationTable.Cols.TASK_ID + " = ?",       //select by primary key ID
                new String[]{Long.toString(task.getId())}
        );
        TaskLocationCursorWrapper cw = new TaskLocationCursorWrapper(cursor);
        long ids[] = null;
        try {
            if (cw.getCount() == 0) {
                return null;
            }
            cw.moveToFirst();
            ids = cw.get();
        } finally {
            cw.close();
        }

        Location loc = null;
        if (ids != null) {
            loc = getLocation(ids[2]);
        }
        return loc;
    }


    public <T extends Model> T loadFromCursor(Class<T> type, Cursor cursor) {
        if (cursor.getCount() == 0) {
            return null;
        }
        T model = null;

        String className = type.getSimpleName();
        if ("Task".equals(className)) { // Task class
            TaskCursorWrapper cw = new TaskCursorWrapper(cursor);
            cw.moveToFirst();
            model = (T) cw.get(mContext);
        } else if ("Schedule".equals(className)) { // Schedule class

        } else if ("Location".equals(className)) { // Location class
            LocationCursorWrapper cw = new LocationCursorWrapper(cursor);
            cw.moveToFirst();
            model = (T) cw.get();
        } else if ("GPSCoordinate".equals(className)) { // GPSCoordinate class

        } else if ("WiFIPosition".equals(className)) { // WiFIPosition

        }
        return model;
    }

    public <T extends Model> List<T> findAll(Class<T> type) {
        List<T> list = null;
        Cursor cursor = query(getTableName(type), null, null);
        try {
            list = loadAllFromCursor(type, cursor);
        } finally {
            cursor.close();
        }
        return list;
    }

    public <T extends Model> List<T> loadAllFromCursor(Class<T> type, Cursor cursor) {
        List<T> list = new ArrayList<>();
        String className = type.getSimpleName();

        if ("Task".equals(className)) { // Task class
            TaskCursorWrapper cw = new TaskCursorWrapper(cursor);
            cw.moveToFirst();
            while (!cw.isAfterLast()) {
                list.add((T) cw.get(mContext));
                cw.moveToNext();
            }
        } else if ("Schedule".equals(className)) { // Schedule class

        } else if ("Location".equals(className)) { // Location class
            LocationCursorWrapper cw = new LocationCursorWrapper(cursor);
            cw.moveToFirst();
            while (!cw.isAfterLast()) {
                list.add((T) cw.get());
                cw.moveToNext();
            }
        } else if ("GPSCoordinate".equals(className)) { // GPSCoordinate class

        } else if ("WiFIPosition".equals(className)) { // WiFIPosition

        }
        return list;
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
