package com.serviceslab.unipv.librarynavapp.classes.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by mikim on 14/02/2017.
 */

public class Book {
    @SerializedName("id")
    private String id;

    @SerializedName("inventory")
    private String inventory;

    @SerializedName("library_id")
    private String libraryCode;

    @SerializedName("serie_code")
    private String serieCode;

    @SerializedName("armadio_id")
    private String armadioId;

    @SerializedName("availability_id")
    private String availabilityId;

    @SerializedName("title")
    private String title;

    public Book() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getInventory() {
        return inventory;
    }

    public void setInventory(String inventory) {
        this.inventory = inventory;
    }

    public String getLibraryCode() {
        return libraryCode;
    }

    public void setLibraryCode(String libraryCode) {
        this.libraryCode = libraryCode;
    }

    public String getSerieCode() {
        return serieCode;
    }

    public void setSerieCode(String serieCode) {
        this.serieCode = serieCode;
    }

    public String getArmadioId() {
        return armadioId;
    }

    public void setArmadioId(String armadioId) {
        this.armadioId = armadioId;
    }

    public String getAvailabilityId() {
        return availabilityId;
    }

    public void setAvailabilityId(String availabilityId) {
        this.availabilityId = availabilityId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return "Book{" +
                "id='" + id + '\'' +
                ", inventory='" + inventory + '\'' +
                ", libraryCode='" + libraryCode + '\'' +
                ", serieCode='" + serieCode + '\'' +
                ", armadioId='" + armadioId + '\'' +
                ", availabilityId='" + availabilityId + '\'' +
                ", title='" + title + '\'' +
                '}';
    }
}
