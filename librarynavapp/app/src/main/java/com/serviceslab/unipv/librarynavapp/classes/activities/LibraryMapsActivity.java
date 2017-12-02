package com.serviceslab.unipv.librarynavapp.classes.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.hardware.GeomagneticField;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.hardware.TriggerEvent;
import android.hardware.TriggerEventListener;
import android.net.Uri;
import android.os.Build;
import android.os.Looper;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.GroundOverlay;
import com.google.android.gms.maps.model.GroundOverlayOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.indooratlas.android.sdk.IALocation;
import com.indooratlas.android.sdk.IALocationListener;
import com.indooratlas.android.sdk.IALocationManager;
import com.indooratlas.android.sdk.IALocationRequest;
import com.indooratlas.android.sdk.IARegion;
import com.indooratlas.android.sdk.resources.IAFloorPlan;
import com.indooratlas.android.sdk.resources.IALatLng;
import com.indooratlas.android.sdk.resources.IALocationListenerSupport;
import com.indooratlas.android.sdk.resources.IAResourceManager;
import com.indooratlas.android.sdk.resources.IAResult;
import com.indooratlas.android.sdk.resources.IAResultCallback;
import com.indooratlas.android.sdk.resources.IATask;
import com.serviceslab.unipv.librarynavapp.R;
import com.serviceslab.unipv.librarynavapp.classes.algorithm.Distance;
import com.serviceslab.unipv.librarynavapp.classes.model.Path;
import com.serviceslab.unipv.librarynavapp.classes.model.Waypoint;
import com.serviceslab.unipv.librarynavapp.classes.restfulClasses.APIListService;
import com.serviceslab.unipv.librarynavapp.classes.restfulClasses.APIService;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;
import com.squareup.picasso.Target;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.TimeUnit;

