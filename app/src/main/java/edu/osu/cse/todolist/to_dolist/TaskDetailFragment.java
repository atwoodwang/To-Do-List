package edu.osu.cse.todolist.to_dolist;

/**
 * Created by AtwoodWang on 15/10/25.
 */
import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

public class TaskDetailFragment extends Fragment {
    private Task mTask;
    private Button mDateButton;
    private EditText mTitleField;
    private EditText mDetailField;
    private CheckBox mNeedCheckBox;
    private static final String DIALOG_DATE = "DialogDate";
    private static final int REQUEST_DATE = 1;
    private static final String ARG_TASK_ID = "task_id";


    public static TaskDetailFragment newInstance(UUID taskId){
        Bundle args = new Bundle();
        args.putSerializable(ARG_TASK_ID,taskId);

        TaskDetailFragment fragment = new TaskDetailFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);


        setHasOptionsMenu(true);
        UUID taskId = (UUID)getArguments().getSerializable(ARG_TASK_ID);
        mTask = TaskLab.get(getActivity()).getTask(taskId);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_task_detail, container, false);

        mTitleField = (EditText)v.findViewById(R.id.task_title);
        mTitleField.setText(mTask.getTitle());
        mTitleField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mTask.setTitle(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mDetailField = (EditText)v.findViewById(R.id.task_detail);
        mDetailField.setText(mTask.getDetail());
        mDetailField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mTask.setDetail(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mDateButton = (Button)v.findViewById(R.id.task_time);
        mDateButton.setText(Task.formatDate(mTask.getDate()));
        mDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager manager = getFragmentManager();
                DateTimePickerFragment dialog = new DateTimePickerFragment().newInstance(mTask.getDate());
                dialog.setTargetFragment(TaskDetailFragment.this, REQUEST_DATE);
                dialog.show(manager, DIALOG_DATE);

            }
        });


        mNeedCheckBox = (CheckBox)v.findViewById(R.id.needCheckBox);
        mNeedCheckBox.setChecked(mTask.isNeed());
        mNeedCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mTask.setNeed(isChecked);
            }
        });

        return v;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }

        if (requestCode == REQUEST_DATE) {
            Date date = (Date) data
                    .getSerializableExtra(DateTimePickerFragment.EXTRA_DATE);
            mTask.setDate(date);
            mDateButton.setText(Task.formatDate(date));
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu,MenuInflater inflater){
        super.onCreateOptionsMenu(menu,inflater);
        inflater.inflate(R.menu.fragment_task_detail,menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.menu_item_delete_task:
                Dialog alertDialog = new AlertDialog.Builder(getActivity())
                        .setMessage("Do you want to delete this task?")
                        .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                TaskLab taskLab = TaskLab.get(getActivity());
                                List<Task> mTasks = taskLab.getTasks();
                                mTasks.remove(mTask);
                                getActivity().finish();
                            }
                        })
                        .setNegativeButton("Cancel",null)
                        .create();
                alertDialog.show();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

}

