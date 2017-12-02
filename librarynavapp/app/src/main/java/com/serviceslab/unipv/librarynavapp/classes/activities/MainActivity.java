package com.serviceslab.unipv.librarynavapp.classes.activities;

import android.Manifest;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.serviceslab.unipv.librarynavapp.R;
import com.serviceslab.unipv.librarynavapp.classes.SdkExample;
import com.serviceslab.unipv.librarynavapp.classes.mapElements.WayPoint;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    final private int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 124;
    public static final String TAG = "MainActivity";
    private boolean mLogShown;
    private View mLayout;
    private ExampleAdapter mAdapter;
    //private String transactionCode;
    //private String requestCode;
    //private String libraryCode;
    //private String armadioCode;
    //private String bookTitle;
    private String requestTimestamp;
    private String bookInventory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        enablePermissions();
        //ActivityCompat.requestPermissions(this, neededPermissions, CODE_PERMISSIONS);
        setContentView(R.layout.activity_main);
        mAdapter = new ExampleAdapter(this);
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm");
        requestTimestamp = df.format(c.getTime());
        final WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        if (!wifiManager.isWifiEnabled()){
            //TODO ask to open connection
            enableWifi(wifiManager);
        }


    }

    //Button function
    public void goToMap(View v){
        //Intent myIntent = new Intent(MainActivity.this, MapsOverlayActivity.class);
        Intent myIntent = new Intent(MainActivity.this, LibraryMapsActivity.class);
        myIntent.putExtra("codice_topografico_completo", "S001P001L002A024");
        //TODO change to get library code instead and use retrofit to retrieve the id
        //myIntent.putExtra("library_code", "7b7a36ff-5294-48ba-9a45-421952c050e9");
        //myIntent.putExtra("library_code", "6050a63b-f44f-4a40-9df7-bbeb5ebcb158");
        myIntent.putExtra("library_code", "55b06e3f-ea54-4516-95a5-ca88d39fa861");
        myIntent.putExtra("book_inventory", "000024821");
        myIntent.putExtra("book_title", "Dorso pensatore politico");
        myIntent.putExtra("requestTimestamp", requestTimestamp);
        MainActivity.this.startActivity(myIntent);
    }

    //Button function
    public void chooseLibrary(View v){
        //Intent myIntent = new Intent(MainActivity.this, MapsOverlayActivity.class);
        Intent myIntent = new Intent(MainActivity.this, ListLibraryActivity.class);
        myIntent.putExtra("book_inventory", "000024821");
        myIntent.putExtra("book_title", "Dorso pensatore politico");
        myIntent.putExtra("requestTimestamp", requestTimestamp);
        MainActivity.this.startActivity(myIntent);
    }


    private void enablePermissions(){
        //Checking whether wifi permission is enabled
        List<String> permissionNeeded = new ArrayList<String>();

        final List<String> permissionList = new ArrayList<String>();
        if (!addPermission(permissionList, Manifest.permission.ACCESS_FINE_LOCATION)){
            permissionNeeded.add("Access to GPS");
        }
        if (!addPermission(permissionList, Manifest.permission.ACCESS_WIFI_STATE)){
            permissionNeeded.add("Access the WIFI state");
        }
        if (!addPermission(permissionList, Manifest.permission.CHANGE_WIFI_STATE)) {
            permissionNeeded.add("Change the WIFI state");
        }
        if (!addPermission(permissionList, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            permissionNeeded.add("Write external storage");
        }
        if (permissionList.size() > 0) {
            if (permissionNeeded.size() > 0) {
                //Need Rationale
                String message = "You need to grant message to " + permissionNeeded.get(0);
                for (int i = 1; i < permissionNeeded.size(); i++) {
                    message = message + " , " + permissionNeeded.get(i);
                }
                showMessageOkCancel(message,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                    requestPermissions(permissionList.toArray(new String[permissionList.size()]), REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
                                }
                            }
                        });
                return;
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(permissionList.toArray(new String[permissionList.size()]), REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
            }
            return;
        }
    }

    private boolean addPermission(List<String> permissionList, String permission) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if(checkSelfPermission(permission)!= PackageManager.PERMISSION_GRANTED) {
                permissionList.add(permission);
                if (!shouldShowRequestPermissionRationale(permission))
                {
                    return false;
                }
            }
            return true;
        } else {
            return true;
        }
    }

    private void showMessageOkCancel(String message, DialogInterface.OnClickListener okListener) {
    //private void showMessageOkCancel(String message, DialogInterface.OnClickListener okListener, DialogInterface cancelListener) {
        new AlertDialog.Builder(MainActivity.this)
                .setMessage(message)
                .setPositiveButton("Ok", okListener)
                .setNegativeButton("Cancel",  new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,
                                        int whichButton) {
                        // Ask user if he is sure in that case close app.
                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                        builder.setMessage(R.string.denied_permissions_explanation)
                                .setCancelable(false)
                                .setPositiveButton(R.string.understood, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        finish();
                                    }
                                })
                                .setNegativeButton(R.string.ask_again_permissions, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        enablePermissions();
                                    }
                                });
                        builder.create();
                        builder.show();
                    }
                } )
                .setCancelable(false)
                .create()
                .show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults){
        //In this case I'm changing multiple permissions.
        switch (requestCode) {
            case REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS:
            {
                Map<String, Integer> perms = new HashMap<String, Integer>();
                //Initial
                perms.put(Manifest.permission.ACCESS_FINE_LOCATION, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.ACCESS_WIFI_STATE, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.CHANGE_WIFI_STATE, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.WRITE_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
                //Fill with results
                for (int i=0; i < permissions.length; i++) {
                    perms.put(permissions[i], grantResults[i]);
                }
                //Check for all permissions
                if(perms.get(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                        && perms.get(Manifest.permission.ACCESS_WIFI_STATE) == PackageManager.PERMISSION_GRANTED
                        && perms.get(Manifest.permission.CHANGE_WIFI_STATE) == PackageManager.PERMISSION_GRANTED
                        && perms.get(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
                    //All permissions granted
                    Toast.makeText(MainActivity.this, "Granted permissions!", Toast.LENGTH_SHORT).show();
                } else {
                    //Permission denied
                    //Explain why the user should grant permissions.
                    //If the user doesn't give permissions close app
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setMessage(R.string.denied_permissions_explanation)
                            .setCancelable(false)
                            .setPositiveButton(R.string.understood, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    finish();
                                }
                            })
                            .setNegativeButton(R.string.ask_again_permissions, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    enablePermissions();
                                }
                            });
                    builder.create();
                    builder.show();
                }
                break;
            }
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }



    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    /**
     * Adapter for example activities.
     */
    class ExampleAdapter extends BaseAdapter {

        final ArrayList<ExampleEntry> mExamples;

        ExampleAdapter(Context context) {
            mExamples = listActivities(context);
            Collections.sort(mExamples);
        }

        @Override
        public int getCount() {
            return mExamples.size();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ExampleEntry entry = mExamples.get(position);
            if (convertView == null) {
                convertView = getLayoutInflater()
                        .inflate(android.R.layout.simple_list_item_2, parent, false);
            }
            TextView labelText = (TextView) convertView.findViewById(android.R.id.text1);
            TextView descriptionText = (TextView) convertView.findViewById(android.R.id.text2);
            //labelText.setText(entry.mLabel);
            labelText.setText(entry.mActivityName);
            descriptionText.setText(entry.mDescription);
            return convertView;
        }
    }

    /**
     * Returns a list of activities that are part of this application, skipping
     * those from included libraries and *this* activity.
     */
    public ArrayList<ExampleEntry> listActivities(Context context) {
        ArrayList<ExampleEntry> result = new ArrayList<>();
        try {
            final String packageName = context.getPackageName();
            PackageManager pm = context.getPackageManager();
            PackageInfo info = pm.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES);
            ActivityInfo[] activities = info.activities;
            for (int i = 0; i < activities.length; i++) {
                parseExample(activities[i], result);
            }
            return result;
        } catch (Exception e) {
            throw new IllegalStateException("Failed to get list of activities", e);
        }
    }

    @SuppressWarnings("unchecked")
    private void parseExample(ActivityInfo info, ArrayList<ExampleEntry> list) {
        try {
            Class cls = Class.forName(info.name);
            Log.i(TAG, "Activity name: " + cls.getName().replace("com.serviceslab.unipv.librarynavapp.activities.", ""));
            String activityName = cls.getName().replace("com.serviceslab.unipv.librarynavapp.classes.activities.", "");
            if (cls.isAnnotationPresent(SdkExample.class)) {
                SdkExample annotation = (SdkExample) cls.getAnnotation(SdkExample.class);
                //Using the activity name
                list.add(new ExampleEntry(new ComponentName(info.packageName, info.name),
                //Using the package name
                //list.add(new ExampleEntry(new ComponentName(info.packageName, cls.getName()),
                        annotation.title() != -1
                                ? getString(annotation.title())
                                : info.loadLabel(getPackageManager()).toString(),
                        getString(annotation.description()), activityName));
            }
        } catch (ClassNotFoundException e) {
            Log.e(TAG, "Failed to read example info for class: " + info.name, e);
        }
    }


    static class ExampleEntry implements Comparable<ExampleEntry> {
        final ComponentName mComponentName;
        final String mLabel;
        final String mDescription;
        //Adding the activity name
        final String mActivityName;

        ExampleEntry(ComponentName name, String label, String description, String activityName) {
            mComponentName = name;
            mLabel = label;
            mDescription = description;
            mActivityName = activityName;
        }

        @Override
        public int compareTo(ExampleEntry another){
            return mLabel.compareTo(another.mLabel);
        }
    }

    private void enableWifi(final WifiManager wifiManager) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);

        // set title
        alertDialogBuilder.setTitle("Wifi Settings");

        // set dialog message
        alertDialogBuilder
                .setMessage("In order to correctly work, app needs wifi enabled.")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //enable wifi
                        wifiManager.setWifiEnabled(true);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //disable wifi
                        wifiManager.setWifiEnabled(false);
                    }
                });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    }
}
