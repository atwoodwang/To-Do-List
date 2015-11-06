package edu.osu.cse.todolist.to_dolist;

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
// No need for Folder.addTask(), use Task.setFolder() instead
//    public void addTask(Task task) {
//        if (!mTasks.contains(task)) {
//            mTasks.add(task);
//        }
//        // TODO: update database
//    }

    // No need for Folder.removeTask(), use Task.setFolder() instead
    // A Task must belongs to one Folder
//    /**
//     * Remove a Task from this Folder
//     *
//     * @param task the Task need to be removed
//     * @return the removed Task if remove successfully, otherwise return <code>null</code>
//     */
//    public Task removeTask(Task task) {
//        if (mTasks.remove(task)) {
//            return task;
//        } else {
//            return null;
//        }
//        // TODO: update database
//    }

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

}
