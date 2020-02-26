package com.ron.mytodo;

import com.google.firebase.database.IgnoreExtraProperties;







@IgnoreExtraProperties
public class User {


    public String email;
    public String name;
    public String location;
    public String userId;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }



    // Default constructor required for calls to
    // DataSnapshot.getValue(User.class)
    public User() {
    }

    public User(String email, String name, String location, String userId) {

        this.email = email;
        this.name = name;
        this.location = location;
        this.location = location;
        this.userId = userId;

    }




}