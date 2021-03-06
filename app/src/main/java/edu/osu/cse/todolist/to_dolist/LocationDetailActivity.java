package edu.osu.cse.todolist.to_dolist;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.View;

import java.util.List;

/**
 * Created by Zicong on 2015/11/6.
 */
public class LocationDetailActivity extends SingleFragmentActivity {
    private static final String EXTRA_LOCATION_ID = "edu.osu.cse.todolist.to_dolist.location_id";
    private long mlocationId;
    private List<Location> mLocations;
    private Location mLocation;

    @Override
    protected void setToolbar() {
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar_task_list);
        mToolbar.setTitle(R.string.location_setting_title);
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
        mlocationId = (long) getIntent().getSerializableExtra(EXTRA_LOCATION_ID);
        return LocationDetailFragment.newInstance(mlocationId);
    }

    public static Intent newIntent(Context packageContext, long locationId) {
        Intent intent = new Intent(packageContext, LocationDetailActivity.class);
        intent.putExtra(EXTRA_LOCATION_ID, locationId);
        return intent;
    }
}
