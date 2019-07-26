
package com.android.chalkboard.login.modal;

import java.io.Serializable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserRequest implements Serializable
{
    @SerializedName("email")
    @Expose
    private String email;

    @SerializedName("password")
    @Expose
    private String password;

    @SerializedName("role")
    @Expose
    private String role;

    private final static long serialVersionUID = 3103683659418362646L;

    public String getUserName() {
        return email;
    }

    public void setUserName(String userName) {
        this.email = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

}
