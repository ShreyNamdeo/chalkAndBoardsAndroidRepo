
package com.android.chalkboard.login.modal;

import java.io.Serializable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LoginResponse implements Serializable
{

    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("mobileNumber")
    @Expose
    private String mobileNumber;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("otp")
    @Expose
    private String otp;
    @SerializedName("role")
    @Expose
    private String role;
    @SerializedName("metaDataStr")
    @Expose
    private Object metaDataStr;
    @SerializedName("android")
    @Expose
    private Object android;
    @SerializedName("dateOfBirth")
    @Expose
    private String dateOfBirth;
    @SerializedName("jwtToken")
    @Expose
    private String jwtToken;
    private final static long serialVersionUID = -1696178530256427981L;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Object getMetaDataStr() {
        return metaDataStr;
    }

    public void setMetaDataStr(Object metaDataStr) {
        this.metaDataStr = metaDataStr;
    }

    public Object getAndroid() {
        return android;
    }

    public void setAndroid(Object android) {
        this.android = android;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getJwtToken() {
        return jwtToken;
    }

    public void setJwtToken(String jwtToken) {
        this.jwtToken = jwtToken;
    }

}
