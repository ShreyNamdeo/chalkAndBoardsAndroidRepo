package com.android.chalkboard.login.modal;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class UserRequestOTP implements Serializable {

    @SerializedName("mobileNumber")
    private String mobileNo = "mobileNumber";

    @SerializedName("role")
    private String role = "role";


    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }


}
