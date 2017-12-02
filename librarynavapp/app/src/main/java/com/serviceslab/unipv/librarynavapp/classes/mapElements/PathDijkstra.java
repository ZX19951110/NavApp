package com.serviceslab.unipv.librarynavapp.classes.mapElements;

/**
 * Created by mikim on 23/01/2017.
 */

public class PathDijkstra {

    public WayPoint target;
    private int targetId;
    public final double distance;

    public PathDijkstra(WayPoint argTarget, double argDistance) {
        this.target = argTarget;
        this.distance = argDistance;
    }

    public PathDijkstra(int argTargetId, double argDistance) {
        this.targetId = argTargetId;
        this.distance = argDistance;
    }

    public WayPoint getTarget() {
        return target;
    }

    public int getTargetId() {
        return targetId;
    }

    public void setTargetId(int targetId) {
        this.targetId = targetId;
    }

    public double getDistance() {
        return distance;
    }
}
