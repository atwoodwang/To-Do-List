package edu.osu.cse.todolist.to_dolist.database;

import android.database.Cursor;
import android.database.CursorWrapper;

import static edu.osu.cse.todolist.to_dolist.database.ToDoDbSchema.*;

/**
 * Created by Sniper on 2015/11/8.
 */
public class TaskLocationCursorWrapper extends CursorWrapper {
    /**
     * Creates a cursor wrapper.
     *
     * @param cursor The underlying cursor to wrap.
     */
    public TaskLocationCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    /**
     * get
     * @return [ID, Task_ID, Location_ID] in an array of long
     */
    public long[] get() {
        long id = getLong((getColumnIndex(TaskLocationTable.Cols.ID)));
        long taskId = getLong((getColumnIndex(TaskLocationTable.Cols.TASK_ID)));
        long locationId = getLong((getColumnIndex(TaskLocationTable.Cols.LOCATION_ID)));
        long[] result = {id, taskId, locationId};
        return result;
    }
}
