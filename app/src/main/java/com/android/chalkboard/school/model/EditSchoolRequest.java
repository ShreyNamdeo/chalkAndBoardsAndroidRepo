package com.android.chalkboard.school.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class EditSchoolRequest implements Serializable {
    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("numberOfImages")
    @Expose
    private int numberOfImages;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("principalName")
    @Expose
    private String principalName;
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("city")
    @Expose
    private String city;
    @SerializedName("specialization")
    @Expose
    private String specialization;
    @SerializedName("affiliation")
    @Expose
    private String affiliation;
    @SerializedName("registrationNo")
    @Expose
    private String registrationNo;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("standards")
    @Expose
    private List<StandardFeesModel> fee = null;
    @SerializedName("createdDate")
    @Expose
    private String createdDate;
    @SerializedName("images")
    @Expose
    private List<ImageModel> images = null;



    public EditSchoolRequest(int id, String name, String principalName, String address, String city, String specialization, String affiliation,
                             String type, String registrationNo, List<StandardFeesModel> fee,int numberOfImages ,List<ImageModel> images) {
        this.id = id;
        this.name = name;
        this.principalName = principalName;
        this.address = address;
        this.city = city;
        this.specialization = specialization;
        this.affiliation = affiliation;
        this.registrationNo = registrationNo;
        this.type = type;
        this.fee = fee;
        this.images = images;
        this.numberOfImages = numberOfImages;
        //this.createdDate = createdDate;
    }
}
