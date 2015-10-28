package edu.osu.cse.todolist.to_dolist;

import android.support.v4.app.Fragment;

/**
 * Created by Zicong on 2015/10/27.
 */
public class TaskListActivity extends SingleFragmentActivity {
    @Override
    protected Fragment createFragment(){
        return new TaskListFragment();
    }
}
