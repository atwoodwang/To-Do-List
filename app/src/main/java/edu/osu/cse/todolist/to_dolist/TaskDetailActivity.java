package edu.osu.cse.todolist.to_dolist;

/**
 * Created by AtwoodWang on 15/10/25.
 */

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.View;

import java.util.List;
import java.util.UUID;


public class TaskDetailActivity extends SingleFragmentActivity {
    private static final String EXTRA_TASK_ID = "edu.osu.cse.todolist.to_dolist.task_id";
    private long mtaskId;
    private List<Task> mTasks;
    private Task mTask;

    @Override
    protected void setToolbar() {
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar_task_list);
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationIcon(R.drawable.ic_menu_back);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    @Override
    protected Fragment createFragment() {
        mtaskId = (long) getIntent().getSerializableExtra(EXTRA_TASK_ID);
        return TaskDetailFragment.newInstance(mtaskId);
    }

    public static Intent newIntent(Context packageContext, long taskId) {
        Intent intent = new Intent(packageContext, TaskDetailActivity.class);
        intent.putExtra(EXTRA_TASK_ID, taskId);
        return intent;
    }
}