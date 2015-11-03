package edu.osu.cse.todolist.to_dolist;

import java.util.List;

/**
 * Created by NoAnyLove on 2015/11/2.
 */
public class Folder {
    private long mId;
    private String mTitle;
    private List<Task> mTasks;
    // TODO: add ability of virtual folder

    public Folder() {
        mId = -1;
    }

    public Folder(long id) {
        // id is the row id of Folder table
        mId = id;
    }

    public long getId() {
        return mId;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public List<Task> getTasks() {
        return mTasks;
    }

    public void addTask(Task task) {
        if (!mTasks.contains(task)) {
            mTasks.add(task);
        }
        // TODO: update database
    }

    public Task removeTask(Task task) {
        if (mTasks.remove(task)) {
            return task;
        } else {
            return null;
        }
        // TODO: update database
    }

    public List<Task> removeAllTask() {
        return null;
    }

    public int count() {
        return mTasks.size();
    }

    public boolean save() {
        boolean result = false;
        if (mId == -1) {
            // TODO: write into database, and update mId with ROW_ID
            // Save object into database
            result = true;
        } else {
            // Update database
            result = true;
        }
        return result;
    }
}
