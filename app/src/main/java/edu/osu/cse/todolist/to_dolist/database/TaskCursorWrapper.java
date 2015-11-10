package edu.osu.cse.todolist.to_dolist.database;

import android.content.Context;
import android.database.Cursor;
import android.database.CursorWrapper;

import edu.osu.cse.todolist.to_dolist.Location;
import edu.osu.cse.todolist.to_dolist.Schedule;
import edu.osu.cse.todolist.to_dolist.Task;
import edu.osu.cse.todolist.to_dolist.Task.ConfigType;
import edu.osu.cse.todolist.to_dolist.ToDoLab;

import static edu.osu.cse.todolist.to_dolist.database.ToDoDbSchema.TaskTable;

/**
 * Created by Sniper on 2015/11/8.
 */
public class TaskCursorWrapper extends CursorWrapper {
    /**
     * Creates a cursor wrapper.
     *
     * @param cursor The underlying cursor to wrap.
     */
    public TaskCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public Task get(Context context) {
        // get field values
        long id = getLong((getColumnIndex(TaskTable.Cols.ID)));
        String title = getString((getColumnIndex(TaskTable.Cols.TITLE)));
        String note = getString((getColumnIndex(TaskTable.Cols.NOTE)));
        boolean starred = getInt(getColumnIndex(TaskTable.Cols.STARRED)) != 0;
        int configInt = getInt(getColumnIndex(TaskTable.Cols.CONFIG));
        ConfigType config = ConfigType.values()[configInt];
        boolean complete = getInt(getColumnIndex(TaskTable.Cols.COMPLETE)) != 0;
        boolean enabled = getInt(getColumnIndex(TaskTable.Cols.ENABLED)) != 0;

        // create Task object and set values
        Task task = new Task(id);
        task.setTitle(title);
        task.setNote(note);
        task.setStarred(starred);
        task.setConfig(config);
        task.setComplete(complete);
        task.setEnabled(enabled);

        // TODO: set Location and Schedule
        switch (config) {
            case NONE:
                break;
            case TIME:
                Schedule s = ToDoLab.get(context).findScheduleByTask(task);
                task.setSchedule(s);
                break;
            case LOCATION_ARRIVING:
            case LOCATION_LEAVING:
                Location loc = ToDoLab.get(context).findLocationByTask(task);
                task.setLocation(loc);
                break;
        }

        return task;
    }
}
