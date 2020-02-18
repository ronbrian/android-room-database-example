package com.ron.mytodo;

import com.google.firebase.database.IgnoreExtraProperties;







@IgnoreExtraProperties
public class User {


    public String email;
    public String username;
    public String location;

    // Default constructor required for calls to
    // DataSnapshot.getValue(User.class)
    public User() {
    }

    public User(String email, String username,String location) {

        this.email = email;
        this.username = username;
        this.location = location;

    }
}