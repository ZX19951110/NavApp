package com.serviceslab.unipv.librarynavapp.classes.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by mikim on 06/02/2017.
 */

public class Library {
    @SerializedName("id")
    private String id;

    @SerializedName("codice_biblioteca")
    private String codiceBiblioteca;

    @SerializedName("name")
    private String name;

    @SerializedName("telephone")
    private String telephone;

    @SerializedName("email")
    private String email;

    @SerializedName("address")
    private String address;

    @SerializedName("latitude")
    private double latitude;

    @SerializedName("longitude")
    private double longitude;

    @SerializedName("ia_id")
    private String iaId;

    public Library() {
        //Empty constructor
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCodiceBiblioteca() {
        return codiceBiblioteca;
    }

    public void setCodiceBiblioteca(String codiceBiblioteca) {
        this.codiceBiblioteca = codiceBiblioteca;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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

    public String getIaId() {
        return iaId;
    }

    public void setIaId(String iaId) {
        this.iaId = iaId;
    }
}
