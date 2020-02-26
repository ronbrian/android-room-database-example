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

//        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
//
//        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl("http://192.168.1.148:9789/")
//                .addConverterFactory(GsonConverterFactory.create())
//                .client(httpClient.build())
//                .build();
//
//        UserService service = retrofit.create(UserService.class);
//
//
//        String token = "eyJhbGciOiJSUzI1NiIsImtpZCI6ImNiOGUwZDk3Mjg2MWIwNGJlN2RjNzVhMWIzYmUzYjIyOWIyNWYyMDUiLCJ0eXAiOiJKV1QifQ.eyJpc3MiOiJodHRwczovL3NlY3VyZXRva2VuLmdvb2dsZS5jb20vYW5kcm9pZC1yb29tLWRhdGFiYXNlLWV4YW1wbGUiLCJhdWQiOiJhbmRyb2lkLXJvb20tZGF0YWJhc2UtZXhhbXBsZSIsImF1dGhfdGltZSI6MTU4MjY5NDY0MiwidXNlcl9pZCI6IkNOQkxCcmREVVljQUFKcDF4cmFtMFdKaTFWYTIiLCJzdWIiOiJDTkJMQnJkRFVZY0FBSnAxeHJhbTBXSmkxVmEyIiwiaWF0IjoxNTgyNjk0NjQyLCJleHAiOjE1ODI2OTgyNDIsImVtYWlsIjoicm9ubWFjaG9rYUBnbWFpbC5jb20iLCJlbWFpbF92ZXJpZmllZCI6ZmFsc2UsImZpcmViYXNlIjp7ImlkZW50aXRpZXMiOnsiZW1haWwiOlsicm9ubWFjaG9rYUBnbWFpbC5jb20iXX0sInNpZ25faW5fcHJvdmlkZXIiOiJwYXNzd29yZCJ9fQ.Ypy4zz_1qUXNuCSIUQQ2ElDDRU9UhnZ68yuQbGhyfD7f7fFhIKfGjhIM2iEZXHtu3lh2EOGULFOEBaZIbq0YcWJF6otWWekUYc9otq8QAF2-kzrP3yZdBVnmCL2k0PEWV13jEAAqCOm2g_JGA7igq77toIvQC2Z27uYrQj1GYA7xmWss_hDCTJoKwZq6D936EWnbc_Tl0v7ibmYFNbFZC5kj7iAVDIhM1BHK6CHVujdhptUsjTNEw2Tuk0qeT0oKdTemnOlGC3gOMy-QojHYu01VaKHfRNJDQbc4KJV0zce-KZ0lotlxoFW4PkP3kej6sIT4mYvxiTB8IprlaydqsQ\n";
//
//        //Call<UserApiResponse2> callSync = service.verifyUser("Bearer "+ token,12);
//        Call<com.ron.mytodo.model.Response> callSync = service.verifyUser("Bearer "+ token,12);
//        callSync.enqueue(new Callback<com.ron.mytodo.model.Response>() {
//            @Override
//            public void onResponse(Call<com.ron.mytodo.model.Response> call, Response<com.ron.mytodo.model.Response> response) {
//                if (response.isSuccessful()){
//                    Log.e("TAG", "it added successfully");
//                    Log.e("TAG" ,response.body().getMessage());
//
//                    System.out.println(response.body().getMessage()+" l ");
//
//                }
//            }
//
//            @Override
//            public void onFailure(Call<com.ron.mytodo.model.Response> call, Throwable t) {
//
//
//                System.out.println(" Cannot get response ");
//            }
//        });



        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://localhost:9789/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient.build())
                .build();

        UserService service = retrofit.create(UserService.class);

        String token = "eyJhbGciOiJSUzI1NiIsImtpZCI6ImNiOGUwZDk3Mjg2MWIwNGJlN2RjNzVhMWIzYmUzYjIyOWIyNWYyMDUiLCJ0eXAiOiJKV1QifQ.eyJpc3MiOiJodHRwczovL3NlY3VyZXRva2VuLmdvb2dsZS5jb20vYW5kcm9pZC1yb29tLWRhdGFiYXNlLWV4YW1wbGUiLCJhdWQiOiJhbmRyb2lkLXJvb20tZGF0YWJhc2UtZXhhbXBsZSIsImF1dGhfdGltZSI6MTU4MjcwMTI5MiwidXNlcl9pZCI6IkNOQkxCcmREVVljQUFKcDF4cmFtMFdKaTFWYTIiLCJzdWIiOiJDTkJMQnJkRFVZY0FBSnAxeHJhbTBXSmkxVmEyIiwiaWF0IjoxNTgyNzAxMjkyLCJleHAiOjE1ODI3MDQ4OTIsImVtYWlsIjoicm9ubWFjaG9rYUBnbWFpbC5jb20iLCJlbWFpbF92ZXJpZmllZCI6ZmFsc2UsImZpcmViYXNlIjp7ImlkZW50aXRpZXMiOnsiZW1haWwiOlsicm9ubWFjaG9rYUBnbWFpbC5jb20iXX0sInNpZ25faW5fcHJvdmlkZXIiOiJwYXNzd29yZCJ9fQ.eEVSyPpYJlstvAQ_kW3PoRHUBlLhVhaZF0LTAiHIGxFMejUrpRFc8v03tY1QuUl3L1agGEJuNHssiIqWtTjmn-ALCqRoL99R4XI8yjXToqnQy1fseFAEBc-4yQELi2kjdIUgzI0UbPuGACy01bmiU81y7MKOQVgWcgQMmXdnl2c0ADHgBNYhhwAl9GHSuOF6yaDmWWhEiwA9zHy-pH8B_nlTtPO5HHiokpp5pOPX2Jvfkz6wBZN9hwc6NioTmQR_eCvlhBI1wOUv7VO1a0XV4FiRlvALRmTnWk6o9uBivDyP97WqbGd5girZL35JanEGbhCXWGwZLMM5nXs5HQhNRw";
        Call<UserApiResponse> callSync = service.getUser( token ,12);

        try {
            Response<UserApiResponse> response = callSync.execute();
            UserApiResponse apiResponse = response.body();
            System.out.println(apiResponse.toString());


        } catch (Exception ex) {
            ex.printStackTrace();
        }






    }
}