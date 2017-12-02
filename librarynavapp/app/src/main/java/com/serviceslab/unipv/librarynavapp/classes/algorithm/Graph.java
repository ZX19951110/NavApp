package com.serviceslab.unipv.librarynavapp.classes.algorithm;

import com.serviceslab.unipv.librarynavapp.classes.model.Path;
import com.serviceslab.unipv.librarynavapp.classes.model.Waypoint;

import java.util.List;

/**
 * Created by mikim on 27/02/2017.
 */

public class Graph {
    private final List<Waypoint> vertexes;
    private final List<Path> edges;

    public Graph(List<Waypoint> waypoints, List<Path> paths) {
        this.vertexes = waypoints;
        this.edges = paths;
    }

    public List<Waypoint> getVertexes() {
        return vertexes;
    }

    public List<Path> getEdges() {
        return edges;
    }
}
