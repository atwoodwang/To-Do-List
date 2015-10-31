package edu.osu.cse.todolist.to_dolist;

import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;

/**
 * Created by Zicong on 2015/10/27.
 */
public class TaskListActivity extends SingleFragmentActivity {
    private Toolbar mToolbar;

    @Override
    protected Fragment createFragment(){
        return new TaskListFragment();
    }

    @Override
    protected void setToolbar(){
        mToolbar = (Toolbar)findViewById(R.id.toolbar_task_list);
        setSupportActionBar(mToolbar);
    }
}
