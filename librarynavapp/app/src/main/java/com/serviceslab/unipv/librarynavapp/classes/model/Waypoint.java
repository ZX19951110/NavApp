package com.serviceslab.unipv.librarynavapp.classes.model;

import com.google.gson.annotations.SerializedName;

import java.util.Arrays;
import java.util.List;

/**
 * Created by mikim on 15/02/2017.
 */

public class Waypoint {
    @SerializedName("id")
    private String id;

    @SerializedName("library_id")
    private String libraryId;

    @SerializedName("armadio_id")
    private String armadioId;

    @SerializedName("latitude")
    private String latitude;

    @SerializedName("longitude")
    private String longitude;

    @SerializedName("name")
    private String name;

    private int previousId;

    private double minDistance = Double.POSITIVE_INFINITY;

    private List<Path> adjacencies;

    public Waypoint() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLibraryId() {
        return libraryId;
    }

    public void setLibraryId(String libraryId) {
        this.libraryId = libraryId;
    }

    public String getArmadioId() {
        return armadioId;
    }

    public void setArmadioId(String armadioId) {
        this.armadioId = armadioId;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Waypoint{" +
                "id='" + id + '\'' +
                ", libraryId='" + libraryId + '\'' +
                ", armadioId='" + armadioId + '\'' +
                ", latitude='" + latitude + '\'' +
                ", longitude='" + longitude + '\'' +
                ", name='" + name + '\'' +
                ", previousId=" + previousId +
                ", minDistance=" + minDistance +
                ", adjacencies=" + adjacencies +
                '}';
    }

    public int getPreviousId() {
        return previousId;
    }

    public void setPreviousId(int previousId) {
        this.previousId = previousId;
    }

    public double getMinDistance() {
        return minDistance;
    }

    public void setMinDistance(double minDistance) {
        this.minDistance = minDistance;
    }

    public List<Path> getAdjacencies() {
        return adjacencies;
    }

    public void setAdjacencies(List<Path> adjacencies) {
        this.adjacencies = adjacencies;
    }

    //Methods for Dijkstra //////////////////////////////////////////////////////////////
    @Override
    public int hashCode(){
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }

        if(getClass() != obj.getClass()) {
            return false;
        }

        Waypoint other = (Waypoint) obj;
        if (id == null) {
            if (other.getId() != null) {
                return false;
            }
        }
        return true;
    }

    public Waypoint(String id, String libraryId, String armadioId, String latitude,
                    String longitude, String name,
                    int previousId, double minDistance,
                    List<Path> adjacencies) {
        this.id = id;
        this.libraryId = libraryId;
        this.armadioId = armadioId;
        this.latitude = latitude;
        this.longitude = longitude;
        this.name = name;
        this.previousId = previousId;
        this.minDistance = minDistance;
        this.adjacencies = adjacencies;
    }
}
