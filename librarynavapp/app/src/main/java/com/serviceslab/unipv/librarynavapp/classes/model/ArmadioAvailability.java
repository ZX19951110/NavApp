package com.serviceslab.unipv.librarynavapp.classes.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by mikim on 14/02/2017.
 */

public class ArmadioAvailability {
    @SerializedName("id")
    private String id;

    @SerializedName("code")
    private String code;

    @SerializedName("description")
    private String description;

    public ArmadioAvailability() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "ArmadioAvailability{" +
                "id=" + id +
                ", code='" + code + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
