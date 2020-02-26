package com.ron.mytodo.model;

import com.ron.mytodo.User;

import java.util.List;

public class Response {
    private List<User> data;
    private String error;
    private String message;
    private String status;

    public Response() {
    }

    public List<User> getData() {
        return data;
    }

    public void setData(List<User> data) {
        this.data = data;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getMessage() {
        return "## " + message ;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
