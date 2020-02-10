package com.ron.mytodo.rest;


import java.io.IOException;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UserServiceClient
{



    public static void main(String[] args) throws IOException {
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.1.148:9786/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient.build())
                .build();

        UserService service = retrofit.create(UserService.class);
/*

        Task task = new Task();
        task.setTask("Changing class to public");
        task.setDescription("Make it works");
        task.setFinishBy("Teeestt");
        task.setFinished(false);
*/


        // Calling '/api/users/2'
        Call<UserApiResponse> callSync = service.getTask(4);
        Response<UserApiResponse> response = callSync.execute();


    }
}