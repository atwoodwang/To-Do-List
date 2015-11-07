package edu.osu.cse.todolist.to_dolist;

import android.util.Log;

/**
 * Base class for Models, similar to Active Record style
 */
public class Model {
    /**
     *
     */
    private long mId;

    /**
     * Foreign key reference to another Object
     */
    private long mForeignKey;

    /**
     * Tag used for debug
     */
    private static final String TAG = "ToDoList-Model";

    public long getId() {
        return mId;
    }

    /**
     * Set primary key value when save/update object into database
     * <p/>
     * <b>DO NOT CALL</b> this method from Controller. It should only be called from Model
     *
     * @param id object associated primary key
     */
    public void setId(long id) {
        mId = id;
    }

    public Model() {
        mId = -1;
        mForeignKey = -1;

        Log.d(TAG, String.format("%s(id=%d) created", getClass().getName(), mId));
    }

    public Model(long id) {
        mId = id;
        mForeignKey = -1;

        Log.d(TAG, String.format("%s(id=%d) created", getClass().getName(), mId));
    }

    public long getForeignKey() {
        return mForeignKey;
    }

    public void setForeignKey(long foreignKey) {
        mForeignKey = foreignKey;
    }

}
