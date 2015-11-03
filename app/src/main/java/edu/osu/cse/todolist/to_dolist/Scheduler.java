package edu.osu.cse.todolist.to_dolist;

import java.util.List;

/**
 * Created by Sniper on 2015/11/3.
 */

// TODO: This should be a singleton class

public class Scheduler {
    private List<Schedule> mSchedules;

    public List<Schedule> getSchedules() {
        return mSchedules;
    }

    public void setSchedules(List<Schedule> schedules) {
        mSchedules = schedules;
    }

    public void addSchedule(Schedule sch) {

    }

    public Schedule removeSchedule(Schedule sch) {
        return null;
    }

    public Schedule checkSchedule() {
        return null;
    }
}
