package com.serviceslab.unipv.librarynavapp.classes.activities;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;


import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.indooratlas.android.sdk.IALocation;
import com.indooratlas.android.sdk.IALocationListener;

import com.indooratlas.android.sdk.IALocationManager;
import com.indooratlas.android.sdk.IARegion;
import com.serviceslab.unipv.librarynavapp.R;
import com.serviceslab.unipv.librarynavapp.classes.SdkExample;

//Google style
@SdkExample(description = R.string.example_googlemaps_basic_description)
public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback, IALocationListener {

    //TODO make this work with indooratlas localization
    private String TAG = "MapsActivity";
    private static final float HUE_IABLUE = 200.0f;
    private IALocationManager mIALocationManager;

    private SupportMapFragment mapFragment;
    private GoogleMap myGoogleMap = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        mIALocationManager = IALocationManager.create(this);

        // optional setup of floor plan id
        // if setLocation is not called, then location manager tries to find  location automatically
        final String floorPlanId = getString(R.string.indooratlas_floor_plan_id);
        Log.i(TAG, floorPlanId);
        if (!TextUtils.isEmpty(floorPlanId)) {
            final IALocation FLOOR_PLAN_ID = IALocation.from(IARegion.floorPlan(floorPlanId));
            mIALocationManager.setLocation(FLOOR_PLAN_ID);
        }

        //SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                //.findFragmentById(R.id.map);
        //mapFragment.getMapAsync(this);

        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        //Miki
        myGoogleMap = googleMap;
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            myGoogleMap.setMyLocationEnabled(true);
        } else {
            //Ask permission
        }
        LatLng sydney = new LatLng(-33.852, 151.211);
        myGoogleMap.addMarker(new MarkerOptions().position(sydney)
        .title("Marker in Sydney"));
        myGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

        Polyline line = myGoogleMap.addPolyline(new PolylineOptions()
                .add(new LatLng(51.5, -0.1), new LatLng(40.7, -74.0))
                .width(5)
                .color(Color.RED));


        //My position button
        //if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                //== PackageManager.PERMISSION_GRANTED) {
            //googleMap.setMyLocationEnabled(true);
        //} else {
            // Show rationale and request permission.
        //}
        // Add a marker in Sydney, Australia,
        // and move the map's camera to the same location.
        //LatLng sydney = new LatLng(-33.852, 151.211);
        //googleMap.addMarker(new MarkerOptions().position(sydney)
                //.title("Marker in Sydney"));
        //googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));


    }

    @Override
    protected void onDestroy() {
        mIALocationManager.destroy();
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //mIALocationManager.registerRegionListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        //mIALocationManager.unregisterRegionListener(this);
    }

    /**
     * Callback for receiving locations.
     * This is where location updates can be handled by moving markers or the camera.
     */
    public void onLocationChanged(IALocation location) {
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        Double lat = latLng.latitude;
        Double lon = latLng.longitude;
        String latitude = lat.toString();
        String longitude = lon.toString();
        lat = Double.parseDouble(latitude);
        lon = Double.parseDouble(longitude);

        Log.i(TAG, "latitude and logitude: " + latitude + longitude);
        //if (mMarker == null) {
            //if (mMap != null) {
                //mMarker = mMap.addMarker(new MarkerOptions().position(latLng)
                        //.icon(BitmapDescriptorFactory.defaultMarker(HUE_IABLUE)));
                //mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17.0f));
            //}
        //} else {
            //mMarker.setPosition(latLng);
        //}
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }
}
