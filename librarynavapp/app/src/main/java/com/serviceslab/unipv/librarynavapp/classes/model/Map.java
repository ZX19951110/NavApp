package com.serviceslab.unipv.librarynavapp.classes.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by mikim on 15/02/2017.
 */

public class Map {
    @SerializedName("id")
    private String id;

    @SerializedName("library_id")
    private String libraryId;

    @SerializedName("contained_by")
    private String containedBy;

    @SerializedName("description")
    private String description;

    @SerializedName("name")
    private String name;

    @SerializedName("path")
    private String path;

    public Map() {
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

    public String getContainedBy() {
        return containedBy;
    }

    public void setContainedBy(String containedBy) {
        this.containedBy = containedBy;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @Override
    public String toString() {
        return "Map{" +
                "id='" + id + '\'' +
                ", libraryId='" + libraryId + '\'' +
                ", containedBy='" + containedBy + '\'' +
                ", description='" + description + '\'' +
                ", name='" + name + '\'' +
                ", path='" + path + '\'' +
                '}';
    }
}
