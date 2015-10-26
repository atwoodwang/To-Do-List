package edu.osu.cse.todolist.to_dolist;

/**
 * Created by AtwoodWang on 15/10/25.
 */
import android.content.Context;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Created by AtwoodWang on 15/10/25.
 */
public class TaskLab {
    private static TaskLab sTaskLab;

    private ArrayList<Task> mTasks;

    private Date mDate;

    public static TaskLab get(Context context){
        if (sTaskLab == null){
            sTaskLab = new TaskLab(context);
        }
        return sTaskLab;
    }

    private TaskLab(Context context){
        mTasks = new ArrayList<>();
        mDate = new Date();
        for (int i=0;i<100;i++){
            Task task = new Task();
            task.setTitle("Task #" + i);
            task.setDetail("Detail #" + i);
            task.setNeed(i % 2 == 0);
            task.setDate(mDate);
            mTasks.add(task);
        }
    }

    public List<Task> getTasks(){
        return mTasks;
    }

    public Task getTask(UUID id){
        for (Task task:mTasks){
            if (task.getId().equals(id)){
                return task;
            }
        }
        return null;
    }
}
