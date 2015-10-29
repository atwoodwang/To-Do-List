package edu.osu.cse.todolist.to_dolist;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

/**
 * Created by Zicong on 2015/10/27.
 */
public abstract class SingleFragmentActivity extends AppCompatActivity {
    protected abstract Fragment createFragment();
    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);

        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragment_container);

        if (fragment == null) {
            fragment = createFragment();
            fm.beginTransaction().add(R.id.fragment_container, fragment).commit();
        }

        mToolbar = (Toolbar)findViewById(R.id.toolbar_task_list);
        setSupportActionBar(mToolbar);
    }
}
