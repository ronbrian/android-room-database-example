package com.ron.mytodo.rest;

import com.ron.mytodo.allTasks;
import com.ron.mytodo.model.Task;
import com.ron.mytodo.model.TaskWrapper;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface UserService {

    @GET("/tasks/{id}")
    public Call<UserApiResponse> getTask(@Path("id") long id);

    @POST("/tasks/")
    public Call<UserApiResponse> addTask(@Body Task task);

    @GET("/tasks")
    public Call<allTasks> getAllTasks();

    @GET("/tasks")
    public Call<TaskWrapper> getAllTasks2();

    @PUT("/tasks/{id}")
    public Call<UserApiResponse> updateTask(@Path("id") long id, @Body Task task);

    @GET("/tasks/{id}")
    public Call<UserApiResponse> deleteTask(@Body Task task);








}
