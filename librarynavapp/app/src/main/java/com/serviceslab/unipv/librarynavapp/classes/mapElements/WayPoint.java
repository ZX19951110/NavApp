package com.serviceslab.unipv.librarynavapp.classes.mapElements;

/**
 * Created by mikim on 18/01/2017.
 */

public class WayPoint implements Comparable<WayPoint> {
    private static int id;
    private double latitude;
    private double longitude;
    private String name;
    private PathDijkstra[] adjacencies;
    private double minDistance = Double.POSITIVE_INFINITY;
    private WayPoint previous;
    private int armadioId;
    private int previousId;

    public WayPoint(double latitude, double longitude, String name, int id) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.name = name;
        this.id = id;
    }

    public WayPoint(double latitude, double longitude, String name) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.name = name;

    }

    public static int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int compareTo(WayPoint other)
    {
        return Double.compare(minDistance, other.minDistance);
    }

    public PathDijkstra[] getAdjacencies() {
        return adjacencies;
    }

    public void setAdjacencies(PathDijkstra[] adjacencies) {
        this.adjacencies = adjacencies;
    }

    public double getMinDistance() {
        return minDistance;
    }

    public void setMinDistance(double minDistance) {
        this.minDistance = minDistance;
    }

    public WayPoint getPrevious() {
        return previous;
    }

    public void setPrevious(WayPoint previous) {
        this.previous = previous;
    }

    public int getArmadioId() {
        return armadioId;
    }

    public void setArmadioId(int armadioId) {
        this.armadioId = armadioId;
    }

    public int getPreviousId() {
        return previousId;
    }

    public void setPreviousId(int previousId) {
        this.previousId = previousId;
    }
}
