package edu.osu.cse.todolist.to_dolist;

/**
 * Created by AtwoodWang on 15/10/25.
 */

import android.support.v4.app.Fragment;


public class TaskDetailActivity extends SingleFragmentActivity {
    @Override
    protected Fragment createFragment(){
        return new TaskDetailFragment();
    }
}