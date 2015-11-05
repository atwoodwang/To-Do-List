package edu.osu.cse.todolist.to_dolist;

import android.util.Log;

/**
 * Base class for Models, similar to Active Record style
 */
public class Model {
    private long mId;

    /**
     * Tag used for debug
     */
    private static final String TAG = "ToDoList-Model";

    /**
     * Use static class variable to simulate SQLite ROW_ID. This is only used for developing and
     * test
     */
    private static long dummyId = 1;

    public long getId() {
        return mId;
    }

    public Model() {
        mId = -1;

        Log.d(TAG, String.format("%s(id=%d) created", getClass().getName(), mId));
    }

    public Model(long id) {
        mId = id;

        Log.d(TAG, String.format("%s(id=%d) created", getClass().getName(), mId));
    }

    /**
     * Save/update this object to database
     *
     * @return <code>true</code> if save/update successfully, otherwise <code>false</code>
     */
    public boolean save() {
        boolean result = false;
        if (mId == -1) {
            // TODO: write into database, and update mId with ROW_ID
            // Save object into database
            mId = dummyId++;
            result = true;
        } else {
            // Retrieve object from database if necessary
            // and then Update database
            result = true;
        }

        Log.d(TAG, String.format("%s(id=%d) saved", getClass().getName(), mId));

        return result;
    }

    /**
     * Delete object record from database
     *
     * @return <code>true</code> if successfully delete, otherwise <code>false</code>.
     */
    public boolean delete() {
        boolean result = false;
        if (mId != -1) {
            result = true;
            Log.d(TAG, String.format("%s(id=%d) deleted", getClass().getName(), mId));
            mId = -1;
        } else {
            Log.d(TAG, String.format("Cannot delete %s(id=%d)", getClass().getName(), mId));
        }
        return result;
    }

    // Not very sure if we really need this method

    /**
     * Find an object from database with its id
     *
     * @param type class type of the object
     * @param id   primary key id
     * @param <T>  class type
     * @return the object if found, otherwise return <code>null</code>
     */
    public static <T> T findById(Class<T> type, Long id) {
        return null;
    }
}
