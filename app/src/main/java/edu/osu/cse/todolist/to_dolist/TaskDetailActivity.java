package edu.osu.cse.todolist.to_dolist;

/**
 * Created by AtwoodWang on 15/10/25.
 */

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import java.util.UUID;


public class TaskDetailActivity extends SingleFragmentActivity {
    private  static final String EXTRA_TASK_ID = "edu.osu.cse.todolist.to_dolist.task_id";

    @Override
    protected Fragment createFragment(){
        UUID taskId = (UUID) getIntent().getSerializableExtra(EXTRA_TASK_ID);
        return TaskDetailFragment.newInstance(taskId);
    }

    public static Intent newIntent(Context packageContext,UUID taskId){
        Intent intent = new Intent(packageContext,TaskDetailActivity.class);
        intent.putExtra(EXTRA_TASK_ID,taskId);
        return intent;
    }
}