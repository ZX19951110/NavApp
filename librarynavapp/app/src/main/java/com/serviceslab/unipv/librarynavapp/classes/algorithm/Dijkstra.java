package com.serviceslab.unipv.librarynavapp.classes.algorithm;

import android.location.Location;
import android.util.Log;

import com.indooratlas.android.sdk.resources.IALatLng;
import com.serviceslab.unipv.librarynavapp.BuildConfig;
import com.serviceslab.unipv.librarynavapp.classes.mapElements.PathDijkstra;
import com.serviceslab.unipv.librarynavapp.classes.mapElements.WayPoint;
import com.serviceslab.unipv.librarynavapp.classes.model.Path;
import com.serviceslab.unipv.librarynavapp.classes.model.Waypoint;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.PriorityQueue;

/**
 * Created by mikim on 24/01/2017.
 */

public class Dijkstra {

    public final Vertex pandora;
    public final Vertex iman;
    public final Vertex miki;
    public final Vertex toolThesist;
    public final Vertex giacomo;
    public final Vertex pillar;
    public final Vertex paulin;
    public static final String TAG = "Dijkstra class";

    public Dijkstra() {
        //Mark verteces, in our case verteces are waypoints, so they will have the same name!
        this.pandora = new Vertex("pandora");
        this.iman = new Vertex("iman");
        this.miki = new Vertex("miki");
        this.toolThesist = new Vertex("toolThesist");
        this.giacomo = new Vertex("giacomo");
        this.pillar = new Vertex("pillar");
        this.paulin = new Vertex("paulin");

        //Set edges and weights
        pandora.adjacencies = new Edge[]{new Edge(iman, 2.20)};
        iman.adjacencies = new Edge[]{new Edge(miki, 0.9), new Edge(pandora, 2.20)};
        miki.adjacencies = new Edge[]{new Edge(toolThesist, 1.5), new Edge(iman, 0.9)};
        toolThesist.adjacencies = new Edge[]{new Edge(giacomo, 1.4), new Edge(miki, 1.5)};
        giacomo.adjacencies = new Edge[]{new Edge(pillar, 1), new Edge(toolThesist, 1.4)};
        pillar.adjacencies = new Edge[]{new Edge(paulin, 0.8), new Edge(giacomo, 1)};
        paulin.adjacencies = new Edge[]{new Edge(pillar, 0.8)};
    }

    public void computePaths(Vertex source) {
        source.minDistance = 0.;
        PriorityQueue<Vertex> vertexQueue = new PriorityQueue<Vertex>();
        vertexQueue.add(source);

        while(!vertexQueue.isEmpty()) {
            Vertex u = vertexQueue.poll();
            //Log.i(TAG, "Vertex retrieved from the queue: " + u.name);
            //Visiting each edge exiting u
            for(Edge e : u.adjacencies) {
                //Log.i(TAG, "Vertex retrieved from adjacencies of " + u.name + " is " + e.target.name);
                Vertex v = e.target;
                double weight = e.weight;
                //Log.i(TAG, "Edge has weight " + e.weight);
                double distanceThroughU = u.minDistance + weight;
                //Log.i(TAG, "Distance passing through u is " + distanceThroughU);
                if(distanceThroughU < v.minDistance) {
                    vertexQueue.remove(v);
                    v.minDistance = distanceThroughU;
                    v.previous = u;
                    vertexQueue.add(v);
                }
            }
        }
    }

    public static List<Vertex> getShortestPathTo(Vertex target) {
        List<Vertex> path = new ArrayList<Vertex>();
        for(Vertex vertex = target; vertex != null; vertex = vertex.previous) {
            path.add(vertex);
        }
        Collections.reverse(path);
        return path;
    }
}
