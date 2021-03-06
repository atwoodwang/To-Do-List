package edu.osu.cse.todolist.to_dolist;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;


import edu.osu.cse.todolist.to_dolist.database.GPSCoordinateCursorWrapper;
import edu.osu.cse.todolist.to_dolist.database.LocationCursorWrapper;
import edu.osu.cse.todolist.to_dolist.database.ScheduleCursorWrapper;
import edu.osu.cse.todolist.to_dolist.database.TaskCursorWrapper;
import edu.osu.cse.todolist.to_dolist.database.TaskLocationCursorWrapper;
import edu.osu.cse.todolist.to_dolist.database.ToDoBaseHelper;
import edu.osu.cse.todolist.to_dolist.database.WiFiPositionCursorWrapper;

import static edu.osu.cse.todolist.to_dolist.database.ToDoDbSchema.GPSCoordinateTable;
import static edu.osu.cse.todolist.to_dolist.database.ToDoDbSchema.LocationTable;
import static edu.osu.cse.todolist.to_dolist.database.ToDoDbSchema.ScheduleTable;
import static edu.osu.cse.todolist.to_dolist.database.ToDoDbSchema.TaskLocationTable;
import static edu.osu.cse.todolist.to_dolist.database.ToDoDbSchema.TaskTable;
import static edu.osu.cse.todolist.to_dolist.database.ToDoDbSchema.WiFiPositionTable;

/**
 * ToDoLab singleton centralized data class
 */
public class ToDoLab {
    private static ToDoLab sToDoLab;

    private Context mContext;

    /**
     * SQLite database instance for all database manipulation
     */
    private SQLiteDatabase mDatabase;

    /**
     * Currently manipulate Task
     * <p/>
     * Used to transmit Task between different Activities and Fragments
     */
    private Task mTask;

    /**
     * Used to save Task state during rotation
     */
    private Task mTaskState;

    /**
     * Used to save Location state during rotation
     */
    private Location mLocationState;

    /**
     * Current in-memory GPSCoordinate
     * <p/>
     * Used to transmit GPSCoordinate between different Activities and Fragments
     */
    private GPSCoordinate mGPSCoordinate;

    /**
     * Current in-memory Location
     * <p/>
     * Used to transmit Location between different Activities and Fragments
     */
    private Location mLocation;

    /**
     * Cache tasks retrieved from database
     * <p/>
     * used to improve the performance of getTasks() method
     */
    private List<Task> mCachedTasks;

    /**
     * a flag/control on cached tasks
     */
    private boolean mCached;

    /**
     * Tag used for debug
     */
    private static final String TAG = "ToDoLab";
    private long mCurrentTime;

    public static ToDoLab get(Context context) {
        if (sToDoLab == null) {
            sToDoLab = new ToDoLab(context);
        }
        return sToDoLab;
    }

    /**
     * ToDoLab instantiation
     *
     * @param context an Activity context or Application context
     */
    private ToDoLab(Context context) {
        mContext = context.getApplicationContext();
        mDatabase = new ToDoBaseHelper(mContext).getWritableDatabase();

        // initialize cached flag and tasks
        mCached = false;
        mCachedTasks = null;

        //Populate some tasks for test and profiling
        //populateTestData();
    }

    public Context getContext() {
        return mContext;
    }

    public GPSCoordinate getGPSCoordinate() {
        return mGPSCoordinate;
    }

    public void setGPSCoordinate(GPSCoordinate GPSCoordinate) {
        mGPSCoordinate = GPSCoordinate;
    }

    /**
     * Retrieve all tasks from database, cached result after the first retrieved
     *
     * @return the list of all tasks
     */
    public List<Task> getTasks() {
        if (!mCached) {
            mCachedTasks = findAll(Task.class);
            mCached = true;
            Log.d(TAG, "[+] Cached all task list", new Exception());
        } else {
            Log.d(TAG, "[+] Retrieve task list from cache", new Exception());
        }
        return mCachedTasks;
    }

