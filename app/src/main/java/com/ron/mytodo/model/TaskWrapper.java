package com.ron.mytodo.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;


@SuppressWarnings("unused")
public class TaskWrapper {

    @SerializedName("data")
    private List<Task> data;
    @SerializedName("error")
    private Boolean error;
    @SerializedName("message")
    private String message;
    @SerializedName("status")
    private String status;



    public List<Task> getTask() {
        return data;
    }

    public void setTask(List<Task> data) {
        data = data;
    }

    public Boolean getError() {
        return error;
    }

    public void setError(Boolean error) {
        error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        status = status;
    }



}
