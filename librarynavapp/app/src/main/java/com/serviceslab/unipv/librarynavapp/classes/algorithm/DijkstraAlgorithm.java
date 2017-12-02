package com.serviceslab.unipv.librarynavapp.classes.algorithm;

import android.util.Log;

import com.serviceslab.unipv.librarynavapp.classes.model.Path;
import com.serviceslab.unipv.librarynavapp.classes.model.Waypoint;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by mikim on 27/02/2017.
 */

public class DijkstraAlgorithm {
    private final List<Waypoint> nodes;
    private final List<Path> edges;
    private Set<Waypoint> settledNodes;
    private Set<Waypoint> unSettledNodes;
    private Map<Waypoint, Waypoint> predecessors;
    private Map<Waypoint, Float> distance;

    public DijkstraAlgorithm(Graph graph) {
        this.nodes = new ArrayList<Waypoint>(graph.getVertexes());
        this.edges = new ArrayList<Path>(graph.getEdges());
    }

    public void execute(Waypoint source) {
        //Visited nodes
        settledNodes = new HashSet<Waypoint>();
        //Not visited nodes
        unSettledNodes = new HashSet<Waypoint>();
        //Distance between one node and the following
        distance = new HashMap<Waypoint, Float>();
        //Couple of predecessors
        predecessors = new HashMap<Waypoint, Waypoint>();
        //Setting initial distance
        distance.put(source, 0f);
        //distance.put(source, Float.MAX_VALUE);
        //Setting initial unvisited nodes
        unSettledNodes.add(source);
        //for (Waypoint w : nodes) {
            //unSettledNodes.add(w);
        //}
        while (unSettledNodes.size() > 0) {
            Waypoint node = getMinimum(unSettledNodes);
            settledNodes.add(node);
            unSettledNodes.remove(node);
            findMinimalDistances(node);
        }
    }

    private void findMinimalDistances(Waypoint node) {
        List<Waypoint> adjacentNodes = getNeighbors(node);
        for (Waypoint target : adjacentNodes) {
            if (getShortestDistance(target) > getShortestDistance(node)
                    + getDistance(node, target)) {
                distance.put(target, getShortestDistance(node)+ getDistance(node, target));
                predecessors.put(target, node);
                unSettledNodes.add(target);
            }
        }
    }

    private Float getDistance(Waypoint node, Waypoint target) {
        for(Path edge : edges) {
            if(edge.getSourceWp().equals(node) && edge.getTargetWp().equals(target)) {
                return edge.getWeight();
            }
        }
        throw new RuntimeException("Should not happen!");
    }

    private List<Waypoint> getNeighbors(Waypoint node) {
        List<Waypoint> neighbors = new ArrayList<Waypoint>();
        Log.i("getNeighbors", "hello");
        for (Path edge : edges) {
            if (edge.getSourceWp().equals(node) && isSettled(edge.getTargetWp())) {
                neighbors.add(edge.getTargetWp());
            }
        }
        return neighbors;
    }

    private Waypoint getMinimum(Set<Waypoint> unsettledVertexes) {
        Waypoint minimum = null;
        for (Waypoint vertex : unsettledVertexes) {
            if (minimum ==  null) {
                minimum = vertex;
            } else {
                if (getShortestDistance(vertex) < getShortestDistance(minimum)) {
                    minimum = vertex;
                }
            }
        }
        return minimum;
    }

    private boolean isSettled(Waypoint vertex) {
        return settledNodes.contains(vertex);
    }

    private Float getShortestDistance(Waypoint destination) {
        Float d = distance.get(destination);
        if(d == null) {
            return Float.MAX_VALUE;
        } else {
            return d;
        }
    }

    public LinkedList<Waypoint> getPath(Waypoint target) {
        LinkedList<Waypoint> shortestPath = new LinkedList<Waypoint>();
        Waypoint step = target;
        if(predecessors.get(step) == null) {
            return null;
        }
        shortestPath.add(step);
        while(predecessors.get(step) != null) {
            step = predecessors.get(step);
            shortestPath.add(step);
        }
        Collections.reverse(shortestPath);
        return shortestPath;
    }

}
