package com.android.chalkboard.school.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class StandardFeesModel implements Serializable {

    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("fee")
    @Expose
    private int fee;
    @SerializedName("standard")
    @Expose
    private String standard;
    @SerializedName("institutionId")
    @Expose
    private int institutionId;

    public StandardFeesModel(String standard, int fees, int id, int institutionId) {
        this.standard = standard;
        this.fee = fees;
        this.id = id;
        this.institutionId = institutionId;
    }
    public StandardFeesModel(String standard, int fee) {
        this.fee = fee;
        this.standard = standard;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getInstitutionId() {
        return institutionId;
    }

    public void setInstitutionId(int institutionId) {
        this.institutionId = institutionId;
    }

    public String getStandard() {
        return standard;
    }

    public void setStandard(String standard) {
        this.standard = standard;
    }

    public int getFees() {
        return fee;
    }

    public void setFees(int fees) {
        this.fee = fees;
    }



    @Override
    public String toString() {
        return "StandardFeesModel{" +
                "standard='" + standard + '\'' +
                ", fee=" + fee +
                '}';
    }
}
