package edu.osu.cse.todolist.to_dolist;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.location.Criteria;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.text.style.CharacterStyle;
import android.text.style.StyleSpan;
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
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.common.data.DataBufferUtils;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.AutocompletePrediction;
import com.google.android.gms.location.places.AutocompletePredictionBuffer;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

import java.security.Provider;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by Zicong on 2015/11/6.
 */
public class LocationDetailFragment extends Fragment implements GoogleApiClient
        .OnConnectionFailedListener {
    private Location mLocation;
    private EditText mTitleField;
    private EditText mNoteField;
    private Button mDoneButton;
    private LinearLayout mWifiSettingLayout;
    private Button mWifiAdvancedSettingButton;
    private Button mGPSSettingButton;
    private AutoCompleteTextView mLocationAddressEditText;
    private TextView mCurrentWifiInfoTextView;
    private CheckBox mUseCurrentWifiCheckBox;
    private Spinner mLocationTypeSpinner;
    private GPSCoordinate mGPSCoordinate;
    private static final String ARG_LOCATION_ID = "location_id";
    private static final int PLACE_PICKER_REQUEST = 1;
    protected GoogleApiClient mGoogleApiClient;
    private PlaceAutocompleteAdapter mAdapter;
    private LocationManager mLocationManager;
    private String mProvider;
    private LatLngBounds mLatLngBounds;
    private static final String TAG = "LocationDetailFragment";


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

        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .enableAutoManage(getActivity(), 0 /* clientId */, this)
                .addApi(Places.GEO_DATA_API)
                .build();
        mGoogleApiClient.connect();
    }

    @Override
    public void onResume() {
        super.onResume();
        updateGPSTextView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mGoogleApiClient.disconnect();
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
        mLocationAddressEditText = (AutoCompleteTextView) v.findViewById(R.id.location_address_edit_text);
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
                        mLocationAddressEditText.setVisibility(View.VISIBLE);
                        break;
                    case WiFi:
                        mWifiSettingLayout.setVisibility(View.VISIBLE);
                        mWifiAdvancedSettingButton.setVisibility(View.VISIBLE);
                        mCurrentWifiInfoTextView.setVisibility(View.VISIBLE);
                        mGPSSettingButton.setVisibility(View.GONE);
                        mLocationAddressEditText.setVisibility(View.GONE);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mLocationManager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
        try {
            List<String> providerList = mLocationManager.getProviders(true);
            if (providerList.contains(LocationManager.GPS_PROVIDER)) {
                mProvider = LocationManager.GPS_PROVIDER;
            } else if (providerList.contains(LocationManager.NETWORK_PROVIDER)) {
                mProvider = LocationManager.NETWORK_PROVIDER;
            } else if (providerList.contains(LocationManager.PASSIVE_PROVIDER)) {
                mProvider = LocationManager.PASSIVE_PROVIDER;
            }

            android.location.Location location = mLocationManager.getLastKnownLocation(mProvider);
            if (location == null) {
                location = mLocationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
            }
            if (location != null) {
                Double lng = location.getLongitude();
                Double lat = location.getLatitude();
                mLatLngBounds = new LatLngBounds(new LatLng(lat - 0.1, lng - 0.1), new LatLng(lat + 0.1,
                        lng + 0.1));
            }
        } catch (SecurityException ex) {
            Toast.makeText(getActivity(), "Unable to access GPS service, permission denied.",
                    Toast.LENGTH_LONG).show();
            mLatLngBounds = new LatLngBounds(new LatLng(-0.1, -0.1), new LatLng(0.1, 0.1));
        } catch (IllegalArgumentException ex) {
            Toast.makeText(getActivity(), "GPS service is disabled.", Toast.LENGTH_SHORT).show();
            mLatLngBounds = new LatLngBounds(new LatLng(-0.1, -0.1), new LatLng(0.1, 0.1));
        }
        mAdapter = new PlaceAutocompleteAdapter(getActivity(), mGoogleApiClient, mLatLngBounds, null);
        mLocationAddressEditText.setAdapter(mAdapter);
        mLocationAddressEditText.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final AutocompletePrediction item = mAdapter.getItem(position);
                final String placeId = item.getPlaceId();
                final CharSequence primaryText = item.getPrimaryText(null);

                PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi
                        .getPlaceById(mGoogleApiClient, placeId);
                placeResult.setResultCallback(new ResultCallback<PlaceBuffer>() {
                    @Override
                    public void onResult(PlaceBuffer places) {
                        if (!places.getStatus().isSuccess()) {
                            places.release();
                            return;
                        }

                        final Place place = places.get(0);
                        mGPSCoordinate.setAddress(place.getAddress().toString());
                        mGPSCoordinate.setPlaceId(place.getId());
                        mGPSCoordinate.setLongitude(place.getLatLng().longitude);
                        mGPSCoordinate.setLatitude(place.getLatLng().latitude);
                        places.release();
                    }
                });

            }
        });


        mGPSSettingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
                    if (mGPSCoordinate.getLatitude() != 0 & mGPSCoordinate.getLongitude() != 0) {
                        Double lat = mGPSCoordinate.getLatitude();
                        Double lng = mGPSCoordinate.getLongitude();
                        builder.setLatLngBounds(new LatLngBounds(new LatLng(lat - 0.002, lng - 0.002), new
                                LatLng(lat + 0.002, lng + 0.002)));
                    }
                    startActivityForResult(builder.build(getActivity()), PLACE_PICKER_REQUEST);

                } catch (GooglePlayServicesRepairableException e) {
                    GooglePlayServicesUtil
                            .getErrorDialog(e.getConnectionStatusCode(), getActivity(), 0);
                    Log.d("Error", "error");
                } catch (GooglePlayServicesNotAvailableException e) {
                    Toast.makeText(getActivity(), "Google Play Services is not available.", Toast.LENGTH_LONG)
                            .show();
                    Log.d("Error", "error");
                }
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
                    if (info == null) {
                        Toast.makeText(getActivity(), R.string.no_wifi_connection_warning, Toast
                                .LENGTH_SHORT)
                                .show();
                        mUseCurrentWifiCheckBox.setChecked(false);
                    } else {
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
                if (mLocation.getTitle() == null || mLocation.getTitle().isEmpty()) {
                    Dialog alertDialog = new AlertDialog.Builder(getActivity())
                            .setMessage("Have to enter a title for this location.")
                            .setPositiveButton("OK", null)
                            .create();
                    alertDialog.show();
                } else if (mLocation.getConfig() == Location.ConfigType.GPS & (mGPSCoordinate
                        .getLongitude() == 0.0 && mGPSCoordinate.getLatitude() == 0.0)) {
                    Dialog alertDialog = new AlertDialog.Builder(getActivity())
                            .setMessage("Make sure GPS service is enabled and have selected a " +
                                    "GPS " +
                                    "location coordinate.")
                            .setPositiveButton("OK", null)
                            .create();
                    alertDialog.show();
                } else if (mLocation.getConfig() == Location.ConfigType.WiFi & mLocation
                        .getWiFiPosition() == null) {
                    Dialog alertDialog = new AlertDialog.Builder(getActivity())
                            .setMessage("Have to select a Wifi location.")
                            .setPositiveButton("OK", null)
                            .create();
                    alertDialog.show();
                } else {
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
        if (mGPSCoordinate.getLatitude() == 0 && mGPSCoordinate.getLongitude() == 0) {
            mLocationAddressEditText.setText("");
        } else if (mGPSCoordinate.getAddress() != null) {
            mLocationAddressEditText.setText(mGPSCoordinate.getAddress());
        } else {
            mLocationAddressEditText.setText(mGPSCoordinate.getLatitude() + " " + mGPSCoordinate
                    .getLongitude());
        }
    }


    private void updateWifiTextView() {
        if (mLocation.getWiFiPosition() == null) {
            mCurrentWifiInfoTextView.setVisibility(View.GONE);
        } else {
            mCurrentWifiInfoTextView.setVisibility(View.VISIBLE);
            mCurrentWifiInfoTextView.setText(getString(R.string.current_wifi_info_title)
                    + " " + mLocation.getWiFiPosition()
                    .getSSID() + "\n" + getString(R.string.current_wifi_bssid) + " " + mLocation
                    .getWiFiPosition()
                    .getBSSID());
        }
    }

    @Override
    public void onActivityResult(int requestCode,
                                 int resultCode, Intent data) {

        if (requestCode == PLACE_PICKER_REQUEST
                && resultCode == Activity.RESULT_OK) {

            // The user has selected a place. Extract the name and address.
            final Place place = PlacePicker.getPlace(data, getActivity());
            final CharSequence address = place.getAddress();
            final String placeId = place.getId();
            final LatLng latLng = place.getLatLng();
            mGPSCoordinate.setPlaceId(placeId);
            if (address.length() != 0) {
                mGPSCoordinate.setAddress(address.toString());
            } else {
                mGPSCoordinate.setAddress(null);
            }

            mGPSCoordinate.setLatitude(latLng.latitude);
            mGPSCoordinate.setLongitude(latLng.longitude);
            updateGPSTextView();
            String attributions = PlacePicker.getAttributions(data);
            if (attributions == null) {
                attributions = "";
            }

        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

        Log.e("Location", "onConnectionFailed: ConnectionResult.getErrorCode() = "
                + connectionResult.getErrorCode());

        // TODO(Developer): Check error code and notify the user of error state and resolution.
        Toast.makeText(getActivity(),
                "Could not connect to Google API Client: Error " + connectionResult.getErrorCode(),
                Toast.LENGTH_SHORT).show();
    }

}
