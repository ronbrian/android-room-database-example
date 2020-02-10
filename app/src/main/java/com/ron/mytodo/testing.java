package com.ron.mytodo;


import com.ron.mytodo.model.Task;
import com.ron.mytodo.rest.ApiClient;
import com.ron.mytodo.rest.UserApiResponse;
import com.ron.mytodo.rest.UserService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class testing{



    public static void main(String[] args) throws IOException {


        Task task = new Task();
        task.setTask("Updated Task");
        task.setDescription("Desccription");
        task.setFinishBy("Whenever");
        task.setFinished(true);
        task.setLocation("Current Location");





        UserService apiService = ApiClient.getClient().create(UserService.class);
        Call<UserApiResponse> call = apiService.updateTask(35, task);

        call.enqueue(new Callback<UserApiResponse>() {
            public List<UserApiResponse> tasks = new ArrayList<>();
            List<String> mobileArray = new ArrayList<>();
            @Override
            public void onResponse(Call<UserApiResponse>call, Response<UserApiResponse> response) {
            }
            @Override
            public void onFailure(Call<UserApiResponse> call, Throwable t) {
                System.out.println(" !!! Error !! " + t.getMessage());
            }
        });






    }
}