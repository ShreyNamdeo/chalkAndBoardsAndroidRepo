package com.android.chalkboard.register.model;

public class User {
    private String name;
    private String mobileNumber;
    private String email;
    private String role;
    private String dateOfBirth;

    private String password;

    public User(){
        name = "";
        mobileNumber = "";
        email = "";
        role = "";
    }


    // Getter Methods

    public String getName() {
        return name;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public String getEmail() {
        return email;
    }

    public String getRole() {
        return role;
    }

    public String getDateOfBirth() { return dateOfBirth;  }

    //Setter Methods

    public void setName(String name) {
        this.name = name;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setDateOfBirth(String dateOfBirth) {
      this.dateOfBirth = dateOfBirth;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
