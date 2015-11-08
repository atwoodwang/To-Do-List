package edu.osu.cse.todolist.to_dolist;

import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.View;

/**
 * Created by Zicong on 2015/11/6.
 */
public class LocationListActivity extends SingleFragmentActivity {
    private Toolbar mToolbar;

    @Override
    protected Fragment createFragment() {
        return new LocationListFragment();
    }

    @Override
    protected void setToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar_task_list);
        mToolbar.setTitle(R.string.location_setting);
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationIcon(R.drawable.ic_menu_back);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

}