    public List<Location> getLocations() {
        return findAll(Location.class);
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


    public Task getTaskState() {
        return mTaskState;
    }

    public void setTaskState(Task taskState) {
        mTaskState = taskState;
    }

    public Location getLocationState() {
        return mLocationState;
    }

    public void setLocationState(Location locationState) {
        mLocationState = locationState;
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

    public boolean saveLocation(Location location) {
        if (location == null) {
            return false;
        }

        // save Location first
        if (!save(location)) {
            return false;
        }

        // save associated GPSCoordinate or WiFiPosition according to ConfigType
        boolean result = false;
        switch (location.getConfig()) {
            case GPS:
                GPSCoordinate gps = location.getGPSCoordinate();
                // Avoid saving null GPSCoordinate object
                if (gps != null) {
                    gps.setForeignKey(location.getId());
                    if (save(gps)) {
                        result = true;
                    }
                }
                break;
            case WiFi:
                WiFiPosition wifiPos = location.getWiFiPosition();
                // Avoid saving null WiFiPosition object
                if (wifiPos != null) {
                    wifiPos.setForeignKey(location.getId());
                    if (save(wifiPos)) {
                        result = true;
                    }
                }
                break;
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
                result = true;
                break;
            case TIME:
                Schedule schedule = task.getSchedule();
                // Avoid saving null schedule object
                if (schedule != null && save(schedule)) {
                    result = true;
                }
                break;
            case LOCATION_ARRIVING:
            case LOCATION_LEAVING:
                // TODO: refactor createTaskLocation and updateTaskLocation into one method
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

        // if successfully saved the task, and the tasks list is already cached, then update the
        // cached list
        if (result && mCached) {
            /* the simplest way is to retrieve tasks from database again. However, since we're trying
             to improve the performance by decrease invoking SQLite database query, we're going to
             directly manipulate the memory list */
            // TODO: may implement Task.equals(), and then use mCachedTasks.contains()
            // indicates if the newly add task is already in cached list
            boolean bFound = false;
            long taskId = task.getId();
            for (int i = 0; i < mCachedTasks.size(); i++) {
                if (mCachedTasks.get(i).getId() == taskId) {
                    bFound = true;
                    mCachedTasks.set(i, task);
                    break;
                }
            }
            // if newly add task isn't in cached list, just add it to
            if (!bFound) {
                mCachedTasks.add(task);
            }

            Log.d(TAG, "[+] Update cached task list");
        }

        return result;
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
            Log.d(TAG, String.format("%s(id=%d) failed to delete", model.getClass().getSimpleName
                    (), id));
        }
        return result;
    }

    public void deleteLocation(Location location) {
        if (location == null) {
            return;
        }
        if (location.getId() == -1) {
            return;
        }
        // Remove associated GPSCoordinate
        GPSCoordinate gps = findGPSCoordinateByLocation(location);
        if (gps != null) {
            delete(gps);
        }

        // Remove associated WiFiPosition
        WiFiPosition wifiPos = findWiFiPositionByLocation(location);
        if (wifiPos != null) {
            delete(wifiPos);
        }

        // Remove Location itself
        delete(location);
    }

    public void deleteTask(Task task) {
        if (task == null) {
            return;
        }
        if (task.getId() == -1) {
            return;
        }
        // Remove associated Schedule
        Schedule schedule = findScheduleByTask(task);
        if (schedule != null) {
            delete(schedule);
        }
        // Remove associated Task-Location joint table record
        deleteTaskLocation(task);

        // Remove Task itself
        delete(task);

        // handle cached task list
        if (mCached) {
            /* the simplest way is to retrieve tasks from database again. However, since we're trying
             to improve the performance by decrease invoking SQLite database query, we're going to
             directly manipulate the memory list */
            long taskId = task.getId();
            int taskIndex = -1;
            for (int i = 0; i < mCachedTasks.size(); i++) {
                if (mCachedTasks.get(i).getId() == taskId) {
                    taskIndex = i;
                    break;
                }
            }
            // if found the task in cached list, delete it
            if (taskIndex != -1) {
                mCachedTasks.remove(taskIndex);
                Log.d(TAG, "[+] Update cached task list");
            }
        }
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

    public GPSCoordinate findGPSCoordinateByLocation(Location loc) {
        if (loc == null) {
            return null;
        }
        if (loc.getId() == -1) {
            return null;
        }
        GPSCoordinate gps = null;
        Cursor cursor = query(GPSCoordinateTable.NAME,
                GPSCoordinateTable.Cols.LOCATION_ID + " = ?",
                new String[]{Long.toString(loc.getId())}
        );
        GPSCoordinateCursorWrapper cw = new GPSCoordinateCursorWrapper(cursor);
        try {
            if (cw.getCount() == 0) {
                return null;
            }
            cw.moveToFirst();
            gps = cw.get();
        } finally {
            cw.close();
        }

        return gps;
    }

    public WiFiPosition findWiFiPositionByLocation(Location loc) {
        if (loc == null) {
            return null;
        }
        if (loc.getId() == -1) {
            return null;
        }
        WiFiPosition wifiPos = null;
        Cursor cursor = query(WiFiPositionTable.NAME,
                WiFiPositionTable.Cols.LOCATION_ID + " = ?",
                new String[]{Long.toString(loc.getId())}
        );
        WiFiPositionCursorWrapper cw = new WiFiPositionCursorWrapper(cursor);
        try {
            if (cw.getCount() == 0) {
                return null;
            }
            cw.moveToFirst();
            wifiPos = cw.get();
        } finally {
            cw.close();
        }

        return wifiPos;
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
            model = (T) cw.get(mContext);
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
                list.add((T) cw.get(mContext));
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
     * @return the associated table name of given class name if exists, otherwise an empty string
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
        } else if ("WiFiPosition".equals(className)) { // WiFIPosition
            tableName = WiFiPositionTable.NAME;
        }

        return tableName;
    }

    /**
     * Check if there exists task that need to be reminded
     *
     * @return <code>true</code> if exists task which needs remind, otherwise <code>false</code>
     */
    public boolean checkRemindTask() {
        boolean result = false;

        List<Task> tasks = getTasks();
        for (Task task : tasks) {
            // bypass finished tasks
            if (task.isComplete()) {
                continue;
            }
            // bypass remind disabled tasks
            if (!task.isEnabled()) {
                continue;
            }

            Task.ConfigType config = task.getConfig();
            // bypass NONE remind type tasks
            if (Task.ConfigType.NONE.equals(config)) {
                continue;
            }

//            // check if a task is reminded by time, and its time isn't passed by
//            if (Task.ConfigType.TIME.equals(config)) {
//                long currentTime = new Date().getTime();
//                // TODO: Warining, if launch the alarm too late, it may not be triggered
//                // TODO: need improve time remind
//                // bypass timeout tasks
//                if (task.isTimeout(currentTime)) {
//                    continue;
//                } else {
//                    return true;
//                }
//            }

            if (Task.ConfigType.LOCATION_ARRIVING.equals(config) ||
                    Task.ConfigType.LOCATION_LEAVING.equals(config)) {
                // avoid null error
                if (task.getLocation() != null) {
                    return true;
                }
            }

        }

        Log.d(TAG, "checkRemindTask called, return " + result, new Exception());
        return result;
    }

    /**
     * Set the time alarm if needed, cancel the alarm if no tasks needed to be reminded.
     */
    public void setTimeAlarm() {
        List<Task> tasks = getCurrentTimeTasks();
        TimeAlarmReceiver timeAlarmReceiver = new TimeAlarmReceiver();
        //Cancel the alarm if no tasks.
        if (tasks == null || tasks.size() == 0) {
            timeAlarmReceiver.cancelAlarm(getContext());
            return;
        }
        //Get the earliest task.
        Task task = tasks.get(0);
        timeAlarmReceiver.setAlarm(getContext(), task);
    }

    /**
     * Check whether there is time task needed to be reminded.
     *
     * @return List of tasks that needs remind in a sorted order, otherwise <code>null</code>
     */
    public List<Task> getCurrentTimeTasks() {
        List<Task> tasks = getTasks();
        List<Task> timeTasks = new ArrayList<>();
        for (Task task : tasks) {
            // bypass finished tasks
            if (task.isComplete()) {
                continue;
            }
            // bypass remind disabled tasks
            if (!task.isEnabled()) {
                continue;
            }

            Task.ConfigType config = task.getConfig();
            // bypass Other remind type tasks
            if (!Task.ConfigType.TIME.equals(config)) {
                continue;
            }
            //bypass the Time task that is out of date.
            long currentTime = new Date().getTime();
            if (task.isTimeout(currentTime)) {
                continue;
            }
            timeTasks.add(task);
        }
        //Sort the tasks in a increasing time order.The earlier task will be earlier in the list.
        Collections.sort(timeTasks, new Comparator<Task>() {
            @Override
            public int compare(Task a, Task b) {
                return a.getTime() < b.getTime() ? -1 : 0;
            }
        });

        return timeTasks;
    }


    /**
     * Turn on/off the alarm used for remind service, according to the exists of tasks that needs
     * remind. The exists of remind task is determined by checkRemindTask().
     */
    public void setupRemindService() {
        Log.d(TAG, "setupRemindService called");

        AlarmReceiver alarmReceiver = new AlarmReceiver();
        if (checkRemindTask()) {
            Log.d(TAG, "turn on alarm service");
            alarmReceiver.setAlarm(getContext());
        } else {
            Log.d(TAG, "turn off alarm service");
            alarmReceiver.cancelAlarm(getContext());
        }

        if (getLocationArrivingTasks().size() > 0 || getLocationLeavingTasks().size() > 0) {
            // save the current connected WiFi info, because we won't receive CONNECTED intent
            // for already connected WiFi AP
            String info[] = WiFiPosition.getCurrentWifiInfo(mContext);
            if (info != null) {
                String ssid = info[0];
                String bssid = info[1];
                WiFiReceiver.setLastConnectedWiFi(mContext, ssid, bssid);
            }
            WiFiReceiver.setWiFiRemindEnabled(mContext, true);
        } else {
            WiFiReceiver.setWiFiRemindEnabled(mContext, false);
        }
    }

    /**
     * Return the list of tasks which needs to be reminded. Remind disabled tasks, finished tasks
     * and timeout tasks are not included in the list.
     *
     * @return the list of tasks satisfied above criteria
     */
    public List<Task> getInTimeTasks(long currentTime) {
        List<Task> allTasks = getTasks();
        List<Task> filteredTasks = new ArrayList<>();
        for (Task task : allTasks) {
            // bypass finished tasks
            if (task.isComplete()) {
                continue;
            }
            // bypass remind disabled tasks
            if (!task.isEnabled()) {
                continue;
            }

            // bypass other remind type tasks
            Task.ConfigType config = task.getConfig();
            if (!Task.ConfigType.TIME.equals(config)) {
                continue;
            }

            if (task.isInTime(currentTime)) {
                // only add eligible tasks to the filteredTasks
                filteredTasks.add(task);
            }
        }
        return filteredTasks;
    }

    public List<Task> getLocationArrivingTasks() {
        List<Task> allTasks = getTasks();
        List<Task> filteredTasks = new ArrayList<>();
        for (Task task : allTasks) {
            // bypass finished tasks
            if (task.isComplete()) {
                continue;
            }
            // bypass remind disabled tasks
            if (!task.isEnabled()) {
                continue;
            }

            // bypass other remind type tasks
            Task.ConfigType config = task.getConfig();
            if (!Task.ConfigType.LOCATION_ARRIVING.equals(config)) {
                continue;
            }

            // avoid null pointer crash
            Location location = task.getLocation();
            if (location != null) {
                WiFiPosition wiFiPosition = location.getWiFiPosition();
                if (wiFiPosition != null && wiFiPosition.getSSID() != null
                        && wiFiPosition.getBSSID() != null) {
                    filteredTasks.add(task);
                }
            }
        }
        return filteredTasks;
    }

    public List<Task> getLocationLeavingTasks() {
        List<Task> allTasks = getTasks();
        List<Task> filteredTasks = new ArrayList<>();
        for (Task task : allTasks) {
            // bypass finished tasks
            if (task.isComplete()) {
                continue;
            }
            // bypass remind disabled tasks
            if (!task.isEnabled()) {
                continue;
            }

            // bypass other remind type tasks
            Task.ConfigType config = task.getConfig();
            if (!Task.ConfigType.LOCATION_LEAVING.equals(config)) {
                continue;
            }

            // avoid null pointer crash
            Location location = task.getLocation();
            if (location != null) {
                WiFiPosition wiFiPosition = location.getWiFiPosition();
                if (wiFiPosition != null && wiFiPosition.getSSID() != null
                        && wiFiPosition.getBSSID() != null) {
                    filteredTasks.add(task);
                }
            }
        }
        return filteredTasks;
    }

    /**
     * Populate given {@code num} number of tasks into database, used for testing and profiling
     *
     * @param num
     */
    public void populateTestData(int num) {
        //Generate 300 data for performance profiling
        Log.d(TAG, "Start populate data");
        for (int i = 0; i < num; i++) {
            Task task = new Task();
            task.setTitle("Task #" + i);

            if (i % 2 == 0) {
                task.setStarred(true);
            }

            if (i % 3 == 0) {
                task.setComplete(true);
            }

            saveTask(task);
        }
        Log.d(TAG, "finish populate data");
    }
}
