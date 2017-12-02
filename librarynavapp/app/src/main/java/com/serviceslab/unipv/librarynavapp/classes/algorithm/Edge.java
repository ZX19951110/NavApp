package com.serviceslab.unipv.librarynavapp.classes.algorithm;

/**
 * Created by mikim on 24/01/2017.
 */

public class Edge {
    public final Vertex target;
    public final double weight;
    public Edge(Vertex argTarget, double argWeight) {
        this.target = argTarget;
        this.weight = argWeight;
    }
}