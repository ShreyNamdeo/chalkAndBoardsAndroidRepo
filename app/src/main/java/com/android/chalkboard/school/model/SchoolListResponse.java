package com.android.chalkboard.school.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class SchoolListResponse implements Serializable {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("type")
    @Expose
    private String type;
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
    @SerializedName("standards")
    @Expose
    private List<StandardFeesModel> fee = null;

    public List<ImageModel> getImages() {
        return images;
    }

    public void setImages(List<ImageModel> images) {
        this.images = images;
    }

    @SerializedName("images")
    @Expose
    private List<ImageModel> images = null;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrincipalName() {
        return principalName;
    }

    public void setPrincipalName(String principalName) {
        this.principalName = principalName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public String getAffiliation() {
        return affiliation;
    }

    public void setAffiliation(String affiliation) {
        this.affiliation = affiliation;
    }

    public String getRegistrationNo() {
        return registrationNo;
    }

    public void setRegistrationNo(String registrationNo) {
        this.registrationNo = registrationNo;
    }

    public List<StandardFeesModel> getFee() {
        return fee;
    }

    public void setFee(List<StandardFeesModel> fee) {
        this.fee = fee;
    }
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }


}





