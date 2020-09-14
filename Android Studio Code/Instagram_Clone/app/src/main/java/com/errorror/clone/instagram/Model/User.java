package com.errorror.clone.instagram.Model;

public class User {
    private String id;
    private String userName;
    private String fullName;
    private String email;
    private String imageurl;
    private String bio;

    public User() {
    }

    public User(String id, String userName, String fullName, String email, String imageurl, String bio) {
        this.id = id;
        this.userName = userName;
        this.fullName = fullName;
        this.email = email;
        this.imageurl = imageurl;
        this.bio = bio;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getId() {
        return id;
    }

    public String getUserName() {
        return userName;
    }

    public String getFullName() {
        return fullName;
    }

    public String getEmail() {
        return email;
    }

    public String getImageurl() {
        return imageurl;
    }

    public String getBio() {
        return bio;
    }
}
