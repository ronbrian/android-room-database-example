package com.ron.mytodo.rest;

public class User
{
    private long id;
    private String name;
    private String userId;
    private String email;

    //Setters and getters

    @Override
    public String toString() {
        return "User [id=" + id + ", "
                + "first_name=" + name + ", "
                + "last_name=" + userId + ", "
                + "email=" + email + "]";
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}