package edu.osu.cse.todolist.to_dolist;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sniper on 2015/11/3.
 */
public class ToDoLab {
    private static ToDoLab sToDoLab;

    private Context mContext;

    private List<Task> mTasks;
    private List<Folder> mFolders;
    private List<Location> mLocations;
    private List<GPSCoordinate> mGPSCoordinates;
    private List<WiFiPosition> mWiFiPositions;
    private List<AccessPoint> mAccessPoints;
    private List<Schedule> mSchedules;

    public static ToDoLab get(Context context) {
        if (sToDoLab == null) {
            sToDoLab = new ToDoLab(context);
        }
        return sToDoLab;
    }

    private ToDoLab(Context context) {
        mContext = context.getApplicationContext();

        mTasks = new ArrayList<>();
        mFolders = new ArrayList<>();
        mLocations = new ArrayList<>();
        mGPSCoordinates = new ArrayList<>();
        mWiFiPositions = new ArrayList<>();
        mAccessPoints = new ArrayList<>();
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

    public List<Folder> getFolders() {
        return mFolders;
    }

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
}
