package com.android.chalkboard.school.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class AddSchoolRequest implements Serializable {

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
    @SerializedName("numberOfImages")
    @Expose
    private Integer numberOfImages;
    @SerializedName("standards")
    @Expose
    private List<StandardFeesModel> fee = null;

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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getNoOfImags() {
        return numberOfImages;
    }

    public void setNoOfImags(Integer noOfImags) {
        this.numberOfImages = noOfImags;
    }

    public List<StandardFeesModel> getFee() {
        return fee;
    }

    public void setFee(List<StandardFeesModel> fee) {
        this.fee = fee;
    }

    public AddSchoolRequest(String name, String principalName, String address, String city, String specialization, String affiliation, String registrationNo, String type, Integer numberOfImages, List<StandardFeesModel> fee) {
        this.name = name;
        this.principalName = principalName;
        this.address = address;
        this.city = city;
        this.specialization = specialization;
        this.affiliation = affiliation;
        this.registrationNo = registrationNo;
        this.type = type;
        this.numberOfImages = numberOfImages;
        this.fee = fee;
    }
}
