package com.serviceslab.unipv.librarynavapp.classes.algorithm;

import android.location.Location;
import android.util.Log;

import com.indooratlas.android.sdk.resources.IALatLng;
import com.serviceslab.unipv.librarynavapp.classes.mapElements.WayPoint;
import com.serviceslab.unipv.librarynavapp.classes.model.Path;
import com.serviceslab.unipv.librarynavapp.classes.model.Waypoint;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mikim on 23/02/2017.
 */

public class Distance {
    //private List<Path> paths;
    //private List<Waypoint> waypoints;

    //public Distance(List<Path> paths, List<Waypoint> waypoints) {
        //this.paths = paths;
        //this.waypoints = waypoints;
    //}


    public Distance() {
    }

    public static List<Path> setAllDistances(List<Path> paths, List<Waypoint> waypoints) {
        Waypoint source = new Waypoint();
        Waypoint target = new Waypoint();
        for (int i = 0; i < paths.size(); i++) {
            //Find the ids of target and source waypoints
            String sourceWpId = paths.get(i).getSourceWpId();
            String targetWpId = paths.get(i).getTargetWpId();

            //Find those waypoints
            for (int j = 0; j < waypoints.size(); j++){
                if (waypoints.get(j).getId().equals(sourceWpId)){
                    source = waypoints.get(j);
                    //String sorgente = source.toString();
                    //Log.i("ciao",sorgente);
                }
                if (waypoints.get(j).getId().equals(targetWpId)) {
                    target = waypoints.get(j);
                    //String bersaglio = target.toString();
                    //Log.i("ciao",bersaglio);
                }
            }
            if (source.getId().equals(sourceWpId) && target.getId().equals(targetWpId)) {
                Location sourceLoc = new Location("source");
                Location targetLoc = new Location("target");
                sourceLoc.setLatitude(Float.parseFloat(source.getLatitude()));
                sourceLoc.setLongitude(Float.parseFloat(source.getLongitude()));
                targetLoc.setLatitude(Float.parseFloat(target.getLatitude()));
                targetLoc.setLongitude(Float.parseFloat(target.getLongitude()));
                paths.get(i).setDistance(Float.toString(sourceLoc.distanceTo(targetLoc)));

                //for Dijkstra
                paths.get(i).setWeight(sourceLoc.distanceTo(targetLoc));
            }
        }
        return paths;
    }

    public static List<Waypoint> setWPAdjacencies(List<Path> paths, List<Waypoint> waypoints) {
        //external cycle on wp list
        for (int i = 0; i < waypoints.size(); i++) {
            //i-th waypoint
            String currentWp = waypoints.get(i).getId();
            //initializing the wp adjacencies
            waypoints.get(i).setAdjacencies(new ArrayList<Path>());
            //internal cycle on path list
            for (int j = 0; j < paths.size(); j++) {
                //j-th path
                Path currentPath = paths.get(j);
                //id of the source waypoint of the j-th path
                String sourceWPID = currentPath.getSourceWpId();
                //if the ids matches then add the j-th path to the adjacencies of the i-th waypoint
                if (currentWp.equals(sourceWPID)) {
                    waypoints.get(i).getAdjacencies().add(currentPath);
                }
            }
        }
        return waypoints;
    }

    public Waypoint getClosestWaypoint(List<Waypoint> waypoints, IALatLng userPosition) {
        float[] distances = new float[waypoints.size()];
        float minDistance = 1000;

        Waypoint closestWp = new Waypoint();

        if(waypoints.size() != 0) {
            for (int i = 0; i < waypoints.size(); i++) {
                distances[i] = this.getDistance(waypoints.get(i), userPosition);
            }
            minDistance = distances[0];
            for (int i = 1; i < waypoints.size(); i++) {
                if(minDistance >=  distances[i]) {
                    /*
                    Updating minDist id if the current distance is less
                    than the previous in the array
                    */
                    minDistance = distances[i];
                    closestWp = waypoints.get(i);
                }
            }
            return closestWp;
        } else {
            /* Returning a value for error */
            return null;
        }
    }

    /* Method to find the closest wayPoint to user current position. */
    public int getClosestWPId(List<Waypoint> waypoints, IALatLng userPosition) {
        /*
        Array which contains the distances between each
        WayPoint object and the user current position
        */
        float[] distances =  new float[waypoints.size()];
        /* Initializing the distance with an high number of meters. */
        float minDist = 1000;
        /*
        id will contain the id of the WayPoint with the minimum
        distance to the user current position
        */
        int id = 0;
        /* If the list of WayPoints is not empty execute the block */
        if (waypoints.size() != 0){
            /* Cycle to compute the distances. */
            for(int i = 0; i < waypoints.size(); i++) {
                distances[i] = this.getDistance(waypoints.get(i), userPosition);
            }
            /*
            Setting minDist to be equal to the distance between
            first WayPoint and current user position
            */
            minDist = distances[0];
            /* Cycle to compute minimum value in the distances array */
            for (int i = 1; i < waypoints.size(); i++) {
                if(minDist >=  distances[i]) {
                    /*
                    Updating minDist id if the current distance is less
                    than the previous in the array
                    */
                    minDist = distances[i];
                    id = i;
                }
            }
            /* Returning the id of the WayPoint with minimum distance to user position */
            //Log.i(TAG, "The minimum distance is: " + String.valueOf(distances[id]) + " to the node " + waypoints.get(id).getName());
            return id;
        } else {
            /* Returning a value for error */
            return -1;
        }

    }

    /* Method to get the distance between the current user position and a WayPoint */
    public float getDistance(Waypoint waypoint, IALatLng userPosition) {
        /*
        Using the Location class, because it provides methods to compute distance between
        two location which contains latitude and longitude
        */
        Location locationA = new Location("Point A");
        locationA.setLatitude(userPosition.latitude);
        locationA.setLongitude(userPosition.longitude);

        Location locationB = new Location("Point B");
        locationB.setLatitude(Float.parseFloat(waypoint.getLatitude()));
        locationB.setLongitude(Float.parseFloat(waypoint.getLongitude()));

        /* This method returns the distance in meters between the locations. */
        float distance = locationA.distanceTo(locationB);

        return distance;
    }
}
