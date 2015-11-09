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
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.security.acl.LastOwnerException;
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
    private EditText mNoteField;
    private TextView mTimeTitleTextView;
    private TextView mLocationTextView;
    private static final String DIALOG_DATE = "DialogDate";
    private static final int REQUEST_DATE = 1;
    private static final String ARG_TASK_ID = "task_id";
    private Spinner mRemindSpinner;
    private Spinner mLocationSpinner;
    private Button mShortCutButton;
    private Button mDoneButton;
    private MenuItem mStarred;
    private List<Location> mLocations;
    private List<String> mlocationArray;

    public static TaskDetailFragment newInstance(long taskId) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_TASK_ID, taskId);

        TaskDetailFragment fragment = new TaskDetailFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
        long taskId = (long) getArguments().getSerializable(ARG_TASK_ID);
        mTask = ToDoLab.get(getActivity()).getTask(taskId);


    }

    @Override
    public void onResume() {
        super.onResume();
        updateLocationSpinner();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_task_detail, container, false);


        mTitleField = (EditText) v.findViewById(R.id.task_title);
        mTitleField.setText(mTask.getTitle());
        mTitleField.setCursorVisible(false);
        mTitleField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTitleField.setCursorVisible(true);
            }
        });

        mTitleField.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });
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


        mNoteField = (EditText) v.findViewById(R.id.task_detail);
        mNoteField.setText(mTask.getNote());
        mNoteField.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });

        mNoteField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mTask.setNote(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        mDateButton = (Button) v.findViewById(R.id.task_time);
        if (mTask.getRemindDate() == null) {
            mDateButton.setText(R.string.date_time_setting);
        } else {
            mDateButton.setText(mTask.getRemindDateString());
        }
        mDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mTask.getRemindDate() == null) {
                    mTask.setRemindDate(new Date());
                }
                FragmentManager manager = getFragmentManager();
                DateTimePickerFragment dialog = new DateTimePickerFragment().newInstance(mTask.getRemindDate());
                dialog.setTargetFragment(TaskDetailFragment.this, REQUEST_DATE);
                dialog.show(manager, DIALOG_DATE);
            }
        });


        mTimeTitleTextView = (TextView) v.findViewById(R.id.task_time_label);
        mLocationTextView = (TextView) v.findViewById(R.id.task_location_label);
        mRemindSpinner = (Spinner) v.findViewById(R.id.reminder_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(), R.array.reminder_type_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mRemindSpinner.setAdapter(adapter);
        int position = mTask.getConfig().ordinal();
        mRemindSpinner.setSelection(position);
        mRemindSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                mTask.setConfig(Task.ConfigType.values()[pos]);
                switch (mTask.getConfig()) {
                    case NONE:
                        mTimeTitleTextView.setVisibility(View.GONE);
                        mDateButton.setVisibility(View.GONE);
                        mLocationTextView.setVisibility(View.GONE);
                        mLocationSpinner.setVisibility(View.GONE);
                        mShortCutButton.setVisibility(View.GONE);
                        break;
                    case TIME:
                        mTimeTitleTextView.setVisibility(View.VISIBLE);
                        mDateButton.setVisibility(View.VISIBLE);
                        mLocationTextView.setVisibility(View.GONE);
                        mLocationSpinner.setVisibility(View.GONE);
                        mShortCutButton.setVisibility(View.GONE);
                        break;
                    default:
                        mTimeTitleTextView.setVisibility(View.GONE);
                        mDateButton.setVisibility(View.GONE);
                        mLocationTextView.setVisibility(View.VISIBLE);
                        mLocationSpinner.setVisibility(View.VISIBLE);
                        mShortCutButton.setVisibility(View.VISIBLE);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mLocationSpinner = (Spinner) v.findViewById(R.id.location_spinner);
        updateLocationSpinner();
        mLocationSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                Location loc = (Location) parent.getItemAtPosition(pos);
                if (loc.getId() != -1) {
                    mTask.setLocation(loc);
                } else {
                    mTask.setLocation(null);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mShortCutButton = (Button) v.findViewById(R.id.go_to_location_setting);
        mShortCutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), LocationListActivity.class);
                startActivity(intent);
            }
        });

        mDoneButton = (Button) v.findViewById(R.id.done_button);
        mDoneButton.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View v) {
                if (mTask.getTitle() == null || mTask.getTitle().isEmpty()) {
                    Dialog alertDialog = new AlertDialog.Builder(getActivity())
                            .setMessage("Have to enter a title for your task.")
                            .setPositiveButton("OK", null)
                            .create();
                    alertDialog.show();
                } else if (mTask.getConfig() == Task.ConfigType.TIME & mTask.getRemindDate() == null) {
                    Dialog alertDialog = new AlertDialog.Builder(getActivity())
                            .setMessage("Have to select a time for your task")
                            .setPositiveButton("OK", null)
                            .create();
                    alertDialog.show();
                } else if ((mTask.getConfig() == Task.ConfigType.LOCATION_ARRIVING || mTask.getConfig() == Task.ConfigType.LOCATION_LEAVING) & mTask.getLocation() == null) {
                    Dialog alertDialog = new AlertDialog.Builder(getActivity())
                            .setMessage("Have to select a location for your task")
                            .setPositiveButton("OK", null)
                            .create();
                    alertDialog.show();
                } else {
                    ToDoLab.get(getActivity()).saveTask(mTask);
                    getActivity().onBackPressed();
                }
            }
        });


        return v;
    }

    private void updateLocationSpinner() {
        Location dummyLoc = new Location();
        dummyLoc.setTitle("Select Location");

        List<Location> locations = ToDoLab.get(getActivity()).getLocations();
        locations.add(0, dummyLoc);

        ArrayAdapter<Location> adapter = new ArrayAdapter<>(this.getActivity(), android.R.layout
                .simple_spinner_item, locations);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        mLocationSpinner.setAdapter(adapter);
        int pos = locations.indexOf(mTask.getLocation());
        mLocationSpinner.setSelection(pos);

//        mlocationArray = new ArrayList<String>();
//        mLocations = ToDoLab.get(getActivity()).getLocations();
//        mlocationArray.add("Select Location");
//        for (Location location : mLocations) {
//            mlocationArray.add(location.getTitle());
//        }
//        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_spinner_item, mlocationArray);
//        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        mLocationSpinner.setAdapter(adapter1);
//        int locationposition = mLocations.indexOf(mTask.getLocation()) + 1;
//        mLocationSpinner.setSelection(locationposition);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }

        if (requestCode == REQUEST_DATE) {
            Date date = (Date) data
                    .getSerializableExtra(DateTimePickerFragment.EXTRA_DATE);
            mTask.setRemindDate(date);
            // Update schedule into database if it's not in-memory
//            Schedule s = mTask.getSchedule();
//            if (s.getId() != -1) {
//                ToDoLab.get(getActivity()).save(s);
//            }
            mDateButton.setText(mTask.getRemindDateString());
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_task_detail, menu);
        mStarred = menu.findItem(R.id.menu_item_task_importance);
        if (mTask.isStarred()) {
            mStarred.setIcon(R.drawable.ic_task_important);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_delete_task:
                Dialog alertDialog = new AlertDialog.Builder(getActivity())
                        .setMessage("Do you want to delete this task?")
                        .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
//                                ToDoLab.get(getActivity()).removeTask(mTask);
                                ToDoLab.get(getActivity()).delete(mTask);
                                getActivity().finish();
                            }
                        })
                        .setNegativeButton("Cancel", null)
                        .create();
                alertDialog.show();
                return true;
            case R.id.menu_item_task_importance:
                if (!mTask.isStarred()) {
                    mTask.setStarred(true);
                    mStarred.setIcon(R.drawable.ic_task_important);
                    Toast.makeText(getActivity(), "You have set this task to be important", Toast.LENGTH_SHORT).show();
                } else {
                    mTask.setStarred(false);
                    mStarred.setIcon(R.drawable.ic_task_not_important);
                    Toast.makeText(getActivity(), "You have set this task to be unimportant", Toast.LENGTH_SHORT).show();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}

//TODO FIX ROTATE BUG