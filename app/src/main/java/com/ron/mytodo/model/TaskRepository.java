package com.ron.mytodo.model;

import android.app.Application;

import androidx.lifecycle.MutableLiveData;

import com.ron.mytodo.rest.ApiClient;
import com.ron.mytodo.rest.UserService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TaskRepository {

    private ArrayList<Task> tasks = new ArrayList<>();
    private MutableLiveData<List<Task>> mutableLiveData = new MutableLiveData<>();
    private Application application;

    public TaskRepository(Application application) {
        this.application = application;
    }

    public MutableLiveData<List<Task>> getMutableLiveData() {


        UserService apiService = ApiClient.getClient().create(UserService.class);
        Call<TaskWrapper> call = apiService.getAllTasks2();

        call.enqueue(new Callback<TaskWrapper>() {
            @Override
            public void onResponse(Call<TaskWrapper> call, Response<TaskWrapper> response) {
                TaskWrapper mTaskWrapper = response.body();
                if (mTaskWrapper != null && mTaskWrapper.getTask() != null) {
                    tasks = (ArrayList<Task>) mTaskWrapper.getTask();
                    mutableLiveData.setValue(tasks);
                }
            }

            @Override
            public void onFailure(Call<TaskWrapper> call, Throwable t) {

            }

        });

        return mutableLiveData;

        }


    }