import es.usc.citius.hipster.algorithm.Algorithm;
import es.usc.citius.hipster.algorithm.Hipster;
import es.usc.citius.hipster.graph.GraphBuilder;
import es.usc.citius.hipster.graph.GraphSearchProblem;
import es.usc.citius.hipster.graph.HipsterGraph;
import es.usc.citius.hipster.model.problem.SearchProblem;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LibraryMapsActivity extends AppCompatActivity implements OnMapReadyCallback, SensorEventListener {

    private static final String TAG = "LibraryMapsActivity";
    private static final float HUE_IABLUE = 200.0f;

    //MAP STUFF
    //The max dimension is used to decide when bitmap should be downscaled
    private static final int MAX_DIMENSION = 2048;
    private GoogleMap mMap;
    //User marker, marks user current position
    private Marker mMarker;
    //Marker of destination armadio
    private Marker destinationMarker;
    //IA and Picasso stuff
    private IALatLng userPosition;
    private IARegion mOverlayFloorPlan = null;
    private GroundOverlay mGroundOverlay = null;
    private IALocationManager mIALocationManager;
    private IAResourceManager mResourceManager;
    private IATask<IAFloorPlan> mFetchFloorPlanTask;
    private Target mLoadTarget;
    private boolean mCameraPositionNeedsUpdating = true; //Update on first location
    private SupportMapFragment mSupportMapFragment;
    private List<Polyline> polylineList;
    private List<Circle> circles = new ArrayList<Circle>();

    //Needed for navigation
    private List<Waypoint> waypoints;
    private List<Path> paths;
    //TODO change base url
    //Xiaomi robolab
    private final String baseUrl = "http://192.168.31.181:2020/";
    //Iman's hotspot
    //private final String baseUrl = "http://192.168.43.170:2020/";
    //Miki's home
    //private final String baseUrl = "http://192.168.1.104:2020/";
    //Library wifi
    //private final String baseUrl = "http://192.168.43.170:2020/";
    //private final String baseUrl = "http:/192.168.43.198:2020/";
    private com.serviceslab.unipv.librarynavapp.classes.model.Log navLog;

    //Waypoints related attributes
    //Paulin
    //private String targetId = "3f69a259-0db9-4b73-8e57-8f5bbd4c6d7f";
    private String targetId = "";
    private Waypoint targetWaypoint;
    //Passed with intent from previous activity.
    private String book_inventory;
    private String book_title;
    private String libraryCode;
    private String codiceTopograficoCompleto;
    private String armadioId;
    //Sensors
    private SensorManager mSensorManager;
    private Sensor mSensor;
    private Sensor rotation;
    private TriggerEventListener mTriggerEventListener = new TriggerListener();
    private float myDeclination;

    //Graphic elements
    private ProgressBar spinner;
    private Button buttonStopNav;
    private Button buttonNavigation;

    //Flags
    private boolean isNavigationOn = false;
    private boolean stillLoading = true;
    private boolean tooFar = false;
    private int currentFloorLevel = 0;
    private int targetFloorLevel = 0;
    private boolean stairs = false;

    //Waypoints
    private Waypoint stairsWpLevelZero;
    private Waypoint stairsWpLevelSoppalco;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        codiceTopograficoCompleto = intent.getExtras()
                .getString("codice_topografico_completo");
        libraryCode = intent.getExtras().getString("library_code");
        book_inventory = intent.getExtras().getString("book_inventory");
        book_title = intent.getExtras().getString("book_title");
        //Trying with armadioId, to retrieve only that.
        armadioId = intent.getExtras().getString("armadio_id");
        String requestTimestamp = intent.getExtras().getString("requestTimestamp");
        //Initializing navigation log with the request timestamp
        //and inventory code of the book.
        //The type of operation is set by default as query.
        navLog = new com.serviceslab.unipv.librarynavapp.classes.model.Log();
        navLog.setRequestTimestamp(requestTimestamp);
        navLog.setBookId(book_inventory);
        navLog.setOperationCode("Q");
        navLog.setArmadioId(codiceTopograficoCompleto);
        navLog.setLibraryId(libraryCode);
        navLog.setConfirmationTimestamp(getCurrentTimestamp());
        navLog.setConfirmationTypeId("N");
        setContentView(R.layout.activity_maps);

        //Showing progress bar when retrieving from NAVAPP server
        spinner = (ProgressBar) findViewById(R.id.progressBarMap);
        spinner.setIndeterminate(true);
        spinner.setVisibility(View.VISIBLE);
        //The button for stopping the navigation is not clickable if the navigation has not
        //started yet.
        buttonStopNav = (Button) findViewById(R.id.buttonStopNav);
        buttonStopNav.setClickable(false);
        buttonNavigation = (Button) findViewById(R.id.buttonDijkstra);
        buttonNavigation.setClickable(false);
        //Prevent the screen switching off while app is in foreground
        findViewById(android.R.id.content).setKeepScreenOn(true);
        //Instantiate IALocationManager and IAResource Manager
        mIALocationManager = IALocationManager.create(this);
        mResourceManager = IAResourceManager.create(this);
        //Instantiate the polyline list to draw the shortest path
        polylineList = new ArrayList<Polyline>();
        waypoints = new ArrayList<Waypoint>();
        //Getting the waypoints
        getWaypoints(libraryCode);
        //Defining sensors that will be used during navigation
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);
        rotation = mSensorManager.getDefaultSensor(Sensor.TYPE_GEOMAGNETIC_ROTATION_VECTOR);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //Remember to clean up after ourselves
        mIALocationManager.destroy();
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Override
    protected void onResume() {
        super.onResume();
        if (mMap == null) {
            //Try to obtain the map from the SupportMapFragment
            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);
        }
        //Start receiving location updates and monitor region changes
        mIALocationManager.requestLocationUpdates(IALocationRequest.create(), mListener);
        mIALocationManager.registerRegionListener(mRegionListener);
        mSensorManager.requestTriggerSensor(mTriggerEventListener, mSensor);
        mSensorManager.registerListener(this, rotation, 300000);
    }

    //Instantiating the basemap
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setRotateGesturesEnabled(false);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Override
    public void onPause() {
        super.onPause();
        //Unregister location and region changes
        mIALocationManager.removeLocationUpdates(mListener);
        mIALocationManager.unregisterRegionListener(mRegionListener);
        mSensorManager.cancelTriggerSensor(mTriggerEventListener, mSensor);
        mSensorManager.unregisterListener(this);
    }


    //Sets bitmap of floor plan as ground overlay on Google Maps
    private void setupGroundOverlay(IAFloorPlan floorPlan, Bitmap bitmap) {
        if (mGroundOverlay != null) {
            mGroundOverlay.remove();
        }
        if (mMap != null) {
            BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.fromBitmap(bitmap);
            IALatLng iaLatLng = floorPlan.getCenter();
            LatLng center = new LatLng(iaLatLng.latitude, iaLatLng.longitude);
            GroundOverlayOptions fpOverlay = new GroundOverlayOptions()
                    .image(bitmapDescriptor)
                    .position(center, floorPlan.getWidthMeters(), floorPlan.getHeightMeters())
                    .bearing(floorPlan.getBearing());
            mGroundOverlay = mMap.addGroundOverlay(fpOverlay);
        }
    }

    //Download floor plan using Picasso library.
    private void fetchFloorPlanBitmap(final IAFloorPlan floorPlan) {

        final String url = floorPlan.getUrl();

        if (mLoadTarget == null) {
            mLoadTarget = new Target() {
                @Override
                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                    /*Log.d(TAG, "onBitmap loaded with dimensions: " + bitmap.getWidth()
                            + "x" + bitmap.getHeight());*/
                    setupGroundOverlay(floorPlan, bitmap);
                }

                @Override
                public void onPrepareLoad(Drawable placeholderDrawable) {
                    //Nothing to implement
                }

                @Override
                public void onBitmapFailed(Drawable placeHolderDrawable) {
                    Log.i("onBitmapFailed","failed to load bitmap");
                    mOverlayFloorPlan = null;
                }
            };
        }
        RequestCreator request = Picasso.with(this).load(url);
        final int bitmapWidth = floorPlan.getBitmapWidth();
        final int bitmapHeight = floorPlan.getBitmapHeight();

        if (bitmapHeight > MAX_DIMENSION) {
            request.resize(0, MAX_DIMENSION);
        } else if (bitmapWidth > MAX_DIMENSION) {
            request.resize(0, MAX_DIMENSION);
        }
        request.into(mLoadTarget);

    }

    //Fetches floor plan data from IndoorAtlas server.
    private void fetchFloorPlan(String id) {
        //If there is already running task, cancel it
        cancelPendingNetworkCalls();
        final IATask<IAFloorPlan> task = mResourceManager.fetchFloorPlanWithId(id);
        task.setCallback(new IAResultCallback<IAFloorPlan>() {
            @Override
            public void onResult(IAResult<IAFloorPlan> result) {
                if (result.isSuccess() && result.getResult() != null) {
                    //Retrieve bitmap for this floor plan metadata
                    fetchFloorPlanBitmap(result.getResult());
                    //spinner.setVisibility();
                } else {
                    Log.i("fetchFloorPlan","failed load floorplan");
                    // ignore errors if this task was already canceled
                    if (!task.isCancelled()) {
                        //Do something with error
                        Toast.makeText(LibraryMapsActivity.this,
                                "Loading floor plan failed; " + result.getError(),
                                Toast.LENGTH_SHORT).show();

                        mOverlayFloorPlan = null;
                    }
                }
            }
        }, Looper.getMainLooper()); // Deliver callbacks using mainLooper

        //Keep reference to task so that it can be canceled if needed
        mFetchFloorPlanTask = task;
    }

    //Helper method to cancel current task if any.
    private void cancelPendingNetworkCalls() {
        if (mFetchFloorPlanTask != null && !mFetchFloorPlanTask.isCancelled()) {
            mFetchFloorPlanTask.cancel();
            Log.i("cancelPendingNetCalls","cancelled map loading");
        }
    }

    //Listener that handles location change events.
    private IALocationListener mListener = new IALocationListenerSupport() {
        /* Location changed, move marker and camera position.*/
        @Override
        public void onLocationChanged(IALocation location) {
            //Section used to get the direction
            GeomagneticField field = new GeomagneticField(
                    (float)location.getLatitude(),
                    (float)location.getLongitude(),
                    (float)location.getAltitude(),
                    System.currentTimeMillis()
            );
            myDeclination = field.getDeclination();
            //Log.i("onLocationChanged", "Geomagnetic field declination is: " + Float.toString(myDeclination));
            //Get user position.
            userPosition = new IALatLng(location.getLatitude(), location.getLongitude());
            if (mMap == null) {
                //Location received before map is initialized, ignoring update here
                return;
            }
            //Getting user Latitude and Longitude to instantiate a marker
            LatLng userLatLng = new LatLng(location.getLatitude(), location.getLongitude());
            if (mMarker == null) {
                //First location, so we need a marker
                mMarker = mMap.addMarker(new MarkerOptions().position(userLatLng)
                        .title("Your position")
                        .snippet(Double.toString(location.getAltitude())
                                + Double.toString(location.getLongitude()))
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.navigation_marker))
                        .anchor(0.5f, 0.5f));
                buttonNavigation.setClickable(true);
            } else {
                //If there is already a marker, move it from where it is to the new location
                mMarker.setPosition(userLatLng);
            }
            //Update the camera if the location has a very different position from the previous
            if (mCameraPositionNeedsUpdating) {
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(userLatLng, 20.0f));
                mCameraPositionNeedsUpdating = false;
            }
            if (waypoints.size() > 0 && paths.size() > 0) {
                getShortestPath(stairs);
            }

            if (isNavigationOn) {
                moveCamera(userPosition);
            }
        }
    };

    //Method for rotating the map when change is detected
    @Override
    public void onSensorChanged(SensorEvent event) {
        if(event.sensor.getType() == Sensor.TYPE_GEOMAGNETIC_ROTATION_VECTOR) {
            float[] myRotationMatrix = new float[16];
            SensorManager.getRotationMatrixFromVector(myRotationMatrix, event.values);
            float[] orientation = new float[3];
            SensorManager.getOrientation(myRotationMatrix, orientation);
            float bearing = (float) (Math.toDegrees(orientation[0]) + myDeclination);
            //Log.i("onSensorChanged", "bearing is: " + Float.toString(bearing));
            if (userPosition != null && mGroundOverlay!= null) {
                rotateUserMarker(bearing);
            }

        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        //Nothing to do
    }

    //Listener that changes overlay if needed.
    private IARegion.Listener mRegionListener = new IARegion.Listener() {
        @Override
        public void onEnterRegion(IARegion region) {
            if (region.getType() == IARegion.TYPE_FLOOR_PLAN) {
                final String newId = region.getId();
                // Are we entering a new floor plan or coming back the floor plan we just left?
                if (mGroundOverlay == null || !region.equals(mOverlayFloorPlan)) {
                    //If we are going into a new floor plan we need to update the camera view
                    mCameraPositionNeedsUpdating = true;
                    if (mGroundOverlay != null) {
                        mGroundOverlay.remove();
                        mGroundOverlay = null;
                    }
                    mOverlayFloorPlan = region; // overlay will be this (unless error in loading)
                    fetchFloorPlan(newId);
                } else {
                    mGroundOverlay.setTransparency(0.0f);
                }
                //Toast.makeText(LibraryMapsActivity.this, newId, Toast.LENGTH_SHORT).show();
                Toast t = Toast.makeText(LibraryMapsActivity.this,
                        "Please, when the map is loaded, walk a few seconds to adjust positioning.",
                        Toast.LENGTH_LONG);
                t.setGravity(Gravity.CENTER, 0, 0);
                t.show();

                //showInfo("Enter " + (region.getType() == IARegion.TYPE_VENUE
                        //? "VENUE "
                        //: "FLOOR_PLAN ") + region.getId());
            }
        }

        @Override
        public void onExitRegion(IARegion region) {
            if (mGroundOverlay != null) {
                // Indicate we left this floor plan but leave it there for reference
                // If we enter another floor plan, this one will be removed and another one loaded
                mGroundOverlay.setTransparency(0.5f);
                //Here should be implemented the integration with gps
            }
        }
    };

    //Retrieving data from NAVAPP server: waypoints list
    public void getWaypoints(final String libraryId) {
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS).build();

        Retrofit retrofit = new Retrofit.Builder().client(client).baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        APIListService service = retrofit.create(APIListService.class);
        Call<List<Waypoint>> call = service.getWaypoints();
        call.enqueue(new Callback<List<Waypoint>>() {
            @Override
            public void onResponse(Call<List<Waypoint>> call, Response<List<Waypoint>> response) {
                if (response != null) {
                    try {
                        //When my library Id is the one of the soppalco, I use the waypoints
                        //of the ground floor and the same for when I want a shelf on the ground floor
                        //I load the waypoints of the soppalco as well.
                        if (libraryId.equals("7b7a36ff-5294-48ba-9a45-421952c050e9")) {
                            for (Waypoint w : response.body()) {
                                if (w.getLibraryId().equals(libraryId)
                                        || w.getLibraryId().equals("6050a63b-f44f-4a40-9df7-bbeb5ebcb158")) {
                                    waypoints.add(w);
                                }
                            }
                        } else if(libraryId.equals("6050a63b-f44f-4a40-9df7-bbeb5ebcb158")) {
                            for (Waypoint w : response.body()) {
                                if(w.getLibraryId().equals(libraryId)
                                        || w.getLibraryId().equals("7b7a36ff-5294-48ba-9a45-421952c050e9"))
                                    waypoints.add(w);
                            }
                        } else {
                            for (Waypoint w : response.body()) {
                                if (w.getLibraryId().equals(libraryId)) {
                                    waypoints.add(w);
                                }
                            }
                        }
                        /////END OLD

                        //My waypoints are all the waypoints.
                        //waypoints = response.body();
                        //Getting also the paths
                        Log.i("onResponseGetWaypoints", "hello");
                        for (Waypoint w : waypoints) {
                            if (w.getArmadioId().equals(armadioId)) {
                                LatLng targetCoordinates =
                                        new LatLng(Double.parseDouble(w.getLatitude()),
                                                Double.parseDouble(w.getLongitude()));
                                Marker targetMarker = mMap.addMarker(new MarkerOptions()
                                        .position(targetCoordinates)
                                        .title(w.getName())
                                        .snippet(w.getName())
                                        .icon(BitmapDescriptorFactory.defaultMarker(HUE_IABLUE)));
                                targetWaypoint = w;
                                //Setting the target id for the shortest path
                                targetId = w.getId();
                            } else if(w.getId().equals("f086d4bc-d255-45cf-a197-e57da59a1861")) {
                                stairsWpLevelZero = w;
                            } else if(w.getId().equals("787c0c8f-c150-4247-99e1-16326204abf2")) {
                                stairsWpLevelSoppalco = w;
                            } else if(w.getId().equals("95d35906-bbcc-4640-b1dc-4a34e3eb4418")) {
                                LatLng targetCoordinates =
                                        new LatLng(Double.parseDouble(w.getLatitude()),
                                                Double.parseDouble(w.getLongitude()));

                                Marker entranceDoorMarker = mMap.addMarker(new MarkerOptions()
                                        .position(targetCoordinates)
                                        .title(w.getName())
                                        .snippet(w.getName())
                                        .icon(BitmapDescriptorFactory
                                                .fromResource(R.drawable.porticina)));
                            }

                        }
                        getPaths("1");
                        Log.i("onResponse", "waypoints found");
                    } catch (Exception e) {
                        Log.e("onResponse", "Waypoints not obtained");
                        e.printStackTrace();
                    }
                } else {
                    //This dialog can be managed better
                    Log.i("onResponseGetWaypoints", "body is null");
                }
            }

            @Override
            public void onFailure(Call<List<Waypoint>> call, Throwable t) {
                //Log.e("onFailure waypoints", "Fail to get the json array", t);
            }
        });
    }

    //Retrieving data from NAVAPP server: paths list
    public void getPaths(String libraryId) {
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS).build();
        Retrofit retrofit = new Retrofit.Builder().client(client)
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        APIListService service = retrofit.create(APIListService.class);
        Call<List<Path>> call = service.getPaths();
        call.enqueue(new Callback<List<Path>>() {
            @Override
            public void onResponse(Call<List<Path>> call, Response<List<Path>> response) {
                if (response != null) {
                    try {
                        paths = new ArrayList<Path>();
                        //paths = response.body();
                        for (Waypoint w : waypoints) {
                            for (int i = 0; i < response.body().size(); i++) {
                                if (response.body().get(i).getSourceWpId().equals(w.getId())
                                        || response.body().get(i).getTargetWpId().equals(w.getId())) {
                                    paths.add(response.body().get(i));
                                }
                            }
                        }
                        Distance distance = new Distance();
                        paths = distance.setAllDistances(paths, waypoints);
                        for (int i = 0; i < paths.size(); i++) {
                            paths.get(i).setSourceWp(waypoints);
                            paths.get(i).setTargetWp(waypoints);
                        }
                        Log.i("onResponseGetPaths", "distances have been set");
                        spinner.setVisibility(View.GONE);
                    } catch (Exception e) {
                        Log.e("onResponse", "paths not obtained");
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Path>> call, Throwable t) {
                //This can be managed better
                Log.e("onFailure path", "Fail to get the json array", t);
            }
        });
    }

    public void openArmadioInfo(View view) {
        Intent myIntent = new Intent(LibraryMapsActivity.this, ArmadioInfoActivity.class);
        myIntent.putExtra("codice_topografico_completo", codiceTopograficoCompleto);
        myIntent.putExtra("libraryId", libraryCode);
        myIntent.putExtra("armadio_id", armadioId);
        LibraryMapsActivity.this.startActivity(myIntent);
    }

    //StartNavigation
    public void dijkstraDistance(View view) {
        isNavigationOn = true;
        //checkCurrentFloorLevel(libraryCode);
        //Button b = (Button) findViewById(R.id.buttonDijkstra);
        //b.setClickable(false);
        buttonNavigation.setClickable(false);
        buttonStopNav.setClickable(true);
        IALocationRequest request = IALocationRequest.create();
        double fastestInterval = 100.00;
        float shortestDisplacement = 1;
        request.setFastestInterval((long) fastestInterval);
        request.setSmallestDisplacement(shortestDisplacement);
        Toast.makeText(LibraryMapsActivity.this, "Navigation mode has started.",
                Toast.LENGTH_SHORT).show();
        navLog.setStartNavTimestamp(this.getCurrentTimestamp());

        getShortestPath(stairs);
        //moveCamera(userPosition);
    }

    //Method that finds shortest path
    public void getShortestPath(boolean stairs) {
        //Resetting polyline
        if (polylineList.size() != 0) {
            for (Polyline line : polylineList) {
                line.remove();
            }
        }
        polylineList.clear();
        //Resetting circles
        if (circles.size() != 0) {
            for (Circle c : circles) {
                c.remove();
            }
        }
        circles.clear();
        Distance distance = new Distance();
        int closestWpId = distance.getClosestWPId(waypoints, userPosition);
        Waypoint closestWp = waypoints.get(closestWpId);
        Float userDistanceFromTarget = distance.getDistance(closestWp, userPosition);
        if (userDistanceFromTarget > 50) {
            tooFar = true;
        }

        List<Waypoint> waypointsOfLevel = new ArrayList<Waypoint>();
        waypointsOfLevel = findCurrentLevelWps();
        if(stairs) {
            switch (currentFloorLevel) {
                case 0:
                    computeShortestPath(stairsWpLevelZero.getId(), closestWp, waypointsOfLevel);
                    break;
                case 1:
                    computeShortestPath(stairsWpLevelSoppalco.getId(), closestWp, waypointsOfLevel);
                    break;
            }
        } else if (waypointsOfLevel.size() != 0){
            computeShortestPath(targetId,closestWp,waypointsOfLevel);
        } else {
            computeShortestPath(targetId,closestWp,waypoints);
        }
    }

    public List<Waypoint> findCurrentLevelWps() {
        List<Waypoint> waypointsOfCurrentLevel = new ArrayList<Waypoint>();
        switch(currentFloorLevel) {
            case 0:
                for (Waypoint w : waypoints) {
                    if (w.getLibraryId().equals(R.string.floor_zero_library_id)) {
                        waypointsOfCurrentLevel.add(w);
                    }
                }
                break;
            case 1:
                for (Waypoint w : waypoints) {
                    if (w.getLibraryId().equals(R.string.soppalco_id)) {
                        waypointsOfCurrentLevel.add(w);
                    }
                }
                break;
            default:
                waypointsOfCurrentLevel = waypoints;
        }
        return waypointsOfCurrentLevel;
    }

    //This method is called when the arrived to next floor buttons are clicked.
    public void arrivedToNextFloor(View v) {
        v.setVisibility(View.GONE);
        stairs = false;
        switch(targetFloorLevel) {
            case 0:
                currentFloorLevel = 0;
                break;
            case 1:
                currentFloorLevel = 1;
                break;
        }
        getShortestPath(stairs);
    }

    //Computation of shortest path
    private void computeShortestPath(String targetId, Waypoint closestWp, List<Waypoint> waypoints){
        HipsterGraph<String, Float> graphHip;
        GraphBuilder<String, Float> graphHipBuilder = GraphBuilder.<String, Float>create();
        for (Path edge : paths) {
            graphHipBuilder.connect(edge.getSourceWpId()).to(edge.getTargetWpId()).withEdge(edge.getWeight());
        }
        graphHip = graphHipBuilder.createUndirectedGraph();

        SearchProblem p = GraphSearchProblem
                .startingFrom(closestWp.getId())
                .in(graphHip)
                .takeCostsFromEdges()
                .build();
        Algorithm.SearchResult result = Hipster.createDijkstra(p).search(targetId);
        String shortestPathResult = result.toString();
        String[] tokens = shortestPathResult.split("\n");
        shortestPathResult = tokens[5];
        shortestPathResult = shortestPathResult.replace("\t[", "");
        shortestPathResult = shortestPathResult.replace("]", "");
        String[] wpIdSequence = shortestPathResult.split(", ");

        List<Waypoint> shortestPath = new ArrayList<Waypoint>();
        for (String s : wpIdSequence) {
            for (Waypoint w : waypoints) {
                if (s.equals(w.getId())) {
                    shortestPath.add(w);
                }
            }
        }
        drawShortestPath(shortestPath);
    }

    //Method to draw shortest path
    private void drawShortestPath(List<Waypoint> shortestPath) {
        float z = 2;
        for (Waypoint w : shortestPath) {
            if (w.getLibraryId().equals("7b7a36ff-5294-48ba-9a45-421952c050e9")) {
                circles.add(mMap.addCircle(new CircleOptions()
                        .center(new LatLng(Double.parseDouble(w.getLatitude()), Double.parseDouble(w.getLongitude())))
                        .radius(0.1)
                        .strokeColor(Color.parseColor("#ff009966"))
                        .fillColor(Color.parseColor("#ff009966")).visible(true)));
            } else {
                circles.add(mMap.addCircle(new CircleOptions()
                        .center(new LatLng(Double.parseDouble(w.getLatitude()), Double.parseDouble(w.getLongitude())))
                        .radius(0.1)
                        .strokeColor(Color.BLUE)
                        .fillColor(Color.BLUE).visible(true)));
            }
        }
        for (Circle c : circles) {
            c.setZIndex(z);
        }

        Polyline pl;
        if (tooFar) {
            pl = mMap.addPolyline(new PolylineOptions()
                    .add(new LatLng(userPosition.latitude, userPosition.longitude),
                            new LatLng(Double.parseDouble(shortestPath.get(0).getLatitude()),
                                    Double.parseDouble(shortestPath.get(0).getLongitude())))
                    .width(20).color(Color.parseColor("#00000000")));
        } else {
            pl = mMap.addPolyline(new PolylineOptions()
                    .add(new LatLng(userPosition.latitude, userPosition.longitude),
                            new LatLng(Double.parseDouble(shortestPath.get(0).getLatitude()),
                                    Double.parseDouble(shortestPath.get(0).getLongitude())))
                    .width(20).color(Color.CYAN));
        }

        polylineList.add(pl);
        for (int i = 1; i < shortestPath.size(); i++) {
            if (shortestPath.get(i).getLibraryId().equals(shortestPath.get(i - 1).getLibraryId())) {
                if (shortestPath.get(i).getLibraryId().equals("7b7a36ff-5294-48ba-9a45-421952c050e9")
                        && shortestPath.get(i - 1).getLibraryId().equals("7b7a36ff-5294-48ba-9a45-421952c050e9")) {
                    polylineList.add(mMap.addPolyline(new PolylineOptions()
                            .add(
                                    new LatLng(Double.parseDouble(shortestPath.get(i - 1).getLatitude()),
                                            Double.parseDouble(shortestPath.get(i - 1).getLongitude())),
                                    new LatLng(Double.parseDouble(shortestPath.get(i).getLatitude()),
                                            Double.parseDouble(shortestPath.get(i).getLongitude())))
                            .width(20).color(Color.parseColor("#ff66ff99"))));
                } else {
                    polylineList.add(mMap.addPolyline(new PolylineOptions()
                            .add(
                                    new LatLng(Double.parseDouble(shortestPath.get(i - 1).getLatitude()),
                                            Double.parseDouble(shortestPath.get(i - 1).getLongitude())),
                                    new LatLng(Double.parseDouble(shortestPath.get(i).getLatitude()),
                                            Double.parseDouble(shortestPath.get(i).getLongitude())))
                            .width(20).color(Color.CYAN)));
                }
            } else {
                polylineList.add(mMap.addPolyline(new PolylineOptions()
                        .add(
                                new LatLng(Double.parseDouble(shortestPath.get(i - 1).getLatitude()),
                                        Double.parseDouble(shortestPath.get(i - 1).getLongitude())),
                                new LatLng(Double.parseDouble(shortestPath.get(i).getLatitude()),
                                        Double.parseDouble(shortestPath.get(i).getLongitude())))
                        .width(20).color(Color.MAGENTA)));
            }


            /**polylineList.add(mMap.addPolyline(new PolylineOptions()
             .add(
             new LatLng(Double.parseDouble(shortestPath.get(i-1).getLatitude()),
             Double.parseDouble(shortestPath.get(i-1).getLongitude())),
             new LatLng(Double.parseDouble(shortestPath.get(i).getLatitude()),
             Double.parseDouble(shortestPath.get(i).getLongitude())))
             .width(20).color(Color.CYAN)));**/
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    class TriggerListener extends TriggerEventListener {
        public void onTrigger(TriggerEvent event) {
            mIALocationManager.requestLocationUpdates(IALocationRequest.create(), mListener);
            // As it is a one shot sensor, it will be canceled automatically.
            // SensorManager.requestTriggerSensor(this, mSigMotion); needs to
            // be called again, if needed.
        }
    }

    //Method called when pressed button to stop navigation, show a dialog.
    public void arrivedToDestination(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(LibraryMapsActivity.this);
        builder.setMessage(R.string.arrived_to_armadio_dialogue)
                .setCancelable(false)
                .setPositiveButton(R.string.arrived, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        navLog.setEndNavTimestamp(getCurrentTimestamp());
                        buttonStopNav.setClickable(false);
                        isNavigationOn = false;
                        askOperation();
                    }
                })
                .setNegativeButton(R.string.not_arrived_yet, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //This just says that the user has not yet finished to navigate
                    }
                });
        builder.create();
        builder.show();
    }

    //Getting current timestamp
    private String getCurrentTimestamp() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm");
        String formattedDate = df.format(c.getTime());
        return formattedDate;
    }

    //Show a dialog, user chooses the type of operation
    public void askOperation() {
        AlertDialog.Builder builder = new AlertDialog.Builder(LibraryMapsActivity.this);
        builder.setTitle(R.string.pick_operation).setCancelable(false)
                .setItems(R.array.operation_type, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        //The 'which' argument contains the index position
                        // of the selected item
                        switch (which) {
                            case 0:
                                navLog.setOperationCode("Q");
                                askConfirmation();
                                break;
                            case 1:
                                navLog.setOperationCode("P");
                                askConfirmation();
                                break;
                            case 2:
                                navLog.setOperationCode("S");
                                askConfirmation();
                                break;
                            default:
                                navLog.setOperationCode("Q");
                                askConfirmation();
                                break;
                        }
                    }
                });
        builder.create();
        builder.show();
    }

    //Dialog asks user the confirmation on operation
    private void askConfirmation() {
        AlertDialog.Builder builder = new AlertDialog.Builder(LibraryMapsActivity.this);
        builder.setTitle(R.string.pick_confirmation).setCancelable(false)
                .setItems(R.array.confirmation_type, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        //The 'which' argument contains the index position
                        // of the selected item
                        switch (which) {
                            case 0:
                                navLog.setConfirmationTypeId("1");
                                navLog.setConfirmationTimestamp(getCurrentTimestamp());
                                Log.i("confirmation section", "success");
                                postLog(navLog);
                                break;
                            case 1:
                                navLog.setConfirmationTypeId("2");
                                navLog.setConfirmationTimestamp(getCurrentTimestamp());
                                if (navLog.getOperationCode().equals("P"))
                                    suggestCheckAtDesk();
                                Log.i("confirmation section", "unsuccess");
                                postLog(navLog);
                                break;
                            case 2:
                                navLog.setConfirmationTypeId("N");
                                navLog.setConfirmationTimestamp(getCurrentTimestamp());
                                Log.i("confirmation section", "cancel");
                                postLog(navLog);
                                break;
                        }
                    }
                });
        builder.create();
        builder.show();
    }

    //Dialog which suggest to check at front desk
    private void suggestCheckAtDesk() {
        AlertDialog.Builder builder = new AlertDialog.Builder(LibraryMapsActivity.this);
        builder.setMessage(R.string.suggest_ask_clerk)
                .setCancelable(false)
                .setPositiveButton(R.string.button_ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Log.i("Suggestion dialog", "suggested check at front desk");
                    }
                });
        builder.create();
        builder.show();
    }

    //Method to post log into server
    private void postLog(com.serviceslab.unipv.librarynavapp.classes.model.Log navLog) {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        APIService service = retrofit.create(APIService.class);
        String json = gson.toJson(navLog);
        Call<com.serviceslab.unipv.librarynavapp.classes.model.Log> call =
                service.postLog(navLog);
        call.enqueue(new Callback<com.serviceslab.unipv.librarynavapp.classes.model.Log>() {
            @Override
            public void onResponse(Call<com.serviceslab.unipv.librarynavapp.classes.model.Log> call,
                                   Response<com.serviceslab.unipv.librarynavapp.classes.model.Log> response) {
                if (response.isSuccessful()) {
                    //textRegionName.setText(response.body().toString());
                    Log.i("OnResponse", "post submitted to API." + response.body().toString());
                } else {
                    Log.i("OnResponse", "post not submitted to API." + response.body().toString());
                }
            }

            @Override
            public void onFailure(Call<com.serviceslab.unipv.librarynavapp.classes.model.Log> call,
                                  Throwable t) {
                Log.e("OnFailure", "Unable to submit post to API.");
            }
        });

        Button b = (Button) findViewById(R.id.buttonDijkstra);
        b.setClickable(true);
    }

    //Method to move the camera
    private void moveCamera(IALatLng userPosition) {
        LatLng destination = new LatLng(userPosition.latitude, userPosition.longitude);
        Projection projection = mMap.getProjection();
        //Getting the pixel boundaries of the map
        LatLngBounds bounds = projection.getVisibleRegion().latLngBounds;
        int boundsTopY = projection.toScreenLocation(bounds.northeast).y;
        int boundsBottomY = projection.toScreenLocation(bounds.southwest).y;
        int boundsTopX = projection.toScreenLocation(bounds.northeast).x;
        int boundsBottomX = projection.toScreenLocation(bounds.southwest).x;
        //int offsetY = (boundsBottomY - boundsTopY) / 10;
        //This offset is less than the previous to allow marker to go closer to screen border
        int offsetY = (boundsBottomY - boundsTopY) / 50;
        //int offsetX = (boundsBottomX - boundsTopX) / 10;
        int offsetX = (boundsBottomX - boundsTopX) / 50;
        Point destinationPoint = projection.toScreenLocation(destination);
        int destinationX = destinationPoint.x;
        int destinationY = destinationPoint.y;
        int scrollX = 0;
        int scrollY = 0;
        //User marker going out of the screen
        if (destinationY <= (boundsTopY + offsetY)) {
            //Preventing user marker going out of the screen
            //if (destinationY >= boundsTopY - offsetY) {
            //This computes the scrolling amount, but we already have the camera moving
            //when changing location, so it's faster to set the boolean.
            //scrollY = -(Math.abs((boundsTopY + offsetY) - destinationY));
            mCameraPositionNeedsUpdating = true;
        } else if (destinationY >= (boundsBottomY - offsetY)) {
            //Same goes for this if and the others
            //else if (destinationY <= (boundsBottomY + offsetY)) {
            //scrollY = (Math.abs(destinationY - (boundsBottomY - offsetY)));
            mCameraPositionNeedsUpdating = true;
        }
        if (destinationX >= (boundsTopX - offsetX)) {
            //if (destinationX <= (boundsTopX + offsetX)) {
            //scrollX = (Math.abs(destinationX - (boundsTopX - offsetX)));
            mCameraPositionNeedsUpdating = true;
        } else if (destinationX <= (boundsBottomX + offsetX)) {
            //else if (destinationX >= (boundsBottomX - offsetX)) {
            //scrollX = -(Math.abs((boundsBottomX + offsetX) - destinationX));
            mCameraPositionNeedsUpdating = true;
        }
        //mMap.animateCamera(CameraUpdateFactory.scrollBy(scrollX,scrollY));
    }

    //Method to rotate map according to user bearing while navigating
    private void rotateUserMarker(float bearing){
        mMarker.setRotation(bearing);
    }

    //Show floor plan id
    private void showInfo(String text) {
        final Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), text,
                Snackbar.LENGTH_INDEFINITE);
        snackbar.setAction(R.string.button_close, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                snackbar.dismiss();
            }
        });
        snackbar.show();
    }

    /**
    private void checkCurrentFloorLevel(String libraryId) {
        switch(libraryId) {
            case "6050a63b-f44f-4a40-9df7-bbeb5ebcb158":
                targetFloorLevel = 0;
                break;
            case "7b7a36ff-5294-48ba-9a45-421952c050e9":
                targetFloorLevel = 1;
                break;
            default:
                targetFloorLevel = 0;
        }
        switch (targetFloorLevel) {
            case 0:
                isUserDownstairsDialog();
                break;
            case 1:
                isUserUpstairsDialog();
        }
    }

    private void isUserUpstairsDialog() {
        //Dialog suggests to go upstairs
        AlertDialog.Builder builder = new AlertDialog.Builder(LibraryMapsActivity.this);
        builder.setMessage("Sei già situato sul soppalco?").setCancelable(false)
                .setPositiveButton(R.string.button_ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        currentFloorLevel = 1;
                        Log.i("isUserUpstairsDialog","user is upstairs: level "
                                + Integer.toString(currentFloorLevel));
                    }
                })
                .setNegativeButton("no", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        currentFloorLevel = 0;
                        Log.i("isUserUpstairsDialog","user is downstairs:level "
                                + Integer.toString(currentFloorLevel));
                        Button arrivedUpstairsButton =
                                (Button) findViewById(R.id.arrivedUpstairsButton);
                        arrivedUpstairsButton.setVisibility(View.VISIBLE);
                        goUpstairsDialog();

                    }
        });
        builder.create();
        builder.show();
    }

    //Dialog suggests to go upstairs
    private void goUpstairsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(LibraryMapsActivity.this);
        builder.setMessage("Sarai guidato fino alle scale. " +
                "Per favore, clicca sul bottone in alto a destra quando sei salito sul soppalco.")
                .setPositiveButton(R.string.button_ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Log.i("goUpstairs dialog", "goupstairs");
                        stairs = true;
                    }
                });
        builder.create();
        builder.show();
    }

    private void isUserDownstairsDialog() {
        //Dialog suggests to go upstairs
        AlertDialog.Builder builder = new AlertDialog.Builder(LibraryMapsActivity.this);
        builder.setMessage("Sei situato al piano terra?").setCancelable(false)
                .setPositiveButton(R.string.button_ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        currentFloorLevel = 0;
                        Log.i("isUserDownstairsDialog","user is downstairs: level "
                                + Integer.toString(currentFloorLevel));
                    }
                })
                .setNegativeButton("no", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        currentFloorLevel = 1;
                        Log.i("isUserDownstairsDialog","user is upstairs: level "
                                + Integer.toString(currentFloorLevel));
                        Button arrivedDownstairsButton =
                                (Button) findViewById(R.id.arrivedDownstairsButton);
                        arrivedDownstairsButton.setVisibility(View.VISIBLE);
                        goDownstairsDialog();
                    }
                });
        builder.create();
        builder.show();
    }
    //Dialog suggests to go downstairs
    private void goDownstairsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(LibraryMapsActivity.this);
        builder.setMessage("Sarai guidato fino alle scale. " +
                "Per favore, clicca sul bottone in alto a sinistra appena sarai arrivato al piano terra.")
                .setPositiveButton(R.string.button_ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Log.i("goDownstairs dialog", "godownstairs");
                        stairs = true;
                    }
                });
        builder.create();
        builder.show();
    }
     **/
    //Method to open BookInfoActivity
    public void openBookInfo(View v) {
        Intent myIntent = new Intent(LibraryMapsActivity.this, BookInfoActivity.class);
        myIntent.putExtra("codice_topografico_completo", codiceTopograficoCompleto);
        myIntent.putExtra("book_inventory", book_inventory);
        LibraryMapsActivity.this.startActivity(myIntent);
    }
}