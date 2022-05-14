package com.example.interviewproject.POJO;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ProfilePOJO {

    @SerializedName("response")
    @Expose
    private Response response;

    public Response getResponse() {
        return response;
    }

    public void setResponse(Response response) {
        this.response = response;
    }

    public class Profile {

        @SerializedName("id")
        @Expose
        private Integer id;
        @SerializedName("Fullname")
        @Expose
        private String fullname;
        @SerializedName("Dob")
        @Expose
        private String dob;
        @SerializedName("mobile")
        @Expose
        private String mobile;
        @SerializedName("groupno")
        @Expose
        private String groupno;
        @SerializedName("email")
        @Expose
        private String email;
        @SerializedName("user_image")
        @Expose
        private String userImage;
        @SerializedName("status")
        @Expose
        private String status;
        @SerializedName("updated_at")
        @Expose
        private String updatedAt;
        @SerializedName("created_at")
        @Expose
        private String createdAt;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getFullname() {
            return fullname;
        }

        public void setFullname(String fullname) {
            this.fullname = fullname;
        }

        public String getDob() {
            return dob;
        }

        public void setDob(String dob) {
            this.dob = dob;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public String getGroupno() {
            return groupno;
        }

        public void setGroupno(String groupno) {
            this.groupno = groupno;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getUserImage() {
            return userImage;
        }

        public void setUserImage(String userImage) {
            this.userImage = userImage;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getUpdatedAt() {
            return updatedAt;
        }

        public void setUpdatedAt(String updatedAt) {
            this.updatedAt = updatedAt;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(String createdAt) {
            this.createdAt = createdAt;
        }

    }
    public class Response {

        @SerializedName("status")
        @Expose
        private String status;
        @SerializedName("message")
        @Expose
        private String message;
        @SerializedName("profile")
        @Expose
        private List<Profile> profile = null;

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public List<Profile> getProfile() {
            return profile;
        }

        public void setProfile(List<Profile> profile) {
            this.profile = profile;
        }

    }
}
