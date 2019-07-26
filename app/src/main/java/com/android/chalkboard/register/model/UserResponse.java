package com.android.chalkboard.register.model;

import java.io.Serializable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

    public class UserResponse implements Serializable
    {
        private final static long serialVersionUID = -135089465080332810L;


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
        @SerializedName("activated")
        @Expose
        private boolean activated;
        @SerializedName("metaData")
        @Expose
        private Object metaData;
        @SerializedName("androidNotificationId")
        @Expose
        private Object androidNotificationId;
        @SerializedName("appleNotificationId")
        @Expose
        private Object appleNotificationId;
        @SerializedName("userRole")
        @Expose
        private UserRole userRole;
        @SerializedName("dateOfBirth")
        @Expose
        private int dateOfBirth;
        @SerializedName("s3Key")
        @Expose
        private Object s3Key;
        @SerializedName("dateOfBirthFormattedStr")
        @Expose
        private String dateOfBirthFormattedStr;
        @SerializedName("image")
        @Expose
        private Image image;

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

        public boolean isActivated() {
            return activated;
        }

        public void setActivated(boolean activated) {
            this.activated = activated;
        }

        public Object getMetaData() {
            return metaData;
        }

        public void setMetaData(Object metaData) {
            this.metaData = metaData;
        }

        public Object getAndroidNotificationId() {
            return androidNotificationId;
        }

        public void setAndroidNotificationId(Object androidNotificationId) {
            this.androidNotificationId = androidNotificationId;
        }

        public Object getAppleNotificationId() {
            return appleNotificationId;
        }

        public void setAppleNotificationId(Object appleNotificationId) {
            this.appleNotificationId = appleNotificationId;
        }

        public UserRole getUserRole() {
            return userRole;
        }

        public void setUserRole(UserRole userRole) {
            this.userRole = userRole;
        }

        public int getDateOfBirth() {
            return dateOfBirth;
        }

        public void setDateOfBirth(int dateOfBirth) {
            this.dateOfBirth = dateOfBirth;
        }

        public Object getS3Key() {
            return s3Key;
        }

        public void setS3Key(Object s3Key) {
            this.s3Key = s3Key;
        }

        public String getDateOfBirthFormattedStr() {
            return dateOfBirthFormattedStr;
        }

        public void setDateOfBirthFormattedStr(String dateOfBirthFormattedStr) {
            this.dateOfBirthFormattedStr = dateOfBirthFormattedStr;
        }

        public Image getImage() {
            return image;
        }

        public void setImage(Image image) {
            this.image = image;
        }

    }

