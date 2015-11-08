package edu.osu.cse.todolist.to_dolist.database;

import android.database.Cursor;
import android.database.CursorWrapper;

import java.util.Date;

import edu.osu.cse.todolist.to_dolist.Schedule;
import edu.osu.cse.todolist.to_dolist.Schedule.ConfigType;
import edu.osu.cse.todolist.to_dolist.Task;

import static edu.osu.cse.todolist.to_dolist.database.ToDoDbSchema.*;

/**
 * Created by Sniper on 2015/11/8.
 */
public class ScheduleCursorWrapper extends CursorWrapper {
    /**
     * Creates a cursor wrapper.
     *
     * @param cursor The underlying cursor to wrap.
     */
    public ScheduleCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public Schedule get(Task task) {
        // get field values
        long id = getLong((getColumnIndex(ScheduleTable.Cols.ID)));
        long taskId = getLong((getColumnIndex(ScheduleTable.Cols.TASK_ID)));
        int configInt = getInt(getColumnIndex(ScheduleTable.Cols.CONFIG));
        ConfigType config = ConfigType.values()[configInt];
        long date = getLong((getColumnIndex(ScheduleTable.Cols.DATE)));

        // create Task object and set values
        Schedule s = new Schedule(id, task);
        s.setConfig(config);
        s.setDate(new Date(date));

        return s;
    }
}
