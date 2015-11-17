package edu.osu.cse.todolist.to_dolist;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
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
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Zicong on 2015/11/6.
 */
public class LocationDetailFragment extends Fragment {
    private Location mLocation;
    private EditText mTitleField;
    private EditText mNoteField;
    private Button mDoneButton;
    private LinearLayout mWifiSettingLayout;
    private Button mWifiAdvancedSettingButton;
    private Button mGPSSettingButton;
    private TextView mGPSCurrentSettingTextView;
    private TextView mCurrentWifiInfoTextView;
    private CheckBox mUseCurrentWifiCheckBox;
    private Spinner mLocationTypeSpinner;
    private GPSCoordinate mGPSCoordinate;
    private static final String ARG_LOCATION_ID = "location_id";

    public static LocationDetailFragment newInstance(long locationId) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_LOCATION_ID, locationId);

        LocationDetailFragment fragment = new LocationDetailFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        long locationId = (long) getArguments().getSerializable(ARG_LOCATION_ID);
        mLocation = ToDoLab.get(getActivity()).getLocation(locationId);
        mGPSCoordinate = mLocation.getGPSCoordinate();
        if (mGPSCoordinate == null) {
            mGPSCoordinate = new GPSCoordinate(-1);
            mLocation.setGPSCoordinate(mGPSCoordinate);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        updateGPSTextView();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_location_detail, container, false);

        mTitleField = (EditText) v.findViewById(R.id.location_title);
        mTitleField.setText(mLocation.getTitle());
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
                mLocation.setTitle(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });


        mNoteField = (EditText) v.findViewById(R.id.location_detail);
        mNoteField.setText(mLocation.getNote());
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
                mLocation.setNote(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mWifiSettingLayout = (LinearLayout) v.findViewById(R.id.wifi_setting_layout);
        mCurrentWifiInfoTextView = (TextView) v.findViewById(R.id.current_wifi_info_textview);
        mWifiAdvancedSettingButton = (Button) v.findViewById(R.id.wifi_advanced_setting);
        mGPSSettingButton = (Button) v.findViewById(R.id.gps_location_setting);
        mGPSCurrentSettingTextView = (TextView) v.findViewById(R.id.gps_current_setting);
        mLocationTypeSpinner = (Spinner) v.findViewById(R.id.location_type_spinner);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(), R.array.location_type_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mLocationTypeSpinner.setAdapter(adapter);
        int position = mLocation.getConfig().ordinal();
        mLocationTypeSpinner.setSelection(position);
        mLocationTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mLocation.setConfig(Location.ConfigType.values()[position]);
                switch (mLocation.getConfig()) {
                    case GPS:
                        mWifiSettingLayout.setVisibility(View.GONE);
                        mWifiAdvancedSettingButton.setVisibility(View.GONE);
                        mCurrentWifiInfoTextView.setVisibility(View.GONE);
                        mGPSSettingButton.setVisibility(View.VISIBLE);
                        updateGPSTextView();
                        break;
                    case WiFi:
                        mWifiSettingLayout.setVisibility(View.VISIBLE);
                        mWifiAdvancedSettingButton.setVisibility(View.VISIBLE);
                        updateWifiTextView();
                        mGPSSettingButton.setVisibility(View.GONE);
                        mGPSCurrentSettingTextView.setVisibility(View.GONE);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mGPSSettingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = MapsActivity.newIntent(getActivity(), mLocation.getId());
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
            }
        });

        updateGPSTextView();

        mUseCurrentWifiCheckBox = (CheckBox) v.findViewById(R.id.use_current_wifi_check_bot);
        mUseCurrentWifiCheckBox.setChecked(mLocation.getWiFiPosition() != null);
        mUseCurrentWifiCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    WiFiPosition wiFiPosition = new WiFiPosition();
                    String[] info = WiFiPosition.getCurrentWifiInfo(getContext());
                    if (info==null){
                        Toast.makeText(getActivity(), R.string.no_wifi_connection_warning, Toast
                                .LENGTH_SHORT)
                                .show();
                        mUseCurrentWifiCheckBox.setChecked(false);
                    }else{
                        String ssid = info[0];
                    String bssid = info[1];
                        wiFiPosition.setSSID(ssid);
                    wiFiPosition.setBSSID(bssid);
                        mLocation.setWiFiPosition(wiFiPosition);
                        updateWifiTextView();
                    }
                } else {
                    mLocation.setWiFiPosition(null);
                    updateWifiTextView();
                }
            }
        });

        updateWifiTextView();

        mDoneButton = (Button) v.findViewById(R.id.done_button);
        mDoneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mGPSCoordinate = ToDoLab.get(getActivity()).getGPSCoordinate();
                if (mLocation.getTitle() == null || mLocation.getTitle().isEmpty()) {
                    Dialog alertDialog = new AlertDialog.Builder(getActivity())
                            .setMessage("Have to enter a title for this location.")
                            .setPositiveButton("OK", null)
                            .create();
                    alertDialog.show();
                } else if (mLocation.getConfig() == Location.ConfigType.GPS & (mGPSCoordinate
                        .getLongitude() == 0.0 && mGPSCoordinate.getLatitude() == 0.0)) {
                    Dialog alertDialog = new AlertDialog.Builder(getActivity())
                            .setMessage("Have to select a GPS location coordinate.")
                            .setPositiveButton("OK", null)
                            .create();
                    alertDialog.show();
                } else {
                    mLocation.setGPSCoordinate(mGPSCoordinate);
                    ToDoLab.get(getActivity()).saveLocation(mLocation);
                    getActivity().onBackPressed();
                }
            }
        });
        return v;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_location_detail, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_delete_location:
                Dialog alertDialog = new AlertDialog.Builder(getActivity())
                        .setMessage("Do you want to delete this location?")
                        .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
//                                ToDoLab.get(getActivity()).removeLocation(mLocation);
                                // TODO: Create deleteLocation to deal with foreign key in TaskLocation Table
                                ToDoLab.get(getActivity()).deleteLocation(mLocation);
                                getActivity().finish();
                            }
                        })
                        .setNegativeButton("Cancel", null)
                        .create();
                alertDialog.show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    private void updateGPSTextView() {
        if (mGPSCoordinate.getLatitude()==0&mGPSCoordinate.getLongitude()==0) {
            mGPSCurrentSettingTextView.setVisibility(View.GONE);
        } else {
            mGPSCurrentSettingTextView.setVisibility(View.VISIBLE);
            mGPSCurrentSettingTextView.setText(mGPSCoordinate.getLatitude() + "  " + mGPSCoordinate.getLongitude());
        }
    }


    private void updateWifiTextView() {
        if (mLocation.getWiFiPosition() == null) {
            mCurrentWifiInfoTextView.setVisibility(View.GONE);
        } else {
            mCurrentWifiInfoTextView.setVisibility(View.VISIBLE);
            mCurrentWifiInfoTextView.setText(getString(R.string.current_wifi_info_title)
                    + " " + mLocation.getWiFiPosition()
                    .getSSID()+"\n" + "Current Wi-Fi BSSID:"+" "+mLocation.getWiFiPosition()
                    .getBSSID());
        }//TODO SHOW BSSID ADDRESS
    }


}
