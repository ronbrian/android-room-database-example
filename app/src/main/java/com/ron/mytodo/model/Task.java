package com.ron.mytodo.model;



import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

@Entity
public class Task implements Serializable {

    @SerializedName("id")
    @PrimaryKey(autoGenerate = true)
    private int id;

    @SerializedName("task")
    @ColumnInfo(name = "task")
    private String task;

    @SerializedName("description")
    @ColumnInfo(name = "description")
    private String description;

    @SerializedName("finishBy")
    @ColumnInfo(name = "finishBy")
    private String finishBy;

    @SerializedName("finished")
    @ColumnInfo(name = "finished")
    private boolean finished;

    @SerializedName("location")
    @ColumnInfo(name = "location")
    private String location;





     //User Defined Getters and Setters






     //Auto Generated  Getters and Setters


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFinishBy() {
        return finishBy;
    }

    public void setFinishBy(String finishBy) {
        this.finishBy = finishBy;
    }

    public boolean isFinished() {
        return finished;
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }

    public String getLocation() { return location; }

    public void setLocation(String location) { this.location = location; }



    @Override
    public String toString() {
        return id+":"+task+":"+description+":"+finishBy+":"+finished+": "+location;
    }






}
