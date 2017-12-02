package com.serviceslab.unipv.librarynavapp.classes.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by mikim on 15/02/2017.
 */

public class Path {
    @SerializedName("id")
    private String id;

    @SerializedName("source_wp_id")
    private String sourceWpId;

    @SerializedName("distance")
    private String distance;

    @SerializedName("target_wp_id")
    private String targetWpId;


    //Attributes for Dijkstra////////////////////////////////////
    public Waypoint sourceWp = new Waypoint();

    public Waypoint targetWp = new Waypoint();

    public Float weight;
    //////////////////////////////////////////////////////////////


    public Path() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSourceWpId() {
        return sourceWpId;
    }

    public void setSourceWpId(String sourceWpId) {
        this.sourceWpId = sourceWpId;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getTargetWpId() {
        return targetWpId;
    }

    public void setTargetWpId(String targetWpId) {
        this.targetWpId = targetWpId;
    }

    @Override
    public String toString() {
        return "Path{" +
                "id='" + id + '\'' +
                ", sourceWpId='" + sourceWpId + '\'' +
                ", distance='" + distance + '\'' +
                ", targetWpId='" + targetWpId + '\'' +
                '}';
    }

    //Methods for Dijkstra//////////////////////////////////////////////////
    public void setSourceWp(List<Waypoint> waypoints) {
        for (int i = 0; i < waypoints.size(); i++) {
            if(waypoints.get(i).getId() == this.getSourceWpId()){
                sourceWp = waypoints.get(i);
            }
        }
    }

    public Waypoint getSourceWp() {
        return this.sourceWp;
    }

    public void setTargetWp(List<Waypoint> waypoints) {
        for (int i=0; i<waypoints.size(); i++) {
            if(waypoints.get(i).getId() == this.getTargetWpId()) {
                targetWp = waypoints.get(i);
            }
        }
    }

    public Waypoint getTargetWp() {
        return this.targetWp;
    }

    public void setWeight(Float distance) {
        this.weight = distance;
    }

    public Float getWeight() {
        return this.weight;
    }

    /////////////////////////////////////////////////////////////////
}
