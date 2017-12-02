package com.serviceslab.unipv.librarynavapp.classes.mapElements;

import android.location.Location;

import com.indooratlas.android.sdk.resources.IALatLng;
import com.serviceslab.unipv.librarynavapp.BuildConfig;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.PriorityQueue;


/**
 * Created by mikim on 25/01/2017.
 */
/*
This class wants to compute the shortest path from the WP next to actual user position
to the destination waypoint.
*/
public class DijkstraRevised {
    /* List of WayPoints of a floor plan */
    private List<WayPoint> waypoints;
    public static final String TAG = "DijkstraRevised class";

    /* Constructor, instantiate the WayPoint list */
    public DijkstraRevised(){
        waypoints = new ArrayList<WayPoint>();
    }

    /* Constructor, uses directly an existing ArrayList<WayPoints> object */
    public DijkstraRevised(List<WayPoint> wayPoints){
        waypoints = wayPoints;
    }

    public List<WayPoint> getWaypoints() {
        if (waypoints.isEmpty() == false) {
            return waypoints;
        } else {
            return null;
        }
    }

    /* Used to populate the WayPoint list if the first constructor is used. */
    public void addWayPoint(WayPoint wayPoint) {
        waypoints.add(wayPoint);
    }

    /* Method to get the distance between the current user position and a WayPoint */
    public float getDistance(WayPoint wayPoint, IALatLng userPosition) {
        /*
        Using the Location class, because it provides methods to compute distance between
        two location which contains latitude and longitude
        */
        Location locationA = new Location("Point A");
        locationA.setLatitude(userPosition.latitude);
        locationA.setLongitude(userPosition.longitude);

        Location locationB = new Location("Point B");
        locationB.setLatitude(wayPoint.getLatitude());
        locationB.setLongitude(wayPoint.getLongitude());

        /* This method returns the distance in meters between the locations. */
        float distance = locationA.distanceTo(locationB);

        return distance;
    }

    /* Method to find the closest wayPoint to user current position. */
    public int getClosestWPId(IALatLng userPosition) {
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

    /*
    This method computes the shortest path between two waypoints.
    The source WayPoint is supposed to be the one computed by the
    previous method getClosestWPId.
    */
    public void computePath(int sourceId) {
        /* Getting the source WayPoint by using the id. */
        WayPoint source = waypoints.get(sourceId);
        //Log.i(TAG, "WayPoint retrieved from the ArrayList: " + source.name);
        /*Putting the minDistance attribute of the source WP equal to 0 */
        source.setMinDistance(0.);
        /* Creating the PriorityQueue object that stores WPs */
        PriorityQueue<WayPoint> wayPointQueue = new PriorityQueue<WayPoint>();
        /* Adding the source to the queue */
        wayPointQueue.add(source);

        while(!wayPointQueue.isEmpty()) {
            /* Extracting wp from queue */
            WayPoint u = wayPointQueue.poll();
            //Log.i(TAG, "Vertex retrieved from the queue: " + u.name);
            /*Visiting each edge exiting u */
            for(PathDijkstra p : u.getAdjacencies()) {
                /* Retrieving next WayPoint on the considered PathDijkstra object */
                WayPoint v = p.target;
                //Log.i(TAG, "Vertex retrieved from adjacencies of " + u.name + " is " + p.target.name);
                /*
                Retrieving the distance that the PathDijkstra object covers (the distance between
                the two adjacent nodes or Waypoints)
                */
                double distance = p.distance;
                //Log.i(TAG, "PathDijkstra covers the following distance: " + p.distance);
                /* Computing the total distance covered when passing by this PathDijkstra */
                double distanceThroughU = u.getMinDistance() + distance;
                /*
                Log.i(TAG, "Distance passing through u is " + distanceThroughU);
                Checking if the PathDijkstra covered is the most convenient
                It is verified if of course this path is the only available for a node
                */
                if (distanceThroughU < v.getMinDistance()) {
                    /* Removing v from the queue */
                    wayPointQueue.remove(v);
                    /* Updating minimum distance */
                    v.setMinDistance(distanceThroughU);
                    /* Setting u as previous of v */
                    v.setPrevious(u);
                    /* Updating also the waypoints list */
                    for (int i = 0; i < waypoints.size(); i++) {
                        if(waypoints.get(i).getName() == v.getName()) {
                            waypoints.get(i).setMinDistance(distanceThroughU);
                            waypoints.get(i).setPrevious(u);
                        }
                    }
                    /* v takes the place of u in the queue */
                    wayPointQueue.add(v);
                }
            }
        }
    }

    /* Method to get the shortest path by scanning wp and their previous */
    public List<WayPoint> getShortestPathTo(WayPoint target){
        waypoints.clear();
        /* Debug stuff */
        int i = 0;
        for (WayPoint wp = target; wp != null; wp = wp.getPrevious()) {
            /* Debug stuff */
            i++;
            /* Debug stuff */
            if (BuildConfig.DEBUG && (i > 100))
                throw new AssertionError();
            waypoints.add(wp);
        }
        Collections.reverse(waypoints);
        return waypoints;
    }
}
