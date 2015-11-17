package edu.osu.cse.todolist.to_dolist;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.*;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends AppCompatActivity
        implements
        OnMapReadyCallback,
        ActivityCompat.OnRequestPermissionsResultCallback {

    /**
     * Request code for location permission request.
     *
     * @see #onRequestPermissionsResult(int, String[], int[])
     */
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private static final String EXTRA_LOCATION = "edu.osu.cse.todolist.to_dolist.location";
    private LocationManager mlocationManager;
    private String mprovider;
    private LatLng mLatLng;
    private Location mLocation;
    private GPSCoordinate mGPSCoordinate;

    /**
     * Flag indicating whether a requested permission has been denied after returning in {@link
     * #onRequestPermissionsResult(int, String[], int[])}.
     */
    private boolean mPermissionDenied = false;

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        long locationId = (long) getIntent().getSerializableExtra(EXTRA_LOCATION);
        mLocation = ToDoLab.get(this).getLocation(locationId);

        mGPSCoordinate = mLocation.getGPSCoordinate();

        SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap map) {
        mMap = map;
        if (mGPSCoordinate.getLatitude()==0&mGPSCoordinate.getLongitude() == 0) {
            enableMyLocation();
        } else {
            mMap.setMyLocationEnabled(true);
            double lat = mGPSCoordinate.getLatitude();
            double lng = mGPSCoordinate.getLongitude();
            mLatLng = new LatLng(lat, lng);
            addMarker(mLatLng);
        }

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

            @Override
            public void onMapClick(LatLng latLng) {

                mLatLng = latLng;
                addMarker(latLng);
            }
        });
    }


    /**
     * Enables the My Location layer if the fine location permission has been granted.
     */
    private void enableMyLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission to access the location is missing.
            PermissionUtils.requestPermission(this, LOCATION_PERMISSION_REQUEST_CODE,
                    Manifest.permission.ACCESS_FINE_LOCATION, true);
        } else if (mMap != null) {
            // Access to the location has been granted to the app.
            mMap.setMyLocationEnabled(true);
            mlocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            Criteria criteria = new Criteria();
            mprovider = mlocationManager.getBestProvider(criteria, true);
            android.location.Location location = mlocationManager.getLastKnownLocation(mprovider);
            if (location != null) {
                // Toast.makeText(BasicMapActivity_new.this, "Selected Provider " + provider,
                //Toast.LENGTH_SHORT).show();
                mLatLng = new LatLng(location.getLatitude(), location.getLongitude());
                addMarker(mLatLng);
            } else {
                return;
            }
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode != LOCATION_PERMISSION_REQUEST_CODE) {
            return;
        }

        if (PermissionUtils.isPermissionGranted(permissions, grantResults,
                Manifest.permission.ACCESS_FINE_LOCATION)) {
            // Enable the my location layer if the permission has been granted.
            enableMyLocation();
        } else {
            // Display the missing permission error dialog when the fragments resume.
            mPermissionDenied = true;
        }
    }

    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();
        if (mPermissionDenied) {
            // Permission was not granted, display error dialog.
            showMissingPermissionError();
            mPermissionDenied = false;
        }
    }

    /**
     * Displays a dialog with error message explaining that the location permission is missing.
     */
    private void showMissingPermissionError() {
        PermissionUtils.PermissionDeniedDialog
                .newInstance(true).show(getSupportFragmentManager(), "dialog");
    }


    public static Intent newIntent(Context packageContext, long locationId) {
        Intent intent = new Intent(packageContext, MapsActivity.class);
        intent.putExtra(EXTRA_LOCATION, locationId);
        return intent;
    }

    @Override
    public void onBackPressed() {
        Dialog alertDialog = new AlertDialog.Builder(this)
                .setMessage("Do you want to save the maker's location as your reminder location?")
                .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
//                        GPSCoordinate gpsCoordinate = new GPSCoordinate();
//                        mLocation.setGPSCoordinate(gpsCoordinate);
//                        gpsCoordinate.setLatitude(mLatLng.latitude);
//                        gpsCoordinate.setLongitude(mLatLng.longitude);
                        mGPSCoordinate.setLongitude(mLatLng.longitude);
                        mGPSCoordinate.setLatitude(mLatLng.latitude);
                        ToDoLab.get(getApplicationContext()).setGPSCoordinate(mGPSCoordinate);
                        finish();
                    }
                })
                .setNegativeButton("Cancel", null)
                .create();
        alertDialog.show();
    }


    public void addMarker(LatLng latLng) {
        // Creating a marker
        MarkerOptions markerOptions = new MarkerOptions();

        // Setting the position for the marker
        markerOptions.position(latLng);

        // Setting the title for the marker.
        // This will be displayed on taping the marker
        markerOptions.title(latLng.latitude + " : " + latLng.longitude);

        // Clears the previously touched position
        mMap.clear();

        // Animating to the touched position
        CameraPosition position = new CameraPosition.Builder()
                .target(latLng)
                .zoom(14).build();
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(position));

        // Placing a marker on the touched position
        mMap.addMarker(markerOptions);
    }
}

//TODO Change coordinates to street name
//TODO ADD USE CURRENT LOCATION AS DEFAULT
//TODO CHECK IF USERS' DOESN'T SELECT ANY LOCATION