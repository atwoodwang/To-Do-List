package edu.osu.cse.todolist.to_dolist;

import android.content.ContentValues;

import java.util.List;

/**
 * Created by NoAnyLove on 2015/11/2.
 */
public class Folder extends Model {
    private String mTitle;
    private List<Task> mTasks;
    // TODO: add ability of virtual folder

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    /**
     * Get all uncompleted Tasks inside this Folder
     *
     * @return all Tasks belongs to this Folder with isComplete() return false
     */
    public List<Task> getTasks() {
        return mTasks;
    }

    public boolean addTask(Task task) {
        boolean result = false;
        if (task != null) {
            if (!mTasks.contains(task)) {
                // TODO: tasks with same id but different instances maybe treated as different
                // tasks, need sovle this problem
                task.setForeignKey(getId());
                if (mTasks.add(task)) {
                    result = true;
                }
            }
        }
        return result;
    }

    /**
     * Remove a Task from this Folder
     *
     * @param task the Task need to be removed
     * @return the removed Task if remove successfully, otherwise return <code>null</code>
     */

    public Task removeTask(Task task) {
        Task result = null;
        if (mTasks.remove(task)) {
            result = task;
//            task.setForeignKey(ToDoLab.getDefaultFolder.getId());
//            task.save();
        }
        // TODO: update database
        return result;
    }

    /**
     * Remove all Tasks inside this Folder
     * <p/>
     * Remove all Tasks inside this Folder, including completed and uncompleted. This method is used
     * to completely remove a Folder and all Tasks associated with it.
     *
     * @return all removed tasks inside this Folder if successful, otherwise null
     */
    public List<Task> removeAllTask() {
        return null;
    }

    /**
     * Get the number of uncompleted tasks inside this folder
     *
     * @return number of tasks inside this folder
     */
    public int count() {
        return mTasks.size();
    }

    //TODO: add methods for completed Tasks
    public List<Task> getCompleteTasks() {
        return null;
    }

    public ContentValues getContentValues() {
        return null;
    }
}
