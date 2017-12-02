package com.serviceslab.unipv.librarynavapp.classes.algorithm;

import com.serviceslab.unipv.librarynavapp.classes.model.Armadio;

import java.util.Comparator;

/**
 * Created by mikim on 02/05/2017.
 */

public class MyComparator implements Comparator<Armadio>{
    @Override
    public int compare(Armadio a1, Armadio a2) {
        //int n1 = Integer.valueOf(a1.getNumber());
        //int n2 = Integer.valueOf(a2.getNumber());
        return a1.getNumber().compareTo(a2.getNumber());
        //return n1 < n2 ? -1 : n1 == n2 ? 0 : 1;
    }
}
